package car.controllers.advanced.ecu

import car.controllers.advanced.ecu.sensors.UltrasonicDistanceMeterImpl
import car.controllers.basic.ThrottleBrakeImpl

object CdmImpl: Cdm {
    override val throttlePwmValue: Int
        get() = calculateThrottle(ThrottleBrakeImpl.value, UltrasonicDistanceMeterImpl.value)

    private fun calculateThrottle(throttle: Int, distance: Double): Int {
        return when (distance) {
            Cdm.STOP_THRESHOLD_DISTANCE -> 0
            Cdm.LOW_SPEED_LIMITER_THRESHOLD_DISTANCE -> 40
            Cdm.MEDIUM_SPEED_LIMITER_THRESHOLD_DISTANCE -> 60
            Cdm.HIGH_SPEED_LIMITER_THRESHOLD_DISTANCE -> 85
            else -> throttle
        }
    }
}