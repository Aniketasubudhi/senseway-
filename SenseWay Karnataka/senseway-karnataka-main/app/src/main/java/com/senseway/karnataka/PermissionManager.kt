package com.senseway.karnataka

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

/**
 * PermissionManager handles all runtime permissions
 * Compatible with Android 10-14+
 */
object PermissionManager {
    
    const val PERMISSION_REQUEST_CODE = 1001
    
    val REQUIRED_PERMISSIONS = mutableListOf(
        Manifest.permission.RECORD_AUDIO,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.POST_NOTIFICATIONS,
        Manifest.permission.CALL_PHONE,
        Manifest.permission.SEND_SMS,
        Manifest.permission.CAMERA
    ).apply {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            add(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
        }
    }
    
    fun checkPermissions(activity: Activity): Boolean {
        val missingPermissions = REQUIRED_PERMISSIONS.filter {
            ContextCompat.checkSelfPermission(activity, it) != PackageManager.PERMISSION_GRANTED
        }
        
        return missingPermissions.isEmpty()
    }
    
    fun requestPermissions(activity: Activity) {
        val missingPermissions = REQUIRED_PERMISSIONS.filter {
            ContextCompat.checkSelfPermission(activity, it) != PackageManager.PERMISSION_GRANTED
        }
        
        if (missingPermissions.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                activity,
                missingPermissions.toTypedArray(),
                PERMISSION_REQUEST_CODE
            )
        }
    }
    
    fun hasPermission(activity: Activity, permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            activity,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }
    
    fun hasAllRequiredPermissions(activity: Activity): Boolean {
        return REQUIRED_PERMISSIONS.all { hasPermission(activity, it) }
    }
}
