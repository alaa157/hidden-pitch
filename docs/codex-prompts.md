# Hidden Pitch: Codex setup + Prompt 1 (M2)

## How to use this file

1. Create the GitHub repo and clone it.
2. Make a `docs/` folder and put these four files in it: `SRS.md`, `user-stories.md`, `data-model.md`, `architecture.md`.
3. Copy **Part A** below into a file called `AGENTS.md` in the repo root. Codex reads it automatically on every task.
4. Open Codex in the repo, on a new branch: `git checkout -b phase/m2-skeleton`.
5. Paste **Part B** as the task. Replace `<PACKAGE_NAME>` first with the exact package name you used in the Google Cloud Android client (task A2).
6. When Codex reports back, review, run the app yourself, merge, then come back to me for Prompt 2.

One phase = one branch = one prompt. Do not combine phases.

---

# PART A: `AGENTS.md` (repo root)

```markdown
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
```

---

# PART B: Prompt 1 (Phase M2: App skeleton + language/RTL)

````markdown
You are the lead Android engineer on "Hidden Pitch". Read `AGENTS.md` and all four files in `docs/` completely before writing any code. They are the source of truth.

# PHASE: M2. App skeleton, theme, navigation shell, Arabic/English switch with full RTL (covers user story US-13 and task C1-C4)

This is the FIRST phase. There is no backend code, no sign-in, no network in this phase. Build only the foundation that every later phase will sit on, and build it properly, because everything else depends on it.

## 0. Before coding
1. Restate in your own words (10 lines max) what this phase includes and what it explicitly excludes. Then proceed without waiting for me.
2. List the exact files you plan to create. Then proceed.

## 1. Project setup
- Create a new Android project in the repo root. Single module `app`.
- Gradle Kotlin DSL + a version catalog (`gradle/libs.versions.toml`). Use the latest STABLE versions of AGP, Kotlin, Compose BOM, and AndroidX libraries. No alpha/beta/RC.
- `namespace` and `applicationId`: `<PACKAGE_NAME>`.
- `minSdk = 26`, `compileSdk` and `targetSdk` = latest stable API level.
- Java/Kotlin target: use the toolchain the current stable AGP expects.
- Dependencies allowed in this phase (and ONLY these):
  - Compose BOM, `ui`, `ui-tooling-preview`, `material3`, `material-icons-core` (use `Icons.AutoMirrored` where needed; do NOT add `material-icons-extended`, it is heavy; draw/ship tiny vector drawables yourself if you need an icon outside core)
  - `androidx.activity:activity-compose`
  - `androidx.appcompat:appcompat` (needed for per-app language on API < 33)
  - `androidx.navigation:navigation-compose`
  - `androidx.lifecycle:lifecycle-viewmodel-compose` and `lifecycle-runtime-compose`
  - `androidx.datastore:datastore-preferences`
  - `org.jetbrains.kotlinx:kotlinx-coroutines-android`
  - Test: `junit`, and `ui-tooling` + `ui-test-manifest` as debug-only
- Release build type: `isMinifyEnabled = true`, `isShrinkResources = true`, default Proguard files. Debug build must run without minify.
- Enable `buildConfig = true`. Add `resourceConfigurations`/`androidResources { localeFilters }` limited to `en` and `ar` so no other library locales bloat the APK.
- Disable anything unused: no vector-drawable support-library fallback beyond what minSdk needs, no `multiDex`.
- `.gitignore`: standard Android + `local.properties`, `*.keystore`, `*.jks`, `.idea/`, `build/`.

## 2. Config without secrets
- Read `SUPABASE_URL` and `SUPABASE_ANON_KEY` from `local.properties` (fall back to empty string if absent, so the project still builds on a fresh clone).
- Expose them as `BuildConfig.SUPABASE_URL` and `BuildConfig.SUPABASE_ANON_KEY`.
- Commit a `local.properties.example` with placeholder values and a one-line comment. Do NOT add any Supabase library yet.
- Add `AppConfig` object in `util/` wrapping these two values.

## 3. Package structure
Create exactly the structure from `docs/architecture.md` section 4:

```
<PACKAGE_NAME>/
  data/        (empty package with a package-info comment is NOT allowed; just leave it out until used, but create AppContainer)
  domain/      Sport, Audience, Surface, PlaceStatus
  ui/theme/    Color, Type, Theme, RtlHelpers
  ui/home/ ui/detail/ ui/add/ ui/myplaces/ ui/settings/ ui/common/
  util/        LocaleManager, AppConfig
  AppContainer.kt
  MainActivity.kt
  HiddenPitchApp.kt   (Application subclass, creates AppContainer)
```
Do not create empty directories with no files. Create a package only when it has at least one file.

## 4. Domain enums
In `domain/`, create these enums. Each has a stable `dbValue: String` (exactly the values from `docs/data-model.md` section 4 and the status rules), a `@StringRes labelRes: Int`, and a `companion fun fromDb(value: String): X?`.
- `Sport`: football, basketball, volleyball, tennis, padel, playground, other
- `Audience`: everyone, boys_men, girls_women
- `Surface`: grass, sand, concrete, artificial_turf
- `PlaceStatus`: pending, approved, rejected, deleted (see architecture.md 3.2)

Add one pure-JVM unit test per enum: every `dbValue` is unique, `fromDb(dbValue)` round-trips, and unknown input returns null.

## 5. Strings (English + Arabic)
Create `res/values/strings.xml` and `res/values-ar/strings.xml` with identical keys. Arabic must be real, natural Arabic (Modern Standard, simple, friendly), not transliteration. Include at minimum:
- App name: "Hidden Pitch" in English; "Hidden Pitch" also in Arabic file (keep brand name Latin) with a note in your report so I can decide later.
- Screen titles: Home, Place details, Add place, Edit place, My places, Settings
- Actions: Search, Filters, Clear filters, Add place, Sign in, Sign out, Retry, Cancel, Confirm, Back
- Settings: Language, System default, Arabic (shown as "العربية" in both files), English (shown as "English" in both files)
- Empty/error states: "No places found", generic error, no connection
- All enum labels: every Sport, Audience, Surface, plus statuses (Pending review, Approved, Rejected, Deletion pending)
- Placeholder strings for the shell screens (e.g. "Place list will appear here")
Naming convention: `screen_feature_element` (e.g. `settings_language_title`, `sport_football`, `audience_girls_women`).
Language names must always show in their own language regardless of the current locale.

## 6. Language switching (the core of this phase)
Implement `LocaleManager` so that:
- Default = follow the device language. If the device language is neither Arabic nor English, fall back to English.
- User can choose: System default / العربية / English in Settings.
- Switching applies immediately WITHOUT restarting the app manually and without reinstalling, using `AppCompatDelegate.setApplicationLocales(...)`.
- Choice persists across process death and reboots. Use AppCompat's own persistence for API < 33 (register `AppLocalesMetadataHolderService` with `autoStoreLocales` metadata in the manifest) and the system locale store on API 33+. Do not hand-roll a second persistence layer for the locale; DataStore stays unused unless you have a real need, and if so explain it.
- Add `res/xml/locales_config.xml` listing `en` and `ar`, and reference it via `android:localeConfig` so Android 13+ shows the app in system per-app language settings.
- `MainActivity` must extend `AppCompatActivity` (required for pre-33 locale switching) and call `setContent {}`.
- Manifest: `android:supportsRtl="true"`, `android:allowBackup="false"`, no unneeded permissions. (INTERNET permission will be added in the phase that needs it; do NOT add it now.)

## 7. Theme
- Material 3, custom color scheme (light + dark following system). Dynamic color OFF so the brand is consistent.
- Palette: a calm sport-green primary, a warm accent, neutral surfaces. Pick accessible contrast (WCAG AA for text). Define both schemes explicitly in `Color.kt`.
- Typography: system default font family only (no downloadable fonts, no bundled fonts, they cost size). Make sure Arabic text renders with proper line height: set `lineHeight` generously in `Type.kt` and verify it in an Arabic preview.
- `RtlHelpers.kt`: small helpers if needed (e.g. a `isRtl()` composable based on `LocalLayoutDirection`). Do not over-engineer.

## 8. Navigation shell (placeholders only)
Use Navigation Compose with simple string routes defined in a single `Routes` object.
Screens (each is a real composable with its own ViewModel stub where it makes sense, showing placeholder content from string resources):
- `HomeScreen`: top app bar with title, a search icon placeholder and a filter icon placeholder (non-functional), a floating "Add place" button, a settings icon, and the placeholder body.
- `DetailScreen(placeId)`: reachable from a dummy item on Home so navigation args are proven; back arrow in the top bar (AutoMirrored).
- `AddPlaceScreen`: placeholder.
- `MyPlacesScreen`: placeholder.
- `SettingsScreen`: the working language selector (three radio options), plus a disabled/placeholder "Sign in" row.
Navigation rules:
- "Add place" and "My places" must go through an `AuthGate` stub: a `fun interface`/small class in `data/` or `ui/common/` returning `isSignedIn = false` for now, and for this phase simply navigate to the destination anyway but via the gate function so Phase M3 only has to change the gate's implementation. Document this in a code comment of one line.
- Back-stack behavior is sane (Home is the root; back from Home exits).
- Screen state survives rotation and language change (no crashes, no lost navigation state). Test by switching language while on the Settings screen and while on a Detail screen.

## 9. AppContainer
Create `AppContainer` (manual DI) holding `localeManager` and `appConfig`. `HiddenPitchApp` creates it; screens get what they need from it through a tiny `ViewModelProvider.Factory` helper. Keep it under 60 lines total.

## 10. Quality gates
- Enable lint checks as ERRORS for: `HardcodedText`, `RtlHardcoded`, `RtlSymmetry`, `RtlEnabled`, `MissingTranslation`, `ExtraTranslation`. Configure in `lint {}` in `app/build.gradle.kts`.
- Add a unit test or a Gradle verification that fails if `values/strings.xml` and `values-ar/strings.xml` have different key sets.
- Add `@Preview` for `HomeScreen` and `SettingsScreen` in BOTH English LTR and Arabic RTL (`locale = "ar"`) and both light/dark where cheap.
- No unused imports, no unused resources (run lint and fix `UnusedResources`).

## 11. Size check
After everything passes, build the release variant and report:
- the size of `app-release-unsigned.apk` in KB
- the size of the `.aab` if you can build `bundleRelease`
This is the baseline. Later phases will be compared against it.

## 12. Verification you must perform and report
Run and report results of:
```
./gradlew assembleDebug testDebugUnitTest lintDebug assembleRelease
```
Then prove the "Done when" list below, item by item, by explaining how you verified each (code reference, test name, or tool output). If you cannot run something on an emulator, say so explicitly, do not pretend.

## DONE WHEN
1. The app builds on a fresh clone with no `local.properties` present.
2. Opening the app shows the Home placeholder; navigation to Detail, Add, My places and Settings works.
3. In Settings, switching between System default / Arabic / English changes all text immediately and the layout flips RTL/LTR correctly (top bar, back arrow, FAB position, lists, radio buttons).
4. The chosen language survives app restart and process death.
5. On an Arabic-language device, the app starts in Arabic RTL by default; on a device in any other language, it starts in English.
6. `strings.xml` (en/ar) have identical keys; lint fails the build if they ever diverge or if hardcoded text/left-right is introduced.
7. All four Gradle commands pass.
8. Release APK size baseline reported.
9. No dependency outside the allowed list was added.

## DO NOT
- Do not add Supabase, Ktor, Coil, Credential Manager, or any network code. Those come in later phases.
- Do not add the INTERNET permission.
- Do not build real forms, lists, search, filters, or sign-in.
- Do not create a second module.
- Do not start Phase M3.

## FINAL REPORT
Use the exact 7-part format from `AGENTS.md`. Finish with a short "Ready for Phase M3 (Google sign-in)?" checklist of anything I must prepare on my side (accounts, keys, SHA-1).
````

---

# What the next prompts will be (do not paste these yet)

| Prompt | Phase | Scope |
|---|---|---|
| 2 | M3 | Supabase client + Google sign-in (Credential Manager), auth gate, sign out, `users` row check |
| 3 | M4a | Image compressor + unit tests |
| 4 | M4b | Add-place form, validation, upload + insert, retry |
| 5 | M6 | Home list, pagination, details, photo pager |
| 6 | M7 | Search + filters |
| 7 | M8 | Open in Google Maps + My places |
| 8 | M9 | Edit + delete requests |
| 9 | M10 | Release hardening, size, privacy policy |

The SQL backend (M1) is a separate step I will write for you. Run it in Supabase **before** Prompt 2.
