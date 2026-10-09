# Hidden Pitch — Android

Phase M2 provides a lightweight Jetpack Compose shell, Material 3 theme, navigation placeholders, and Arabic/English language switching with RTL. It intentionally has no sign-in flow, Supabase client, or networking.

## Requirements

- JDK 17
- Android SDK Platform 37.2 (API 37)
- Internet access for initial Gradle and dependency downloads

The text launchers bootstrap the pinned Gradle 9.6.0 distribution on first run, because the binary Gradle Wrapper JAR is not stored in this repository.

## Build

    ./gradlew assembleDebug
    ./gradlew testDebugUnitTest
    ./gradlew lintDebug
    ./gradlew assembleRelease
    ./gradlew bundleRelease

The Gradle verification task checks that English and Arabic resource keys stay identical.

## Local configuration

Copy local.properties.example to local.properties and replace its optional Supabase URL and anon-key placeholders if available. Both default to empty values and no secrets are committed. M2 has no Supabase dependency and no INTERNET permission.

## Package identifiers

The Kotlin namespace is `hiddenpitch`. The Android application ID is `hiddenpitch.app`, because Android requires an application ID with at least two dot-separated segments. Use `hiddenpitch.app` for the Android OAuth client's package name when configuring Google sign-in in Phase M3.

## Language

Settings switches between System default, العربية, and English using AppCompat application locales. The locale choice is persisted by AppCompat on older Android versions and by the platform locale store on Android 13+.
