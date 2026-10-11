---
name: tgp-content-pipeline
description: Use when importing, normalizing, validating, or updating TGP-APP exam content (previous-year papers, daily current-affairs). Covers the raw -> normalized -> app folders, the canonical paper/question JSON schema, bilingual fields, validation rules, the importer to Appwrite, versioning, and legal handling of scraped content.
---

# TGP Content Pipeline

Content flows one way: `raw/` (never edited) → `normalized/` (canonical, validated) → Appwrite
(via importer) → app (Room cache). Never hand-edit the raw source; never load raw directly into the app.

```
raw/            # untouched source (copied originals) — sacred
normalized/     # canonical schema, cleaned, one file per paper
tools/importer/ # scripts: validate + push to Appwrite
app/            # Android project (reads from Appwrite/Room only)
```

## Canonical schema (per paper)

Based on the TG-Police source format. Keep it stable; it is the contract for importer + app.

```jsonc
{
  "id": "constable-prelims-2022",
  "track": "constable",          // constable | si
  "stage": "prelims",            // prelims | mains
  "year": 2022,                  // ALWAYS use metadata year, not folder name (they mismatch)
  "paperNo": 1,
  "series": "TG Police",
  "titleEn": "...", "titleTe": "...",
  "marks": 100, "durationMins": 120, "totalQuestions": 100,
  "reviewPending": true,         // true until QA'd
  "schemaVersion": 1,
  "questions": [
    {
      "n": 1,
      "prompt": { "en": "...", "te": "..." },
      "options": [ { "en": "...", "te": "..." }, /* x4 */ ],
      "answer": 1,               // 1-indexed into options
      "topic": "Telangana History",
      "conceptKey": "ts-history:kakatiya-dynasty",
      "solution": { "rule": { "en": "...", "te": "..." }, "en": "...", "te": "..." }
    }
  ]
}
```

- `answer` is **1-indexed**. Validate strictly.
- Every user-visible string is bilingual `{en, te}`. If Telugu is missing, flag the question —
  do not silently ship English-only in a bilingual paper.
- `solution.rule` is the reusable concept/rule and is the basis of the future weightage/repetition
  clustering — populate it as cleanly as possible.

## Validation (importer must fail loudly)

Per paper: unique `id`; `track`/`stage` in allowed set; `year` present; `totalQuestions` ==
`questions.length`; `marks`/`durationMins` > 0; `paperNo` within range.
Per question: exactly 4 options; `answer` in 1..4; `prompt.en` non-empty; no duplicate option text;
`n` sequential and unique. Report a summary (errors/warnings: missing `te`, missing `solution`,
missing `conceptKey`) and exit non-zero on errors.

## Import to Appwrite

- Deterministic IDs (e.g. `paperId:qN`) so re-import is idempotent (upsert).
- Set `updatedAt` (ISO) + `contentHash` per paper; only push changed papers.
- Batched writes respecting Appwrite rate limits; resumable on failure.
- Strip `questions` into the `questions` collection; keep paper metadata in `papers`.
- Mark `reviewPending: true` for the initial TG-Police set (3,000 Q) until QA'd.

## Versioning

- Bump `schemaVersion` on shape changes; importer supports the previous version (migrations).
- Preserve `questionVersion` semantics: attempts store which version they answered, so edits don't
  retroactively change a user's reviewed result.

## Adding content later

- **Daily current affairs:** 20–30 Q/day entered via the admin panel, same schema, `reviewPending`
  until verified. Add an `edition`/`date` field for CA papers.
- **Deferred bulk content** (`/home/kali/tspc`, `/home/kali/wrexa`) must be normalized into this
  schema before import; do not import their native formats directly.

## Legal

- `raw/` includes material scraped from third parties (tspc via SQLi; wrexa reverse-engineered).
  Do NOT commercially resell scraped content as-is. Verify licensing / rewrite questions / use only
  as reference. Keep provenance notes in `normalized/` metadata and PRD risks.

## Verify

- Run the validator over all 15 TG-Police papers: 0 errors, expected 3,000 questions total.
- Re-run the importer twice: second run should push 0 changes (idempotent).
- Spot-check 10 random questions in the app for correct answer index + bilingual rendering.
