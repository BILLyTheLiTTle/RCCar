package car.controllers.basic

import car.server.ThrottleBrakeSystem.Companion.ACTION_BRAKING_STILL
import car.server.ThrottleBrakeSystem.Companion.ACTION_HANDBRAKE
import car.server.ThrottleBrakeSystem.Companion.ACTION_MOVE_BACKWARD
import car.server.ThrottleBrakeSystem.Companion.ACTION_MOVE_FORWARD
import car.server.ThrottleBrakeSystem.Companion.ACTION_NEUTRAL
import car.server.ThrottleBrakeSystem.Companion.ACTION_PARKING_BRAKE
import car.server.EngineSystem.Companion.EMPTY_STRING

object ThrottleBrakeImpl:ThrottleBrake {

    override var action = ACTION_BRAKING_STILL
    override var value = 0

    override var parkingBrakeState = false
        get() = action == ACTION_PARKING_BRAKE && ThrottleBrakeImpl.value == 100
    override var handbrakeState = false
        get() = action == ACTION_HANDBRAKE && ThrottleBrakeImpl.value == 100
    override var motionState = EMPTY_STRING
        get() = when {
                isNeutral -> ACTION_NEUTRAL
                isBrakingStill -> ACTION_BRAKING_STILL
                isMovingForward -> ACTION_MOVE_FORWARD
                isMovingBackward -> ACTION_MOVE_BACKWARD
                else -> EMPTY_STRING
            }
    override var isMovingForward = false
        get() = action == ACTION_MOVE_FORWARD
    override var isMovingBackward = false
        get() = action == ACTION_MOVE_BACKWARD
    override var isBrakingStill = false
        get() = action == ACTION_BRAKING_STILL
    override var isNeutral = false
        get() = action == ACTION_NEUTRAL

    override fun throttle(direction: String, value: Int): String {
        if(direction == ACTION_MOVE_FORWARD){
            // TODO prepare H-bridge for forward movement

            //////
            // Testing purposes
            if (EngineImpl.ENABLE_PINS) {
                EngineImpl.pinInput1.high()
                EngineImpl.pinInput2.low()
            }
            //////

            action = ACTION_MOVE_FORWARD
        }
        else if (direction == ACTION_MOVE_BACKWARD){
            // TODO prepare H-bridge for backward movement

            //////
            // Testing purposes
            if (EngineImpl.ENABLE_PINS) {
                EngineImpl.pinInput1.low()
                EngineImpl.pinInput2.high()
            }
            //////

            action = ACTION_MOVE_BACKWARD
        }

        //TODO set value to pins

        //////
        // Testing purposes
        if (EngineImpl.ENABLE_PINS) {
            EngineImpl.pwmPinEnable.pwm = value
        }
        //////

	    ThrottleBrakeImpl.value = value
        return EngineImpl.SUCCESS // or error message from pins
    }

    override fun brake(value: Int): String {
        //TODO set value to pins

        //////
        // Testing purposes
        if (EngineImpl.ENABLE_PINS) {
            EngineImpl.pwmPinEnable.pwm = value
            EngineImpl.pinInput1.low()
            EngineImpl.pinInput2.low()
        }
        //////

        action = ACTION_BRAKING_STILL
	    ThrottleBrakeImpl.value = value
        return EngineImpl.SUCCESS // or error message from pins
    }

    override fun parkingBrake(value: Int): String {
        //TODO set value to pins

        //////
        // Testing purposes
        if (EngineImpl.ENABLE_PINS) {
            EngineImpl.pwmPinEnable.pwm = value
            EngineImpl.pinInput1.low()
            EngineImpl.pinInput2.low()
        }
        //////

        action = ACTION_PARKING_BRAKE
        ThrottleBrakeImpl.value = value
        return EngineImpl.SUCCESS // or error message from pins
    }

    override fun handbrake(value: Int): String {
        //TODO set value to pins

        //////
        // Testing purposes
        if (EngineImpl.ENABLE_PINS) {
            EngineImpl.pwmPinEnable.pwm = value
            EngineImpl.pinInput1.low()
            EngineImpl.pinInput2.low()
        }
        //////

        action = ACTION_HANDBRAKE
	    ThrottleBrakeImpl.value = value
        return EngineImpl.SUCCESS // or error message from pins
    }

    override fun setNeutral(): String {
        //TODO set the motor to neutral

        //////
        // Testing purposes
        if (EngineImpl.ENABLE_PINS) {
            EngineImpl.pwmPinEnable.pwm = value
            EngineImpl.pinInput1.low()
            EngineImpl.pinInput2.low()
        }
        //////

        action = ACTION_NEUTRAL
        return EngineImpl.SUCCESS // or error message from pins
    }
}