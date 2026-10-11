---
name: tgp-data-sync
description: Use when designing or changing TGP-APP content storage and sync. Covers the Appwrite Cloud schema (collections/buckets/roles), offline-first Room cache, delta sync by updatedAt, pagination, conflict handling, sync cursors, retries, and Appwrite free-tier limits (5GB bandwidth, 2 functions, idle pause).
---

# TGP Data & Sync (Appwrite + Room)

Appwrite Cloud is the system of record; Room is the on-device cache and the UI's source of truth.
Sync is incremental, resumable, and bandwidth-frugal (free tier = 5GB/mo).

## Appwrite schema

Use ONE database with collections (free tier: 1 DB, 1 bucket). Suggested collections:

- `papers` — id, track(constable|si), stage(prelims|mains), year, paperNo, series,
  titleEn, titleTe, marks, durationMins, totalQuestions, reviewPending, updatedAt, schemaVersion,
  contentHash.
- `questions` — id, paperId, n, promptEn, promptTe, optionsJson (4 bilingual options),
  answer(1-indexed), solutionJson({rule,en,te}), topic, conceptKey, updatedAt, deleted(bool).
- `topics` / `concepts` — for the future weightage/repetition module (conceptKey → label).
- `appConfig` — appVersion gates, min supported version, feature flags, free demo count.
- `users` (via Appwrite Auth) + `entitlements` — purchased packs/subscriptions (RevenueCat webhook writes here).

Use server-side document security + Appwrite roles/teams. Content is read-only to clients;
only admins (team role) can write. Enforce with Appwrite permissions, not client checks.

## Local cache (Room)

- Mirror the remote tables into Room (`PaperEntity`, `QuestionEntity`, `TopicEntity`).
- Room is the UI's source of truth: repositories emit Flows from Room; the network only writes into Room.
- Maintain a `sync_state` table: per-collection `cursorUpdatedAt` + `lastSyncEpochMs` + `schemaVersion`.

## Delta sync algorithm

1. Read local `cursorUpdatedAt` for the collection (default epoch 0 / null).
2. Query Appwrite with `updatedAt > cursor` (server-side query), paginated (limit 100, use cursors).
3. Upsert results into Room in a transaction; for rows flagged `deleted`, soft-delete locally
   (respect user attempts — never hard-delete questions referenced by attempts).
4. Advance `cursorUpdatedAt` to the max `updatedAt` seen. Persist ONLY after the transaction commits.
5. Repeat until no more pages. Then optionally run a "tombs/thin" check for deletions.

- Content is append/update-mostly; full re-sync should be rare (bump `schemaVersion`/`contentHash`).
- **Images/assets:** store file references + checksums; download only new/changed files into app
  storage, verify checksum, then mark available. Never re-download unchanged assets (bandwidth).
- Sync triggers: app start (throttled), before opening a paper (ensure its content is present),
  pull-to-refresh in catalog, and (if added) push/notification wake.

## Offline-first behavior

- Catalog and downloaded papers must render with zero network.
- Show per-paper state: `Available offline` / `Needs download` / `Update available`.
- Queue any user-generated writes (attempts, profile) locally and flush on reconnect; attempts are
  local-first and only optionally uploaded.

## Reliability

- Wrap all network calls in retry with exponential backoff + jitter; classify retriable vs fatal.
- Handle Appwrite idle pause (project pauses after ~1 week idle): a cold request may be slow/fail
  once — retry, and surface a friendly "reconnecting" state.
- Respect free-tier limits: batch/paginate, cache aggressively, avoid polling; only 2 cloud
  functions exist, so put sync logic client-side where possible and reserve functions for
  privileged ops (entitlement grant, webhooks).
- Never trust client for entitlements — verify server-side (RevenueCat webhook → `entitlements`).

## Migrations

- Version the content schema (`schemaVersion`). On mismatch, run a Room migration; if the remote
  shape changed incompatibly, trigger a targeted re-sync of affected collections.
- Keep backward compat for in-flight exam attempts (store `questionVersion` on attempts).

## Verify

- Airplane-mode: catalog + a downloaded paper fully usable.
- Interrupt sync mid-way (kill app), relaunch, confirm resume from cursor with no duplicates.
- Measure bytes transferred on a repeat sync with no changes — should be ~one small query per collection.
