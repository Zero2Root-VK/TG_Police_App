#!/usr/bin/env python3
"""Build Android assets from normalized/ content.

Copies each canonical paper into core/data assets and writes a compact catalog
index (metadata only) so the app can list papers without parsing full papers.

Usage:
    python3 tools/importer/build_assets.py [--norm DIR] [--out DIR]
"""

from __future__ import annotations

import argparse
import json
import pathlib
import shutil

DEFAULT_ROOT = pathlib.Path(__file__).resolve().parents[2]
DEFAULT_NORM = DEFAULT_ROOT / "normalized"
DEFAULT_OUT = DEFAULT_ROOT / "core" / "data" / "src" / "main" / "assets" / "papers"

SKIP = {"_index.json", "_report.json"}
META_FIELDS = (
    "id",
    "track",
    "stage",
    "year",
    "paperNo",
    "series",
    "titleEn",
    "titleTe",
    "marks",
    "durationMins",
    "positiveMarks",
    "negativeMarks",
    "totalQuestions",
    "reviewPending",
)


def main() -> int:
    ap = argparse.ArgumentParser(description=__doc__)
    ap.add_argument("--norm", type=pathlib.Path, default=DEFAULT_NORM)
    ap.add_argument("--out", type=pathlib.Path, default=DEFAULT_OUT)
    args = ap.parse_args()

    files = sorted(p for p in args.norm.glob("*.json") if p.name not in SKIP)
    if not files:
        print(f"no normalized papers under {args.norm}")
        return 1

    if args.out.exists():
        shutil.rmtree(args.out)
    args.out.mkdir(parents=True, exist_ok=True)

    index = []
    total_q = 0
    for f in files:
        paper = json.loads(f.read_text(encoding="utf-8"))
        paper.pop("source", None)
        args.out.joinpath(f"{paper['id']}.json").write_text(
            json.dumps(paper, ensure_ascii=False, separators=(",", ":")) + "\n",
            encoding="utf-8",
        )
        qn = len(paper.get("questions", []))
        total_q += qn
        index.append({k: paper.get(k) for k in META_FIELDS})

    index.sort(key=lambda p: (p["track"], p["stage"], p["year"], p["paperNo"]))
    args.out.joinpath("index.json").write_text(
        json.dumps(index, ensure_ascii=False, indent=2) + "\n", encoding="utf-8"
    )

    size_mb = sum(p.stat().st_size for p in args.out.glob("*.json")) / (1024 * 1024)
    print(f"papers: {len(index)}  questions: {total_q}  assets: {size_mb:.1f} MB")
    print(f"written to {args.out}")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
