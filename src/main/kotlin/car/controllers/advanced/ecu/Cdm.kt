package car.controllers.advanced.ecu

/* CDM stands for Collision Detection Module

 */

interface Cdm {
    val throttlePwmValue: Int

    companion object {
        // counted in meters
        const val HIGH_SPEED_LIMITER_THRESHOLD_DISTANCE = 0.8 // 85% < pwm value %
        const val MEDIUM_SPEED_LIMITER_THRESHOLD_DISTANCE = 0.5 // 60% < pwm value < 85%
        const val LOW_SPEED_LIMITER_THRESHOLD_DISTANCE = 0.3 // 40% < pwm value < 60%
        const val STOP_THRESHOLD_DISTANCE = 0.1 // just stop the vehicle no matter what
    }

}