---
name: tgp-architecture
description: Use when scaffolding, structuring, or refactoring TGP-APP Kotlin/Compose code. Covers module layout, unidirectional data flow (UDF/MVVM), Hilt DI, repository/data-layer patterns, Kotlin conventions, Gradle version catalogs, Navigation 3 integration, and error/Result handling. Mirrors android/nowinandroid guidance.
---

# TGP Architecture

Follow Google's official app architecture guidance and the `android/nowinandroid` reference.
One Activity, Compose UI, UDF, Hilt, Kotlin coroutines/Flow, offline-first data.

## Module layout

```
app/                     # MainActivity, nav graph, Hilt app, DI aggregation
core/
  model/                 # pure Kotlin domain models (no Android deps)
  designsystem/          # theme + components + icons (see tgp-design-system)
  ui/                    # shared UI primitives, TgpScaffold, reusable widgets
  common/                # Result, dispatchers, time, logging, extensions
  domain/                # use cases (one class = one action)
  data/                  # repositories, data sources (remote=Appwrite, local=Room)
  database/              # Room entities, DAOs, migrations
  network/               # Appwrite client, DTOs, mappers, paging
  datastore/             # user prefs, entitlement cache, sync cursors
  testing/               # fakes, test rules, dispatcher rule
feature/
  catalog/  exam/  result/  auth/  paywall/  profile/  admin/  weightage/
```

Rules: feature modules depend on `core:*`, never on each other. Domain/data have no Compose deps.
Each feature exposes a small API (screen entry + nav keys) and keeps internals private.

## UDF / MVVM

- `ViewModel` = state holder. Exposes `StateFlow<UiState>` (immutable data class) + a one-shot
  events channel for effects (navigation, toasts).
- Screens are stateless composables taking `UiState` + lambdas; a `*Route` collect-and-hoist wrapper
  lives in the feature and calls the ViewModel.
- State is the single source of truth; UI never mutates state directly.
- Model async work explicitly: `Loading / Success / Error` (sealed or as fields), plus empty.

```kotlin
@HiltViewModel
class ExamViewModel @Inject constructor(
    private val startExam: StartExamUseCase,
    private val saveAnswer: SaveAnswerUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(ExamUiState())
    val uiState: StateFlow<ExamUiState> = _uiState.asStateFlow()
    // ...
}
```

## Repositories & data layer

- Repository interface lives in `core/domain`; implementation in `core/data`.
- **Offline-first:** local Room/DataStore is the UI's source of truth; remote sync happens in the
  background (see `tgp-data-sync`). Repos expose `Flow` from the local DB, never from the network.
- Wrap failures in a `Result<T>` (`core/common`). Never throw across layer boundaries for expected
  errors. Log with a redacting logger; never log PII/answers.
- Inject `Dispatchers` via a `DispatcherProvider` — don't hard-code `Dispatchers.IO`.

## DI (Hilt)

- Convention modules per layer/feature (`@InstallIn(SingletonComponent::class)`).
- Qualifiers for distinct bindings (e.g. `@RemoteAppwrite`, `@IoDispatcher`).
- Fakes for tests analog to production bindings (`core/testing`).
- Scoped to `ViewModel` where state must survive config change; process-death survival via
  `SavedStateHandle` for exam sessions (critical — see `tgp-exam-engine`).

## Navigation

- Use **Navigation 3** (see the vendored `navigation-3` skill). Type-safe `NavKey`s, `NavDisplay`
  per feature area, multiple backstacks where needed (exam is a full-screen stack).
- Deep links for content/notifications. Predictive back via `navigation-event`.

## Kotlin & code conventions

- Kotlin DSL Gradle + **version catalog** (`gradle/libs.versions.toml`) as the only place for versions.
- Coroutines + Flow throughout; structured concurrency; no `GlobalScope`.
- Immutable models (`data class`, `val`), `@Immutable`/`@Stable` on UI state.
- Prefer `sealed interface` for state/events; `enum` for fixed sets.
- Keep functions small and pure; extract use cases when logic is reused or needs testing in isolation.
- Follow the project's no-comments convention — code should be self-explanatory; document
  non-obvious decisions in PRD/STATUS, not inline.
- Run `spotlessApply`/ktlint and the module tests before finishing any change.

## Build & tests

- `./gradlew assembleDemoDebug` for a build; `./gradlew test` for unit tests.
- Screenshot tests (Roborazzi) for design components and key screens (see `testing-setup`).
- Use the `android-cli` skill to create/run/inspect with the `android` tool, and `r8-analyzer`
  before release to trim keep rules.

## Definition of done for any feature

1. State holder + immutable state + events.
2. Stateless composable(s) with light/dark/fontScale previews.
3. Repository via domain interface + Room source; unit tests with fakes.
4. nav key wired; deep link if content-bearing.
5. Offline behavior verified (airplane mode).
6. Lint/spotless + tests green.
