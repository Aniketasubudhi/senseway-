package com.senseway.karnataka

import org.json.JSONObject

/**
 * Data class for warning zones
 */
data class WarningZone(
    val id: String,
    val name: String,
    val warningText: String,
    val latitude: Double,
    val longitude: Double,
    val radiusMeters: Double
) {
    fun toJsonString(): String {
        return """{"id":"$id","name":"$name","warningText":"$warningText","lat":$latitude,"lng":$longitude,"radius":$radiusMeters}"""
    }
    
    companion object {
        fun fromJsonString(json: String): WarningZone? {
            return try {
                val jsonObject = JSONObject(json)
                WarningZone(
                    id = jsonObject.getString("id"),
                    name = jsonObject.getString("name"),
                    warningText = jsonObject.getString("warningText"),
                    latitude = jsonObject.getDouble("lat"),
                    longitude = jsonObject.getDouble("lng"),
                    radiusMeters = jsonObject.getDouble("radius")
                )
            } catch (e: Exception) {
                null
            }
        }
    }
}
