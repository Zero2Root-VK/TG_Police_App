---
name: tgp-icons-images
description: Use when adding icons, images, illustrations, fonts, the app icon, splash screen, or remote image loading in TGP-APP. Covers Material Symbols workflow, bundled vector drawables, Coil 3 setup and caching, adaptive/themed launcher icons, image assets, and Telugu font bundling.
---

# TGP Icons, Images, Fonts & Assets

Visual assets must be consistent, crisp at every density, and cheap to ship. This app is
bilingual (EN + Telugu) and image-heavy (question figures, banners, diagrams).

## Icons

1. **Use Material Symbols** (the current set), not the deprecated `material-icons-extended`
   dependency in release builds. Extended icons bloat the APK and are not tree-shaken well.
2. **Workflow:** download the exact SVGs needed from the Material Symbols library and convert
   to Android Vector Drawables (Android Studio → New → Vector Asset, or `vd-tool`). Bundle only
   the icons actually used. Wrap them in a single `TgpIcons` object (see `core/designsystem/icon/`).
3. Icon style: pick ONE variable-axis config (outlined, weight 400–500, rounded terminals preferred)
   and stay consistent. Use filled variant for selected/active states.
4. Sizing follows the type scale: 24dp default, 20dp dense/chips, 40–48dp for hero/achievement icons.
   Tint with `LocalContentColor` / theme roles; never hard-code color.
5. Provide `contentDescription` for meaningful icons; `null` for decorative ones.

```kotlin
object TgpIcons {
    val Timer: ImageVector get() = ImageVector.vectorResource(R.drawable.ic_timer)
    val Correct: ImageVector get() = ImageVector.vectorResource(R.drawable.ic_correct)
    // ...
}
```

Acceptable alternative while prototyping: `material-icons-extended` in a `debugImplementation`
so it is excluded from release. Do not ship it.

## Launcher icon & splash

- Adaptive icon: `mipmap-anydpi-v26/ic_launcher.xml` with foreground (safe zone ≤ 66dp circle),
  background, and **monochrome** layer for Android 13+ themed icons.
- Provide a proper monochrome layer so the icon matches Material You theming.
- Splash: use `androidx.core:core-splashscreen` with brand background + centered logo;
  keep it matching the app's first-frame so there is no visual jump.

## Remote images (Coil 3)

Use **Coil 3** for banners, question figures, and profile avatars — it is Compose-first and
multiplatform-ready.

```toml
# gradle/libs.versions.toml
coil = "3.5.0"        # verify latest 3.x
# libs: io.coil-kt.coil3:coil-compose, coil-network-okhttp, coil-svg (if SVG served)
```

Setup in the Application (`core/designsystem` or app module):

```kotlin
setSingletonImageLoaderFactory { context ->
    ImageLoader.Builder(context)
        .components { add(SvgDecoder.Factory()) }   // only if remote SVGs are used
        .memoryCache { MemoryCache.Builder(context).maxSizePercent(0.25).build() }
        .diskCache { DiskCache.Builder().directory(context.cacheDir.resolve("img")).maxSizeBytes(64L*1024*1024).build() }
        .crossfade(true)
        .build()
}
```

Rules:
- Always pass a stable `key`/`data` and a `placeholder` + `error` drawable (branded, themed).
- Never load a remote image for content that must be offline (exam figures must be cached or bundled).
- **Exam question images must work offline.** Prefer bundling or pre-downloading into the content
  sync, then load from disk/local path. Remote-only images are a failed-exam bug.
- Respect Appwrite storage/bandwidth limits (5GB/mo on free): request appropriately sized images,
  rely on disk cache, and don't re-download unchanged assets (sync by `updatedAt`).

## Illustrations & imagery

- Keep a small illustration set (empty states, onboarding, paywall, achievements). Use vector or
  webp; target ≤ ~50KB each. Consistent stroke/palette drawn from the design tokens.
- Marketing/banner images: provide 2x/3x densities, avoid text baked into images (breaks i18n).

## Fonts

- Bundle `Noto Sans Telugu` (+ Latin fallback) in `res/font/`; register as a `FontFamily` in
  `Type.kt`. Do NOT depend on system Telugu fonts — rendering varies by OEM/skin.
- Enable `fontFeatureSettings("tnum")` for numbers (timer, scores).
- Keep font files subset if possible; test Telugu ligatures and long words for clipping.

## Verify

- Run `analyze*.apk` / APK Analyzer: confirm no unused icon dependency is bundled.
- Check icon at mask shapes (circle/squircle/rounded) and with "Themed icons" enabled.
- Airplane mode: every exam figure still renders (offline requirement).
- Telugu locale: fonts render, no tofu boxes, no clipping at fontScale 2f.
