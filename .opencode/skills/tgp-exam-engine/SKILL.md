---
name: tgp-exam-engine
description: Use when implementing or modifying the TGP-APP test/exam runner. Covers the exam session state machine, authoritative timer with process-death survival, question palette states, bilingual toggling, mark-for-review, auto-submit, offline operation, scoring, resume, and anti-cheat/anti-tamper guardrails.
---

# TGP Exam Engine

The exam runner is the core product. It must be rock-solid: a lost answer or a
timer that stops is a refund. Design it as an explicit state machine persisted to disk.

## Session lifecycle

`NotStarted → InProgress → (Paused) → Submitted → Scored → Reviewed`

- `Paused` is only for user-initiated pause (if the paper allows) or app background; the timer
  policy (continue vs pause) is defined per exam config. Default: **timer keeps running in background**
  (real exam conditions) but state persists.
- Submitting is idempotent. Auto-submit triggers on time expiry.

## Persistence (process-death safe)

- The session lives in `core/database` (Room): `ExamSessionEntity`, `AttemptAnswerEntity`.
- **The timer must survive process death.** Do NOT rely on an in-memory coroutine countdown.
  Persist `startedAtEpochMs` + `durationMins` and derive `remaining = duration - (now - startedAt - pausedMs)`.
  Recompute on every foreground entry. This is the single most important rule.
- Use `SavedStateHandle` for ephemeral UI bits; use Room for durable session state.
- Every answer autosaves immediately (debounced ≤300ms) to Room. Never hold answers only in memory.

## Timer

- Authoritative source is wall-clock math from persisted timestamps, displayed via a ticking flow.
- Handle clock changes: prefer `SystemClock.elapsedRealtime()` for elapsed math (monotonic), persisted
  as a baseline alongside `System.currentTimeMillis()`; reconcile after reboot.
- Last-minute cue: color + motion change (see `tgp-motion`), optional haptic; NO disruptive dialog
  until auto-submit.
- Sectional papers: per-section timers if configured; otherwise one global timer.

## Question palette states

Track per-question state explicitly (an enum), mirroring real exams:
- `NotVisited` — neutral/outline
- `NotAnswered` — seen but unanswered (red/rose)
- `Answered` — has a saved answer (green)
- `MarkedForReview` — flagged (amber), regardless of answered
- `AnsweredAndMarked` — answered + flagged (amber with badge)
- `Current` — ring highlight (shape/color, see design + motion skills)

Palette is a grid with a legend and live counts (Answered / Not answered / Marked / Not visited).
Tapping a cell navigates to that question. Never lose state on fast tapping.

## Navigation within exam

- Prev/Next, jump-to-question, and (optionally) section tabs.
- Confirm before leaving the exam (finish vs. resume). Back gesture shows a confirm dialog.
- Direction-aware transition between questions (slide by direction; see `tgp-motion`).

## Bilingual (EN + Telugu)

- Each question/option/solution has `{en, te}`. A global toggle switches language instantly without
  losing state or scroll position. Toggle is prominent and persisted to DataStore.
- Respect the paper's default language; allow per-user override (open question: default EN or TE).
- Never re-fetch or re-parse content to switch language — both are already in the loaded model.

## Scoring

- Score = sum over answered of (correct ? +marks : -(negativeMarks ?: 0)); support per-paper
  marking scheme (marks per question, negative fraction). Handle unattempted = 0.
- Compute locally from the answer key for instant results; the server may re-validate later
  (store `questionVersion` with each attempt for audit).
- Produced result: score, accuracy, attempted/correct/incorrect/skipped, time taken, per-section and
  per-topic breakdown (feeds the future `weightage`/repetition module).

## Offline & reliability

- A downloaded paper must run fully offline (content cached in Room, figures bundled/cached — see
  `tgp-icons-images`). Sync attempts, not the exam, on reconnect.
- Guard against low storage/high memory: stream questions from DB, don't load all 100 into memory
  if avoidable; keep the current window + palette metadata.

## Anti-cheat / integrity (within Play policy)

- Optional `FLAG_SECURE` to block screenshots/screen-recording during the exam (per paper config).
  Do NOT block screenshots in review mode.
- Detect and log backgrounding/return events; do not punish by default (just record).
- Validate content integrity with a checksum on import; reject tampered question sets.
- Do not build features that require rooted-device detection for the MVP; it is fragile and
  Play-hostile. Focus on content integrity + entitlement.

## Testing

- Unit-test the scoring and timer math (fake clock). Test auto-submit at boundary (0s).
- Instrumented: process-death restore (kill app mid-exam, relaunch, verify answers + remaining time).
- Screenshot-test palette states and the final-minute timer state.

## Definition of done

Answers persist across process death, timer is wall-clock-accurate after relaunch, palette states are
correct and legible, bilingual toggle is instant, scoring matches the key, and the whole paper runs
in airplane mode.
