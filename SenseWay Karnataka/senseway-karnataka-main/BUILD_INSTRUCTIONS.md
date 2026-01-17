# SenseWay Karnataka - Step-by-Step Build Instructions

## Prerequisites
- Android Studio Hedgehog (2023.1.1) or later
- Android SDK with API 24 (Android 7.0) minimum, API 34 (Android 14) target
- Physical Android device (recommended) or emulator with Google Play Services
- Internet connection for first-time Gradle sync

## Step 1: Create New Project

1. **Open Android Studio**
   - Launch Android Studio
   - Click "New Project" or File → New → New Project

2. **Select Project Template**
   - Choose "Empty Activity"
   - Click "Next"

3. **Configure Project**
   - **Name**: `SenseWay Karnataka`
   - **Package name**: `com.senseway.karnataka`
   - **Save location**: Choose your preferred directory
   - **Language**: Kotlin
   - **Minimum SDK**: API 24 (Android 7.0)
   - **Build configuration language**: Kotlin DSL (or Groovy)
   - Click "Finish"

4. **Wait for Gradle Sync**
   - Android Studio will download dependencies (first time may take 5-10 minutes)
   - Wait until "Gradle sync finished" appears

## Step 2: Replace Project Files

**IMPORTANT**: The project structure has been created. You need to:

1. **Replace build.gradle files**:
   - Copy `build.gradle` (project level) to project root
   - Copy `app/build.gradle` to `app/` folder
   - Copy `settings.gradle` to project root

2. **Replace AndroidManifest.xml**:
   - Copy `app/src/main/AndroidManifest.xml` to `app/src/main/`
   - This includes all required permissions

3. **Copy all Kotlin source files**:
   - Copy all `.kt` files from `app/src/main/java/com/senseway/karnataka/` to your project
   - Ensure package name is `com.senseway.karnataka`

4. **Copy resource files**:
   - Copy all XML files from `app/src/main/res/` to your project's `res/` folder
   - This includes layouts, strings, colors, themes

## Step 3: Sync Gradle

1. **Open build.gradle files**:
   - Click "Sync Now" if prompted
   - Or: File → Sync Project with Gradle Files

2. **Verify dependencies**:
   - Check that all dependencies in `app/build.gradle` are downloaded
   - Look for any red error messages in the Build output

## Step 4: Configure Permissions (Android 10-14+)

The app handles runtime permissions automatically, but you may need to:

1. **For Android 12+ (API 31+)**:
   - The app requests `POST_NOTIFICATIONS` permission at runtime
   - User must grant this for notifications to work

2. **For Background Location (Android 10+)**:
   - The app requests `ACCESS_BACKGROUND_LOCATION` if needed
   - User may need to grant this in Settings → Apps → SenseWay Karnataka → Permissions

## Step 5: Build and Run

1. **Connect Device or Start Emulator**:
   - Connect Android device via USB (enable USB debugging)
   - Or start an Android emulator with Google Play Services

2. **Build Project**:
   - Click "Build" → "Make Project" (Ctrl+F9 / Cmd+F9)
   - Wait for build to complete

3. **Run App**:
   - Click "Run" → "Run 'app'" (Shift+F10 / Ctrl+R)
   - Or click the green "Run" button
   - Select your device/emulator
   - Wait for app to install and launch

## Step 6: Grant Permissions

On first launch, the app will request permissions:

1. **Microphone** - Required for voice commands
2. **Location** - Required for navigation and warning zones
3. **Camera** - Required for scene description and money identification
4. **Phone/SMS** - Required for emergency features
5. **Notifications** - Required for foreground service

**Grant all permissions** when prompted.

## Step 7: Test Core Features

### Test Voice Assistant:
1. Click "Start Assistant" button
2. Grant microphone permission if prompted
3. Speak a command: "Transport mode" or "Emergency"
4. App should respond via TTS

### Test Warning Zones:
1. Click "Warning Mode" to activate
2. The app monitors your location
3. When near a default zone (Majestic Junction, Cubbon Park), you'll get a warning

### Test Transport Mode:
1. Click "Transport Mode" or say "Transport mode"
2. Google Maps should open in transit mode
3. (Requires Google Maps app installed)

### Test Emergency:
1. Click "Emergency" button
2. Siren should play
3. Say "Stop alarm" or "I am okay" to cancel
4. If not cancelled within 7 minutes, emergency contact is called

### Test Scene Description:
1. Click "Describe Scene"
2. Grant camera permission
3. Point camera at objects
4. Tap "Capture & Describe"
5. App should describe what it sees

### Test Money Identifier:
1. Click "Identify Money"
2. Point camera at currency note
3. Tap "Capture & Identify"
4. (Note: This is DEMO mode - see code comments for production upgrade path)

## Step 8: Set Emergency Contact

1. Click "Emergency Contact" button
2. Enter contact name and phone number
3. Click "Save Emergency Contact"
4. This contact will be called in emergency situations

## Troubleshooting

### Build Errors:

1. **"Cannot resolve symbol"**:
   - File → Invalidate Caches → Invalidate and Restart
   - Sync Gradle again

2. **"Package not found"**:
   - Check package name in all files matches `com.senseway.karnataka`
   - Rebuild project

3. **Gradle sync fails**:
   - Check internet connection
   - File → Settings → Build → Gradle → Use Gradle from: (select wrapper)
   - Try: File → Invalidate Caches

### Runtime Errors:

1. **"Speech recognition not available"**:
   - Ensure device has Google app installed
   - Check microphone permission is granted

2. **"Location permission denied"**:
   - Go to Settings → Apps → SenseWay Karnataka → Permissions
   - Grant Location permission (including "Allow all the time" for background)

3. **"Google Maps not opening"**:
   - Install Google Maps app from Play Store
   - Or use browser fallback (implemented in code)

4. **"ML Kit not working"**:
   - Ensure device has Google Play Services
   - Check camera permission is granted

## Project Structure

```
app/
├── src/
│   └── main/
│       ├── java/com/senseway/karnataka/
│       │   ├── MainActivity.kt
│       │   ├── VoiceService.kt
│       │   ├── VoiceAssistant.kt
│       │   ├── CommandProcessor.kt
│       │   ├── NotificationUtils.kt
│       │   ├── PermissionManager.kt
│       │   ├── WarningZone.kt
│       │   ├── WarningZoneManager.kt
│       │   ├── ZoneStorage.kt
│       │   ├── AddZoneActivity.kt
│       │   ├── TransportHelper.kt
│       │   ├── EmergencyManager.kt
│       │   ├── EmergencyContact.kt
│       │   ├── EmergencyContactStorage.kt
│       │   ├── EmergencyContactActivity.kt
│       │   ├── DropDetectionManager.kt
│       │   ├── SceneDescriptionActivity.kt
│       │   └── MoneyIdentifierActivity.kt
│       ├── res/
│       │   ├── layout/
│       │   ├── values/
│       │   └── mipmap/
│       └── AndroidManifest.xml
└── build.gradle
```

## Important Notes

1. **FREE APIs ONLY**: All features use free Android/Google services:
   - Android SpeechRecognizer (free)
   - Android TTS (free)
   - Google ML Kit (free, on-device)
   - Google Maps Intents (free, no API key)
   - Google Play Services Location (free)

2. **No Billing Required**: This app does NOT require:
   - Google Cloud Platform billing
   - Google Maps API key
   - Any paid services

3. **Offline Storage**: Uses SharedPreferences (can upgrade to Room later)

4. **Production Upgrades**:
   - Money Identifier: Train custom TFLite model for accurate INR detection
   - Zone Storage: Migrate to Room database for better performance
   - Add more Kannada voice commands

## Next Steps

1. Test all features on a physical device
2. Customize default warning zones for your area
3. Train custom TFLite model for money identification (optional)
4. Add more voice commands in Kannada
5. Optimize battery usage for always-on voice service

## Support

For issues or questions:
- Check Android Studio Logcat for error messages
- Verify all permissions are granted
- Ensure device has Google Play Services
- Test on physical device (emulators may have limitations)
