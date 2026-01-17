package com.senseway.karnataka

import android.content.Context
import android.content.Intent
import android.location.Location
import android.net.Uri
import android.util.Log

/**
 * TransportHelper provides FREE navigation using Google Maps Intents
 * NO API KEY REQUIRED - Uses deep links and intents (completely free)
 */
class TransportHelper(private val context: Context) {
    
    private val warningZoneManager = WarningZoneManager(context)
    
    /**
     * Open Google Maps in transit mode for navigation
     * FREE: Uses Intent, no API key needed
     */
    fun openTransportMode() {
        // Get current location first
        warningZoneManager.getCurrentLocation { location ->
            if (location != null) {
                // Open Google Maps with transit mode
                val uri = Uri.parse("google.navigation:q=${location.latitude},${location.longitude}&mode=t")
                val intent = Intent(Intent.ACTION_VIEW, uri)
                intent.setPackage("com.google.android.apps.maps")
                
                if (intent.resolveActivity(context.packageManager) != null) {
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context.startActivity(intent)
                } else {
                    // Fallback: Open Google Maps app
                    openGoogleMapsApp()
                }
            } else {
                // Just open Google Maps app
                openGoogleMapsApp()
            }
        }
    }
    
    /**
     * Open Google Maps with destination (transit mode)
     */
    fun openRouteToDestination(destination: String) {
        val uri = Uri.parse("google.navigation:q=$destination&mode=t")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.setPackage("com.google.android.apps.maps")
        
        if (intent.resolveActivity(context.packageManager) != null) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        } else {
            // Fallback: Search in Google Maps
            searchInGoogleMaps(destination)
        }
    }
    
    /**
     * Search for bus route number (e.g., "500D")
     * FREE: Uses Google Maps search intent
     */
    fun searchBusRoute(routeNumber: String) {
        // Search for BMTC route in Google Maps
        val query = "BMTC $routeNumber Bangalore"
        searchInGoogleMaps(query)
    }
    
    /**
     * Open Google Maps app (fallback)
     */
    private fun openGoogleMapsApp() {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=Bangalore"))
            intent.setPackage("com.google.android.apps.maps")
            if (intent.resolveActivity(context.packageManager) != null) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
            } else {
                Log.e("TransportHelper", "Google Maps not installed")
            }
        } catch (e: Exception) {
            Log.e("TransportHelper", "Error opening Google Maps: ${e.message}")
        }
    }
    
    /**
     * Search in Google Maps
     */
    private fun searchInGoogleMaps(query: String) {
        try {
            val uri = Uri.parse("geo:0,0?q=${Uri.encode(query)}")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            intent.setPackage("com.google.android.apps.maps")
            
            if (intent.resolveActivity(context.packageManager) != null) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
            } else {
                // Ultimate fallback: Browser search
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps/search/$query"))
                browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(browserIntent)
            }
        } catch (e: Exception) {
            Log.e("TransportHelper", "Error searching in Google Maps: ${e.message}")
        }
    }
    
    /**
     * Get transit directions between two points
     * FREE: Opens Google Maps with transit mode
     */
    fun getTransitDirections(fromLat: Double, fromLng: Double, toLat: Double, toLng: Double) {
        val uri = Uri.parse("https://www.google.com/maps/dir/$fromLat,$fromLng/$toLat,$toLng/data=!4m2!4m1!3e3")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.setPackage("com.google.android.apps.maps")
        
        if (intent.resolveActivity(context.packageManager) != null) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        } else {
            // Fallback: Browser
            val browserIntent = Intent(Intent.ACTION_VIEW, uri)
            browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(browserIntent)
        }
    }
}
