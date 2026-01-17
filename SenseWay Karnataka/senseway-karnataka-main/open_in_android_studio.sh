#!/bin/bash
# Script to open project in Android Studio

PROJECT_DIR="/Applications/ main_el"

# Try to find Android Studio
ANDROID_STUDIO_PATHS=(
    "/Applications/Android Studio.app"
    "/Applications/Android Studio Arctic Fox.app"
    "/Applications/Android Studio Bumblebee.app"
    "/Applications/Android Studio Chipmunk.app"
    "/Applications/Android Studio Dolphin.app"
    "/Applications/Android Studio Electric Eel.app"
    "/Applications/Android Studio Flamingo.app"
    "/Applications/Android Studio Giraffe.app"
    "/Applications/Android Studio Hedgehog.app"
    "$HOME/Applications/Android Studio.app"
)

ANDROID_STUDIO=""

for path in "${ANDROID_STUDIO_PATHS[@]}"; do
    if [ -d "$path" ]; then
        ANDROID_STUDIO="$path"
        break
    fi
done

if [ -z "$ANDROID_STUDIO" ]; then
    echo "❌ Android Studio not found!"
    echo ""
    echo "Please install Android Studio:"
    echo "1. Download from: https://developer.android.com/studio"
    echo "2. Install it to /Applications/"
    echo "3. Run this script again"
    echo ""
    echo "Or open Android Studio manually and:"
    echo "1. File → Open"
    echo "2. Select: $PROJECT_DIR"
    exit 1
fi

echo "✅ Found Android Studio at: $ANDROID_STUDIO"
echo "🚀 Opening project..."

# Open project in Android Studio
open -a "$ANDROID_STUDIO" "$PROJECT_DIR"

echo ""
echo "📱 Once Android Studio opens:"
echo "1. Wait for Gradle sync to complete"
echo "2. Create/start an emulator (Tools → Device Manager)"
echo "3. Click the green ▶️ Run button"
echo ""
echo "See EMULATOR_SETUP.md for detailed emulator instructions."
