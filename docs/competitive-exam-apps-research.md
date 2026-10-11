# Competitive Exam App Research — TGP-APP

> Research deliverable (2026-10-11). Goal: understand how top Indian govt-exam / mock-test
> apps are designed and structured (interface, architecture, modules, workflow, design, UI,
> features) and translate that into concrete, actionable decisions for **TGP Mock Exam**
> (Telangana Police SI & Constable). Sources: Testbook, Adda247, Oliveboard, PracticeMock,
> ixambee, ExamBook, various CET/PSC apps, NTA/CBT patterns, Play Store reviews.

---

## 0. TL;DR — What matters most for TGP-APP

| # | Recommendation | Why |
|---|---|---|
| 1 | **Real, exam-accurate marking engine** (per-paper `negativeMarks`) | TSLPRB SI prelims = **−0.20/wrong**; Constable = **no negative**. Our engine defaults to 0 → wrong for SI. |
| 2 | **Deep analytics after every test** (rank/percentile, accuracy, time/question, topic & section breakdown, compare-topper) | The *analysis*, not the test, is the retention hook (MockApp, Oliveboard). |
| 3 | **Exam-hall (CBT) interface parity** — palette states, Save&Next, Mark for Review, Clear Response, timer, auto-submit | Users judge "realness"; Testbook's #1 selling point is "real exam interface". |
| 4 | **Language-first onboarding (Telugu)** + bilingual toggle everywhere | TSLPRB candidates are Telugu-first; 12+ language support is table stakes at competitors. |
| 5 | **Offline-first** (already done via Room + bundled assets) | Low-end devices, patchy data in Telangana tier-2/3 areas. |
| 6 | **Simple freemium**: free demo papers → one low-priced pass (₹299–₹999/yr) | Market anchor: Testbook ₹299 promo/₹749–1199 list; Prepp ₹249/yr; ExamBook ₹99–999. |
| 7 | **AVOID competitor pain points**: cluttered UI, forced batch selection, noisy notifications | Top complaints in Play Store reviews. |

---

## 1. Competitive landscape — feature sets

### Testbook (market leader, 50M+ installs, 4.3★)
- **2 Lakh+ mock tests & PY papers**, 1.85 Lakh+ selections claimed.
- **Real Exam Interface** (marketed as a top feature).
- **12+ languages**; free **Daily Current Affairs & GK capsules**.
- **Smart Performance Analysis**; AI doubt solving; recommendations by progress.
- 250+ exams in one app.
- **Testbook Pass**: one subscription for mocks + PYQ + notes + AI doubt (₹749/yr list ₹1199; promo ₹299/yr; ₹499/2yr historically).
- **SuperPass / SuperCoaching**: live classes, 750+ courses, 900+ test series.

### Adda247
- Study material, CA, quizzes, magazines, video courses, e-books, PYQs, doubt-solving.
- **12 languages incl. Telugu**; **offline download**.
- Dedicated **TS Police SI 2026 test series** (direct competitor).
- Products: **Test Prime / Mahapack** (₹269 promo / ₹999 list).
- **Pain points (reviews):** cluttered UI, forced batch-enrollment choice at start, excessive/useless notifications, unclear class-progress.

### Oliveboard
- Full-length / sectional / topic / GK mocks; 25,000 free questions.
- **AI mock analysis**: summary (attempted/correct/wrong/rank/score/top-score), **percentile + accuracy**, sectional summary & scores, **time distribution** (time on correct/wrong/unattempted, minute-by-minute progress tracker, compare with topper's score & time), **weak areas**, solutions tab.
- Discuss forum, planner, adaptive engine.

### PracticeMock
- Exam-level questions, detailed performance analysis, **reattempt mode**, **bookmark question**, bilingual, offline PDF course.

### ixambee / ExamBook / niche apps
- Result screen: Score, Rank, Time Taken, Accuracy, Correct/Wrong/Skipped/Attempted, section-wise; **Exam Hall Mode**; Top Rankers leaderboard; compare-with-topper.
- **ExamBook pricing (2026)**: Per Category ₹99/mo, All Access ₹199/mo, **Annual ₹999/yr** (7-day refund).
- **Test Series Pro**: All-in-One Exam Pass ₹599/yr (88% off anchor), per-exam ₹149–449.
- **Prepp**: ₹249/yr all-exams. **MockApp**: free mocks, monetizes AI analytics.

### Market packaging conventions
- **Per-exam pass** (₹149–499), **All-access pass** (₹599–999/yr), **bundle packs** (test series ₹299–999 one-time).
- Free tier: 1–3 full mocks + basic score; analytics/solutions often gated.
- 7-day refund standard; Razorpay/UPI for web; Play Billing for in-app.
- Heavy **discount anchoring** ("₹4,999 → ₹599", "88% off") — common in this market.

---

## 2. Test-taking (CBT / exam-hall) UX — de-facto standard

Adopt the **NTA/CBT conventions** users already expect:

- **Timer** top-right, always visible; **auto-submit at 0**.
- **Question palette** with 5 states + legend:
  1. Answered (green)
  2. Not Answered (red)
  3. Not Visited (grey)
  4. Marked for Review (purple)
  5. Answered & Marked for Review (purple + tick)
- **Buttons**: `Save & Next`, `Mark for Review & Next`, `Clear Response`, `Previous`, `Submit`.
- **Section tabs** (Prelims has Arithmetic&Reasoning + General Studies).
- **Instructions page** before start.
- Collapsible/scrollable palette; current-question highlight.
- **Process-death-safe timer** and answer autosave (already implemented).
- Question-level: bookmark (PracticeMock), optional "report error".

**TGP state today:** palette + timer + autosave + resume are done. Missing vs standard: explicit 5-state semantics labels, Save&Next vs Mark&Review distinction, section tabs, instructions screen, bookmark, Clear Response.

---

## 3. Results & analytics UX

Proven components to build:

- Header: **Score, Rank (AIR/state), Percentile, Accuracy, Time Taken**.
- Attempt breakdown: **Correct / Wrong / Skipped / Attempted**.
- **Section-wise** scores & accuracy.
- **Topic-wise** strength (Very Strong → Very Weak) — needs `topic`/`conceptKey` (our data has none yet).
- **Time per question** and **time on correct vs wrong vs unattempted**.
- **Compare with topper** (score + time).
- **Exam Hall Mode / reattempt** the same paper.
- **Leaderboard** (top rankers for that test) — needs backend aggregation.
- **Solutions review**: per-question your answer vs correct vs explanation (bilingual).
- **Percentile formula**: `(candidates with score ≤ yours / total candidates) × 100` (report to 7 decimals).

**TGP state today:** basic result (score, "Your answer / Correct"). Analytics is the biggest
value-add gap.

---

## 4. Information architecture & navigation

Common successful IA:
- **Home/Dashboard**: current exam date countdown, resume test, daily CA, quick practice, big exam tiles.
- **Tests**: exam-wise → stage → year → paper; also sectional/topic/practice modes.
- **Current Affairs**: daily digest + quizzes + monthly PDF.
- **Analysis/Performance**: progress over time, weak areas.
- **Profile**: language, notifications, purchases/restore, help.

**TGP state today:** bottom nav = Home · Tests · Current Affairs · Profile (matches).
Home dashboard = hero stats + track tiles + Explore rows (good). Remaining: add
**Performance/Analysis** surface and **exam-date countdown**; CA + Profile are placeholders.

---

## 5. Monetization / gating (recommended for TGP)

- **Free**: 2–3 demo papers (e.g. one per track/stage) with full result + solutions to establish trust.
- **Paid pass options** (pick 1–2):
  - Track pass: **All Constable** or **All SI** — ₹399–₹599.
  - All-access annual: **₹799–₹999/yr** (anchor ₹2,999+).
  - Single paper one-time: **₹49–₹99** (impulse).
- Enforce **server-side** (Appwrite `entitlements` table, server-only write) — client only reflects.
- Use **RevenueCat free tier** on top of **Play Billing** for verify/grant/refund/restore.
- Show **Restore Purchases** and honor **7-day money-back** (market norm).
- **Anti-pattern to avoid:** Testbook SuperPass reviews show users bounced to paywall *before reaching home*. Never hard-gate app entry; let users browse and hit the paywall at the point of value.

**Pricing anchors (2026):** Testbook ₹299 promo/₹749–1199 list; ExamBook ₹99/₹199/₹999;
Test Series Pro ₹599/yr; Prepp ₹249/yr; MPSC ₹299–499. TGP is niche (one exam) → lean
**low price + high volume**, or one-time bundle because the content set is finite.

---

## 6. Engagement & gamification

- **Streaks** (daily practice), **XP/levels**, **badges**.
- **Leaderboards**: global / monthly / streak-based; per-test rank.
- **Daily quiz / Daily Current Affairs** (habit loop).
- **Exam-date countdown + reminders** (push).
- **Progress tracker** ("you improved X% in Reasoning since last month").
- **Push notifications**: new paper, exam date, result/rank, streak reminder — but **low frequency** (excess notifications is a top complaint).

---

## 7. Architecture & tech (validates our choices)

Best-in-class pattern: **offline-first, MVVM/UDF, Hilt DI, multi-module, Room cache,
WorkManager sync, Compose UI, navigation component**. Competitors emphasize:
- Offline download of tests/PDFs.
- Fast test load (content cached locally).
- Bilingual content model.
- Server-side entitlement + ranking/percentile services.
- Admin question management + CSV import.

**TGP state today:** exactly this stack (Kotlin/Compose, Hilt, Room v2 cache, Nav3,
Appwrite TablesDB sync, server-side content). On track. Gaps: `core:datastore` (prefs),
auth module, entitlement module, analytics/ranking backend, admin surfaces, WorkManager
for scheduled sync/CA.

---

## 8. Design system & accessibility

- **Typography**: large, readable; Telugu script needs a proper bundled font and extra line-height.
- **Contrast** WCAG AA; **dark mode**; **dynamic color** optional (M3).
- **Low-end devices**: light UI, minimal images, caching.
- **Bilingual toggle** prominent (not buried).
- **Reduced motion** respected.
- Simplicity wins: cluttered UI is the #1 review complaint (Adda247). Keep test screen chrome minimal.

---

## 9. TSLPRB exam facts (drives engine config)

### Telangana Police Constable (2026: ~6,180 posts)
- **Prelims (PWT)**: 200 Q / 200 marks / 3 hrs.
  - English, Arithmetic Ability & Test of Reasoning — 100 Q / 100 marks
  - General Studies — 100 Q / 100 marks
  - **NO negative marking.**
- Then PMT + PET (1600 m men / 800 m women + long jump + shot put), Final Written Exam, DV, Medical.
- Languages: English, Telugu, Urdu.

### Telangana Police SI (2026: ~700–1,000+ posts)
- **Prelims**: 200 Q / 200 marks / 3 hrs.
  - Arithmetic Ability & Test of Reasoning / Mental Ability — 100 Q / 100 marks
  - General Studies — 100 Q / 100 marks
  - **Negative marking −0.20 per wrong answer.** Min qualifying: OC 40%, BC 35%, SC/ST/Ex-serv 30%.
- Then PMT + PET, **Final Written Exam**:
  - Paper 1 English (Part A 25–50 objective Q / 25 marks; Part B descriptive 75) — **negative marking 1/4 per wrong (Part A)**
  - Paper 2 Telugu/Urdu (same split)
  - Paper 3 Arithmetic & Reasoning — 200 Q / 200 marks
  - Paper 4 General Studies — 200 Q / 200 marks
- Official: `tspolice.gov.in` / `tslprb.in`.

**Action:** model marking per paper (`negativeMarks`, `sectionMarks`, qualifying %), not globally.

---

## 10. Gap analysis — TGP-APP vs market

| Area | Status | Action |
|---|---|---|
| Exam engine (timer/palette/resume) | Strong | Add 5-state labels, Save&Next, Mark&Review, section tabs, instructions, Clear Response, bookmark |
| Marking accuracy | ❌ Wrong default | Add per-paper `negativeMarks` (SI prelims 0.20, SI mains P1 0.25, Constable 0) |
| Results | Basic | Build analytics (see §3) |
| Rank / percentile / leaderboard | ❌ | Backend aggregation + `results` table |
| Topic/concept analytics | ❌ (no topic data) | Derive topics from `solution.rule` (Repetition & Weightage module) |
| Home dashboard | Good | Add exam-date countdown + resume-test + daily CA |
| Current Affairs | Placeholder | Build CA module (Appwrite `daily_ca`) |
| Profile | Placeholder | Language, purchases/restore, notifications, help |
| Auth | ❌ | Phone OTP / Google via Appwrite |
| Monetization | ❌ | Play Billing + RevenueCat + `entitlements` |
| Onboarding | ❌ | Language-first, exam selection, value prop |
| Gamification | ❌ | Streaks, XP, badges, daily quiz |
| Push | ❌ | Appwrite Messaging / FCM, low-frequency |
| Offline-first | ✅ | Keep; add WorkManager scheduled sync |

---

## 11. Prioritized roadmap (post-research)

1. **M4a — Marking engine correctness** (per-paper negative marking + qualifying) — cheap, high impact.
2. **M4b — Auth** (Appwrite phone OTP + Google) + `core:datastore` (prefs, language).
3. **M4c — Entitlements + Play Billing/RevenueCat** + paywall + restore.
4. **M4d — Results analytics v1** (accuracy, time, section-wise, solutions review).
5. **M5 — Onboarding** (language-first) + Profile + Current Affairs real.
6. **M6 — Rank/percentile backend + leaderboard** (needs user base) + push.
7. **M7 — Admin panels** (in-app + web) → Daily CA publisher.
8. **M8 — Repetition & Weightage module** (topic derivation) → topic-wise analytics.
9. **M9 — Gamification** (streaks/XP/badges) + Play Store release.

---

## 12. Source links

- Testbook Play listing: https://play.google.com/store/apps/details?id=com.testbook.tbapp
- Oliveboard AI mock analysis: https://www.oliveboard.in/blog/ai-based-mock-test-analysis
- TS Police SI pattern: https://www.careerpower.in/telangana-police-si-syllabus.html
- TS Police Constable pattern: https://www.careerpower.in/telangana-police-constable-syllabus.html
- ExamBook pricing: https://exambook.in/subscription
- Test Series Pro pricing: https://testseriespro.com/
- MockApp (analytics messaging): https://mockapp.in/
