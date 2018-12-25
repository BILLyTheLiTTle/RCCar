package car.cockpit.pedals

import car.cockpit.engine.EngineImpl

interface ThrottleBrake {

    val parkingBrakeState: Boolean
    val handbrakeState: Boolean
    val motionState: String
    val isMovingForward: Boolean
    val isMovingBackward: Boolean
    val isBrakingStill: Boolean
    val isNeutral: Boolean

    fun throttle(direction: String, value: Int): String
    fun brake(value: Int): String
    fun parkingBrake(value: Int): String
    fun handbrake(value: Int): String
    fun setNeutral(): String

    fun applyPinValues(motorFrontRightPwm: Int? = null, motorFrontRightDigital: Boolean? = null,
                       motorFrontLeftPwm: Int? = null, motorFrontLeftDigital: Boolean? = null,
                       motorRearRightPwm: Int? = null, motorRearRightDigital: Boolean? = null,
                       motorRearLeftPwm: Int? = null, motorRearLeftDigital: Boolean? = null){

        motorFrontRightPwm?.let { EngineImpl.motorFrontRightPwmPin.pwm = it }
        motorFrontRightDigital?.let { EngineImpl.motorFrontRightDirPin.setState(it) }
        motorFrontLeftPwm?.let { EngineImpl.motorFrontLeftPwmPin.pwm = it }
        motorFrontLeftDigital?.let { EngineImpl.motorFrontLeftDirPin.setState(it) }
        motorRearRightPwm?.let { EngineImpl.motorRearRightPwmPin.pwm = it }
        motorRearRightDigital?.let { EngineImpl.motorRearRightDirPin.setState(it) }
        motorRearLeftPwm?.let { EngineImpl.motorRearLeftPwmPin.pwm = it }
        motorRearLeftDigital?.let { EngineImpl.motorRearLeftDirPin.setState(it) }
    }

    fun reset()
}