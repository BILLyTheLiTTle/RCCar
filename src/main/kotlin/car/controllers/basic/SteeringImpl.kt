package car.controllers.basic

import car.server.EngineSystem
import car.server.SteeringSystem.Companion.ACTION_STRAIGHT
import car.server.SteeringSystem.Companion.ACTION_TURN_LEFT
import car.server.SteeringSystem.Companion.ACTION_TURN_RIGHT
import car.server.doNonBlockingRequest

object SteeringImpl:Steering {
    override val theta: Double
        get() =
            when (value) {
                20 -> innerFrontDegreesTheta[0]
                40 -> innerFrontDegreesTheta[1]
                60 -> innerFrontDegreesTheta[2]
                80 -> innerFrontDegreesTheta[3]
                100 -> innerFrontDegreesTheta[4]
                else -> 0.00
            }
    override val phi: Double
        get() =
            when (value) {
                20 -> outerFrontDegreesPhi[0]
                40 -> outerFrontDegreesPhi[1]
                60 -> outerFrontDegreesPhi[2]
                80 -> outerFrontDegreesPhi[3]
                100 -> outerFrontDegreesPhi[4]
                else -> 0.00
            }


    private val innerFrontDegreesTheta = doubleArrayOf(22.0, 45.0, 52.0, 60.0, 66.0)
    private val outerFrontDegreesPhi = doubleArrayOf(19.9, 37.9, 41.7, 49.2, 47.8)
    var direction = ACTION_STRAIGHT
        private set
    private var value = 0

    override fun turn(direction: String, value: Int): String {
        when (direction) {
            ACTION_TURN_RIGHT ->
                // TODO prepare hardware for turning right

                SteeringImpl.direction = ACTION_TURN_RIGHT
            ACTION_TURN_LEFT ->
                // TODO prepare hardware for turning left

                SteeringImpl.direction = ACTION_TURN_LEFT
            else  -> {
                // TODO prepare hardware for turning straight

                // turn off the turn lights
                if (SteeringImpl.direction != ACTION_STRAIGHT) {
                    ElectricsImpl.rightTurnLightsState = false
                    ElectricsImpl.leftTurnLightsState = false
                }

                SteeringImpl.direction = ACTION_STRAIGHT
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