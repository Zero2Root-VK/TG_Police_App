#!/usr/bin/env python3
"""TGP content pipeline: raw/ -> normalized/ (canonical schema + validation).

Usage:
    python3 tools/importer/normalize.py [--raw DIR] [--out DIR] [--strict]

Exit codes:
    0  normalized written; no structural ERRORS (warnings allowed unless --strict)
    1  one or more structural ERRORS (or warnings when --strict)
"""

from __future__ import annotations

import argparse
import datetime as _dt
import hashlib
import json
import pathlib
import sys

SCHEMA_VERSION = 2
ALLOWED_TRACK = {"constable", "si"}
ALLOWED_STAGE = {"prelims", "mains"}

# Per-paper marking scheme (TSLPRB). positiveMarks = marks per correct answer,
# negativeMarks = marks deducted per wrong answer.  Source: research doc §9 +
# TSLPRB 2018/2022 notifications.
#   constable (prelims + mains)         : 1 mark/Q, NO negative marking (confirmed)
#   si prelims                          : 1 mark/Q, -0.20 per wrong (confirmed)
#   si mains (Final Written Exam AR/GS) : 1 mark/Q, -0.25 per wrong (PROVISIONAL — the
#                                         1/4 objective-paper rule; needs QA confirmation)
# Override per paper by adding positiveMarks/negativeMarks to the raw JSON.
_MARKING = {
    ("constable", "prelims"): (1.0, 0.0),
    ("constable", "mains"): (1.0, 0.0),
    ("si", "prelims"): (1.0, 0.20),
    ("si", "mains"): (1.0, 0.25),
}


def _marking(track: str, stage: str, raw: dict) -> tuple[float, float]:
    positive, negative = _MARKING.get((track, stage), (1.0, 0.0))
    return float(raw.get("positiveMarks", positive)), float(raw.get("negativeMarks", negative))


DEFAULT_ROOT = pathlib.Path(__file__).resolve().parents[2]
DEFAULT_RAW = DEFAULT_ROOT / "raw" / "tg-police" / "Previous Papers"
DEFAULT_OUT = DEFAULT_ROOT / "normalized"


class PaperReport:
    def __init__(self, paper_id: str, source: str):
        self.id = paper_id
        self.source = source
        self.questions = 0
        self.errors: list[str] = []
        self.warnings: list[str] = []

    def to_dict(self):
        return {
            "id": self.id,
            "source": self.source,
            "questions": self.questions,
            "errors": self.errors,
            "warnings": self.warnings,
        }


def _canonical_question(q: dict, paper_id: str, rep: PaperReport) -> dict:
    n = q.get("n")
    prompt = q.get("prompt") or {}
    options = q.get("options") or []
    answer = q.get("answer")
    solution = q.get("solution") or {}

    en = (prompt.get("en") or "").strip()
    te = (prompt.get("te") or "").strip()
    if not en:
        rep.errors.append(f"q{n}: prompt.en empty")
    if not te:
        rep.warnings.append(f"q{n}: prompt.te missing")

    if len(options) != 4:
        rep.warnings.append(f"q{n}: options count = {len(options)} (expected 4)")

    opt_texts = []
    for i, o in enumerate(options):
        oe = ((o or {}).get("en") or "").strip()
        ote = ((o or {}).get("te") or "").strip()
        opt_texts.append(oe.lower())
        if not oe and not ote:
            rep.errors.append(f"q{n}: option {i} empty (en+te)")

    nonempty = [t for t in opt_texts if t]
    if len(nonempty) != len(set(nonempty)):
        rep.warnings.append(f"q{n}: duplicate option text")

    if not isinstance(answer, int):
        rep.errors.append(f"q{n}: answer not an int ({answer!r})")
    elif answer == 0:
        rep.warnings.append(f"q{n}: answer unknown (0) - needs key")
    elif options and not (1 <= answer <= len(options)):
        rep.errors.append(f"q{n}: answer {answer} out of range 1..{len(options)}")

    rule = solution.get("rule") or {}
    if not solution:
        rep.warnings.append(f"q{n}: missing solution")
    else:
        if not (solution.get("en") or "").strip():
            rep.warnings.append(f"q{n}: solution.en missing")
        if not (solution.get("te") or "").strip():
            rep.warnings.append(f"q{n}: solution.te missing")
    if not ((rule.get("en") or "").strip() or (rule.get("te") or "").strip()):
        rep.warnings.append(f"q{n}: missing solution.rule (concept)")

    out = {
        "n": n,
        "prompt": {"en": prompt.get("en", ""), "te": prompt.get("te", "")},
        "options": [
            {"en": (o or {}).get("en", ""), "te": (o or {}).get("te", "")}
            for o in options
        ],
        "answer": answer,
        "solution": {
            "rule": {
                "en": (rule.get("en") or ""),
                "te": (rule.get("te") or ""),
            },
            "en": solution.get("en", ""),
            "te": solution.get("te", ""),
        },
    }
    for extra in ("table", "diagramSvg", "solutionSvg"):
        if q.get(extra):
            out[extra] = q[extra]
    return out


def _content_hash(payload: dict) -> str:
    blob = json.dumps(payload, ensure_ascii=False, sort_keys=True, separators=(",", ":"))
    return hashlib.sha256(blob.encode("utf-8")).hexdigest()


def normalize_paper(raw: dict, source_rel: str, rep: PaperReport) -> dict:
    paper_id = raw.get("id")
    track = raw.get("track")
    stage = raw.get("stage")
    year = raw.get("year")

    if not paper_id:
        rep.errors.append("missing paper id")
    if track not in ALLOWED_TRACK:
        rep.errors.append(f"invalid track: {track!r}")
    if stage not in ALLOWED_STAGE:
        rep.errors.append(f"invalid stage: {stage!r}")
    if not isinstance(year, int):
        rep.errors.append(f"invalid year: {year!r}")

    qs = raw.get("questions") or []
    rep.questions = len(qs)
    declared = raw.get("totalQuestions")
    if declared != len(qs):
        rep.errors.append(f"totalQuestions {declared} != questions {len(qs)}")

    seen_n = []
    norms = []
    for q in qs:
        seen_n.append(q.get("n"))
        norms.append(_canonical_question(q, paper_id, rep))
    if seen_n != list(range(1, len(qs) + 1)):
        rep.errors.append("question numbers not sequential 1..N")

    positive_marks, negative_marks = _marking(track, stage, raw)

    body = {
        "id": paper_id,
        "track": track,
        "stage": stage,
        "year": year,
        "paperNo": raw.get("paperNo"),
        "series": raw.get("series"),
        "titleEn": raw.get("titleEn", ""),
        "titleTe": raw.get("titleTe", ""),
        "marks": raw.get("marks"),
        "durationMins": raw.get("durationMins"),
        "positiveMarks": positive_marks,
        "negativeMarks": negative_marks,
        "totalQuestions": len(qs),
        "reviewPending": bool(raw.get("reviewPending", True)),
        "schemaVersion": SCHEMA_VERSION,
        "source": {
            "file": source_rel,
            "digitizedCount": raw.get("digitizedCount"),
            "officialKey": raw.get("officialKey"),
        },
        "questions": norms,
    }
    body["contentHash"] = _content_hash(body)
    return body


def main() -> int:
    ap = argparse.ArgumentParser(description=__doc__)
    ap.add_argument("--raw", type=pathlib.Path, default=DEFAULT_RAW)
    ap.add_argument("--out", type=pathlib.Path, default=DEFAULT_OUT)
    ap.add_argument("--strict", action="store_true", help="treat warnings as errors")
    args = ap.parse_args()

    files = sorted(args.raw.rglob("*.json"))
    if not files:
        print(f"no JSON files under {args.raw}", file=sys.stderr)
        return 1

    reports: list[PaperReport] = []
    written = 0
    unchanged = 0
    seen_ids: dict[str, str] = {}

    for f in files:
        rel = f.relative_to(args.raw).as_posix()
        try:
            raw = json.loads(f.read_text(encoding="utf-8"))
        except json.JSONDecodeError as e:
            r = PaperReport(f.stem, rel)
            r.errors.append(f"invalid JSON: {e}")
            reports.append(r)
            continue

        pid = raw.get("id", f.stem)
        r = PaperReport(pid, rel)
        if pid in seen_ids:
            r.errors.append(f"duplicate paper id (also {seen_ids[pid]})")
        seen_ids[pid] = rel

        body = normalize_paper(raw, rel, r)
        reports.append(r)

        out_path = args.out / f"{pid}.json"
        if out_path.exists():
            try:
                prev = json.loads(out_path.read_text(encoding="utf-8"))
                if prev.get("contentHash") == body["contentHash"]:
                    body["updatedAt"] = prev.get("updatedAt")
                    body["generatedAt"] = prev.get("generatedAt")
                    unchanged += 1
                else:
                    body["updatedAt"] = _dt.datetime.now(_dt.timezone.utc).isoformat(timespec="seconds")
            except json.JSONDecodeError:
                body["updatedAt"] = _dt.datetime.now(_dt.timezone.utc).isoformat(timespec="seconds")
        else:
            body["updatedAt"] = _dt.datetime.now(_dt.timezone.utc).isoformat(timespec="seconds")
        body["generatedAt"] = body.get("generatedAt") or _dt.datetime.now(_dt.timezone.utc).isoformat(timespec="seconds")

        out_path.parent.mkdir(parents=True, exist_ok=True)
        out_path.write_text(
            json.dumps(body, ensure_ascii=False, indent=2) + "\n", encoding="utf-8"
        )
        written += 1

    total_q = sum(r.questions for r in reports)
    total_err = sum(len(r.errors) for r in reports)
    total_warn = sum(len(r.warnings) for r in reports)

    index = {
        "schemaVersion": SCHEMA_VERSION,
        "generatedAt": _dt.datetime.now(_dt.timezone.utc).isoformat(timespec="seconds"),
        "papers": [
            {
                "id": r.id,
                "source": r.source,
                "questions": r.questions,
                "errors": len(r.errors),
                "warnings": len(r.warnings),
            }
            for r in reports
        ],
        "totals": {
            "papers": len(reports),
            "questions": total_q,
            "errors": total_err,
            "warnings": total_warn,
        },
    }
    args.out.mkdir(parents=True, exist_ok=True)
    (args.out / "_index.json").write_text(
        json.dumps(index, ensure_ascii=False, indent=2) + "\n", encoding="utf-8"
    )
    (args.out / "_report.json").write_text(
        json.dumps({"papers": [r.to_dict() for r in reports]}, ensure_ascii=False, indent=2) + "\n",
        encoding="utf-8",
    )

    print(f"papers: {len(reports)}  questions: {total_q}  written: {written} (unchanged: {unchanged})")
    print(f"errors: {total_err}  warnings: {total_warn}")
    for r in reports:
        if r.errors or r.warnings:
            print(f"  {r.id}: {len(r.errors)}E {len(r.warnings)}W  ({r.source})")
    for r in reports:
        for e in r.errors:
            print(f"  ERROR {r.id} {e}")
    if args.strict and total_warn:
        return 1
    return 1 if total_err else 0


if __name__ == "__main__":
    raise SystemExit(main())
