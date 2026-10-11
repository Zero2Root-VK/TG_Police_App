---
name: tgp-monetization
description: Use when implementing purchases, subscriptions, paywalls, trials, or entitlements in TGP-APP. Covers Google Play Billing (latest), RevenueCat free tier integration, product/pack design, server-side entitlement verification, paywall UX, restoring purchases, and test/refund handling.
---

# TGP Monetization

Paid app: users buy individual paper packs and/or a subscription. Use **Google Play Billing** for
the transaction and **RevenueCat** (free tier) as the abstraction + server-side entitlement source
of truth. Never trust the client for entitlement.

## Products

- **One-time packs** — per paper or per exam bundle (e.g. "Constable 2022 Prelims"). Play product
  type `inapp`.
- **Subscription** — "All-access monthly/annual" for full catalog + new content. Product type `subs`
  with base plans + offers (intro/free trial if desired).
- Keep product IDs stable and mirrored in `gradle/libs.versions.toml` or a config constant. IDs are
  referenced by both Play Console and RevenueCat.
- Pricing/bundles and free-demo count are OPEN QUESTIONS (see PRD) — confirm before wiring paywall copy.

## Architecture

```
feature/paywall + core/billing
  BillingClient (Play)  ->  RevenueCat SDK  ->  RevenueCat backend (server truth)
                                                 | webhook
                                                 v
                                          Appwrite `entitlements`
                                                 ^
                                    app reads entitlement (cached in DataStore)
```

- Use the RevenueCat Android SDK (it wraps Play Billing). It is free within the revenue threshold
  and handles receipt validation, restores, and cross-platform entitlements.
- The UI gates content on a locally cached entitlement; the cache is refreshed from RevenueCat and
  re-validated on the server (Appwrite) for privileged actions.

## Entitlement rules

- Entitlements: e.g. `all_access`, and per-pack entitlements keyed by paper/bundle id.
- `core/domain`: an `EntitlementRepository` exposing `Flow<Set<Entitlement>>`; a use case
  `CanOpenPaper(paperId): Boolean`.
- **Gate at the content boundary**, not just the button: even if a deep link opens a locked paper,
  it must check entitlement and route to the paywall.
- Offline: honor the last verified entitlement for a grace period (e.g. 7–14 days) so offline users
  are not locked out mid-session; re-verify on reconnect.

## Play Billing specifics

- Use the latest Play Billing Library (see the vendored `play-billing-library-version-upgrade` skill).
- Handle: `onPurchasesUpdated`, pending transactions (slow cards), acknowledgements (must ack within
  3 days or auto-refund), out-of-app purchases, and `queryPurchasesAsync` on start (restore path).
- Never fulfill (grant content) until the purchase is acknowledged/verified.
- Consumables are not expected here (packs are entitlements, not consumables) — treat pack purchase
  as a non-consumable `inapp` product; a "pack" entitlement persists.

## Paywall UX

- Design as a bottom sheet (`TgpPaywallSheet`) using the design + motion skills. Clear value prop,
  price, what's included, restore link, and terms — no dark patterns.
- Show locked content as previewable (blur/sample questions) to demonstrate value, then gate.
- After purchase: celebrate subtly (expressive motion + badge), immediately unlock, and refresh
  entitlement cache.
- Provide "Restore purchases" and "Manage subscription" (deep link to Play subscriptions).

## Testing

- Play Console license testers + test cards for sandbox purchases; test pending, cancelled,
  refunded, and out-of-app flows.
- Verify entitlement persists after reinstall (RevenueCat restore) and after a refund (revoked).
- Confirm the app never grants content before verification/acknowledgement.

## Compliance

- Show price + renewal terms; link Privacy Policy and Terms; support account/subscription deletion.
- Do not gate the ability to delete the account or access core offline downloads behind payment
  beyond the content itself. Run the vendored `play-policy-insights` skill before submission.

## Verify

- Buy a pack (test card) → unlocked instantly + after relaunch + after reinstall (restore).
- Refund the purchase → entitlement revoked on next verification.
- Airplane mode with prior entitlement → still usable within grace; new purchase fails gracefully.
