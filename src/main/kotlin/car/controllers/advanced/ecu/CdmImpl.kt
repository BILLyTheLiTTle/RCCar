package car.controllers.advanced.ecu

import car.controllers.advanced.ecu.sensors.UltrasonicDistanceMeterImpl
import car.controllers.basic.SetupImpl
import car.server.SetupSystem
import car.server.ThrottleBrakeSystem
import car.showMessage


object CdmImpl: Cdm {
    override var isActive = false

    /* This function must be called in order to proceed with calculation.
        It is something like an initializer.
        Without this function called at the beginning the calculation could
        be outdated.
     */
    override fun calculateThrottleValue(direction: String, rawThrottleValue: Int): Int {
        val distance: Double =
            when (direction) {
                ThrottleBrakeSystem.ACTION_MOVE_FORWARD -> UltrasonicDistanceMeterImpl.frontDistance
                ThrottleBrakeSystem.ACTION_MOVE_BACKWARD -> UltrasonicDistanceMeterImpl.rearDistance
                else -> Cdm.ERROR_DISTANCE
            }

        val throttleValue = when  {
            distance < Cdm.STOP_THRESHOLD_DISTANCE -> {
                isActive = true
                0
            }
            distance < Cdm.LOW_SPEED_LIMITER_THRESHOLD_DISTANCE -> {
                if (rawThrottleValue > Cdm.LOW_SPEED_LIMITER_THRESHOLD_SPEED) {
                    isActive = true
                    Cdm.LOW_SPEED_LIMITER_THRESHOLD_SPEED
                }
                else {
                    isActive = false
                    rawThrottleValue
                }
            }
            distance < Cdm.MEDIUM_SPEED_LIMITER_THRESHOLD_DISTANCE -> {
                if (rawThrottleValue > Cdm.MEDIUM_SPEED_LIMITER_THRESHOLD_SPEED) {
                    isActive = true
                    Cdm.MEDIUM_SPEED_LIMITER_THRESHOLD_SPEED
                }
                else {
                    isActive = false
                    rawThrottleValue
                }
            }
            distance < Cdm.HIGH_SPEED_LIMITER_THRESHOLD_DISTANCE -> {
                if (rawThrottleValue > Cdm.HIGH_SPEED_LIMITER_THRESHOLD_SPEED) {
                    isActive = true
                    Cdm.HIGH_SPEED_LIMITER_THRESHOLD_SPEED
                }
                else {
                    isActive = false
                    rawThrottleValue
                }
            }
            else -> {
                isActive = false
                rawThrottleValue
            }
        }

        showMessage(title = "-- ECU / CDM Activated: $isActive-- ",
            body = "Entered Throttle Value: $rawThrottleValue\n" +
                    "Calculated Throttle Value: $throttleValue\n" +
                    "Calculated Thorttle Value Applied: " +
                    "${SetupImpl.handlingAssistanceState == SetupSystem.ASSISTANCE_FULL}\n" +
                    "Obstacle's distance: $distance meters\n" +
                    "Vehicle's direction: $direction")

        return throttleValue
    }
}