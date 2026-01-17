package com.senseway.karnataka

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.telephony.SmsManager
import android.util.Log
import android.os.Handler
import android.os.Looper
import androidx.core.content.ContextCompat

/**
 * EmergencyManager handles emergency situations (drop detection, manual trigger)
 * FREE: Uses Android's built-in call/SMS (no paid APIs)
 */
class EmergencyManager(private val context: Context) {
    
    private val voiceAssistant = VoiceAssistant(context)
    private val warningZoneManager = WarningZoneManager(context)
    private val dropDetectionManager = DropDetectionManager(context)
    
    private var mediaPlayer: MediaPlayer? = null
    private var alarmHandler: Handler? = null
    private var isAlarmActive = false
    private val EMERGENCY_TIMEOUT_MS = 7 * 60 * 1000L // 7 minutes
    
    private val emergencyContactStorage = EmergencyContactStorage(context)
    
    init {
        // Setup drop detection
        dropDetectionManager.startMonitoring {
            triggerEmergency()
        }
    }
    
    /**
     * Trigger emergency (siren + TTS + auto-call after timeout)
     */
    fun triggerEmergency() {
        if (isAlarmActive) return
        
        isAlarmActive = true
        Log.d("EmergencyManager", "Emergency triggered")
        
        // Start siren
        startSiren()
        
        // Ask "Are you okay?"
        voiceAssistant.speak("Are you okay? Say stop alarm or I am okay to cancel.", "en")
        
        // Schedule auto-call after timeout
        alarmHandler = Handler(Looper.getMainLooper())
        alarmHandler?.postDelayed({
            if (isAlarmActive) {
                // User didn't cancel - trigger emergency actions
                performEmergencyActions()
            }
        }, EMERGENCY_TIMEOUT_MS)
    }
    
    /**
     * Cancel alarm
     */
    fun cancelAlarm() {
        if (!isAlarmActive) return
        
        isAlarmActive = false
        stopSiren()
        alarmHandler?.removeCallbacksAndMessages(null)
        Log.d("EmergencyManager", "Emergency cancelled")
    }
    
    private fun startSiren() {
        try {
            // Use system alarm sound
            val ringtoneUri = android.provider.Settings.System.DEFAULT_ALARM_ALERT_URI
            mediaPlayer = MediaPlayer.create(context, ringtoneUri)
            mediaPlayer?.isLooping = true
            mediaPlayer?.setVolume(1.0f, 1.0f)
            mediaPlayer?.start()
        } catch (e: Exception) {
            Log.e("EmergencyManager", "Error playing siren: ${e.message}")
        }
    }
    
    private fun stopSiren() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }
    
    /**
     * Perform emergency actions (call + SMS with location)
     */
    private fun performEmergencyActions() {
        val contact = emergencyContactStorage.getEmergencyContact()
        
        if (contact == null) {
            voiceAssistant.speak("Emergency contact not set. Please set emergency contact in settings.", "en")
            return
        }
        
        // Get current location
        warningZoneManager.getCurrentLocation { location ->
            val locationLink = if (location != null) {
                "https://www.google.com/maps?q=${location.latitude},${location.longitude}"
            } else {
                "Location unavailable"
            }
            
            val message = "EMERGENCY! I need help. My location: $locationLink"
            
            // Send SMS
            sendEmergencySMS(contact.phoneNumber, message)
            
            // Make call
            makeEmergencyCall(contact.phoneNumber)
            
            voiceAssistant.speak("Emergency contact has been notified.", "en")
        }
    }
    
    private fun sendEmergencySMS(phoneNumber: String, message: String) {
        try {
            val smsManager = context.getSystemService(Context.TELEPHONY_SERVICE) as? android.telephony.SmsManager
                ?: SmsManager.getDefault()
            
            smsManager.sendTextMessage(
                phoneNumber,
                null,
                message,
                null,
                null
            )
            Log.d("EmergencyManager", "Emergency SMS sent to $phoneNumber")
        } catch (e: Exception) {
            Log.e("EmergencyManager", "Error sending SMS: ${e.message}")
        }
    }
    
    private fun makeEmergencyCall(phoneNumber: String) {
        try {
            val intent = Intent(Intent.ACTION_CALL).apply {
                data = Uri.parse("tel:$phoneNumber")
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
            Log.d("EmergencyManager", "Emergency call initiated to $phoneNumber")
        } catch (e: Exception) {
            Log.e("EmergencyManager", "Error making call: ${e.message}")
        }
    }
    
    fun shutdown() {
        cancelAlarm()
        dropDetectionManager.stopMonitoring()
    }
}
