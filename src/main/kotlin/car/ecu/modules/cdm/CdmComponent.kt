package car.ecu.modules.cdm

import car.cockpit.pedals.Motion
import car.cockpit.setup.HandlingAssistance
import car.cockpit.setup.Setup
import car.ecu.sensors.distance.UltrasonicDistanceMeter
import car.showMessage
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component("Collision Detection Module Component")
class CdmComponent: Cdm {

    @Autowired
    private lateinit var setupComponent: Setup

    @Autowired
    private lateinit var udmComponent: UltrasonicDistanceMeter

    private val logger = LoggerFactory.getLogger(CdmComponent::class.java)

    /* This function must be called in order to proceed with calculation.
        It is something like an initializer.
        Without this function called at the beginning the calculation could
        be outdated.
     */
    override fun calculateThrottleValue(direction: Motion, rawThrottleValue: Int): Int {
        val distance: Double =
            when (direction) {
                Motion.FORWARD -> udmComponent.frontDistance
                Motion.BACKWARD -> udmComponent.rearDistance
                Motion.NEUTRAL -> CollisionThresholdValues.NO_SPEED.distance
                else -> CollisionThresholdValues.ERROR_SPEED.distance
            }

        val throttleValue = when  {
            distance <= CollisionThresholdValues.NO_SPEED.distance -> {
                0
            }
            distance <= CollisionThresholdValues.LOW_SPEED.distance -> {
                if (rawThrottleValue > CollisionThresholdValues.LOW_SPEED.speed) {
                    CollisionThresholdValues.LOW_SPEED.speed
                }
                else {
                    rawThrottleValue
                }
            }
            distance <= CollisionThresholdValues.MEDIUM_SPEED.distance -> {
                if (rawThrottleValue > CollisionThresholdValues.MEDIUM_SPEED.speed) {
                    CollisionThresholdValues.MEDIUM_SPEED.speed
                }
                else {
                    rawThrottleValue
                }
            }
            distance <= CollisionThresholdValues.HIGH_SPEED.distance -> {
                if (rawThrottleValue > CollisionThresholdValues.HIGH_SPEED.speed) {
                    CollisionThresholdValues.HIGH_SPEED.speed
                }
                else {
                    rawThrottleValue
                }
            }
            else -> {
                rawThrottleValue
            }
        }

        showMessage(logger = logger,
            body = "ECU / CDM Activated: ${throttleValue != rawThrottleValue}\n" +
                    "Entered Throttle Value: $rawThrottleValue\n" +
                    "Calculated Throttle Value: $throttleValue\n" +
                    "Calculated Throttle Value Applied: " +
                    "${setupComponent.handlingAssistanceState == HandlingAssistance.FULL}\n" +
                    "Obstacle's distance: $distance meters\n" +
                    "Vehicle's direction: $direction")



        return throttleValue
    }
}