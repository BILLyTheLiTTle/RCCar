package car.controllers.advanced.ecu

/* CDM stands for Collision Detection Module

 */

interface Cdm {

    fun calculateThrottleValue(direction: String, rawThrottleValue: Int): Int

    companion object {
        // counted in meters
        const val HIGH_SPEED_LIMITER_THRESHOLD_DISTANCE = 0.8
        const val MEDIUM_SPEED_LIMITER_THRESHOLD_DISTANCE = 0.5
        const val LOW_SPEED_LIMITER_THRESHOLD_DISTANCE = 0.3
        const val STOP_THRESHOLD_DISTANCE = 0.1

        // pwm values
        const val HIGH_SPEED_LIMITER_THRESHOLD_SPEED = 85
        const val MEDIUM_SPEED_LIMITER_THRESHOLD_SPEED = 60
        const val LOW_SPEED_LIMITER_THRESHOLD_SPEED = 40

        const val ERROR_DISTANCE = -1.0
    }
}