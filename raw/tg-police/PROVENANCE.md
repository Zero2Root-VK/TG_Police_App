# Provenance — `raw/tg-police/`

> **Status: UNDOCUMENTED / UNVERIFIED.** This file is a placeholder to track that the
> origin and licensing of this content is *not yet established*. Fill this in before any
> commercial release. See PRD §11.1.

## What this folder is
- **15 papers · 3,000 questions · bilingual (EN + TE)** for Telangana Police
  **Constable** (prelims + mains 2015, 2018, 2022) and **SI** (prelims + mains 2016, 2018, 2022/2023).
- Copied verbatim from `/home/kali/TG-Police` (outside this repo) — the original source
  of *that* folder is not recorded.
- Every paper is `reviewPending: true` (no human QA done).

## What the JSON contains
- `officialKey` — the official per-question answer key (government-origin; lower risk).
- Question stems + 4 options (bilingual).
- `solution` / `solution.rule` — **explanations**, i.e. third-party authored text
  (copyrighted literary work; the main legal exposure).
- Extra rendering fields: `table`, `diagramSvg`, `solutionSvg` (not yet rendered by the app).

## KNOWN unknown (must be resolved)
- [ ] Original publisher / URL of this paper set.
- [ ] Whether explanations were authored in-house, licensed, or copied from a coaching source.
- [ ] Any licence terms attached to the source.
- [ ] Whether the source itself was scraped (the deferred `tspc`/`wrexa` sets were — this one is
      described as the "safer seed" but that is **not** proof of licence).

## Legal position (not legal advice)
- Reproducing previous-year government papers for commercial sale has **no clear safe harbour**
  in Indian copyright law (Copyright Act §52 exceptions don't cleanly cover exam papers).
- The **explanations** are the clearest infringement risk if copied from a third party.
- **Brand/trademark:** avoid TSLPRB/"Telangana Police" logos and any implied official affiliation.

## Resolution options (pick one before paid release)
1. Obtain a written licence / assignment for questions + explanations; **or**
2. Source official/RTI-cleared papers and **rewrite all explanations in-house**; **or**
3. Ship only government-origin material (papers + official keys) and drop third-party solutions.

## QA (accuracy) — separate from legal
- 26 questions with no key (`answer: -1`); 3 broken questions (0 options) in `si-mains-2018-ar`
  n≈189–191; 90 questions missing `prompt.te`. See `normalized/_report.json`.
