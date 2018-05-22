package car.controllers.basic

import car.server.SteeringSystem.Companion.ACTION_STRAIGHT
import car.server.SteeringSystem.Companion.ACTION_TURN_LEFT
import car.server.SteeringSystem.Companion.ACTION_TURN_RIGHT

object SteeringImpl:Steering {
    override var direction = ACTION_STRAIGHT
    override var value = 0

    override fun turn(direction: String, value: Int): String {
        when (direction) {
            ACTION_TURN_RIGHT ->
                // TODO prepare H-bridge for forward movement
                SteeringImpl.direction = ACTION_TURN_RIGHT
            ACTION_TURN_LEFT ->
                // TODO prepare H-bridge for backward movement
                SteeringImpl.direction = ACTION_TURN_LEFT
            else  ->
                // TODO prepare H-bridge for backward movement
                SteeringImpl.direction = ACTION_STRAIGHT
        }

        //TODO set value to pins

        SteeringImpl.value = value
        return EngineImpl.SUCCESS // or error message from pins
    }
}