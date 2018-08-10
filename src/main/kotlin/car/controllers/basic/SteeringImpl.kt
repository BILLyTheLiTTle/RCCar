package car.controllers.basic

import car.controllers.advanced.ecu.TcmImpl
import car.server.EngineSystem
import car.server.SteeringSystem
import car.server.SteeringSystem.Companion.ACTION_STRAIGHT
import car.server.SteeringSystem.Companion.ACTION_TURN_LEFT
import car.server.SteeringSystem.Companion.ACTION_TURN_RIGHT

object SteeringImpl:Steering {
    private val innerFrontDegreesTheta = doubleArrayOf(22.0, 45.0, 52.0, 60.0, 66.0)
    private val outerFrontDegreesPhi = doubleArrayOf(19.9, 37.9, 41.7, 49.2, 47.8)
    var direction = ACTION_STRAIGHT
        private set
    var value = 0
        private set

    override fun turn(direction: String, value: Int): String {

        // Inform TCM of the ECU
        when (value) {
            SteeringSystem.STEERING_VALUE_20 -> {
                TcmImpl.phi = outerFrontDegreesPhi[0]
                TcmImpl.theta = innerFrontDegreesTheta[0]
            }
            SteeringSystem.STEERING_VALUE_40 -> {
                TcmImpl.phi = outerFrontDegreesPhi[1]
                TcmImpl.theta = innerFrontDegreesTheta[1]
            }
            SteeringSystem.STEERING_VALUE_60 -> {
                TcmImpl.phi = outerFrontDegreesPhi[2]
                TcmImpl.theta = innerFrontDegreesTheta[2]
            }
            SteeringSystem.STEERING_VALUE_80 -> {
                TcmImpl.phi = outerFrontDegreesPhi[3]
                TcmImpl.theta = innerFrontDegreesTheta[3]
            }
            SteeringSystem.STEERING_VALUE_100 -> {
                TcmImpl.phi = outerFrontDegreesPhi[4]
                TcmImpl.theta = innerFrontDegreesTheta[4]
            }
            else -> 0.00
        }

        SteeringImpl.direction = when (direction) {
            ACTION_TURN_RIGHT ->
                // TODO prepare hardware for turning right

                 ACTION_TURN_RIGHT
            ACTION_TURN_LEFT ->
                // TODO prepare hardware for turning left

                ACTION_TURN_LEFT
            else  -> {
                // TODO prepare hardware for turning straight

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
        value = 0
    }
}