package car.ecu.modules.cdm

import car.*
import car.cockpit.pedals.Motion

/* CDM stands for Collision Detection Module

 */

interface Cdm {

    fun calculateThrottleValue(direction: Motion, rawThrottleValue: Int): Int
}

enum class CollisionThresholdValues(val distance: Double, val speed: Int) {
    // counted in meters and pwm values
    HIGH_SPEED(HIGH_SPEED_THRESHOLD_DISTANCE, HIGH_SPEED_THRESHOLD_VALUE),
    MEDIUM_SPEED(MEDIUM_SPEED_THRESHOLD_DISTANCE, MEDIUM_SPEED_THRESHOLD_VALUE),
    LOW_SPEED(LOW_SPEED_THRESHOLD_DISTANCE, LOW_SPEED_THRESHOLD_VALUE),
    NO_SPEED(NO_SPEED_THRESHOLD_DISTANCE, 0),
    ERROR_SPEED(-1.0, -1)
}