package car.controllers.basic

import car.server.EngineSystem
import car.server.SteeringSystem.Companion.ACTION_STRAIGHT
import car.server.SteeringSystem.Companion.ACTION_TURN_LEFT
import car.server.SteeringSystem.Companion.ACTION_TURN_RIGHT
import car.server.doNonBlockingRequest

object SteeringImpl:Steering {
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

                if(SteeringImpl.direction == ACTION_TURN_RIGHT ) {
                    // turn off the turn lights
                    ElectricsImpl.rightTurnLightsState = false
                }
                else if(SteeringImpl.direction == ACTION_TURN_LEFT) {
                    // turn off the turn lights
                    ElectricsImpl.leftTurnLightsState = false
                }

                SteeringImpl.direction = ACTION_STRAIGHT
            }
        }

        //TODO set value to pins

        SteeringImpl.value = value
        return EngineImpl.SUCCESS // or error message from pins
    }

    override fun reset() {
        direction = ACTION_STRAIGHT
        value = 0
    }
}