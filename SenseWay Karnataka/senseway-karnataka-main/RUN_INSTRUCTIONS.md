# How to Run This Project in Android Studio

## Quick Start

### Option 1: Using the Helper Script
```bash
cd "/Applications/ main_el"
chmod +x open_in_android_studio.sh
./open_in_android_studio.sh
```

### Option 2: Manual Steps

1. **Install Android Studio** (if not installed)
   - Download from: https://developer.android.com/studio
   - Install to `/Applications/`
   - Launch Android Studio

2. **Open the Project**
   - File → Open
   - Navigate to: `/Applications/ main_el`
   - Click "OK"

3. **Wait for Gradle Sync**
   - Android Studio will automatically sync Gradle
   - Wait for "Gradle sync finished" message
   - This may take 5-10 minutes on first run

4. **Set Up Emulator**
   - Go to **Tools** → **Device Manager**
   - Click **"Create Device"**
   - Choose a phone (Pixel 6 recommended)
   - Select **API 34 with Google Play Store**
   - Click "Finish"
   - Click **▶️ Play** button to start emulator

5. **Run the App**
   - Wait for emulator to boot
   - Click the green **▶️ Run** button (or press `Ctrl+R` / `Cmd+R`)
   - Select your emulator from the device list
   - App will build and install automatically

6. **Grant Permissions**
   - When app launches, grant all permissions:
     - Microphone
     - Location (select "Allow all the time")
     - Camera
     - Phone/SMS
     - Notifications

## Troubleshooting

### "Android Studio not found"
- Download and install Android Studio from: https://developer.android.com/studio
- Make sure it's in `/Applications/Android Studio.app`

### "Gradle sync failed"
- Check internet connection
- File → Invalidate Caches → Invalidate and Restart
- Try again

### "No emulator available"
- Tools → Device Manager → Create Device
- Make sure to select system image **with Google Play Store**

### "Build failed"
- Check that minimum SDK is 24 and target SDK is 34
- File → Sync Project with Gradle Files
- Check Build output for specific errors

## Requirements

- ✅ Android Studio Hedgehog (2023.1.1) or later
- ✅ Android SDK with API 24-34
- ✅ Emulator with Google Play Services
- ✅ Internet connection (for first-time downloads)

## Next Steps After Running

1. Test voice commands: Click "Start Assistant" and say "Transport mode"
2. Test warning zones: Click "Warning Mode"
3. Test emergency: Click "Emergency" button
4. Set emergency contact: Click "Emergency Contact" button

For more details, see:
- [EMULATOR_SETUP.md](EMULATOR_SETUP.md) - Detailed emulator guide
- [BUILD_INSTRUCTIONS.md](BUILD_INSTRUCTIONS.md) - Complete build instructions
- [README.md](README.md) - App features and usage
