package car.cockpit.pedals

interface ThrottleBrake {

    val value: Int

    val parkingBrakeState: Boolean
    val handbrakeState: Boolean
    val motionState: Motion
    val isMovingForward: Boolean
    val isMovingBackward: Boolean
    val isBrakingStill: Boolean
    val isNeutral: Boolean

    fun throttle(direction: Motion, value: Int): String
    fun brake(value: Int): String
    fun parkingBrake(value: Int): String
    fun handbrake(value: Int): String
    fun setNeutral(): String

    fun reset()
}

enum class Motion {
    NOTHING, FORWARD, BACKWARD, NEUTRAL, BRAKING_STILL, PARKING_BRAKE, HANDBRAKE;

    companion object {
        // Give the name of the enum which will be the default when needed
        const val DEFAULT = "NEUTRAL"
    }
}