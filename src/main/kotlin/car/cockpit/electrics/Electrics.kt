package car.cockpit.electrics

interface Electrics {
    var positionLightsState: Boolean
    var drivingLightsState: Boolean
    var longRangeLightsState: Boolean

    var brakingLightsState: Boolean

    var reverseLightsState: Boolean

    var leftTurnLightsState: Boolean
    var rightTurnLightsState: Boolean

    var emergencyLightsState: Boolean

    fun doHeadlightsSignal(): String

    /* When I wrote @Synchronized here, the following error was thrown on the Pi

        java.lang.ClassFormatError:
        Method handleLeds in class car/controllers/basic/Electrics has illegal modifiers: 0x421

        I don't understand why, but implement this function in the child class.
        It seems a good implementation this way because functions designed for overriding should not be synchronized.
    */
    fun handleLeds()

    fun reset()
}

enum class MainLight {
    NOTHING,
    LIGHTS_OFF,
    POSITION_LIGHTS, DRIVING_LIGHTS, LONG_RANGE_LIGHTS,
    LONG_RANGE_SIGNAL_LIGHTS;

    companion object {
        // Give the name of the enum which will be the default when needed
        const val DEFAULT = "LIGHTS_OFF"
    }
}

enum class CorneringLight{
    NOTHING,
    RIGHT_LIGHTS, LEFT_LIGHTS, STRAIGHT_LIGHTS;

    companion object {
        // Give the name of the enum which will be the default when needed
        const val DEFAULT = "STRAIGHT_LIGHT"
    }
}

enum class OtherLight{
    BRAKING_LIGHTS,
    REVERSE_LIGHTS,
    EMERGENCY_LIGHTS
}