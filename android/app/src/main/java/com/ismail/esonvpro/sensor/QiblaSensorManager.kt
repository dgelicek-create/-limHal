package com.ismail.esonvpro.sensor

import android.content.Context
import android.hardware.GeomagneticField
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

class QiblaSensorManager(context: Context) : SensorEventListener {

    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    private val magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)

    private var lastAccelerometer = FloatArray(3)
    private var lastMagnetometer = FloatArray(3)
    private var lastAccelerometerSet = false
    private var lastMagnetometerSet = false

    private val rotationMatrix = FloatArray(9)
    private val orientation = FloatArray(3)

    // ALPHA constant for Low-Pass Filter
    private val ALPHA = 0.07f

    // Current smoothed azimuth
    private var currentAzimuth = 0f

    // StateFlow to expose rotation angle to Jetpack Compose UI
    private val _qiblaAngle = MutableStateFlow(0f)
    val qiblaAngle: StateFlow<Float> = _qiblaAngle.asStateFlow()

    // Default Kaaba Coordinates
    private val KAABA_LATITUDE = 21.4225
    private val KAABA_LONGITUDE = 39.8262

    // Current User Location (Needs to be provided via GPS provider in a real app)
    // For demonstration, Istanbul is used
    private val userLatitude = 41.0082
    private val userLongitude = 28.9784
    private val userAltitude = 0.0

    fun startListening() {
        accelerometer?.also { acc ->
            sensorManager.registerListener(this, acc, SensorManager.SENSOR_DELAY_UI)
        }
        magnetometer?.also { mag ->
            sensorManager.registerListener(this, mag, SensorManager.SENSOR_DELAY_UI)
        }
    }

    fun stopListening() {
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event == null) return

        if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            lastAccelerometer = lowPassFilter(event.values.clone(), lastAccelerometer)
            lastAccelerometerSet = true
        } else if (event.sensor.type == Sensor.TYPE_MAGNETIC_FIELD) {
            lastMagnetometer = lowPassFilter(event.values.clone(), lastMagnetometer)
            lastMagnetometerSet = true
        }

        if (lastAccelerometerSet && lastMagnetometerSet) {
            SensorManager.getRotationMatrix(rotationMatrix, null, lastAccelerometer, lastMagnetometer)
            SensorManager.getOrientation(rotationMatrix, orientation)

            // Calculate azimuth in degrees
            val azimuthInRadians = orientation[0]
            var azimuthInDegrees = Math.toDegrees(azimuthInRadians.toDouble()).toFloat()

            // Calculate true north by adding declination
            val geomagneticField = GeomagneticField(
                userLatitude.toFloat(),
                userLongitude.toFloat(),
                userAltitude.toFloat(),
                System.currentTimeMillis()
            )
            azimuthInDegrees += geomagneticField.declination

            // Calculate bearing to Kaaba
            val bearingToKaaba = calculateBearingToKaaba(userLatitude, userLongitude)

            // Final needle rotation (smoothly filtered)
            var targetRotation = (bearingToKaaba - azimuthInDegrees + 360) % 360
            
            // Normalize for shortest path rotation logic if needed
            
            // Apply smoothing for needle itself if needed, but sensor values are already low-pass filtered.
            _qiblaAngle.value = -targetRotation // Negative because of Compose rotation logic
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Not needed for this implementation
    }

    /**
     * Mathematical Low-Pass Filter implementation.
     * filtered_value = previous_value * 0.93 + raw_value * 0.07
     */
    private fun lowPassFilter(input: FloatArray, output: FloatArray?): FloatArray {
        if (output == null) return input
        for (i in input.indices) {
            output[i] = output[i] * (1.0f - ALPHA) + input[i] * ALPHA
        }
        return output
    }

    private fun calculateBearingToKaaba(lat: Double, lon: Double): Float {
        val lat1 = Math.toRadians(lat)
        val lon1 = Math.toRadians(lon)
        val lat2 = Math.toRadians(KAABA_LATITUDE)
        val lon2 = Math.toRadians(KAABA_LONGITUDE)

        val dLon = lon2 - lon1
        val y = sin(dLon) * cos(lat2)
        val x = cos(lat1) * sin(lat2) - sin(lat1) * cos(lat2) * cos(dLon)
        val bearing = atan2(y, x)

        return ((Math.toDegrees(bearing) + 360) % 360).toFloat()
    }
}
