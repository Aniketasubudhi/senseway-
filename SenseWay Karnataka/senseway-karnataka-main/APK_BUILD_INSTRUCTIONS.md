# Build APK File - Quick Guide

## Option 1: Using Android Studio (Recommended)

### Steps:
1. **Open Android Studio**
2. **Open the project**: File → Open → Select `/Applications/ main_el`
3. **Wait for Gradle sync** to complete (5-10 minutes first time)
4. **Build APK**: 
   - Go to **Build** → **Build Bundle(s) / APK(s)** → **Build APK(s)**
   - Wait for build to complete
5. **Find your APK**:
   - Click the notification "APK(s) generated successfully"
   - Or navigate to: `app/build/outputs/apk/debug/app-debug.apk`

### To install on Android device:
1. Transfer the APK file to your Android device (via USB, email, cloud storage, etc.)
2. On your Android device:
   - Go to **Settings** → **Security** → Enable **"Install from Unknown Sources"**
3. Tap the APK file on your device to install

---

## Option 2: Command Line (Requires Android SDK)

### Prerequisites:
- **Android SDK installed** (usually comes with Android Studio)
- **Java 11 or 17** installed

### Steps:

1. **Install Android Studio** (if not already installed):
   - Download from: https://developer.android.com/studio
   - Install and open Android Studio once to set up the SDK

2. **Set up local.properties**:
   ```bash
   cd "/Applications/ main_el"
   echo "sdk.dir=$HOME/Library/Android/sdk" > local.properties
   ```
   (Adjust path if your SDK is in a different location)

3. **Build the APK**:
   ```bash
   ./build_apk.sh
   ```

4. **Find your APK**:
   - Location: `app/build/outputs/apk/debug/app-debug.apk`
   - Also copied to: `releases/SenseWay_Karnataka_v1.0_debug.apk`

---

## What You'll Get

- **Debug APK**: `app-debug.apk` (suitable for testing)
- **Location**: `app/build/outputs/apk/debug/app-debug.apk`
- **File size**: Approximately 10-20 MB

## Installing on Android Device

### Method 1: USB Transfer
1. Connect Android device to computer via USB
2. Copy APK file to device
3. On device: Open file manager → Tap APK → Install

### Method 2: Cloud Storage
1. Upload APK to Google Drive, Dropbox, etc.
2. On device: Download APK from cloud
3. Tap downloaded APK → Install

### Method 3: Email/Message
1. Email APK to yourself or send via messaging app
2. On device: Open email/message → Download APK
3. Tap APK → Install

### Enable Unknown Sources:
- Android 8+: Settings → Apps → Special access → Install unknown apps → Select your app → Allow
- Older Android: Settings → Security → Unknown sources → Enable

---

## Troubleshooting

### "SDK location not found"
- Install Android Studio first
- The SDK is automatically installed with Android Studio
- Default location: `~/Library/Android/sdk` (macOS)

### "Java version incompatible"
- Android Gradle Plugin 7.4.2 requires Java 11+
- Check your Java version: `java -version`
- Install Java 17 if needed (recommended for Android development)

### "Build failed - dependencies"
- Check internet connection
- Gradle needs to download dependencies on first build
- Wait for download to complete (may take 10-15 minutes)

### "Permission denied" when running script
- Make script executable: `chmod +x build_apk.sh`
- Or run: `bash build_apk.sh`

---

## Need Help?

See:
- [BUILD_INSTRUCTIONS.md](BUILD_INSTRUCTIONS.md) - Full build guide
- [EMULATOR_SETUP.md](EMULATOR_SETUP.md) - Emulator setup
- [README.md](README.md) - App features and usage
