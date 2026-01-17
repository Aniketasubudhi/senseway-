package com.senseway.karnataka

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log

/**
 * DropDetectionManager detects phone drops using accelerometer
 * FREE: Uses Android's built-in sensors (no paid APIs)
 */
class DropDetectionManager(private val context: Context) {
    
    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    
    private var isMonitoring = false
    private var lastAcceleration = 0f
    private val DROP_THRESHOLD = 15.0f // m/s² threshold for drop detection
    private val TIME_THRESHOLD = 100L // ms between readings
    
    private var lastUpdateTime = 0L
    
    private val sensorListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent?) {
            if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
                val currentTime = System.currentTimeMillis()
                
                if (currentTime - lastUpdateTime > TIME_THRESHOLD) {
                    val x = event.values[0]
                    val y = event.values[1]
                    val z = event.values[2]
                    
                    val acceleration = Math.sqrt(
                        (x * x + y * y + z * z).toDouble()
                    ).toFloat()
                    
                    val delta = Math.abs(acceleration - lastAcceleration)
                    
                    // Detect sudden change (drop)
                    if (delta > DROP_THRESHOLD) {
                        Log.d("DropDetectionManager", "Drop detected! Delta: $delta")
                        onDropDetected()
                    }
                    
                    lastAcceleration = acceleration
                    lastUpdateTime = currentTime
                }
            }
        }
        
        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
            // Not used
        }
    }
    
    private var onDropCallback: (() -> Unit)? = null
    
    /**
     * Start monitoring for drops
     */
    fun startMonitoring(onDrop: () -> Unit) {
        if (isMonitoring) return
        
        onDropCallback = onDrop
        accelerometer?.let {
            sensorManager.registerListener(
                sensorListener,
                it,
                SensorManager.SENSOR_DELAY_UI
            )
            isMonitoring = true
            Log.d("DropDetectionManager", "Started drop detection")
        } ?: Log.e("DropDetectionManager", "Accelerometer not available")
    }
    
    /**
     * Stop monitoring
     */
    fun stopMonitoring() {
        if (!isMonitoring) return
        
        sensorManager.unregisterListener(sensorListener)
        isMonitoring = false
        onDropCallback = null
        Log.d("DropDetectionManager", "Stopped drop detection")
    }
    
    private fun onDropDetected() {
        onDropCallback?.invoke()
    }
}
