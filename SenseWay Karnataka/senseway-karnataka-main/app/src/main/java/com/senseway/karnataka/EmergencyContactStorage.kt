package com.senseway.karnataka

import android.content.Context
import android.content.SharedPreferences

/**
 * EmergencyContactStorage manages emergency contact using SharedPreferences (FREE, offline)
 */
class EmergencyContactStorage(private val context: Context) {
    
    private val prefs: SharedPreferences = 
        context.getSharedPreferences("emergency_contact", Context.MODE_PRIVATE)
    private val KEY_NAME = "contact_name"
    private val KEY_PHONE = "contact_phone"
    
    fun saveEmergencyContact(contact: EmergencyContact) {
        prefs.edit().apply {
            putString(KEY_NAME, contact.name)
            putString(KEY_PHONE, contact.phoneNumber)
            apply()
        }
    }
    
    fun getEmergencyContact(): EmergencyContact? {
        val name = prefs.getString(KEY_NAME, null)
        val phone = prefs.getString(KEY_PHONE, null)
        
        return if (name != null && phone != null) {
            EmergencyContact(name, phone)
        } else {
            null
        }
    }
}
