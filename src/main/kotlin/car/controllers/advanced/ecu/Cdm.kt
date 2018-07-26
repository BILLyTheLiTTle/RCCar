package car.controllers.advanced.ecu

import car.controllers.advanced.ecu.sensors.UltrasonicDistanceMeterImpl
import car.server.ThrottleBrakeSystem
import car.showMessage

/* CDM stands for Collision Detection Module

 */

interface Cdm {
    fun calculateThrottleValue(direction: String, rawThrottleValue: Int): Int {
        val distance =
            if (direction == ThrottleBrakeSystem.ACTION_MOVE_FORWARD) {
            UltrasonicDistanceMeterImpl.frontDistance
        }
        else if (direction == ThrottleBrakeSystem.ACTION_MOVE_BACKWARD) {
                UltrasonicDistanceMeterImpl.rearDistance
            }
        else ERROR_DISTANCE

        val throttleValue = when (distance) {
            Cdm.STOP_THRESHOLD_DISTANCE -> 0
            Cdm.LOW_SPEED_LIMITER_THRESHOLD_DISTANCE -> {
                if (rawThrottleValue > LOW_SPEED_LIMITER_THRESHOLD_SPEED) LOW_SPEED_LIMITER_THRESHOLD_SPEED
                else rawThrottleValue
            }
            Cdm.MEDIUM_SPEED_LIMITER_THRESHOLD_DISTANCE -> {
                if (rawThrottleValue > MEDIUM_SPEED_LIMITER_THRESHOLD_SPEED) MEDIUM_SPEED_LIMITER_THRESHOLD_SPEED
                else rawThrottleValue
            }
            Cdm.HIGH_SPEED_LIMITER_THRESHOLD_DISTANCE -> {
                if (rawThrottleValue > HIGH_SPEED_LIMITER_THRESHOLD_SPEED) HIGH_SPEED_LIMITER_THRESHOLD_SPEED
                else rawThrottleValue
            }
            else -> rawThrottleValue
        }

        showMessage(title = "-- ECU / CDM -- ",
            body = "Entered Throttle Value: $rawThrottleValue\n" +
                    "Calculated Throttle Value: $throttleValue" +
                    "Obstacle's distance: $distance meters" +
                    "Vehicle's direction: $direction")

        return throttleValue
    }
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

        const val ERROR_DISTANCE = -1
    }

}