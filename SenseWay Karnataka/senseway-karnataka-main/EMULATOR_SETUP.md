# Android Emulator Setup Guide

## Quick Steps to Run Android Emulator

### Step 1: Open Android Studio
1. Launch Android Studio
2. Open this project: `/Applications/ main_el`

### Step 2: Check if AVD Manager is Available
- Go to **Tools** → **Device Manager** (or **Tools** → **AVD Manager** in older versions)
- This opens the Android Virtual Device (AVD) Manager

### Step 3: Create a New Virtual Device (if needed)

If you don't have an emulator set up yet:

1. **Click "Create Device"** button

2. **Select Device Hardware:**
   - Choose a phone (e.g., Pixel 6, Pixel 7, or any phone)
   - Click "Next"

3. **Select System Image:**
   - **IMPORTANT**: Choose an image with **Google Play Services** (look for the "Play Store" icon)
   - Recommended: **API 34 (Android 14)** with Google Play Store
   - If API 34 isn't available, you can use API 33 or API 32
   - **Minimum**: API 24 (Android 7.0) - but prefer API 34
   - If the image isn't downloaded, click "Download" next to it
   - Click "Next"

4. **Configure AVD:**
   - Name: Give it a name (e.g., "Pixel_6_API_34")
   - Leave other settings as default
   - Click "Finish"

### Step 4: Start the Emulator

**Option A: From Device Manager**
1. In the Device Manager, find your emulator
2. Click the **▶️ Play** button next to the emulator name
3. Wait for the emulator to boot (first time may take 2-3 minutes)

**Option B: From Android Studio**
1. Make sure the emulator is listed in the device dropdown (top toolbar)
2. If not started, click the dropdown and select your emulator
3. It will automatically start when you run the app

**Option C: From Command Line**
```bash
# List available emulators
emulator -list-avds

# Start specific emulator (replace with your AVD name)
emulator -avd Pixel_6_API_34
```

### Step 5: Run the App on Emulator

Once the emulator is running:

1. **In Android Studio:**
   - Click the green **▶️ Run** button (or press `Ctrl+R` / `Cmd+R`)
   - Or go to **Run** → **Run 'app'**
   - Select your emulator from the device list
   - Wait for the app to build and install

2. **The app will:**
   - Build the project
   - Install on the emulator
   - Launch automatically

### Step 6: Grant Permissions

When the app launches on the emulator:

1. **Grant permissions when prompted:**
   - Microphone (for voice commands)
   - Location (for navigation and warning zones)
   - Camera (for scene description)
   - Phone/SMS (for emergency features)
   - Notifications (for foreground service)

2. **For Location (important):**
   - Go to Settings → Apps → SenseWay Karnataka → Permissions
   - Grant "Location" → Select "Allow all the time" (for background location)

## Requirements for Emulator

This app requires:
- ✅ **Google Play Services** (must use system image with Play Store)
- ✅ **API 24 minimum** (Android 7.0) - but API 34 recommended
- ✅ **Internet connection** (for Google Maps and location services)

## Common Issues & Solutions

### Issue: "No AVD Manager found"
**Solution:**
- Go to **Tools** → **SDK Manager**
- Install "Android Emulator" if not installed
- Restart Android Studio

### Issue: "Google Play Services not available"
**Solution:**
- Make sure you selected a system image **with Google Play Store** icon
- Cannot use system images without Play Services (like "Google APIs" without Play Store)

### Issue: "Emulator is slow"
**Solution:**
- Enable **Hardware Acceleration** in your system BIOS/UEFI (Intel VT-x or AMD-V)
- In AVD settings, increase RAM allocation (4GB recommended)
- Enable "Use Host GPU" in emulator settings

### Issue: "Location not working on emulator"
**Solution:**
- In emulator, click the **three dots** (⋮) menu
- Go to **Settings** → **Location**
- Set a mock location or use GPS coordinates
- Or use the location controls in the emulator sidebar

### Issue: "Microphone not working"
**Solution:**
- In emulator settings, enable microphone access
- On macOS: System Preferences → Security → Microphone → Allow Android Studio

### Issue: "Camera not working"
**Solution:**
- Emulator has a virtual camera (webcam)
- Test with emulator's camera controls

## Verifying Emulator Setup

To check if your emulator is properly configured:

1. **Check API Level:**
   - Settings → About phone → Android version (should be 7.0 or higher)

2. **Check Google Play Services:**
   - Open Play Store app in emulator
   - If it opens, Google Play Services is installed ✅

3. **Check Internet:**
   - Open browser in emulator
   - Navigate to a website

## Running from Terminal (Advanced)

If you prefer command line:

```bash
# Navigate to project directory
cd "/Applications/ main_el"

# Start emulator
emulator -avd YOUR_AVD_NAME &

# Build and install app
./gradlew installDebug

# Or run directly
./gradlew installDebug && adb shell am start -n com.senseway.karnataka/.MainActivity
```

## Recommended Emulator Configuration

- **Device**: Pixel 6 or Pixel 7
- **API Level**: 34 (Android 14)
- **System Image**: With Google Play Store
- **RAM**: 4096 MB
- **VM Heap**: 512 MB
- **Graphics**: Automatic or Hardware - GLES 2.0

---

**Need Help?**
- Check [BUILD_INSTRUCTIONS.md](BUILD_INSTRUCTIONS.md) for general setup
- Check Android Studio Logcat for error messages
- Ensure all permissions are granted in emulator settings
