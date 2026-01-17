package com.senseway.karnataka

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import androidx.lifecycle.LifecycleService
import java.util.Locale

/**
 * VoiceService - Foreground Service for always-on voice recognition
 * FREE: Uses Android's built-in SpeechRecognizer (no paid APIs)
 * Supports Kannada (kn-IN) and English (en-IN)
 */
class VoiceService : LifecycleService() {
    
    private var speechRecognizer: SpeechRecognizer? = null
    private var commandProcessor: CommandProcessor? = null
    private var isListening = false
    private var currentLanguage = "en" // Default to English
    
    private val binder = LocalBinder()
    
    inner class LocalBinder : Binder() {
        fun getService(): VoiceService = this@VoiceService
    }
    
    override fun onCreate() {
        super.onCreate()
        Log.d("VoiceService", "Service created")
        
        NotificationUtils.createNotificationChannel(this)
        commandProcessor = CommandProcessor(this)
        
        // Initialize speech recognizer
        if (SpeechRecognizer.isRecognitionAvailable(this)) {
            speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)
            setupRecognitionListener()
        } else {
            Log.e("VoiceService", "Speech recognition not available")
        }
    }
    
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        
        // Start as foreground service
        startForeground(NotificationUtils.NOTIFICATION_ID, NotificationUtils.createNotification(this))
        
        when (intent?.action) {
            ACTION_START_LISTENING -> {
                startListening(getLanguageCode())
            }
            ACTION_STOP_LISTENING -> {
                stopListening()
            }
            ACTION_TOGGLE_LANGUAGE -> {
                toggleLanguage()
            }
            else -> {
                // Default: start listening if no action specified
                startListening(getLanguageCode())
            }
        }
        
        return START_STICKY
    }
    
    override fun onBind(intent: Intent): IBinder {
        super.onBind(intent)
        return binder
    }
    
    fun startListening(language: String) {
        if (isListening) return
        
        if (speechRecognizer == null) {
            if (SpeechRecognizer.isRecognitionAvailable(this)) {
                speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)
                setupRecognitionListener()
            } else {
                Log.e("VoiceService", "Speech recognition not available, cannot start listening.")
                return
            }
        }
        
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, language)
            putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
            putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)
        }
        
        try {
            speechRecognizer?.startListening(intent)
            isListening = true
            Log.d("VoiceService", "Started listening (language: $language)")
        } catch (e: Exception) {
            Log.e("VoiceService", "Error starting recognition: ${e.message}")
        }
    }
    
    fun stopListening() {
        speechRecognizer?.stopListening()
        isListening = false
        Log.d("VoiceService", "Stopped listening")
    }
    
    private fun toggleLanguage() {
        currentLanguage = if (currentLanguage == "en") "kn" else "en"
        Log.d("VoiceService", "Language toggled to: $currentLanguage")
        if (isListening) {
            stopListening()
            startListening(getLanguageCode())
        }
    }
    
    private fun getLanguageCode(): String {
        return when (currentLanguage) {
            "kn" -> "kn-IN"
            else -> "en-IN"
        }
    }
    
    private fun setupRecognitionListener() {
        speechRecognizer?.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(params: android.os.Bundle?) {
                Log.d("VoiceService", "Ready for speech")
            }
            
            override fun onBeginningOfSpeech() {
                Log.d("VoiceService", "Beginning of speech")
            }
            
            override fun onRmsChanged(rmsdB: Float) {
                // Audio level feedback (optional)
            }
            
            override fun onBufferReceived(buffer: ByteArray?) {
                // Not used
            }
            
            override fun onEndOfSpeech() {
                Log.d("VoiceService", "End of speech")
                // Restart listening after a short delay
                android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
                    if (isListening) {
                        startListening(getLanguageCode())
                    }
                }, 500)
            }
            
            override fun onError(error: Int) {
                Log.e("VoiceService", "Recognition error: $error")
                // Restart listening after a short delay
                android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
                    if (isListening) {
                        startListening(getLanguageCode())
                    }
                }, 1000)
            }
            
            override fun onResults(results: android.os.Bundle?) {
                val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                if (matches != null && matches.isNotEmpty()) {
                    val command = matches[0]
                    Log.d("VoiceService", "Recognized: $command")

                    commandProcessor?.processCommand(command, currentLanguage)
                }
                
                // Continue listening
                if (isListening) {
                    startListening(getLanguageCode())
                }
            }
            
            override fun onPartialResults(partialResults: android.os.Bundle?) {
                // Optional: handle partial results
            }
            
            override fun onEvent(eventType: Int, params: android.os.Bundle?) {
                // Not used
            }
        })
    }
    
    override fun onDestroy() {
        super.onDestroy()
        stopListening()
        speechRecognizer?.destroy()
        speechRecognizer = null
        commandProcessor?.shutdown()
        commandProcessor = null
        Log.d("VoiceService", "Service destroyed")
    }
    
    companion object {
        const val ACTION_START_LISTENING = "com.senseway.karnataka.START_LISTENING"
        const val ACTION_STOP_LISTENING = "com.senseway.karnataka.STOP_LISTENING"
        const val ACTION_TOGGLE_LANGUAGE = "com.senseway.karnataka.TOGGLE_LANGUAGE"
    }
}
