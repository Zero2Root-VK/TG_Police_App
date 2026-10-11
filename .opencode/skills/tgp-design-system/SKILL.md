---
name: tgp-design-system
description: Use when building, styling, or reviewing any TGP-APP (Telangana Police exam) UI. Covers the brand color scheme, Material 3 Expressive color/type/shape/elevation tokens, the core/designsystem module layout, custom Compose components, dark mode, dynamic color, and accessibility. Trigger on any theme, color, typography, spacing, shape, component, or design-token task.
---

# TGP Design System

The single source of truth for how TGP-APP looks. This app must feel premium and
trustworthy (a paid exam product for police aspirants), not like a generic template.
Every screen is built from `core/designsystem`. No hard-coded colors, sizes, or fonts
outside that module.

## Ground rules

1. **Tokens only.** UI code references `MaterialTheme.colorScheme`, `MaterialTheme.typography`,
   `MaterialTheme.shapes`, and local `TgpTheme` extensions (spacing, elevation). Never write
   `Color(0xFF...)`, `16.dp`, or `FontFamily(...)` inline in a screen.
2. **Material 3 Expressive is the target**, not plain M3. Use `MaterialExpressiveTheme`
   with a `MotionScheme` (see the `tgp-motion` skill). If the exact API is unavailable in the
   pinned version, fall back to `MaterialTheme` + the same token objects, never to raw values.
3. **One module owns design:** `core/designsystem`. Feature modules depend on it, never the reverse.
4. **Dark mode is first-class.** Both schemes must be designed and screenshot-tested.
5. **Accessibility is not optional:** WCAG AA contrast (4.5:1 body text, 3:1 large/UI),
   48dp min touch targets, support 200% font scale, honor reduced-motion.

## Module layout (`core/designsystem`)

```
core/designsystem/src/main/kotlin/<pkg>/designsystem/
  theme/
    Color.kt        # brand + neutral palettes, light/dark ColorScheme
    Type.kt         # Typography (EN + Telugu)
    Shape.kt        # Shapes + custom M3E morph shapes
    Motion.kt       # MotionScheme + motion tokens (see tgp-motion)
    Spacing.kt      # TgpSpacing: xxxs..xxxl + custom CompositionLocal
    Elevation.kt    # TgpElevation tokens
    TgpTheme.kt     # MaterialExpressiveTheme wiring, dynamic color, CompositionLocals
  component/        # TgpButton, TgpQuestionCard, TgpTimer, TgpOptionRow, TgpProgressRing...
  icon/             # TgpIcons object referencing bundled vector drawables
  util/             # preview annotations, LocalTgpSpacing helpers
```

Feature modules only import `component`, `icon`, and the theme. They never define palettes.

## Color

### Brand direction
Telangana Police identity: authoritative, official, calm. Proposed seed palette
(confirm final with stakeholder before release):

- **Primary** — deep police navy/indigo `#1B3A6B` (used for app bars, primary actions).
- **Secondary** — khaki/olive `#6B6B3A`-family (uniform nod; used sparingly as accents).
- **Tertiary** — gold/brass `#B8860B`-family (badges, streaks, "achievement" moments).
- **Error** — standard M3 error red; **surface/neutrals** — cool-warm neutral tuned to primary.

Define full tonal palettes (0/10/20/.../100) for each, then map into M3 roles.
Do **not** hand-pick only a handful of roles — use the complete role set:
`primary`, `onPrimary`, `primaryContainer`, `onPrimaryContainer`, `surface*`,
`surfaceContainerLowest..Highest`, `outline`, `outlineVariant`, `inverse*`, etc.

### Dynamic color
Support Android 12+ Material You, but keep brand identity:

```kotlin
val scheme = when {
    dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S ->
        if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
    darkTheme -> TgpDarkColorScheme
    else -> TgpLightColorScheme
}
```

Provide a settings toggle ("Use device colors"). Default ON is fine for a utility app, but
brand scheme must always ship and be the fallback + screenshot baseline.

### Semantic color
Add a `TgpColors` extension for domain states not covered by M3:
`correct` (green), `incorrect` (red), `markedForReview` (amber), `notVisited` (neutral),
`skipped` (outline), `streak`, `premium` (gold). Map them in both schemes.

## Typography

- Base font: a clean grotesque for EN (e.g. Google Sans/Roboto Flex or Inter).
- **Telugu**: bundle `Noto Sans Telugu` (and a Telugu-capable fallback) — never rely on device fonts
  for Telugu rendering consistency. Register with `FontFamily` + downloadable-font fallback.
- Use the full flexible type scale (Display/Headline/Title/Body/Label × L/M/S). Add explicit
  emphasis styles (Emphasized variants) where M3E provides them.
- Exam text needs high legibility: prefer Body Large/Medium for question prompts, adequate
  line height (≥1.4), and no ultra-light weights.
- Numbers (timer, question counts, scores) use tabular figures — enable `fontFeatureSettings("tnum")`.

## Shape

- Use `Shapes` (extraSmall..extraLarge) mapped from M3E defaults.
- Provide the M3E **shape morph** set (`MaterialShapes`: circle, square, slanted, arch, pill,
  cookie, clover, ...) for expressive accents (e.g. animated selected-tab shape, earning badge).
- Corner radius conveys hierarchy: cards large (16–28dp), chips full/pill, sheets top-rounded 28dp.

## Spacing, elevation, sizing

- `TgpSpacing`: `xxxs=2, xxs=4, xs=8, s=12, m=16, l=24, xl=32, xxl=48` (dp). Expose via
  `CompositionLocal` so previews/tests can override.
- Elevation from tokens only; prefer tonal surfaces (`surfaceContainer*`) over shadows.
- Consistent content max width on tablets (see `adaptive` skill), 4dp grid everywhere.

## Component standards

Build small, reusable, preview-annotated components. Every component must have:
- Light + dark `@Preview`, and a large-font `@Preview(fontScale = 2f)`.
- Content descriptions / semantics for a11y.
- No business logic — pure params + callbacks (hoisted state).
- Signature components to build first: `TgpButton` (filled/tonal/outlined/text + loading state),
  `TgpQuestionCard`, `TgpOptionRow` (correct/incorrect/selected/disabled), `TgpTimer`,
  `TgpProgressRing`, `TgpQuestionPalette` cell, `TgpStatCard`, `TgpEmptyState`, `TgpPaywallSheet`.

## Do / Don't

- Do: rely on tokens; design both themes; test at fontScale 2f; use M3E motion + shape for delight.
- Don't: inline literals; skip dark mode; use pure black/white; use more than ~3 accent hues;
  use color alone to signal correctness (add icon/label too — a11y for color-blind users).

## Verify

- `./gradlew :core:designsystem:test` and screenshot tests for both schemes.
- Manual: toggle dark mode, dynamic color, font scale 200%, and Telugu locale; confirm no clipping
  and AA contrast.
