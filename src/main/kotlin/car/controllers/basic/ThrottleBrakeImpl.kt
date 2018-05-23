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

    override val parkingBrakeState
        get() = action == ACTION_PARKING_BRAKE && ThrottleBrakeImpl.value == 100
    override val handbrakeState
        get() = action == ACTION_HANDBRAKE && ThrottleBrakeImpl.value == 100
    override val motionState
        get() = when {
                isNeutral -> ACTION_NEUTRAL
                isBrakingStill -> ACTION_BRAKING_STILL
                isMovingForward -> ACTION_MOVE_FORWARD
                isMovingBackward -> ACTION_MOVE_BACKWARD
                else -> EMPTY_STRING
            }
    override val isMovingForward
        get() = action == ACTION_MOVE_FORWARD
    override val isMovingBackward
        get() = action == ACTION_MOVE_BACKWARD
    override val isBrakingStill
        get() = action == ACTION_BRAKING_STILL
    override val isNeutral
        get() = action == ACTION_NEUTRAL

    override fun throttle(direction: String, value: Int): String {
        if(direction == ACTION_MOVE_FORWARD){
            // TODO prepare H-bridge for forward movement

            //////
            // Pi related
            if (EngineImpl.RUN_ON_PI) {
                EngineImpl.pinInput1.high()
                EngineImpl.pinInput2.low()
            }
            //////

            action = ACTION_MOVE_FORWARD
        }
        else if (direction == ACTION_MOVE_BACKWARD){
            // TODO prepare H-bridge for backward movement

            //////
            // Pi related
            if (EngineImpl.RUN_ON_PI) {
                EngineImpl.pinInput1.low()
                EngineImpl.pinInput2.high()
            }
            //////

            action = ACTION_MOVE_BACKWARD
        }

        //TODO set value to pins

        //////
        // Pi related
        if (EngineImpl.RUN_ON_PI) {
            EngineImpl.pwmPinEnable.pwm = value
        }
        //////

	    ThrottleBrakeImpl.value = value
        return EngineImpl.SUCCESS // or error message from pins
    }

    override fun brake(value: Int): String {
        //TODO set value to pins

        //////
        // Pi related
        if (EngineImpl.RUN_ON_PI) {
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
        // Pi related
        if (EngineImpl.RUN_ON_PI) {
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
        // Pi related
        if (EngineImpl.RUN_ON_PI) {
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
        // Pi related
        if (EngineImpl.RUN_ON_PI) {
            EngineImpl.pwmPinEnable.pwm = value
            EngineImpl.pinInput1.low()
            EngineImpl.pinInput2.low()
        }
        //////

        action = ACTION_NEUTRAL
        return EngineImpl.SUCCESS // or error message from pins
    }
}