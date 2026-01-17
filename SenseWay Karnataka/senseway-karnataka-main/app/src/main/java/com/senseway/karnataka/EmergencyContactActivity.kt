package com.senseway.karnataka

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

/**
 * EmergencyContactActivity allows users to set emergency contact
 */
class EmergencyContactActivity : AppCompatActivity() {
    
    private lateinit var etContactName: EditText
    private lateinit var etContactPhone: EditText
    private lateinit var btnSaveContact: Button
    
    private val contactStorage = EmergencyContactStorage(this)
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_emergency_contact)
        
        etContactName = findViewById(R.id.etContactName)
        etContactPhone = findViewById(R.id.etContactPhone)
        btnSaveContact = findViewById(R.id.btnSaveContact)
        
        // Load existing contact
        contactStorage.getEmergencyContact()?.let { contact ->
            etContactName.setText(contact.name)
            etContactPhone.setText(contact.phoneNumber)
        }
        
        btnSaveContact.setOnClickListener {
            saveContact()
        }
    }
    
    private fun saveContact() {
        val name = etContactName.text.toString().trim()
        val phone = etContactPhone.text.toString().trim()
        
        if (name.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }
        
        val contact = EmergencyContact(name, phone)
        contactStorage.saveEmergencyContact(contact)
        
        Toast.makeText(this, "Emergency contact saved", Toast.LENGTH_SHORT).show()
        finish()
    }
}
