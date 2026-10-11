# STATUS — TGP-APP

> Session/state doc. Read this first on session start to resume.
> Last updated: 2026-10-11

---

## Current phase
**Auth + onboarding LIVE (email/password against Appwrite).** First launch now opens a 3-page bilingual
onboarding (Welcome → Language EN/తెలుగు → Exam Constable/SI/Both), then an Auth screen (sign in / sign up,
Google button = "coming soon", guest skip), then the Home dashboard. Profile is real: signed-in card (name/email/
initials) + **Sign out**, or guest sign-in CTA. `onboardingCompleted` + language + track persist in DataStore.
**App shell LIVE (Home + bottom nav)** — Home dashboard (hero stats, per-track tiles, Explore rows) + M3 bottom nav
**Home · Tests · Current Affairs · Profile**; catalog under **Tests**; Current Affairs still a placeholder; exam
screens hide the bottom bar. Content streams Appwrite → Room (asset fallback).
**Deep competitive-exam-app research DONE** → `docs/competitive-exam-apps-research.md` (landscape, CBT/analytics
UX, IA, pricing, gamification, architecture, design, TSLPRB exam facts, gap analysis, prioritized roadmap).
**Marking correctness LIVE (per-paper `positiveMarks`/`negativeMarks`).** SI prelims = **−0.20**, SI mains = **−0.25**
(provisional), Constable = **0** — plumbed through normalize → Appwrite columns → Room (v3) → engine, and shown on the
exam intro ("Marking: +1 per correct · −0.20 per wrong"). Next: Play Billing/RevenueCat entitlement + results analytics.

### Verified on emulator (2026-10-11)
Onboarding page 1→2→3 renders; **live Telugu switch** flips the whole UI; Skip **persists** `onboardingCompleted`
(relaunch skips onboarding); **sign-up** creates a real Appwrite account (`qa…@example.com`); **sign-in** reaches
Home; guest skip reaches Home; Profile shows signed-in user + **Sign out** reverts to guest; sign-in works from the
Profile tab and survives relaunch. **0 crashes** after fixing 2 Nav bugs (see Done).
**Marking verified end-to-end (2026-10-11)**: SI Prelims intro shows "−0.20 per wrong"; answering Q1 wrong → result
score **−0.20** ("Your answer B · Correct A"). Constable intro shows "no negative marking"; wrong answer → score
**0.00**. Remote `papers` rows carry `positiveMarks=1` / `negativeMarks` 0 (Constable), 0.2 (SI prelims), 0.25 (SI mains).

## Decisions (LOCKED)
- App: **Kotlin + Jetpack Compose** (Android only)
- Backend: **Appwrite Cloud (free)** — auth, DB, functions, storage, messaging
- Payments: **Google Play Billing + RevenueCat (free tier)** — auto-verify/grant/refund
- Content model: **Appwrite DB as source of truth + Room cache**, delta sync by `updatedAt`
- Admin: **in-app (role-gated) + web panel**
- Canonical schema: TG-Police bilingual format (see PRD §5)
- Initial content: **ONLY** TG-Police (15 papers / 3,000 Q)

## Folder structure
```
/home/kali/TGP-APP/
├── PRD.md          ← product spec (read PRD.md too)
├── STATUS.md       ← this file
├── opencode.json   ← opencode config (loads STATUS.md + PRD.md as instructions)
├── appwrite.config.json ← Appwrite schema-as-code: projectId `tgp`, endpoint fra, 1 DB (`tgp`), 10 tables
├── docs/            ← competitive-exam-apps-research.md (deep market/UX/IA/pricing/TSPLRB research)
├── .opencode/
│   └── skills/     ← 19 agent skills (11 vendored Google + 8 custom TGP) — see below
├── gradle/         ← version catalog (libs.versions.toml) + wrapper (9.4.1)
├── settings.gradle.kts / build.gradle.kts / gradle.properties / local.properties
├── app/            ← Android app module (MainActivity, TgpApplication, resources)
├── core/
│   ├── model/          ← pure domain models (Paper/Question/Exam/PaperSummary, AuthUser, Bilingual)
│   ├── common/         ← TgpResult, DispatcherProvider (+Hilt module)
│   ├── database/       ← Room: ExamSession/AttemptAnswer + cached_papers/cached_questions, DAOs, v2 + migration
│   ├── network/        ← Appwrite REST clients: TablesDB (content) + Account (auth) — HttpURLConnection, no SDK
│   ├── domain/         ← repo interfaces (listPapers, AuthRepository, SessionRepository, UserPreferencesRepository), engine (ExamTime, ScoreCalculator)
│   ├── data/           ← AssetPaperRepository + CachedPaperRepository + AppwriteContentSyncer + AppwriteAuthRepository, DTOs/mappers, DI
│   ├── datastore/      ← DataStore prefs: uiLanguage, selectedTrack, onboardingCompleted, session cookie
│   └── designsystem/   ← TgpTheme, Color/Type/Shape/Spacing tokens, TgpBrand, bundled Noto Sans Telugu font
├── feature/
│   ├── exam/           ← ExamViewModel(assisted paperId)/ExamUiState, runner + palette + result
│   ├── catalog/        ← CatalogViewModel/CatalogScreen (papers grouped by track/stage) — "Tests" tab
│   ├── home/           ← HomeViewModel/HomeScreen (dashboard: hero stats, track tiles, Explore rows)
│   └── auth/           ← Onboarding + Auth + Profile screens/ViewModels (Appwrite email/password)
│   (app) shell/        ← BootstrapViewModel, Placeholders.kt (Current Affairs tab)
├── raw/
│   └── tg-police/Previous Papers/    ← 15 papers, 3,000 Q (untouched)
├── normalized/              ← canonical schema, validated (15 papers + _index.json + _report.json) — generated
├── core/data/src/main/assets/papers/ ← bundled app content (15 papers + index.json) — generated
├── tools/importer/
│   ├── normalize.py         ← raw→normalized (canonical + validation + contentHash), idempotent
│   ├── build_assets.py      ← normalized→app assets (strips source/officialKey, writes catalog index)
│   └── push_appwrite.py     ← normalized→Appwrite TablesDB (upsert by rowId / unique index, resumable) ✓ done
```
Planned: `feature:result/paywall/admin`, web admin panel.

## Appwrite Cloud backend (LIVE, 2026-10-11)
- **Project:** `tgp` — TGP Mock Exam · region **fra** · endpoint `https://fra.cloud.appwrite.io/v1` · plan free (tier-0)
- **Account:** `bonthavinodkumar999@gmail.com` (owner). Browser login: `appwrite login` on this machine.
- **1 database `tgp`** with **10 tables**: `papers`(15 cols/2 idx), `questions`(16/3), `profiles`, `entitlements`,
  `attempts`, `results`, `app_config`, `daily_ca`, `notifications`, `audit_log` — permissions: content tables
  `read(any)` / no client writes; user-owned tables create/read by `users` (+rowSecurity); `audit_log` server-only.
- **Rows pushed:** 15 papers + **3,000 questions** (26 questions stored with `answer: -1` = key unknown: 23 `answer:0`
  + 3 zero-option broken Q preserved). `app_config` seeded (`app.info`, `sync.cursors`).
- **Server API key:** `tgp-server` (49 scopes) — plaintext stored **OUTSIDE the repo** at
  `~/.config/tgp/appwrite.json` (chmod 600). Env `APPWRITE_API_KEY` also works. **Never commit keys.**
- **Android platform:** `com_tgp_mockexam` → package `com.tgp.mockexam` (for app Auth later).
- **Schema is source-controlled:** edit `appwrite.config.json`, then `appwrite push tables --all --force` (idempotent).
  Requirement note: no `default` on `required` columns (CLI rejects), unique index on `questions(paperId,n)` drives
  batch upsert, column key `answer` uses `-1` for unknown.
- **Idempotent import:** re-run `python3 tools/importer/push_appwrite.py` — unchanged papers skip in <1s
  (contentHash + question-count check); full re-push ≈ 35 s.

## App-side content sync (LIVE, 2026-10-11)
- **`:core:network`** — `TablesClient` + `HttpTablesClient` (HttpURLConnection, no Appwrite SDK), `TablesQuery`
  (`equal`/`offset`/`limit`, `encodeDefaults=true`). **Guest-readable**: only the `X-Appwrite-Project: tgp` header is
  needed for content tables.
- **Pagination gotcha:** the rows endpoint ignores `limit`, `offset`, and `cursor` *params*; page max is **25** and
  paging must be sent as a query object — `queries[1]={"method":"offset","attribute":"","values":[25]}`. Param names
  are URL-encoded (`queries%5B0%5D=`).
- **Room cache** (`core:database`, v2 + `MIGRATION_1_2`): `cached_papers` (id PK) + `cached_questions`
  ((paperId,n) PK) mirror the Appwrite columns; `ContentDao` (`@Upsert`, Flow observers).
- **`AppwriteContentSyncer`** (`core:data`): pulls papers (1 request), then per-paper questions paginated by offset;
  skips a paper when cached `contentHash` matches **and** cached question count ≥ `totalQuestions`. Runs on app
  start (`TgpApplication`), mutex-guarded, fire-and-forget safe.
- **`CachedPaperRepository`** (`PaperRepository` bind): Room-first, **asset fallback** on empty cache/first launch,
  triggers a background sync; features unchanged (they depend on the `PaperRepository` interface).
- **Verified on emulator**: fresh DB → sync wrote 15 papers + 3,000 Q (~56 s); relaunch → `questions fetched: 0`
  (~200 ms, skip works); catalog renders 15 bilingual papers; exam runner shows Q1/200 from cache; manifest has `INTERNET`.
- Unit tests: `TablesQueryTest` (query encodings + row decode) and `AppwriteContentSyncerTest` (pagination, skip,
  answer −1→unknown mapping) — 8/8 green with `assembleDebug`.

## Toolchain (verified 2026-10-10)
- **AGP 9.2.0** + **Gradle 9.4.1** (wrapper cache `~/.gradle/wrapper/dists/gradle-9.4.1-bin`).
- **Kotlin 2.4.21** with AGP 9 **built-in Kotlin** (do NOT apply `org.jetbrains.kotlin.android`; KGP pinned
  via root `buildscript` classpath). Compose compiler plugin `org.jetbrains.kotlin.plugin.compose` still applied.
- **compileSdk 37 / targetSdk 37 / minSdk 26**, Compose BOM **2026.09.00**, material3 **1.4.0** (M3 Expressive).
- Android SDK `/opt/android-sdk`: platforms **android-37.0 / 36 / 35 / 34**, build-tools **37.0.0 / 35.0.0 / 34.0.0**, ndk, emulator.
- JDK: default `java` is **25** (too new). Gradle/AGP runs on **JDK 21** (`org.gradle.java.home=/usr/lib/jvm/java-21-openjdk-amd64`).
- Build: `./gradlew :app:assembleDebug` → `app/build/outputs/apk/debug/app-debug.apk` (verified).
- `android` CLI **not installed** — install per the `android-cli` skill if we want create/run/screenshot tooling.
- Emulator AVD `tf_test` exists; system-images under SDK.
- **Gotcha:** the Gradle wrapper's 10s download timeout fails on this network — distributions were seeded
  manually into `~/.gradle/wrapper/dists`. Use `curl` for any new Gradle distribution.

## Agent skills (`.opencode/skills/`) — for top-tier output
**Vendored from Google's official `android/skills` repo** (frontmatter sanitized for opencode):
- `navigation-3`, `navigation-event`, `adaptive`, `edge-to-edge`, `testing-setup`,
  `android-cli`, `r8-analyzer`, `play-policy-insights`, `play-billing-library-version-upgrade`,
  `android-intent-security`, `android-permissions-security`
**Custom TGP skills:**
- `tgp-design-system` — brand palette + M3 Expressive color/type/shape/elevation, module layout
- `tgp-motion` — M3E motion schemes + spring tokens, Compose animation, shared elements, predictive back
- `tgp-icons-images` — Material Symbols, Coil 3, launcher/themed icon, Telugu fonts, offline images
- `tgp-architecture` — UDF/MVVM, Hilt, module layout, repos, nav, Kotlin conventions
- `tgp-exam-engine` — session state machine, process-death-safe timer, palette, scoring, anti-cheat
- `tgp-data-sync` — Appwrite schema + Room, delta sync by `updatedAt`, free-tier limits
- `tgp-content-pipeline` — raw→normalized→app, canonical schema, validation, versioning, legal
- `tgp-monetization` — Play Billing + RevenueCat, entitlements, paywall UX, restore/refund

> Skills load at opencode startup — **restart opencode** after editing any `SKILL.md`.
> Source of vendored skills: `/tmp/opencode/android-skills` (re-pull from
> `github.com/android/skills` to refresh).

---

## Source content assets (not yet in project)

### `/home/kali/TG-Police` → copied to `raw/tg-police`
- **15 papers / 3,000 questions**, bilingual EN+TE, with solutions
- Constable: prelims+mains 2015, 2018, 2022
- SI: prelims+mains 2016, 2018, 2022/2023
- Schema already canonical (see PRD §5). All `reviewPending: true`.

### `/home/kali/tspc` (270M) — DEFERRED
- **156 papers / 60,597 questions / 253,237 options**, 8,232 JPGs
- Categories: constable, si, asi, app (+ ebooks, qb_packages)
- `TelanganaPolice/INVENTORY.md`, `json/_index.json`, `notifications.json` (exam patterns + TSLPRB history)
- Scripts: `scripts/tgq.py`, `scripts/dump_paper.py`
- Provenance: scraped via SQLi from toppersexam.com → **legal caveat**

### `/home/kali/wrexa` (11M) — DEFERRED
- `docs/` — 10 full blueprints reverse-engineered from wrexaedge.com (PRD, SDD, API, UX, QA, roadmap, gating matrix, security, content ops, BRD) — **use as reference**
- `ts-police-si/` — 50 mocks (3,250 Q) + 60 packs (1,495 Q)
- `ts-police-const/` — same structure
- Free/pro tiers already tagged

---

## Done
- [x] Recon of all content assets (tspc, wrexa, TG-Police)
- [x] Backend research → chose Appwrite (free) over Firebase/Supabase
- [x] Payment research → chose RevenueCat free tier (auto-verify)
- [x] Locked initial content = TG-Police only
- [x] Created `raw/` + copied TG-Police
- [x] Wrote PRD.md + STATUS.md
- [x] Logged future "Repetition & Weightage" module
- [x] Deep research: Android arch (nowinandroid), M3 Expressive, Nav3, Coil 3, official Google skills
- [x] Vendored 11 official Google Android skills + wrote 8 custom TGP skills (19 total)
- [x] Wired `opencode.json`; verified toolchain (SDK/Gradle/JDK)
- [x] Scaffolded Android app (`app` + `core:model` + `core:designsystem`) — `assembleDebug` green
- [x] Migrated to AGP 9.2.0 + Gradle 9.4.1 + built-in Kotlin; installed SDK platforms 36/37 + build-tools 37
- [x] Added `core:common`/`database`/`domain`/`data` + `feature:exam` with Hilt (KSP) + Room — `assembleDebug` green
- [x] **Exam engine working end-to-end on the emulator**: start → answer → process-death resume → palette → submit → scored result
- [x] Unit tests for `ExamTime` + `ScoreCalculator` (green); fixed paused-time math and 1↔0-indexed answer bug
- [x] Content importer `tools/importer/normalize.py`: raw→`normalized/` canonical schema, 15 papers / 3,000 Q, validation + `_report.json`, `contentHash`, idempotent (re-run → 0 changes). 0 errors, 117 warnings.
- [x] `tools/importer/build_assets.py`: `normalized/` → `core/data/src/main/assets/papers/` (15 papers + `index.json`, 7.0 MB, strips `source`/`officialKey`)
- [x] Content loader: `core:data` `AssetPaperRepository` + kotlinx.serialization DTOs; maps canonical `answer` **1-based → `answerIndex` 0-based**, `answer:0` → `UNKNOWN_ANSWER_INDEX=-1` (not scored)
- [x] `feature:catalog`: `CatalogScreen` groups papers by track → stage → year (bilingual titles, meta)
- [x] **Navigation 3** wired: `MainActivity` `NavDisplay` + `rememberNavBackStack` (`CatalogRoute` → `ExamKey(paperId)`); `ExamViewModel` now Hilt **assisted factory** keyed by `paperId`
- [x] **Verified on emulator**: catalog lists 15 real papers → Constable Prelims 2015 intro (200 Q/200 marks/180 min) → runner (real bilingual Q + options) → answer persists (Room) → submit → **1/200 "Your answer D · Correct D"** (canonical answer 4 ✓) → back returns to catalog
- [x] **Appwrite Cloud project created via console REST** (browser OAuth not scriptable): account `bonthavinodkumar999@gmail.com`, org "Personal Projects", project `tgp` (region `fra`), Android platform `com_tgp_mockexam`, server API key `tgp-server` (49 scopes), delivered via account-session cookies + `/v1/projects/{id}/keys`.
- [x] **Appwrite schema-as-code deployed** (`appwrite.config.json` + `appwrite push tables --all --force`): DB `tgp`, 10 tables / 70 columns / 10 indexes, prod permissions (content read-only, user-owned + rowSecurity, audit_log server-only).
- [x] **Content pushed to Appwrite** (`tools/importer/push_appwrite.py`): 15 papers + 3,000 questions upserted (idempotent, resumable, batched 100/req); verified remote totals; API key stored outside repo at `~/.config/tgp/appwrite.json`.
- [x] **Appwrite→Room delta sync LIVE** (`core:network` + `core:data`): guest REST client (no SDK), Room v2 cache (`cached_papers`/`cached_questions`), `AppwriteContentSyncer` (offset pagination + contentHash skip), `CachedPaperRepository` (Room-first, asset fallback). Verified on emulator: 15 papers + 3,000 Q synced in ~56 s; relaunch skips (~200 ms); UI serves from cache. 8/8 unit tests green.
- [x] **App shell LIVE**: new `feature:home` module (`HomeViewModel`/`HomeScreen` dashboard: hero stats + per-track tiles + Explore rows) and `app`-level Material 3 **bottom nav** (Home · Tests · Current Affairs · Profile). `MainActivity` routes are Nav3 keys: `HomeRoute`/`TestsRoute`/`CurrentAffairsRoute`/`ProfileRoute` + `ExamKey(paperId)`; tab select resets the backstack, **bottom bar hidden on exam + non-top-level routes** (`current in topLevelRoutes`). CA/Profile are `shell/Placeholders.kt`. Verified on emulator: Home shows 15 papers/3,000 Q (Constable 6/1200, SI 9/1800), tabs switch, Tests→paper→exam→back works, bottom bar hidden on exam, no crashes; `assembleDebug` green.
- [x] **Deep competitive-exam-app research** → `docs/competitive-exam-apps-research.md` (Testbook/Adda247/Oliveboard/PracticeMock feature sets, CBT interface parity, result-analytics UX, IA, 2026 pricing anchors, gamification, architecture, design/accessibility, TSLPRB exam facts, gap analysis, prioritized roadmap §11). Key finding: **per-paper negative marking** needed (SI prelims −0.20, SI mains P1 −0.25, Constable 0).
- [x] **Onboarding + Auth LIVE (verified on emulator, 0 crashes)**: `feature:auth` (3-page bilingual onboarding via `HorizontalPager` + real auth screen) + new `core:datastore` (DataStore prefs: language, track, `onboardingCompleted`). `core:network` gained `HttpAccountClient` (Appwrite email/password over `HttpURLConnection`, session via `a_session_tgp` cookie), `core:data` `AppwriteAuthRepository`. `MainActivity` now splash-gated (`BootstrapViewModel` → Onboarding vs Main). Verified end-to-end: page 1→2→3, live Telugu UI switch, Skip persists `onboardingCompleted` (relaunch skips), **sign-up created a real Appwrite account**, sign-in → Home, guest skip → Home, Profile signed-in card + **Sign out**, sign-in from Profile persists across relaunch. Fixed 2 Nav bugs: (a) empty-backstack crash on auth completion (`finishAuth`), (b) Skip not persisting the flag. Bundled Noto Sans Telugu variable font; brand shield mark.
- [x] **Appwrite Account REST verified live**: `POST /v1/account`, `POST /v1/account/sessions/email` → `a_session_tgp` cookie (URL-encoded base64 `{id,secret}`), `GET /v1/account`. Test account `qa…@example.com` created during sign-up verification.
- [x] **Marking correctness LIVE** (per-paper `positiveMarks`/`negativeMarks`): SI prelims −0.20, SI mains −0.25 (provisional), Constable 0. Values set in `normalize.py` `_MARKING` table (override via raw `positiveMarks`/`negativeMarks`), carried to `normalized/` + assets + Appwrite (`papers.positiveMarks`/`negativeMarks` double columns), Room `cached_papers` (**v2→v3** `MIGRATION_2_3`), network `RemotePaperRow`, DTOs/mappers, and `Paper`/`PaperSummary`. Exam intro shows the scheme. Verified on emulator: SI prelims wrong → **−0.20**, Constable wrong → **0.00**; 14 unit tests green.

## Content data notes (from normalization, 2026-10-11)
- 15 papers, 3,000 questions, all `reviewPending: true`. `series` = paper letter (A/B/C/D), not "TG Police".
- Use metadata `year` (folder names lie: `SI/2015/mains/si-mains-2016-*.json` → year 2016; `SI/2022/...-2023-*` → 2023).
- SI papers split by section file: `-ar` (Arithmetic & Reasoning) and `-gs` (General Studies) → distinct `id`s/`paperNo`.
- **23 questions have `answer: 0`** = key unknown (needs QA). App must treat 0 as "no key" (don't score).
- **3 questions broken** (SI/2018 mains `si-mains-2018-ar` n=189,190,191): 0 options. Flagged.
- **90 questions missing `prompt.te`** (Constable 2015/2018 early Qs) — bilingual gap, flagged.
- Per-question extra fields preserved in normalized: `table` (252), `diagramSvg` (78), `solutionSvg` (83) — app cannot render these yet (follow-up).
- Raw has no `topic`/`conceptKey`; future weightage module derives them from `solution.rule`.

## Next steps (in order)
1. [x] Scaffold Android app: Kotlin + Compose skeleton (module layout per `tgp-architecture`)
2. [x] Core modules: `common`/`domain`/`data`/`database`/`network` done; **next** `core:datastore` (prefs)
3. [x] Build exam engine (runner) — timer, palette, review, auto-submit, offline (`tgp-exam-engine`)
4. [x] Build importer: `normalize.py` + `build_assets.py` + `push_appwrite.py` all done (raw→normalized→assets→Appwrite)
5. [x] Create Appwrite project (tables per PRD §7, roles, server key) — Cloud LIVE, 10 tables, 3,000 Q pushed
6. [x] Catalog + content loader (assets) + Navigation 3 **done**; **Appwrite→Room delta sync done** (`tgp-data-sync`); **app shell (Home + bottom nav) done**
7. [x] Marking correctness (per-paper `positiveMarks`/`negativeMarks`: SI prelims −0.20, SI mains −0.25, Constable 0) **done**; **TODO** results analytics (rank/percentile, section/topic breakdown)
8. [~] Auth **done** (email/password LIVE, guest, Profile sign-in/out, DataStore prefs); **TODO** Google OAuth + phone OTP + Play Billing/RevenueCat + entitlement (`tgp-monetization`)
9. [~] Onboarding (language-first) + Profile **done**; **TODO** real Current Affairs (still placeholder)
10. [ ] Rank/percentile backend + leaderboard + push → Admin panels → Repetition & Weightage → Play Store release
11. [ ] Replace placeholder brand-vector "hero" art with **real imagery** (source/licensing TBD) + polish onboarding/auth visuals

> Roadmap detail: `docs/competitive-exam-apps-research.md` §11 (prioritized roadmap).

## Open questions
- Bundle pricing (₹)? Free demo count?
- Play account type: personal vs organization?
- Default UI language: Telugu-first or English-first?
- Who QAs the 3,000 reviewPending questions?
- Confirm scope order: engine first vs importer first?
- **Confirm SI mains negative marking = −0.25** (currently provisional: TSLPRB documents 1/4 only for the English Paper-1 Part A objective; our SI-mains papers are AR + GS). Change the `_MARKING` table in `normalize.py` if different.
- **LEGAL BLOCKER (paid release):** `raw/tg-police/` origin/licence is undocumented → see `raw/tg-police/PROVENANCE.md` + PRD §11.1. Explanations are third-party authored; must licence, source official/RTI-clear, or rewrite in-house. Never ship deferred scraped sets (`tspc`/`wrexa`).

## Key constraints / gotchas
- **`Question.answerIndex` is 0-based** (option index). Canonical JSON `answer` is **1-based** → `AssetPaperRepository` maps `answer - 1`; `answer: 0` (23 Q, key unknown) → `Question.UNKNOWN_ANSWER_INDEX = -1`, and `ScoreCalculator` skips questions with `!hasAnswer`.
- **App content is bundled assets** (`core/data/src/main/assets/papers/`, ~7 MB) — generated by `build_assets.py`; replaces the old hardcoded demo. The app now prefers the **Room cache synced from Appwrite** (`CachedPaperRepository`), falling back to these assets on first launch/offline.
- **Appwrite content is guest-readable**: `GET /tablesdb/tgp/tables/{papers|questions}/rows` needs only the `X-Appwrite-Project` header (no key/session). Pagination/query must be a JSON object in `queries[i]` (URL-encoded key), `limit` caps at 25/page; the plain `offset`/`cursor`/`limit` params are ignored by the rows endpoint.
- **Room cache migration**: `TgpDatabase` is v3 with `MIGRATION_1_2` (adds `cached_papers`, `cached_questions`) + `MIGRATION_2_3` (adds `cached_papers.positiveMarks`/`negativeMarks`); `DatabaseModule` calls `.addMigrations(MIGRATION_1_2, MIGRATION_2_3)` (do NOT use destructive fallback — it would wipe exam sessions).
- **Hilt ViewModel with Nav args**: `ExamViewModel` uses `@AssistedInject` + `@AssistedFactory`; obtained via `hiltViewModel(creationCallback = { it.create(paperId) })` from `androidx.hilt:hilt-lifecycle-viewmodel-compose` (NOT the deprecated `hilt-navigation-compose` package).
- **Navigation 3**: routes are `@Serializable … : NavKey`; `NavDisplay` + `rememberNavBackStack(initial)` + decorators `rememberSaveableStateHolderNavEntryDecorator()` + `rememberViewModelStoreNavEntryDecorator()`. Deps: `navigation3-runtime`, `navigation3-ui`, `lifecycle-viewmodel-navigation3`; app applies `kotlin-serialization`.
- **Navigation 3 — backstack must NEVER be empty**: `NavDisplay` throws `IllegalArgumentException: NavDisplay backstack cannot be empty`. Never `clear()`-then-`add()` or `removeLastOrNull()` to an empty list. Swap the top entry in place instead (`backStack[lastIndex] = route`) and guard `onBack = { if (backStack.size > 1) removeLastOrNull() }`.
- **Onboarding must persist via the ViewModel**: the Skip button must call `viewModel.finish(onDone)` (which writes `onboardingCompleted=true`), NOT the raw `onFinish` — otherwise relaunch shows onboarding again.
- **Appwrite auth session cookie**: `POST /v1/account/sessions/email` returns cookie `a_session_<projectId>` (= URL-encoded base64 JSON `{id,secret}`); resend it as `Cookie: a_session_tgp=<value>` on `GET /v1/account`. Account creation needs an explicit `userId`.
- **Engine persistence**: session + answers live in Room (`core:database`); timer derives from persisted `startedAtRealtimeMs`/`startedAtEpochMs`, so it survives process death (verified). Answers autosave (debounced 250 ms).
- **Device test recipe** (headless, works): boot `emulator -avd tf_test -no-window -gpu swiftshader_indirect -accel auto`; drive with `adb shell input touchscreen swipe X Y X Y 120` (plain `input tap` is dropped on the slow renderer); inspect via `adb shell uiautomator dump`; read DB via `adb exec-out run-as com.tgp.mockexam cat databases/tgp.db*` (WAL — pull all 3 files), then open with host Python `sqlite3` (no `sqlite3` CLI installed).
- **Top bar / bottom bar insets**: engine screens use `statusBarsPadding()` / `navigationBarsPadding()` (edge-to-edge); without them the Submit button sits under the status bar and is untappable.
- Appwrite free: **only 2 functions/project** (Jan 2026), 1 DB, 1 bucket, pauses after ~1 week idle
- **Appwrite TablesDB REST** (used by pusher + future sync): upsert row = `PUT /v1/tablesdb/{db}/tables/{t}/rows/{rowId}`; batch upsert = `PUT …/rows` with body `{"rows":[…]}` matched on unique index `(paperId,n)`; list-filter = `GET …/rows?queries[0]=<json query>` returns `total`; **no `default` allowed on `required` columns** in `appwrite.config.json` (CLI rejects); scopes for TablesDB are `tables/columns/indexes/rows.read|write` (not `tablesdb.*`).
- Play: $25 one-time, 15–30% commission; personal account needs 12 testers × 14 days
- Legal: scraped content (tspc/wrexa) — verify/rewrite before commercial resale
- Build: default `java` is 25 (breaks AGP) → point Gradle at JDK 17/21; set `org.gradle.java.home`
- opencode: skills/config are NOT hot-reloaded → **restart opencode** after editing them
