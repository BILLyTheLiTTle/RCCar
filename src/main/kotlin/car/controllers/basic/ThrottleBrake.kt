package car.controllers.basic

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

    fun applyPwmValues(motorFRpin00: Int = 0, motorFRpin01: Int = 0,
                                 motorFLpin00: Int = 0, motorFLpin01: Int = 0,
                                 motorRRpin00: Int = 0, motorRRpin01: Int = 0,
                                 motorRLpin00: Int = 0, motorRLpin01: Int = 0){
        //TODO implement it here
    }

    fun reset()
}