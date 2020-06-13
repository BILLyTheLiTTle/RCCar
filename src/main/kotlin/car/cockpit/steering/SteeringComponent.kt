package car.cockpit.steering

import car.*
import car.cockpit.electrics.Electrics
import car.cockpit.engine.SUCCESS
import car.ecu.modules.dcm.Dcm
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.io.*

@Component("Steering Component")
class SteeringComponent: Steering {

    @Autowired
    private lateinit var dcmComponent: Dcm

    @Autowired
    private lateinit var electricsComponent: Electrics

    override var direction = Turn.STRAIGHT
        protected set
        /*private set -
            achieved by using val in the interface and use the interface instead of this class at the outside*/
    override var value = SteeringValue.VALUE_00
        protected set
        /*private set -
            achieved by using val in the interface and use the interface instead of this class at the outside*/

    private val logger = LoggerFactory.getLogger(SteeringComponent::class.java)

    override fun turn(direction: Turn, value: SteeringValue): String {

        fun calculateRealValue(): Int {
            val leftSteeringDistance = MAX_SERVO_HARDWARE_VALUE - MID_SERVO_HARDWARE_VALUE
            val rightSteeringDistance = MID_SERVO_HARDWARE_VALUE - MIN_SERVO_HARDWARE_VALUE

            return if (leftSteeringDistance != rightSteeringDistance) { // It is now always false, but user can change "car.Specs" values
                showMessage(LoggerTypes.CRITICAL, logger,
                    "Error in steering alignment (left: $leftSteeringDistance, right: $rightSteeringDistance)")
                150
            }
            else {
                val distance: Int = value.value * leftSteeringDistance / 100 // no matter if leftSteeringDistance or rightSteeringDistance

                when (direction.name) {
                    Turn.LEFT.name -> MID_SERVO_HARDWARE_VALUE + distance
                    Turn.RIGHT.name -> MID_SERVO_HARDWARE_VALUE - distance
                    else -> 150
                }
            }
        }

        // Inform DCM of the ECU
        dcmComponent.phi = value.anglePhi
        dcmComponent.theta = value.angleTheta

        val steeringHardwareValue = calculateRealValue()

        showMessage(logger = logger, body = "Hardware value: $steeringHardwareValue")
        applyHardwareValue(steeringHardwareValue)

        this.direction = when (direction) {
            Turn.RIGHT -> Turn.RIGHT
            Turn.LEFT -> Turn.LEFT
            else  -> {
                // turn off the turn lights
                if (this.direction != Turn.STRAIGHT) {
                    electricsComponent.rightTurnLightsState = false
                    electricsComponent.leftTurnLightsState = false
                }

                Turn.STRAIGHT
            }
        }

        //TODO set value to raspi

        this.value = value
        return SUCCESS // or error message from raspi
    }

    override fun initialize() {
        applyHardwareValue(MID_SERVO_HARDWARE_VALUE)
        showMessage(logger = logger, body = "Steering initialized")
    }

    override fun reset() {
        direction = Turn.STRAIGHT
        value = SteeringValue.VALUE_00

        applyHardwareValue(MID_SERVO_HARDWARE_VALUE)
    }

    private fun applyHardwareValue(value: Int) {
        val file = "servo.txt"
        try {
            val writer = PrintWriter(file, "UTF-8")
            writer.println("$value")
            writer.close()
        }
        catch (e: Exception) {showMessage(LoggerTypes.CRITICAL, logger = logger, body = e.message?:"Error writing in \"$file\" file")}
    }
}