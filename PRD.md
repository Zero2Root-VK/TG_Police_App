# PRD — Telangana Police SI & Constable Mock Exam App

> Product Requirements Document. Living doc — update as decisions change.
> Created: 2026-10-10 · Project root: `/home/kali/TGP-APP`

---

## 1. Product Summary

| Item | Value |
|---|---|
| Name | TGP Mock Exam (working title) |
| Platform | Android (Google Play) |
| Positioning | Paid mock test papers + previous-year papers for **Telangana Police SI & Constable** |
| Audience | TSLPRB aspirants (Telugu + English) |
| Business model | Freemium — free demo tests, paid bundles (exam-wise / subject-wise) |
| Language | **Bilingual: English + Telugu** |
| Core hook | Real previous-year papers, real exam interface, instant analytics, daily current affairs |

---

## 2. Scope — Initial Launch (v1)

**Content = ONLY TG-Police previous papers for now.**

- 15 papers · **3,000 questions** · bilingual (EN + TE) with solutions
- Tracks: **Constable** (Prelims + Mains), **SI** (Prelims + Mains)
- Years: 2015, 2016, 2018, 2022/2023

All other content (tspc 60k Q, wrexa mocks) is **deferred** — normalization takes time.

**Non-goals (v1):** iOS, web app for students, live proctoring, AI tutor, enterprise/white-label.

---

## 3. Planned / Future Modules

1. **Repetition & Weightage module** (high priority future)
   - Repeated questions across PY papers (exact + near-duplicate)
   - Repeated concepts / methods / grammar rules (cluster using `solution.rule`)
   - Topic-wise weightage across years (marks per topic)
   - PY trend 2015→2026 → "high-yield predictor"
2. **Daily current affairs** — admin uploads 20–30/day, pushed to app
3. **Subject-wise & bundle-wise** test series
4. **Full 60k question bank** (tspc) + wrexa mock sets after normalization
5. AI tutor / notebook (deferred)

---

## 4. Tech Stack

| Layer | Choice | Notes |
|---|---|---|
| App | **Kotlin + Jetpack Compose** | native, Play-ready |
| Backend | **Appwrite Cloud (free)** | auth, DB, functions, storage, messaging |
| Content store | **Appwrite DB (source of truth)** + **Room cache** on device | delta sync by `updatedAt` |
| Payments | **Google Play Billing + RevenueCat (free tier)** | auto-verify + auto-grant + refund handling |
| Admin | In-app (role-gated) + Web panel | content edits, daily CA, sales |
| Push | Appwrite Messaging / FCM | new paper + exam-date alerts |

### Cost
- **$25 one-time** (Play Console)
- Google commission **15%** (first $1M/yr) → **30%**
- Appwrite/RevenueCat/other: **₹0** at launch scale

---

## 5. Canonical Content Schema

Our unified schema (derived from TG-Police format — highest quality, bilingual):

```json
{
  "id": "constable-prelims-2022",
  "track": "constable | si",
  "stage": "prelims | mains",
  "year": 2022,
  "paperNo": 1,
  "series": "A",
  "titleEn": "...",
  "titleTe": "...",
  "marks": 200,
  "durationMins": 180,
  "totalQuestions": 200,
  "reviewPending": true,
  "questions": [
    {
      "n": 1,
      "prompt": { "en": "...", "te": "..." },
      "options": [ { "en": "..." }, { "en": "..." },
                   { "en": "..." }, { "en": "..." } ],
      "answer": 1,                          // 1-indexed
      "solution": {
        "rule": { "en": "...", "te": "..." },
        "en": "...",
        "te": "..."
      }
    }
  ]
}
```

- `track`, `stage`, `year`, `paperNo`, `series`, `title*` → catalog/meta
- `questions[].prompt.en/te` → bilingual stem
- `options[]` → 4 options, bilingual
- `answer` → correct option (admin-editable)
- `solution.rule` → **concept tag** (feeds weightage/repetition module)
- `reviewPending` → flag for admin review queue

---

## 6. Functional Requirements

### 6.1 Student app
- Auth (phone OTP / Google) via Appwrite
- Catalog: exam-wise (SI/Constable) → stage → year
- Test runner: timer, question palette, mark-for-review, prev/next, auto-submit, offline
- Results: score, section-wise, topic-wise, answer key + solutions
- Free demo tests; paid unlock for full papers
- Bilingual toggle (EN/TE)
- Daily current affairs section
- Push notifications (new papers, exam dates)

### 6.2 Admin (two surfaces)
**In-app admin (role-gated):**
- Quick edit any question: stem, options A–D, **correct answer cell**, explanation
- Publish today's current affairs (20–30 Q)
- Approve/reject flagged questions

**Web panel:**
- Bulk CSV paste/import
- Full question editor + search/filter (paper, subject, id, flagged)
- Daily CA publisher (date + subject)
- Sales/revenue, user management, audit log

### 6.3 Content management rules
- Questions **versioned**; attempts store `questionVersion` used → past scores stay honest
- Soft-delete (never hard-delete) → history integrity
- Every edit **audit-logged** (who/what/when/old→new)
- Admin edits go live on next delta sync

---

## 7. Data Model (Appwrite)

```
users/{uid}            email, name, role (student|admin), createdAt
entitlements/{uid}     plan, expiresAt, purchaseToken(s), productId   ← server-only write
papers/{id}            track, stage, year, meta, questionCount, free|paid, version, updatedAt
questions/{id}         paperId, n, prompt{en,te}, options[], answer,
                       solution{...}, tags[], version, updatedAt, status
daily_ca/{id}          date, subject, access, questions[], publishedAt
attempts/{id}          userId, paperId, answers[], questionVersion, startedAt, submittedAt
results/{id}           userId, paperId, score, sectionStats, topicStats, submittedAt
audit_log/{id}         actorId, action, target, before, after, at
notifications/{id}     title, body, audience, sentAt
```

**Security:** only an Appwrite Function with admin role may write `questions`, `papers`,
`daily_ca`, `entitlements`. Students are read-only on content. Roles enforced server-side
(never UI-only).

---

## 8. Monetization & Gating

| Tier | Access |
|---|---|
| Free | 2–3 demo papers per track + basic analytics + daily CA preview |
| Paid (bundle) | Full bundle: all papers for a track/stage, all analytics, solutions |
| Bundle options | Exam-wise (SI / Constable) · Stage-wise (Prelims / Mains) · All-access |

Gating enforced **server-side**; client only reflects entitlement.

---

## 9. Build Order (milestones)

1. **M1 — Content import** — load 15 TG-Police papers into canonical schema + Appwrite DB
2. **M2 — Exam engine** — runner (timer, palette, review, auto-submit, offline) — hardest, do first
3. **M3 — Catalog + content loader + Room delta sync**
4. **M4 — Auth + entitlements + Play Billing/RevenueCat**
5. **M5 — Admin panels (in-app + web)**
6. **M6 — Daily CA publisher + push**
7. **M7 — Repetition & Weightage module**
8. **M8 — Play Store release**

---

## 10. Content Pipeline

```
raw/         ← untouched source content (copy everything here first)
normalized/  ← cleaned + unified to canonical schema
app/         ← Android app + shipped/streamed content
```
Flow: copy to `raw/` → normalize in `normalized/` → push to `app/` only when ready.

---

## 11. Risks & Caveats

- **Content provenance / legal:** tspc content was scraped from toppersexam.com (via SQLi),
  wrexa docs/content reverse-engineered from wrexaedge.com. **Do not commercially resell
  scraped content as-is** — verify/rewrite or license. TG-Police set is the safer initial seed.
- **reviewPending: true** on all 15 TG-Police papers → needs human QA before paid release.

### 11.1 Legal & content-QA checklist (BLOCKER for paid release)

**Two distinct problems — do not conflate:**
- **Accuracy/QA** (`reviewPending`) → human verify keys + explanations. In the current seed:
  26 Qs with no key (`answer: -1`), 3 broken Qs (zero options, `si-mains-2018-ar` n≈189–191),
  90 Qs missing `prompt.te`. Fix = QA, no legal component.
- **Legal/provenance** → licensing of the source content. Origin of `raw/tg-police/` is **not
  documented** in-repo (no provenance/licence note). The JSON carries `officialKey`
  (government answer key — lower risk) **plus** `solution`/`solution.rule` explanations, which
  are third-party authored literary works (copyrighted). See `raw/tg-police/PROVENANCE.md`.

**Before a paid release, one of:**
1. Obtain a written licence / assignment for the questions + explanations; **or**
2. Source official/RTI-cleared papers and **rewrite all explanations in-house**; **or**
3. Publish only government-origin material (keys/papers) and drop third-party solutions.

**Also:**
- Never ship the deferred scraped sets (`tspc` via SQLi, `wrexa` reverse-engineered) commercially.
- **Brand/trademark:** avoid "Telangana Police"/TSLPRB logos; no implied official affiliation.
- **Not legal advice** — get qualified review before monetizing.
- Tracked as a launch blocker in `STATUS.md` (Open questions).
- **Appwrite free limit:** only **2 functions per project** (since Jan 2026) → design the
  verify/entitlement/content-write APIs within that constraint.
- **Appwrite free projects pause after ~1 week idle** → upgrade or ping to keep alive.
- Content size vs free bandwidth (5GB/mo) → bundle base content in-app, stream only deltas/CA.

---

## 12. Open Questions

- Bundle pricing (₹) per track? All-access price?
- Free demo count — 2 or 3 papers?
- Personal vs Organization Play account (affects 12-tester rule)?
- Telugu-first or English-first default UI?
- Who does content QA on the 3,000 reviewPending questions?
