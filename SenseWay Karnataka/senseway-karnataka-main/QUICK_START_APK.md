# 🚀 Quick Start: Build APK for SenseWay Karnataka

## ⚡ Fastest Way (Using Android Studio)

### Step 1: Install Android Studio
1. Download: https://developer.android.com/studio
2. Install to `/Applications/` (macOS)
3. Launch Android Studio once (it will set up the SDK)

### Step 2: Open Project
1. Open Android Studio
2. **File** → **Open**
3. Select: `/Applications/ main_el`
4. Wait for Gradle sync (5-10 minutes first time)

### Step 3: Build APK
1. **Build** → **Build Bundle(s) / APK(s)** → **Build APK(s)**
2. Wait for build to complete
3. Click notification: **"APK(s) generated successfully - locate"**

### Step 4: Find Your APK
- Location: `app/build/outputs/apk/debug/app-debug.apk`
- File will be highlighted in Finder/Explorer

---

## 📱 Install APK on Android Device

### Enable Installation from Unknown Sources:
- **Android 8+**: Settings → Apps → Special access → Install unknown apps → Select your file manager → Allow
- **Android 7 or below**: Settings → Security → Unknown sources → Enable

### Transfer & Install:
1. **Copy APK** to your Android device (USB, email, cloud, etc.)
2. **Open file manager** on Android device
3. **Tap the APK file** (`app-debug.apk`)
4. **Tap "Install"**
5. **Done!** ✅

---

## 🔧 Command Line Method (Advanced)

### If you have Android SDK installed:

```bash
cd "/Applications/ main_el"

# Create local.properties (update path if different)
echo "sdk.dir=$HOME/Library/Android/sdk" > local.properties

# Build APK
./build_apk.sh
```

APK will be at: `app/build/outputs/apk/debug/app-debug.apk`

---

## ❓ Troubleshooting

### "SDK location not found"
→ Install Android Studio first. The SDK comes with it.

### "Gradle sync failed"
→ Check internet connection. First build needs to download dependencies.

### "Cannot find Java"
→ Install Java JDK 11 or 17

---

## 📋 What You Get

- ✅ **APK file**: Ready to install on any Android device
- ✅ **Debug version**: For testing (not for Play Store)
- ✅ **File size**: ~10-20 MB
- ✅ **Minimum Android**: 7.0 (API 24)
- ✅ **Target Android**: 14 (API 34)

---

**Need more details?** See [APK_BUILD_INSTRUCTIONS.md](APK_BUILD_INSTRUCTIONS.md)
