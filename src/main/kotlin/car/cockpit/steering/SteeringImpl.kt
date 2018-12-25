package car.cockpit.steering

import car.cockpit.electrics.ElectricsImpl
import car.ecu.modules.dcm.DcmImpl
import car.cockpit.engine.EngineSystem
import car.cockpit.steering.SteeringWheel.Companion.ACTION_STRAIGHT
import car.cockpit.steering.SteeringWheel.Companion.ACTION_TURN_LEFT
import car.cockpit.steering.SteeringWheel.Companion.ACTION_TURN_RIGHT
import car.cockpit.steering.SteeringWheel.Companion.STEERING_VALUE_00

object SteeringImpl: Steering {
    private val innerFrontDegreesTheta = doubleArrayOf(22.0, 45.0, 52.0, 60.0, 66.0)
    private val outerFrontDegreesPhi = doubleArrayOf(19.9, 37.9, 41.7, 49.2, 47.8)
    var direction = ACTION_STRAIGHT
        private set
    var value = STEERING_VALUE_00
        private set

    override fun turn(direction: String, value: Int): String {

        // Inform TCM of the ECU
        when (value) {
            SteeringWheel.STEERING_VALUE_20 -> {
                DcmImpl.phi = outerFrontDegreesPhi[0]
                DcmImpl.theta = innerFrontDegreesTheta[0]
            }
            SteeringWheel.STEERING_VALUE_40 -> {
                DcmImpl.phi = outerFrontDegreesPhi[1]
                DcmImpl.theta = innerFrontDegreesTheta[1]
            }
            SteeringWheel.STEERING_VALUE_60 -> {
                DcmImpl.phi = outerFrontDegreesPhi[2]
                DcmImpl.theta = innerFrontDegreesTheta[2]
            }
            SteeringWheel.STEERING_VALUE_80 -> {
                DcmImpl.phi = outerFrontDegreesPhi[3]
                DcmImpl.theta = innerFrontDegreesTheta[3]
            }
            SteeringWheel.STEERING_VALUE_100 -> {
                DcmImpl.phi = outerFrontDegreesPhi[4]
                DcmImpl.theta = innerFrontDegreesTheta[4]
            }
            else -> {
                DcmImpl.phi = 0.00
                DcmImpl.theta = 0.00
            }
        }

        SteeringImpl.direction = when (direction) {
            ACTION_TURN_RIGHT ->
                // TODO prepare pins for turning right

                 ACTION_TURN_RIGHT
            ACTION_TURN_LEFT ->
                // TODO prepare pins for turning left

                ACTION_TURN_LEFT
            else  -> {
                // TODO prepare pins for turning straight

                // turn off the turn lights
                if (SteeringImpl.direction != ACTION_STRAIGHT) {
                    ElectricsImpl.rightTurnLightsState = false
                    ElectricsImpl.leftTurnLightsState = false
                }

                ACTION_STRAIGHT
            }
        }

        //TODO set value to pins

        SteeringImpl.value = value
        return EngineSystem.SUCCESS // or error message from pins
    }

    override fun reset() {
        direction = ACTION_STRAIGHT
        value = STEERING_VALUE_00
    }
}