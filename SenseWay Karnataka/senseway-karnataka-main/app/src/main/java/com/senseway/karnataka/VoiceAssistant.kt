package com.senseway.karnataka

import android.content.Context
import android.speech.tts.TextToSpeech
import android.util.Log
import java.util.Locale

/**
 * VoiceAssistant handles Text-to-Speech in Kannada and English
 * FREE: Uses Android's built-in TTS engine (no paid APIs)
 */
class VoiceAssistant(private val context: Context) : TextToSpeech.OnInitListener {
    
    private var tts: TextToSpeech? = null
    private var isInitialized = false
    
    init {
        tts = TextToSpeech(context, this)
    }
    
    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            isInitialized = true
            // Set default to English, but we'll switch based on detected language
            val result = tts?.setLanguage(Locale.ENGLISH)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("VoiceAssistant", "TTS language not supported")
            }
        } else {
            Log.e("VoiceAssistant", "TTS initialization failed")
        }
    }
    
    /**
     * Speak text in the detected language (Kannada or English)
     */
    fun speak(text: String, language: String = "en") {
        if (!isInitialized || tts == null) {
            Log.w("VoiceAssistant", "TTS not initialized yet")
            return
        }
        
        try {
            // Set language based on detection
            val locale = when (language.lowercase()) {
                "kn", "kannada", "kn-in" -> Locale("kn", "IN")
                "en", "english", "en-in" -> Locale("en", "IN")
                else -> Locale.ENGLISH
            }
            
            val result = tts?.setLanguage(locale)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                // Fallback to English if Kannada not available
                tts?.setLanguage(Locale.ENGLISH)
            }
            
            tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
        } catch (e: Exception) {
            Log.e("VoiceAssistant", "Error speaking: ${e.message}")
        }
    }
    
    /**
     * Stop speaking
     */
    fun stop() {
        tts?.stop()
    }
    
    /**
     * Cleanup
     */
    fun shutdown() {
        tts?.stop()
        tts?.shutdown()
        tts = null
        isInitialized = false
    }
}
