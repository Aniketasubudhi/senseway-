#!/bin/bash
# Script to build APK file for SenseWay Karnataka app

PROJECT_DIR="/Applications/ main_el"
cd "$PROJECT_DIR" || exit 1

echo "🔨 Building APK for SenseWay Karnataka..."
echo ""

# Check for local.properties first
if [ -f "local.properties" ]; then
    echo "✅ Found local.properties"
    SDK_DIR=$(grep "sdk.dir=" local.properties | cut -d'=' -f2 | tr -d '\r')
    if [ -d "$SDK_DIR" ]; then
        export ANDROID_HOME="$SDK_DIR"
        export ANDROID_SDK_ROOT="$SDK_DIR"
        echo "✅ Using Android SDK from local.properties: $ANDROID_HOME"
    fi
fi

# Check if Android SDK is available
if [ -z "$ANDROID_HOME" ] && [ -z "$ANDROID_SDK_ROOT" ]; then
    # Try to find Android SDK in common locations
    ANDROID_SDK_PATHS=(
        "$HOME/Library/Android/sdk"
        "$HOME/Android/Sdk"
        "/Users/$USER/Library/Android/sdk"
    )
    
    for path in "${ANDROID_SDK_PATHS[@]}"; do
        if [ -d "$path" ]; then
            export ANDROID_HOME="$path"
            export ANDROID_SDK_ROOT="$path"
            echo "✅ Found Android SDK at: $ANDROID_HOME"
            # Create local.properties
            echo "sdk.dir=$ANDROID_HOME" > local.properties
            echo "✅ Created local.properties file"
            break
        fi
    done
fi

# Check if SDK was found
if [ -z "$ANDROID_HOME" ] || [ ! -d "$ANDROID_HOME" ]; then
    echo "❌ Android SDK not found!"
    echo ""
    echo "Please install Android Studio first:"
    echo "1. Download from: https://developer.android.com/studio"
    echo "2. Install and open Android Studio once"
    echo "3. The SDK will be installed automatically"
    echo "4. Then create local.properties file:"
    echo "   echo 'sdk.dir=$HOME/Library/Android/sdk' > local.properties"
    echo ""
    echo "Or copy local.properties.template to local.properties and update the path"
    exit 1
fi

# Check if Java is available
if ! command -v java &> /dev/null; then
    echo "❌ Error: Java is not installed or not in PATH"
    echo "Please install Java JDK 11 or higher"
    exit 1
fi

echo "✅ Java version:"
java -version 2>&1 | head -1

# Make gradlew executable
chmod +x gradlew 2>/dev/null

# Check if gradle wrapper jar exists
if [ ! -f "gradle/wrapper/gradle-wrapper.jar" ]; then
    echo "📥 Downloading Gradle wrapper jar..."
    curl -L -o gradle/wrapper/gradle-wrapper.jar \
        https://raw.githubusercontent.com/gradle/gradle/v8.0.0/gradle/wrapper/gradle-wrapper.jar 2>/dev/null
    
    if [ ! -f "gradle/wrapper/gradle-wrapper.jar" ]; then
        echo "❌ Failed to download Gradle wrapper jar"
        echo ""
        echo "Please run this command manually:"
        echo "curl -L -o gradle/wrapper/gradle-wrapper.jar \\"
        echo "  https://services.gradle.org/distributions/gradle-wrapper.jar"
        exit 1
    fi
fi

# Set ANDROID_HOME if needed (for gradle build)
if [ -n "$ANDROID_HOME" ]; then
    export ANDROID_HOME
    export ANDROID_SDK_ROOT="$ANDROID_HOME"
    export PATH="$ANDROID_HOME/platform-tools:$ANDROID_HOME/tools:$PATH"
fi

echo ""
echo "🏗️  Starting Gradle build..."
echo "This may take several minutes on first run..."
echo ""

# Build debug APK
if ./gradlew assembleDebug; then
    APK_PATH="app/build/outputs/apk/debug/app-debug.apk"
    if [ -f "$APK_PATH" ]; then
        echo ""
        echo "✅ ✅ ✅ APK BUILD SUCCESSFUL! ✅ ✅ ✅"
        echo ""
        echo "📦 APK Location:"
        echo "   $PROJECT_DIR/$APK_PATH"
        echo ""
        echo "📱 To install on Android device:"
        echo "   1. Transfer the APK to your Android device"
        echo "   2. Enable 'Install from Unknown Sources' in Settings"
        echo "   3. Tap the APK file to install"
        echo ""
        echo "💾 File size:"
        ls -lh "$APK_PATH" | awk '{print "   " $5}'
        echo ""
        
        # Create a releases directory and copy APK
        mkdir -p releases
        RELEASE_NAME="SenseWay_Karnataka_v1.0_debug.apk"
        cp "$APK_PATH" "releases/$RELEASE_NAME"
        echo "📋 Also copied to: releases/$RELEASE_NAME"
        echo ""
        
        # Try to open the directory
        if [[ "$OSTYPE" == "darwin"* ]]; then
            open -R "$APK_PATH" 2>/dev/null || echo "Navigate to: $PROJECT_DIR/$APK_PATH"
        fi
    else
        echo "❌ APK file not found at expected location"
        exit 1
    fi
else
    echo ""
    echo "❌ Build failed!"
    echo ""
    echo "Common issues:"
    echo "1. Android SDK not found - install Android Studio"
    echo "2. Missing dependencies - check internet connection"
    echo "3. Java version incompatible - need Java 11+"
    echo ""
    echo "For more help, see BUILD_INSTRUCTIONS.md"
    exit 1
fi
