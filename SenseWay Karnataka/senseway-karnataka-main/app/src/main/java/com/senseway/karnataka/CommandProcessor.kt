package com.senseway.karnataka

import android.content.Context
import android.content.Intent
import android.util.Log

/**
 * CommandProcessor routes voice commands to appropriate actions
 * Supports both Kannada and English commands
 */
class CommandProcessor(private val context: Context) {
    
    private val voiceAssistant = VoiceAssistant(context)
    private val transportHelper = TransportHelper(context)
    private val emergencyManager = EmergencyManager(context)
    
    /**
     * Process a voice command and execute the corresponding action
     */
    fun processCommand(command: String, detectedLanguage: String = "en") {
        val normalizedCommand = command.lowercase().trim()
        Log.d("CommandProcessor", "Processing command: $normalizedCommand (lang: $detectedLanguage)")
        
        when {
            // Transport mode commands
            normalizedCommand.contains("transport") || 
            normalizedCommand.contains("ಸಾರಿಗೆ") ||
            normalizedCommand.contains("bus") || 
            normalizedCommand.contains("metro") ||
            normalizedCommand.contains("route") -> {
                handleTransportCommand(normalizedCommand, detectedLanguage)
            }
            
            // Warning mode commands
            normalizedCommand.contains("warning") || 
            normalizedCommand.contains("ಎಚ್ಚರಿಕೆ") -> {
                handleWarningMode(detectedLanguage)
            }
            
            // Add danger zone
            normalizedCommand.contains("add danger") || 
            normalizedCommand.contains("danger zone") ||
            normalizedCommand.contains("ಅಪಾಯಕಾರಿ") -> {
                handleAddDangerZone(detectedLanguage)
            }
            
            // Emergency commands
            normalizedCommand.contains("emergency") || 
            normalizedCommand.contains("ಅನಾಹುತ") ||
            normalizedCommand.contains("help") -> {
                handleEmergency(detectedLanguage)
            }
            
            // Scene description
            normalizedCommand.contains("describe") || 
            normalizedCommand.contains("scene") ||
            normalizedCommand.contains("ವಿವರಿಸಿ") ||
            normalizedCommand.contains("ನೋಡಿ") -> {
                handleSceneDescription(detectedLanguage)
            }
            
            // Money identification
            normalizedCommand.contains("money") || 
            normalizedCommand.contains("identify money") ||
            normalizedCommand.contains("ನಾಣ್ಯ") ||
            normalizedCommand.contains("ಹಣ") -> {
                handleMoneyIdentification(detectedLanguage)
            }
            
            // Stop alarm / I am okay
            normalizedCommand.contains("stop alarm") || 
            normalizedCommand.contains("i am okay") ||
            normalizedCommand.contains("i'm okay") ||
            normalizedCommand.contains("ನಾನು ಸರಿ") ||
            normalizedCommand.contains("ನಿಲ್ಲಿಸು") -> {
                handleStopAlarm(detectedLanguage)
            }
            
            // Bus route number query (e.g., "bus timing 500D")
            normalizedCommand.matches(Regex(".*bus.*timing.*\\d+.*", RegexOption.IGNORE_CASE)) -> {
                handleBusTimingQuery(normalizedCommand, detectedLanguage)
            }

            // Open emergency contact screen
            normalizedCommand.contains("emergency contact") ||
            normalizedCommand.contains("ತುರ್ತು ಸಂಪರ್ಕ") -> {
                handleEmergencyContact(detectedLanguage)
            }
            
            else -> {
                val response = if (detectedLanguage == "kn") {
                    "ಕಮಾಂಡ್ ಅರ್ಥವಾಗಲಿಲ್ಲ. ದಯವಿಟ್ಟು ಮತ್ತೆ ಪ್ರಯತ್ನಿಸಿ."
                } else {
                    "Command not understood. Please try again."
                }
                voiceAssistant.speak(response, detectedLanguage)
            }
        }
    }
    
    private fun handleTransportCommand(command: String, language: String) {
        val response = if (language == "kn") {
            "ಸಾರಿಗೆ ಮೋಡ್ ತೆರೆಯಲಾಗುತ್ತಿದೆ"
        } else {
            "Opening transport mode"
        }
        voiceAssistant.speak(response, language)
        
        // Open transport mode (will prompt for destination)
        transportHelper.openTransportMode()
    }
    
    private fun handleWarningMode(language: String) {
        val response = if (language == "kn") {
            "ಎಚ್ಚರಿಕೆ ಮೋಡ್ ಸಕ್ರಿಯಗೊಳಿಸಲಾಗಿದೆ"
        } else {
            "Warning mode activated"
        }
        voiceAssistant.speak(response, language)
        
        // Warning mode is handled by WarningZoneManager automatically
    }
    
    private fun handleAddDangerZone(language: String) {
        val response = if (language == "kn") {
            "ಅಪಾಯಕಾರಿ ವಲಯವನ್ನು ಸೇರಿಸಲು ತೆರೆಯಲಾಗುತ್ತಿದೆ"
        } else {
            "Opening add danger zone screen"
        }
        voiceAssistant.speak(response, language)
        
        val intent = Intent(context, AddZoneActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }
    
    private fun handleEmergency(language: String) {
        val response = if (language == "kn") {
            "ಅನಾಹುತ ಸಹಾಯವನ್ನು ಸಕ್ರಿಯಗೊಳಿಸಲಾಗುತ್ತಿದೆ"
        } else {
            "Activating emergency assistance"
        }
        voiceAssistant.speak(response, language)
        
        emergencyManager.triggerEmergency()
    }
    
    private fun handleSceneDescription(language: String) {
        val response = if (language == "kn") {
            "ದೃಶ್ಯ ವಿವರಣೆಯನ್ನು ತೆರೆಯಲಾಗುತ್ತಿದೆ"
        } else {
            "Opening scene description"
        }
        voiceAssistant.speak(response, language)
        
        val intent = Intent(context, SceneDescriptionActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }
    
    private fun handleMoneyIdentification(language: String) {
        val response = if (language == "kn") {
            "ಹಣದ ಗುರುತಿಸುವಿಕೆಯನ್ನು ತೆರೆಯಲಾಗುತ್ತಿದೆ"
        } else {
            "Opening money identification"
        }
        voiceAssistant.speak(response, language)
        
        val intent = Intent(context, MoneyIdentifierActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    private fun handleEmergencyContact(language: String) {
        val response = if (language == "kn") {
            "ತುರ್ತು ಸಂಪರ್ಕವನ್ನು ತೆರೆಯಲಾಗುತ್ತಿದೆ"
        } else {
            "Opening emergency contact"
        }
        voiceAssistant.speak(response, language)

        val intent = Intent(context, EmergencyContactActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }
    
    private fun handleStopAlarm(language: String) {
        emergencyManager.cancelAlarm()
        val response = if (language == "kn") {
            "ಅಲಾರ್ಮ್ ನಿಲ್ಲಿಸಲಾಗಿದೆ"
        } else {
            "Alarm stopped"
        }
        voiceAssistant.speak(response, language)
    }
    
    private fun handleBusTimingQuery(command: String, language: String) {
        // Extract route number (e.g., "500D" from "bus timing 500D")
        val routeNumber = command.replace(Regex(".*bus.*timing.*", RegexOption.IGNORE_CASE), "")
            .trim()
            .replace(Regex("[^a-zA-Z0-9]"), "")
        
        if (routeNumber.isNotEmpty()) {
            val response = if (language == "kn") {
                "ಬಸ್ $routeNumber ಗಾಗಿ ಮಾರ್ಗವನ್ನು ಹುಡುಕಲಾಗುತ್ತಿದೆ"
            } else {
                "Searching route for bus $routeNumber"
            }
            voiceAssistant.speak(response, language)
            transportHelper.searchBusRoute(routeNumber)
        } else {
            val response = if (language == "kn") {
                "ದಯವಿಟ್ಟು ಬಸ್ ಸಂಖ್ಯೆಯನ್ನು ನಿರ್ದಿಷ್ಟಪಡಿಸಿ"
            } else {
                "Please specify the bus number"
            }
            voiceAssistant.speak(response, language)
        }
    }
    
    fun shutdown() {
        voiceAssistant.shutdown()
    }
}
