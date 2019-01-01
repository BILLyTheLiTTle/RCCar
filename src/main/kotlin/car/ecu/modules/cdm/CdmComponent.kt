package car.ecu.modules.cdm

import car.cockpit.pedals.ACTION_MOVE_BACKWARD
import car.cockpit.pedals.ACTION_MOVE_FORWARD
import car.cockpit.pedals.ACTION_NEUTRAL
import car.cockpit.setup.ASSISTANCE_FULL
import car.cockpit.setup.Setup
import car.ecu.sensors.distance.UltrasonicDistanceMeter
import car.showMessage
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component("Collision Detection Module Component")
class CdmComponent: Cdm {

    @Autowired
    private lateinit var setupComponent: Setup

    @Autowired
    private lateinit var udmComponent: UltrasonicDistanceMeter

    /* This function must be called in order to proceed with calculation.
        It is something like an initializer.
        Without this function called at the beginning the calculation could
        be outdated.
     */
    override fun calculateThrottleValue(direction: String, rawThrottleValue: Int): Int {
        val distance: Double =
            when (direction) {
                ACTION_MOVE_FORWARD -> udmComponent.frontDistance
                ACTION_MOVE_BACKWARD -> udmComponent.rearDistance
                ACTION_NEUTRAL -> STOP_THRESHOLD_DISTANCE
                else -> ERROR_DISTANCE
            }

        val throttleValue = when  {
            distance <= STOP_THRESHOLD_DISTANCE -> {
                0
            }
            distance <= LOW_SPEED_LIMITER_THRESHOLD_DISTANCE -> {
                if (rawThrottleValue > LOW_SPEED_LIMITER_THRESHOLD_SPEED) {
                    LOW_SPEED_LIMITER_THRESHOLD_SPEED
                }
                else {
                    rawThrottleValue
                }
            }
            distance <= MEDIUM_SPEED_LIMITER_THRESHOLD_DISTANCE -> {
                if (rawThrottleValue > MEDIUM_SPEED_LIMITER_THRESHOLD_SPEED) {
                    MEDIUM_SPEED_LIMITER_THRESHOLD_SPEED
                }
                else {
                    rawThrottleValue
                }
            }
            distance <= HIGH_SPEED_LIMITER_THRESHOLD_DISTANCE -> {
                if (rawThrottleValue > HIGH_SPEED_LIMITER_THRESHOLD_SPEED) {
                    HIGH_SPEED_LIMITER_THRESHOLD_SPEED
                }
                else {
                    rawThrottleValue
                }
            }
            else -> {
                rawThrottleValue
            }
        }

        showMessage(klass = this::class,
            body = "ECU / CDM Activated: ${throttleValue != rawThrottleValue}" +
                    "Entered Throttle Value: $rawThrottleValue\n" +
                    "Calculated Throttle Value: $throttleValue\n" +
                    "Calculated Throttle Value Applied: " +
                    "${setupComponent.handlingAssistanceState == ASSISTANCE_FULL}\n" +
                    "Obstacle's distance: $distance meters\n" +
                    "Vehicle's direction: $direction")



        return throttleValue
    }
}