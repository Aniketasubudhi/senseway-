package com.senseway.karnataka

import android.content.Context
import android.location.Location
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.Build
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority

/**
 * WarningZoneManager monitors user location and warns when entering danger zones
 * FREE: Uses Google Play Services Location (free, no API key needed for basic location)
 */
class WarningZoneManager(private val context: Context) {
    
    private val zoneStorage = ZoneStorage(context)
    private val voiceAssistant = VoiceAssistant(context)
    private val fusedLocationClient: FusedLocationProviderClient = 
        LocationServices.getFusedLocationProviderClient(context)
    
    private var locationCallback: LocationCallback? = null
    private var lastWarningTime = mutableMapOf<String, Long>()
    private val WARNING_COOLDOWN_MS = 30000L // 30 seconds cooldown per zone
    
    private val handler = Handler(Looper.getMainLooper())
    private var isMonitoring = false
    
    val isMonitoringActive: Boolean
        get() = isMonitoring
    
    init {
        zoneStorage.initializeDefaultZones()
    }
    
    /**
     * Start monitoring for warning zones
     */
    fun startMonitoring() {
        if (isMonitoring) return
        
        isMonitoring = true
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.lastLocation?.let { location ->
                    checkWarningZones(location)
                }
            }
        }
        
        val locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            5000L // Update every 5 seconds
        ).apply {
            setMinUpdateIntervalMillis(3000L)
            setMaxUpdateDelayMillis(10000L)
        }.build()
        
        try {
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback!!,
                Looper.getMainLooper()
            )
            Log.d("WarningZoneManager", "Started monitoring warning zones")
        } catch (e: SecurityException) {
            Log.e("WarningZoneManager", "Location permission not granted")
        }
    }
    
    /**
     * Stop monitoring
     */
    fun stopMonitoring() {
        if (!isMonitoring) return
        
        locationCallback?.let {
            fusedLocationClient.removeLocationUpdates(it)
        }
        locationCallback = null
        isMonitoring = false
        Log.d("WarningZoneManager", "Stopped monitoring warning zones")
    }
    
    /**
     * Check if current location is within any warning zone
     */
    private fun checkWarningZones(currentLocation: Location) {
        val zones = zoneStorage.getAllZones()
        
        for (zone in zones) {
            val zoneLocation = Location("").apply {
                latitude = zone.latitude
                longitude = zone.longitude
            }
            
            val distance = currentLocation.distanceTo(zoneLocation)
            
            if (distance <= zone.radiusMeters) {
                // Check cooldown
                val lastWarning = lastWarningTime[zone.id] ?: 0L
                val now = System.currentTimeMillis()
                
                if (now - lastWarning > WARNING_COOLDOWN_MS) {
                    // Trigger warning
                    triggerWarning(zone, distance.toDouble())
                    lastWarningTime[zone.id] = now
                }
            }
        }
    }
    
    /**
     * Trigger warning (vibrate + speak)
     */
    private fun triggerWarning(zone: WarningZone, distance: Double) {
        // Vibrate
        val vibrator = ContextCompat.getSystemService(context, Vibrator::class.java)
        if (vibrator != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(
                    VibrationEffect.createWaveform(
                        longArrayOf(0, 500, 200, 500),
                        -1
                    )
                )
            } else {
                @Suppress("DEPRECATION")
                vibrator.vibrate(longArrayOf(0, 500, 200, 500), -1)
            }
        }
        
        // Speak warning
        val warningMessage = "${zone.name}. ${zone.warningText}"
        voiceAssistant.speak(warningMessage, "en") // Can detect language if needed
        
        Log.d("WarningZoneManager", "Warning triggered: ${zone.name} (distance: ${distance.toInt()}m)")
    }
    
    /**
     * Get current location (one-time)
     */
    fun getCurrentLocation(callback: (Location?) -> Unit) {
        try {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                callback(location)
            }.addOnFailureListener {
                Log.e("WarningZoneManager", "Error getting location: ${it.message}")
                callback(null)
            }
        } catch (e: SecurityException) {
            Log.e("WarningZoneManager", "Location permission not granted")
            callback(null)
        }
    }
}
