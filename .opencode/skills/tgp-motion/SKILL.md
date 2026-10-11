---
name: tgp-motion
description: Use when adding or reviewing animation, transitions, gestures, or motion in TGP-APP. Covers Material 3 Expressive motion schemes and spring tokens, Compose Animation APIs, shared element transitions, predictive back, list/state animations, timer and progress motion, and motion performance/reduced-motion rules.
---

# TGP Motion & Animation

Motion is a headline feature of this app. It must feel physical, responsive, and never gimmicky.
All motion is spring-based (M3 Expressive physics), tied to tokens, and interruptible.

## Principles

1. **Physics over duration.** Prefer spring specs / M3E motion tokens over fixed `tween(300)`.
   Springs are interruptible and feel natural when gestures or state change mid-animation.
2. **Purposeful.** Motion explains hierarchy (where a screen came from), confirms input, or
   directs attention. No decorative loops except the loading indicator.
3. **Fast for frequent, expressive for rare.** Common taps = subtle + quick; milestone/success
   moments (score reveal, streak, purchase) = expressive with shape morph.
4. **Never block input.** Animations must be interruptible; never gate taps behind an animation.
5. **Reduced motion honored.** Read the system animator duration scale; when 0, snap to end state.

## Tokens (`core/designsystem/theme/Motion.kt`)

Wire an M3E `MotionScheme` into the theme (Expressive by default; Standard for dense list work):

```kotlin
// androidx.compose.material3 (Expressive) — verify exact symbol in pinned version
val MotionScheme = MotionScheme.expressive()   // spatial* + effects* springs
```

Expose named `SpringSpec`s so features never invent values:
- `SpatialFast` / `SpatialDefault` / `SpatialSlow` — for size, position, layout (bouncy, visible).
- `EffectsFast` / `EffectsDefault` — for color, alpha, elevation (quick, no overshoot).
- Transition durations kept short (100–300ms) unless it is a deliberate expressive moment.

## When to use which API

| Need | Use |
| --- | --- |
| Single value (alpha, offset, color) | `animate*AsState` / `animateValueAsState` |
| Show/hide a component | `AnimatedVisibility` (+ enter/exit with spring) |
| Swap between two composables | `AnimatedContent` (key on state) |
| Size change (card expands) | `animateContentSize` (spring) |
| Screen-to-screen hero | Shared element transition in `SharedTransitionLayout` + `Modifier.sharedElement/sharedBounds` |
| Scroll-driven effects | `Modifier.graphicsLayer { }` + `derivedStateOf` (avoid recomposing on scroll) |
| Gesture-driven (drag, swipe) | `AnchoredDraggable` / `draggable` + `Animatable.animateTo(spring)` |
| Morphing shape | `animatedShape` / `MaterialShapes` (M3E) |
| Success/celebration | Spring + shape morph + haptics; optional particle burst |
| Predictive back | Navigation 3 scenes + `NavigationEvent` (see `navigation-3`/`navigation-event` skills) |

## Patterns to implement

- **Screen transitions:** Nav3 scenes, shared axis feel via fade+slide; keep under ~250ms; honor predictive back.
- **Question card:** `AnimatedContent` when moving between questions (slide by direction), spring on answer selection.
- **Option selection:** effects spring on background/border + a tiny spatial "pop" scale on the check icon; confirm with haptic.
- **Timer:** numeric text with `AnimatedContent` per digit or `animateIntAsState`; pulse/color shift (effects) in final minute; NO layout push.
- **Progress ring:** `animateFloatAsState(spring)` for value; arc color cross-fades to error near time-out.
- **Question palette:** selected cell uses shape morph (square→circle) + spatial spring; visited state uses effects color only.
- **Streaks/score reveal:** the one place for expressive motion — count-up numbers, shape morph badge, restrained confetti, `HapticFeedbackType.LongPress`.
- **Bottom sheets / dialogs:** M3E default motion; never custom-duration them.

## Performance rules

- Animate only `graphicsLayer`-friendly props (alpha, translation, scale, rotation) where possible;
  layout changes (size, position of siblings) are the expensive ones — prefer `animateContentSize` sparingly.
- Use `remember`/`derivedStateOf`; never read animated state inside a composable that would recompose a whole list.
- Never animate inside `LazyColumn` item lambdas with unstable keys; key items properly.
- Keep off the main thread: compute next states before animating.
- Profile with the `android-profiler` skill if jank > ~1 frame.

## Accessibility

- Respect `LocalMotionDurationScale` / animator duration scale == 0 → skip animation.
- Provide non-animated equivalents of any feedback (color/icon/label), never motion-only.
- Haptics + motion for confirmations; but ensure meaning survives with motion disabled.

## Verify

- Test on a low-end device profile; confirm no dropped frames during list scroll + timer.
- Toggle "Remove animations" in developer options and confirm the app still reads correctly.
- Screenshot/record the four headline moments (start exam, answer, submit, score reveal).
