package com.cs407.lab09

import android.hardware.Sensor
import android.hardware.SensorEvent
import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class BallViewModel : ViewModel() {

    private var ball: Ball? = null
    private var lastTimestamp: Long = 0L

    // Expose the ball's position as a StateFlow
    private val _ballPosition = MutableStateFlow(Offset.Zero)
    val ballPosition: StateFlow<Offset> = _ballPosition.asStateFlow()

    /**
     * Called by the UI when the game field's size is known.
     */
    fun initBall(fieldWidth: Float, fieldHeight: Float, ballSizePx: Float) {
        if (ball == null) {
            // TODO: Initialize the ball instance
            // ball = Ball(...)
            // Initialize the ball instance with the background size and ball size (in pixels)
            ball = Ball(
                backgroundWidth = fieldWidth,
                backgroundHeight = fieldHeight,
                ballSize = ballSizePx
            )
            // TODO: Update the StateFlow with the initial position
            // _ballPosition.value = Offset(ball!!.posX, ball!!.posY)

            // Reset lastTimestamp so the first sensor update will only initialize timing
            lastTimestamp = 0L
            // Update the StateFlow with the initial position of the ball
            _ballPosition.value = Offset(ball!!.posX, ball!!.posY)
        }
    }

    /**
     * Called by the SensorEventListener in the UI.
     */
    fun onSensorDataChanged(event: SensorEvent) {
        // Ensure ball is initialized
        val currentBall = ball ?: return

        if (event.sensor.type == Sensor.TYPE_GRAVITY) {
            if (lastTimestamp != 0L) {
                // TODO: Calculate the time difference (dT) in seconds
                // Hint: event.timestamp is in nanoseconds
                // val NS2S = 1.0f / 1000000000.0f
                // val dT = ...
                val NS2S = 1.0f / 1_000_000_000.0f
                val dT = (event.timestamp - lastTimestamp) * NS2S

                // TODO: Update the ball's position and velocity
                // Hint: The sensor's x and y-axis are inverted
                // currentBall.updatePositionAndVelocity(xAcc = ..., yAcc = ..., dT = ...)
                val sensorX = event.values[0]
                val sensorY = event.values[1]

                // The sensor's x and y axes are inverted relative to the desired directions
                val xAcc = -sensorX
                val yAcc = sensorY

                currentBall.updatePositionAndVelocity(
                    xAcc = xAcc,
                    yAcc = yAcc,
                    dT = dT
                )

                // Ensure the ball stays within the background boundaries
                currentBall.checkBoundaries()

                // TODO: Update the StateFlow to notify the UI
                // _ballPosition.update { Offset(currentBall.posX, currentBall.posY) }
                _ballPosition.update {
                    Offset(currentBall.posX, currentBall.posY)
                }
            }

            // TODO: Update the lastTimestamp
            // lastTimestamp = ...
            lastTimestamp = event.timestamp
        }
    }

    fun reset() {
        // TODO: Reset the ball's state
        // Reset the ball's state to the center with zero velocity and acceleration
        ball?.reset()

        // TODO: Update the StateFlow with the reset position
        // Update the StateFlow with the reset position (if the ball exists)
         ball?.let { resetBall ->
             _ballPosition.value = Offset(resetBall.posX, resetBall.posY)
         }

        // TODO: Reset the lastTimestamp
        // Reset the timestamp so the next sensor update restarts timing
        lastTimestamp = 0L
    }
}