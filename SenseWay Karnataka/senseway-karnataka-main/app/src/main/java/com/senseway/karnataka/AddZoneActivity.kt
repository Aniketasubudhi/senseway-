package com.senseway.karnataka

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.UUID

/**
 * AddZoneActivity allows users to add custom danger zones at current location
 */
class AddZoneActivity : AppCompatActivity() {
    
    private lateinit var etZoneName: EditText
    private lateinit var etWarningText: EditText
    private lateinit var etRadius: EditText
    private lateinit var btnSaveZone: Button
    
    private val zoneStorage = ZoneStorage(this)
    private val warningZoneManager = WarningZoneManager(this)
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_zone)
        
        etZoneName = findViewById(R.id.etZoneName)
        etWarningText = findViewById(R.id.etWarningText)
        etRadius = findViewById(R.id.etRadius)
        btnSaveZone = findViewById(R.id.btnSaveZone)
        
        // Set default radius
        etRadius.setText("50")
        
        btnSaveZone.setOnClickListener {
            saveZone()
        }
    }
    
    private fun saveZone() {
        val name = etZoneName.text.toString().trim()
        val warningText = etWarningText.text.toString().trim()
        val radiusText = etRadius.text.toString().trim()
        
        if (name.isEmpty() || warningText.isEmpty() || radiusText.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }
        
        val radius = try {
            radiusText.toDouble()
        } catch (e: NumberFormatException) {
            Toast.makeText(this, "Invalid radius", Toast.LENGTH_SHORT).show()
            return
        }
        
        // Get current location
        warningZoneManager.getCurrentLocation { location ->
            if (location == null) {
                Toast.makeText(this, "Could not get current location. Please enable location services.", Toast.LENGTH_LONG).show()
                return@getCurrentLocation
            }
            
            val zone = WarningZone(
                id = UUID.randomUUID().toString(),
                name = name,
                warningText = warningText,
                latitude = location.latitude,
                longitude = location.longitude,
                radiusMeters = radius
            )
            
            if (zoneStorage.saveZone(zone)) {
                Toast.makeText(this, "Zone saved successfully", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Failed to save zone", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
