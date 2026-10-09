# AGENTS.md: Hidden Pitch

## What this project is
Hidden Pitch is an ultra-light, Android-only app for finding hidden or little-known sports places (football pitches, padel/tennis courts, playgrounds). Launch area: Kafr el-Sheikh, Egypt. Languages: Arabic (full RTL) and English. Backend: Supabase. Stack: Kotlin + Jetpack Compose.

## Source of truth (read before every task)
Authority order, highest first:
1. `docs/architecture.md` (wins where its section 3 changes the data model)
2. `docs/data-model.md`
3. `docs/user-stories.md`
4. `docs/SRS.md`

If a task prompt conflicts with these docs, STOP and ask. Do not silently pick one.

## Hard rules
- Implement ONLY the phase named in the task prompt. Never start the next phase, never "get ahead".
- Do not add any dependency that is not listed in `docs/architecture.md` section 2 or in the task prompt. If you think one is needed, stop and ask, with the reason and the size cost.
- Nothing from the "not in v1" lists: no map SDK, no GPS/location permission, no ratings, no chat, no booking, no admin screens, no analytics, no Firebase, no iOS.
- Never commit secrets. Supabase URL and anon key come from `local.properties` -> BuildConfig. The `service_role` key must never appear anywhere in the repo or the app.
- Every user-visible string lives in `res/values/strings.xml` (English) and `res/values-ar/strings.xml` (Arabic). No hardcoded text in Kotlin or XML. Both files must always have the same keys.
- RTL: use `start`/`end` (never `left`/`right`), `Arrangement.Start/End`, `TextAlign.Start/End`. Mirror direction-implying icons (`Icons.AutoMirrored.*`). Every new screen must be checked in Arabic RTL.
- Keep it light: single Gradle module, no DI framework, no Room, no Paging 3, no Hilt/Koin, no Retrofit unless the architecture doc's fallback is triggered.
- No business logic in composables. One ViewModel per screen, `StateFlow` for UI state, repositories return `Result`.
- Small, readable Kotlin. No clever abstractions. No commented-out code. No TODOs left without a matching entry in the final report.

## Commands that must pass before you say "done"
```
./gradlew assembleDebug
./gradlew testDebugUnitTest
./gradlew lintDebug
./gradlew assembleRelease
```
Report the exact result of each. If something fails and you cannot fix it, say so; do not claim success.

## Definition of done for any phase
- Every item in the phase's "Done when" list is demonstrably true (explain how you checked).
- The four commands above pass.
- `strings.xml` (en) and `strings.xml` (ar) have identical key sets.
- No new lint errors; no new warnings you could have avoided.
- Work is committed in small logical commits with clear messages (`feat:`, `fix:`, `chore:`, `test:`, `docs:`).
- Final report written in the format the task prompt asks for.

## Final report format (always)
1. Summary (what was built, 5 lines max)
2. File tree of what changed
3. Commands run and their results
4. Decisions you made that were not specified, and why
5. Deviations from the docs (should be none; list any)
6. Anything blocked or needing my input
7. Release APK size (if the phase asks for it)
