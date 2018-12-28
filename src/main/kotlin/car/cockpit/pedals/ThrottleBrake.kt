package car.cockpit.pedals

interface ThrottleBrake {

    //val action: String
    val value: Int

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

    fun reset()
}