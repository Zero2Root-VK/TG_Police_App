#!/usr/bin/env python3
"""Push normalized content (15 papers / 3,000 Q) to Appwrite TablesDB.

Idempotent + resumable:
  - papers  upserted by rowId (paper id)
  - questions  batch-upserted by unique index (paperId, n)
  - a paper is SKIPPED when its remote contentHash matches AND the remote
    question count for the paper already equals totalQuestions

Usage:
  export APPWRITE_API_KEY=standard_...            # or --api-key
  python3 tools/importer/push_appwrite.py         # pushes everything
  python3 tools/importer/push_appwrite.py --paper constable-prelims-2015

Endpoint/project/db id are read from appwrite.config.json (project root).
Reads normalized/_index.json + normalized/<id>.json, never raw/.
"""
import argparse
import json
import os
import sys
import time
import urllib.parse

import requests

HERE = os.path.dirname(os.path.abspath(__file__))
ROOT = os.path.normpath(os.path.join(HERE, "..", ".."))
NORMALIZED = os.path.join(ROOT, "normalized")
CONFIG = os.path.join(ROOT, "appwrite.config.json")
DB_ID = "tgp"
BATCH = 100
RETRIES = 4
BACKOFF = [1, 2, 4, 8]


def load_config():
    c = json.load(open(CONFIG))
    return {
        "endpoint": c["endpoint"].rstrip("/"),
        "projectId": c["projectId"],
        "databaseId": c["tablesDB"][0]["$id"],
    }


def resolve_key(args):
    if args.api_key:
        return args.api_key
    env = os.environ.get("APPWRITE_API_KEY")
    if env:
        return env
    f = os.path.expanduser("~/.config/tgp/appwrite.json")
    if os.path.exists(f):
        cred = json.load(open(f))
        if cred.get("apiKey"):
            return cred["apiKey"]
    sys.exit("No API key. Use --api-key, APPWRITE_API_KEY, or ~/.config/tgp/appwrite.json")


def iso_fields_valid(v):
    return isinstance(v, str) and len(v) > 0


def paper_row(p):
    """Paper metadata row (source / officialKey intentionally dropped)."""
    return {
        "track": p["track"],
        "stage": p["stage"],
        "year": p["year"],
        "paperNo": p["paperNo"],
        "series": p.get("series") or "",
        "titleEn": p["titleEn"],
        "titleTe": p["titleTe"],
        "marks": p["marks"],
        "durationMins": p["durationMins"],
        "positiveMarks": float(p.get("positiveMarks", 1.0)),
        "negativeMarks": float(p.get("negativeMarks", 0.0)),
        "totalQuestions": p["totalQuestions"],
        "reviewPending": p.get("reviewPending", True),
        "schemaVersion": p.get("schemaVersion", 1),
        "contentHash": p["contentHash"],
        "isFree": bool(p.get("isFree", False)),
        "updatedAt": p["updatedAt"],
    }


def question_rows(p, q, updated_at):
    ans = q["answer"]
    if not (isinstance(ans, int) and 1 <= ans <= len(q.get("options") or [])):
        ans = -1
    opts_en = [o.get("en") or "" for o in q.get("options", [])]
    opts_te = [o.get("te") or "" for o in q.get("options", [])]
    rule = (q.get("solution") or {}).get("rule") or {}
    sol = q.get("solution") or {}
    return {
        "paperId": p["id"],
        "n": q["n"],
        "promptEn": (q.get("prompt") or {}).get("en") or "",
        "promptTe": (q.get("prompt") or {}).get("te") or "",
        "optionsEn": json.dumps(opts_en, ensure_ascii=False),
        "optionsTe": json.dumps(opts_te, ensure_ascii=False),
        "answer": ans,
        "solutionEn": sol.get("en") or "",
        "solutionTe": sol.get("te") or "",
        "ruleEn": rule.get("en") or "",
        "ruleTe": rule.get("te") or "",
        "topic": q.get("topic") or "",
        "conceptKey": q.get("conceptKey") or "",
        "version": 1,
        "status": "reviewPending",
        "updatedAt": updated_at,
    }


class Pusher:
    def __init__(self, cfg, key):
        self.base = f"{cfg['endpoint']}/tablesdb/{cfg['databaseId']}"
        self.proj = cfg["projectId"]
        self.s = requests.Session()
        self.s.headers.update(
            {"X-Appwrite-Project": self.proj, "X-Appwrite-Key": key, "Content-Type": "application/json"}
        )
        self.stats = {"papers_created": 0, "papers_updated": 0, "papers_skipped": 0, "questions_pushed": 0}

    def _request(self, method, path, **kw):
        last = None
        for i in range(RETRIES):
            try:
                r = self.s.request(method, self.base + path, timeout=90, **kw)
                if r.status_code < 400:
                    return r
                last = r
                if r.status_code in (429, 500, 502, 503, 504):
                    time.sleep(BACKOFF[min(i, len(BACKOFF) - 1)])
                    continue
                sys.exit(f"API error {r.status_code} {method} {path}: {r.text[:300]}")
            except requests.RequestException as e:
                last = e
                time.sleep(BACKOFF[min(i, len(BACKOFF) - 1)])
        raise SystemExit(f"Failed {method} {path}: {last}")

    def get_row(self, table, row_id):
        r = self._request("GET", f"/tables/{table}/rows/{urllib.parse.quote(row_id, safe='')}")
        return r.json()

    def put_row(self, table, row_id, data):
        r = self._request("PUT", f"/tables/{table}/rows/{urllib.parse.quote(row_id, safe='')}", json={"data": data})
        return r.status_code == 200

    def put_rows(self, table, rows):
        r = self._request("PUT", f"/tables/{table}/rows", json={"rows": rows})
        return r.json().get("total", 0)

    def count(self, table, attr, value):
        q = json.dumps({"method": "equal", "attribute": attr, "values": [value]}, separators=(",", ":"))
        r = self._request("GET", f"/tables/{table}/rows", params={"queries[0]": q})
        return r.json().get("total", 0)

    def push_questions(self, paper, updated_at):
        total = 0
        for i in range(0, len(paper["questions"]), BATCH):
            chunk = paper["questions"][i : i + BATCH]
            rows = [question_rows(paper, q, updated_at) for q in chunk]
            n = self.put_rows("questions", rows)
            total += n
        self.stats["questions_pushed"] += total
        return total

    def push_paper(self, p):
        rid = p["id"]
        data = paper_row(p)
        try:
            existing = self.get_row("papers", rid)
        except SystemExit as e:
            existing = None
            if "404" not in str(e):
                raise
        if existing and existing.get("contentHash") == p["contentHash"]:
            have = self.count("questions", "paperId", rid)
            if have == p["totalQuestions"]:
                self.stats["papers_skipped"] += 1
                print(f"  unchanged ({existing.get('contentHash')[:12]}…) · {have} Q present", flush=True)
                return "skip"
        self.put_row("papers", rid, data)
        self.push_questions(p, p["updatedAt"])
        if existing:
            self.stats["papers_updated"] += 1
            print(f"  updated {rid} → {len(p['questions'])} Q", flush=True)
        else:
            self.stats["papers_created"] += 1
            print(f"  created {rid} → {len(p['questions'])} Q", flush=True)
        return "pushed"

    def seed_app_config(self):
        seeds = [
            ("app.info", {"schemaVersion": 1, "minSupportedVersion": 1, "freeDemoCount": 0}),
            ("sync.cursors", {}),
        ]
        for key, val in seeds:
            self.put_row("app_config", key, {"key": key, "valueJson": json.dumps(val), "updatedAt": time.strftime("%Y-%m-%dT%H:%M:%S+00:00")})
        print("  app_config seeded", flush=True)


def main():
    ap = argparse.ArgumentParser()
    ap.add_argument("--api-key", help="Appwrite server API key (or APPWRITE_API_KEY / ~/.config/tgp/appwrite.json)")
    ap.add_argument("--paper", help="push a single paper id only")
    ap.add_argument("--no-appconfig", action="store_true", help="skip app_config seeding")
    args = ap.parse_args()

    cfg = load_config()
    key = resolve_key(args)
    index = json.load(open(os.path.join(NORMALIZED, "_index.json")))
    papers = index["papers"]
    if args.paper:
        papers = [p for p in papers if p["id"] == args.paper]
        if not papers:
            sys.exit(f"paper {args.paper} not found in normalized/_index.json")

    p = Pusher(cfg, key)
    t0 = time.time()
    for meta in papers:
        fp = os.path.join(NORMALIZED, meta["id"] + ".json")
        paper = json.load(open(fp))
        print(f"[{papers.index(meta)+1}/{len(papers)}] {meta['id']}", flush=True)
        p.push_paper(paper)
    if not args.no_appconfig:
        print("[app_config]", flush=True)
        p.seed_app_config()
    dt = time.time() - t0
    print(json.dumps(p.stats))
    print(f"Done in {dt:.1f}s")


if __name__ == "__main__":
    main()