package com.senseway.karnataka

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import org.json.JSONArray

/**
 * ZoneStorage manages warning zones using SharedPreferences (FREE, offline)
 * Can be upgraded to Room database later if needed
 */
class ZoneStorage(private val context: Context) {
    
    private val prefs: SharedPreferences = 
        context.getSharedPreferences("warning_zones", Context.MODE_PRIVATE)
    private val KEY_ZONES = "zones"
    
    /**
     * Save a warning zone
     */
    fun saveZone(zone: WarningZone): Boolean {
        return try {
            val zones = getAllZones().toMutableList()
            zones.add(zone)
            saveZones(zones)
            true
        } catch (e: Exception) {
            Log.e("ZoneStorage", "Error saving zone: ${e.message}")
            false
        }
    }
    
    /**
     * Get all saved zones
     */
    fun getAllZones(): List<WarningZone> {
        val zonesJson = prefs.getString(KEY_ZONES, "[]") ?: "[]"
        return try {
            val jsonArray = JSONArray(zonesJson)
            val zones = mutableListOf<WarningZone>()
            for (i in 0 until jsonArray.length()) {
                val zone = WarningZone.fromJsonString(jsonArray.getString(i))
                if (zone != null) {
                    zones.add(zone)
                }
            }
            zones
        } catch (e: Exception) {
            Log.e("ZoneStorage", "Error loading zones: ${e.message}")
            emptyList()
        }
    }
    
    /**
     * Delete a zone by ID
     */
    fun deleteZone(zoneId: String): Boolean {
        return try {
            val zones = getAllZones().filter { it.id != zoneId }
            saveZones(zones)
            true
        } catch (e: Exception) {
            Log.e("ZoneStorage", "Error deleting zone: ${e.message}")
            false
        }
    }
    
    private fun saveZones(zones: List<WarningZone>) {
        val jsonArray = JSONArray()
        zones.forEach { zone ->
            jsonArray.put(zone.toJsonString())
        }
        prefs.edit().putString(KEY_ZONES, jsonArray.toString()).apply()
    }
    
    /**
     * Initialize with default demo zones in Karnataka
     */
    fun initializeDefaultZones() {
        if (getAllZones().isEmpty()) {
            val defaultZones = listOf(
                WarningZone(
                    id = "majestic_junction",
                    name = "Majestic Junction",
                    warningText = "High traffic area. Be careful crossing the road.",
                    latitude = 12.9774,
                    longitude = 77.5686,
                    radiusMeters = 100.0
                ),
                WarningZone(
                    id = "cubbon_park",
                    name = "Cubbon Park Entrance",
                    warningText = "Pedestrian crossing ahead. Watch for vehicles.",
                    latitude = 12.9716,
                    longitude = 77.5946,
                    radiusMeters = 50.0
                )
            )
            defaultZones.forEach { saveZone(it) }
        }
    }
}
