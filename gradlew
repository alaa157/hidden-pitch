#!/usr/bin/env sh
set -eu
APP_HOME=$(CDPATH= cd -- "$(dirname -- "$0")" && pwd)
GRADLE_VERSION=9.6.0
DIST_NAME="gradle-$GRADLE_VERSION-bin.zip"
CACHE_ROOT="$HOME/.gradle/manual-wrapper"
DIST_DIR="$CACHE_ROOT/gradle-$GRADLE_VERSION"
if [ ! -x "$DIST_DIR/bin/gradle" ]; then
    mkdir -p "$CACHE_ROOT"
    ZIP_FILE="$CACHE_ROOT/$DIST_NAME"
    echo "Bootstrapping Gradle $GRADLE_VERSION into $CACHE_ROOT"
    curl --fail --location --silent --show-error "https://services.gradle.org/distributions/$DIST_NAME" --output "$ZIP_FILE"
    unzip -q "$ZIP_FILE" -d "$CACHE_ROOT"
    rm -f "$ZIP_FILE"
fi
exec "$DIST_DIR/bin/gradle" -p "$APP_HOME" "$@"
