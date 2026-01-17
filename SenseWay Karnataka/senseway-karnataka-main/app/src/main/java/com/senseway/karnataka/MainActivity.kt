package com.senseway.karnataka

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat

/**
 * MainActivity - Main entry point with voice-controlled UI
 */
class MainActivity : AppCompatActivity() {
    
    private lateinit var btnStartAssistant: Button
    private lateinit var btnTransportMode: Button
    private lateinit var btnWarningMode: Button
    private lateinit var btnAddDangerZone: Button
    private lateinit var btnEmergency: Button
    private lateinit var btnDescribeScene: Button
    private lateinit var btnIdentifyMoney: Button
    private lateinit var btnEmergencyContact: Button
    
    private var isAssistantRunning = false
    private val warningZoneManager: WarningZoneManager by lazy { WarningZoneManager(this) }
    private val emergencyManager: EmergencyManager by lazy { EmergencyManager(this) }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        // Request permissions first
        if (!PermissionManager.hasAllRequiredPermissions(this)) {
            PermissionManager.requestPermissions(this)
        }
        
        initializeViews()
        setupClickListeners()
        
        // Start warning zone monitoring
        if (PermissionManager.hasPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            warningZoneManager.startMonitoring()
        }
    }
    
    private fun initializeViews() {
        btnStartAssistant = findViewById(R.id.btnStartAssistant)
        btnTransportMode = findViewById(R.id.btnTransportMode)
        btnWarningMode = findViewById(R.id.btnWarningMode)
        btnAddDangerZone = findViewById(R.id.btnAddDangerZone)
        btnEmergency = findViewById(R.id.btnEmergency)
        btnDescribeScene = findViewById(R.id.btnDescribeScene)
        btnIdentifyMoney = findViewById(R.id.btnIdentifyMoney)
        btnEmergencyContact = findViewById(R.id.btnEmergencyContact)
    }
    
    private fun setupClickListeners() {
        btnStartAssistant.setOnClickListener {
            toggleVoiceAssistant()
        }
        
        btnTransportMode.setOnClickListener {
            openTransportMode()
        }
        
        btnWarningMode.setOnClickListener {
            toggleWarningMode()
        }
        
        btnAddDangerZone.setOnClickListener {
            openAddZoneActivity()
        }
        
        btnEmergency.setOnClickListener {
            triggerEmergency()
        }
        
        btnDescribeScene.setOnClickListener {
            openSceneDescription()
        }
        
        btnIdentifyMoney.setOnClickListener {
            openMoneyIdentifier()
        }
        
        btnEmergencyContact.setOnClickListener {
            openEmergencyContact()
        }
    }
    
    private fun toggleVoiceAssistant() {
        if (!PermissionManager.hasPermission(this, Manifest.permission.RECORD_AUDIO)) {
            Toast.makeText(this, "Microphone permission required", Toast.LENGTH_SHORT).show()
            PermissionManager.requestPermissions(this)
            return
        }
        
        val intent = Intent(this, VoiceService::class.java)
        
        if (!isAssistantRunning) {
            // Start service
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(intent)
            } else {
                startService(intent)
            }
            
            // Start listening
            intent.action = VoiceService.ACTION_START_LISTENING
            startService(intent)
            
            isAssistantRunning = true
            btnStartAssistant.text = getString(R.string.stop_assistant)
            Toast.makeText(this, "Voice assistant started", Toast.LENGTH_SHORT).show()
        } else {
            // Stop service
            intent.action = VoiceService.ACTION_STOP_LISTENING
            startService(intent)
            stopService(intent)
            
            isAssistantRunning = false
            btnStartAssistant.text = getString(R.string.start_assistant)
            Toast.makeText(this, "Voice assistant stopped", Toast.LENGTH_SHORT).show()
        }
    }
    
    private fun openTransportMode() {
        val transportHelper = TransportHelper(this)
        transportHelper.openTransportMode()
    }
    
    private fun toggleWarningMode() {
        if (PermissionManager.hasPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            if (warningZoneManager.isMonitoringActive) {
                warningZoneManager.stopMonitoring()
                Toast.makeText(this, "Warning mode stopped", Toast.LENGTH_SHORT).show()
            } else {
                warningZoneManager.startMonitoring()
                Toast.makeText(this, "Warning mode activated", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Location permission required", Toast.LENGTH_SHORT).show()
            PermissionManager.requestPermissions(this)
        }
    }
    
    private fun openAddZoneActivity() {
        if (PermissionManager.hasPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            startActivity(Intent(this, AddZoneActivity::class.java))
        } else {
            Toast.makeText(this, "Location permission required", Toast.LENGTH_SHORT).show()
            PermissionManager.requestPermissions(this)
        }
    }
    
    private fun triggerEmergency() {
        emergencyManager.triggerEmergency()
    }
    
    private fun openSceneDescription() {
        if (PermissionManager.hasPermission(this, Manifest.permission.CAMERA)) {
            startActivity(Intent(this, SceneDescriptionActivity::class.java))
        } else {
            Toast.makeText(this, "Camera permission required", Toast.LENGTH_SHORT).show()
            PermissionManager.requestPermissions(this)
        }
    }
    
    private fun openMoneyIdentifier() {
        if (PermissionManager.hasPermission(this, Manifest.permission.CAMERA)) {
            startActivity(Intent(this, MoneyIdentifierActivity::class.java))
        } else {
            Toast.makeText(this, "Camera permission required", Toast.LENGTH_SHORT).show()
            PermissionManager.requestPermissions(this)
        }
    }
    
    private fun openEmergencyContact() {
        startActivity(Intent(this, EmergencyContactActivity::class.java))
    }
    
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        
        if (requestCode == PermissionManager.PERMISSION_REQUEST_CODE) {
            if (PermissionManager.hasAllRequiredPermissions(this)) {
                Toast.makeText(this, "All permissions granted", Toast.LENGTH_SHORT).show()
                
                // Start warning zone monitoring if location permission granted
                if (PermissionManager.hasPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                    warningZoneManager.startMonitoring()
                }
            } else {
                Toast.makeText(this, "Some permissions are required for full functionality", Toast.LENGTH_LONG).show()
            }
        }
    }
    
    override fun onDestroy() {
        super.onDestroy()
        // Note: We don't stop the service here to keep it running in background
        // User can stop it via the button or system settings
    }
}
