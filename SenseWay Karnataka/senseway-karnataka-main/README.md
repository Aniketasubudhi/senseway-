# SenseWay Karnataka

A fully voice-enabled Android app for blind users in Karnataka, supporting Kannada and English voice commands. Built with **100% FREE APIs** - no paid services, no billing required.

## Features

### 🎤 Always-On Voice Assistant
- Continuous hands-free voice recognition using Foreground Service
- Supports Kannada (kn-IN) and English (en-IN)
- Works when app is minimized or screen is off
- Automatic language detection

### 🚌 Navigation & Public Transport (FREE)
- Google Maps transit mode routing (no API key needed)
- Bus route number queries (e.g., "Bus timing 500D")
- Current location → Destination navigation
- Place A → Place B routing

### ⚠️ Warning Zone System
- GPS-based danger zone warnings
- Automatic alerts when entering danger zones
- Custom zones can be added at current location
- Default demo zones in Karnataka (Majestic Junction, Cubbon Park)
- Vibration + voice warnings

### 🆘 Emergency & Drop Detection
- Automatic phone drop detection using accelerometer
- Loud siren + TTS "Are you okay?"
- Auto-call emergency contact after 7 minutes (if not cancelled)
- SMS with live location link
- Voice cancellation: "Stop alarm" or "I am okay"

### 📷 Scene Description (FREE)
- Google ML Kit Object Detection (on-device, free)
- Describes what's in front of the user
- Speaks output in Kannada/English

### 💰 Money Identifier (DEMO)
- Camera-based currency identification
- Supports ₹10, ₹20, ₹50, ₹100, ₹200, ₹500, ₹2000
- Currently in DEMO mode (placeholder)
- Ready for custom TFLite model upgrade

## Voice Commands

### English:
- "Transport mode" - Open navigation
- "Warning mode" - Activate warning zones
- "Add danger zone" - Add custom zone
- "Emergency" - Trigger emergency
- "Describe scene" - Analyze camera view
- "Identify money" - Identify currency
- "Stop alarm" / "I am okay" - Cancel emergency
- "Bus timing 500D" - Search bus route

### Kannada:
- "ನೇವಿಗೆ" - Transport mode
- "ಎಚ್ಚರಿಕೆ" - Warning mode
- "ಅಪಾಯಕಾರಿ" - Add danger zone
- "ಅನಾಹುತ" - Emergency
- "ವಿವರಿಸಿ" / "ನೋಡಿ" - Describe scene
- "ನಾಣ್ಯ" / "ಹಣ" - Identify money
- "ನಾನು ಸರಿ" / "ನಿಲ್ಲಿಸು" - Stop alarm

## Technical Stack

### FREE APIs & Services:
- ✅ Android SpeechRecognizer (built-in, free)
- ✅ Android Text-to-Speech (built-in, free)
- ✅ Google ML Kit (on-device, free)
- ✅ Google Maps Intents (no API key needed)
- ✅ Google Play Services Location (free)
- ✅ CameraX (Android Jetpack, free)
- ✅ SharedPreferences (offline storage, free)

### Architecture:
- **Foreground Service**: Always-on voice recognition
- **Command Router**: Maps voice commands to actions
- **Location Manager**: GPS-based warning zones
- **Sensor Manager**: Drop detection
- **ML Kit Integration**: On-device object detection

## Permissions Required

- `RECORD_AUDIO` - Voice commands
- `ACCESS_FINE_LOCATION` - Navigation & warning zones
- `ACCESS_BACKGROUND_LOCATION` - Warning zones when app in background
- `CAMERA` - Scene description & money identification
- `CALL_PHONE` - Emergency calls
- `SEND_SMS` - Emergency SMS
- `POST_NOTIFICATIONS` - Foreground service notification
- `VIBRATE` - Warning zone alerts

## Installation

See [BUILD_INSTRUCTIONS.md](BUILD_INSTRUCTIONS.md) for detailed step-by-step setup.

### Quick Start:
1. Open project in Android Studio
2. Sync Gradle
3. Build and run on device
4. Grant all permissions
5. Start using voice commands!

## Project Structure

```
app/src/main/java/com/senseway/karnataka/
├── MainActivity.kt              # Main UI
├── VoiceService.kt              # Foreground service for voice
├── VoiceAssistant.kt            # TTS handler
├── CommandProcessor.kt          # Voice command router
├── NotificationUtils.kt         # Foreground service notifications
├── PermissionManager.kt         # Runtime permissions
├── WarningZone.kt               # Zone data model
├── WarningZoneManager.kt        # Location monitoring
├── ZoneStorage.kt               # Offline zone storage
├── AddZoneActivity.kt           # Add custom zones
├── TransportHelper.kt           # Google Maps intents
├── EmergencyManager.kt          # Emergency handling
├── EmergencyContact.kt          # Contact data model
├── EmergencyContactStorage.kt   # Contact storage
├── EmergencyContactActivity.kt  # Set emergency contact
├── DropDetectionManager.kt      # Accelerometer drop detection
├── SceneDescriptionActivity.kt  # ML Kit scene analysis
└── MoneyIdentifierActivity.kt    # Currency identification (demo)
```

## Testing Instructions

### 1. Voice Assistant
- Click "Start Assistant"
- Say: "Transport mode"
- Verify TTS response and Google Maps opens

### 2. Warning Zones
- Click "Warning Mode"
- Move to a default zone (Majestic Junction area)
- Verify vibration + voice warning

### 3. Emergency
- Click "Emergency" or drop phone
- Verify siren plays
- Say "Stop alarm" to cancel
- Or wait 7 minutes to test auto-call (set emergency contact first)

### 4. Scene Description
- Click "Describe Scene"
- Point camera at objects
- Tap "Capture & Describe"
- Verify spoken description

### 5. Transport Mode
- Say "Transport mode" or click button
- Verify Google Maps opens in transit mode
- Test bus route: "Bus timing 500D"

## Important Notes

### FREE ONLY - No Paid Services
- ✅ No Google Cloud Platform billing
- ✅ No Google Maps API key required
- ✅ No paid ML services
- ✅ All features use free Android/Google tools

### Limitations
- Money Identifier is in DEMO mode (needs custom TFLite model for production)
- Scene description uses generic object detection (not specialized for blind users)
- Bus route queries use Google Maps search (may not be 100% accurate)

### Production Upgrades
1. **Money Identifier**: Train custom TFLite model with INR currency images
2. **Zone Storage**: Migrate to Room database for better performance
3. **Scene Description**: Use specialized models for blind assistance
4. **Voice Commands**: Expand Kannada vocabulary

## Compatibility

- **Minimum SDK**: API 24 (Android 7.0)
- **Target SDK**: API 34 (Android 14)
- **Tested on**: Android 10-14
- **Requires**: Google Play Services (for location & ML Kit)

## License

This project is provided as-is for educational and accessibility purposes.

## Contributing

Contributions welcome! Areas for improvement:
- More Kannada voice commands
- Better money identification model
- Enhanced scene description
- Battery optimization
- Offline mode improvements

## Support

For issues:
1. Check [BUILD_INSTRUCTIONS.md](BUILD_INSTRUCTIONS.md) troubleshooting section
2. Verify all permissions are granted
3. Ensure Google Play Services is installed
4. Test on physical device (emulators may have limitations)

---

**Built with ❤️ for accessibility in Karnataka**
