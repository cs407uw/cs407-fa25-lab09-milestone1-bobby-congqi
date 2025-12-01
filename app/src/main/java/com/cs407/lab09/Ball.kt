package com.cs407.lab09

/**
 * Represents a ball that can move. (No Android UI imports!)
 *
 * Constructor parameters:
 * - backgroundWidth: the width of the background, of type Float
 * - backgroundHeight: the height of the background, of type Float
 * - ballSize: the width/height of the ball, of type Float
 */
class Ball(
    private val backgroundWidth: Float,
    private val backgroundHeight: Float,
    private val ballSize: Float
) {
    var posX = 0f
    var posY = 0f
    var velocityX = 0f
    var velocityY = 0f
    private var accX = 0f
    private var accY = 0f

    private var isFirstUpdate = true

    init {
        // TODO: Call reset()
        reset()
    }

    /**
     * Updates the ball's position and velocity based on the given acceleration and time step.
     * (See lab handout for physics equations)
     */
    fun updatePositionAndVelocity(xAcc: Float, yAcc: Float, dT: Float) {
        if(isFirstUpdate) {
            isFirstUpdate = false
            accX = xAcc
            accY = yAcc
            return
        }
        if (dT <= 0f) return

        // ---------- X direction ----------
        val v0x = velocityX
        val a0x = accX
        val a1x = xAcc
        // Velocity update using equation (1)
        val v1x = v0x + 0.5f * (a1x + a0x) * dT
        // Position update using equation (2)
        val dx = v0x * dT + (dT * dT) * (3f * a0x + a1x) / 6f
        velocityX = v1x
        posX += dx
        // ---------- Y direction ----------
        val v0y = velocityY
        val a0y = accY
        val a1y = yAcc
        // Velocity update using equation (1)
        val v1y = v0y + 0.5f * (a1y + a0y) * dT
        // Position update using equation (2)
        val dy = v0y * dT + (dT * dT) * (3f * a0y + a1y) / 6f
        velocityY = v1y
        posY += dy
        // Store the current acceleration for the next update step
        accX = xAcc
        accY = yAcc
    }

    /**
     * Ensures the ball does not move outside the boundaries.
     * When it collides, velocity and acceleration perpendicular to the
     * boundary should be set to 0.
     */
    fun checkBoundaries() {
        // TODO: implement the checkBoundaries function
        // (Check all 4 walls: left, right, top, bottom)

        // Left boundary
        if (posX < 0f) {
            posX = 0f
            velocityX = 0f
            accX = 0f
        }
        // Right boundary
        val maxX = backgroundWidth - ballSize
        if (posX > maxX) {
            posX = maxX
            velocityX = 0f
            accX = 0f
        }
        // Top boundary
        if (posY < 0f) {
            posY = 0f
            velocityY = 0f
            accY = 0f
        }
        // Bottom boundary
        val maxY = backgroundHeight - ballSize
        if (posY > maxY) {
            posY = maxY
            velocityY = 0f
            accY = 0f
        }
    }

    /**
     * Resets the ball to the center of the screen with zero
     * velocity and acceleration.
     */
    fun reset() {
        // TODO: implement the reset function
        // (Reset posX, posY, velocityX, velocityY, accX, accY, isFirstUpdate)

        // Place the ball at the center of the background (posX/posY represent the top-left corner)
        posX = (backgroundWidth - ballSize) / 2f
        posY = (backgroundHeight - ballSize) / 2f
        // Reset velocities and accelerations to zero
        velocityX = 0f
        velocityY = 0f
        accX = 0f
        accY = 0f
        // Mark the next update as the first update
        isFirstUpdate = true
    }
}