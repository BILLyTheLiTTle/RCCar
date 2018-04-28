package car.controllers.basic

import car.server.ThrottleBrakeSystem

object ThrottleBrakeImpl:ThrottleBrake {

    override var action = ThrottleBrakeSystem.ACTION_STILL

    override var value = 0

    override fun throttle(direction: String, value: Int): String {
        if(direction == ThrottleBrakeSystem.ACTION_MOVE_FORWARD){
            // TODO prepare H-bridge for forward movement
            action = ThrottleBrakeSystem.ACTION_MOVE_FORWARD
        }
        else if (direction == ThrottleBrakeSystem.ACTION_MOVE_BACKWARD){
            // TODO prepare H-bridge for backward movement
            action = ThrottleBrakeSystem.ACTION_MOVE_BACKWARD
        }

        //TODO set value to pins

	ThrottleBrakeImpl.value = value
        return EngineImpl.SUCCESS // or error message from pins
    }

    override fun brake(value: Int): String {
        //TODO set value to pins

        action = ThrottleBrakeSystem.ACTION_STILL
	ThrottleBrakeImpl.value = value
        return EngineImpl.SUCCESS // or error message from pins
    }

    override fun parkingBrake(value: Int): String {
        //TODO set value to pins

        action = ThrottleBrakeSystem.ACTION_PARKING_BRAKE
        ThrottleBrakeImpl.value = value
        return EngineImpl.SUCCESS // or error message from pins
    }

    override fun handbrake(value: Int): String {
        //TODO set value to pins

        action = ThrottleBrakeSystem.ACTION_HANDBRAKE
	ThrottleBrakeImpl.value = value
        return EngineImpl.SUCCESS // or error message from pins
    }
}