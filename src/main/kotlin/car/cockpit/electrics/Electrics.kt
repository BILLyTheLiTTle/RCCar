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

enum class MainLight(val id:String){
    LIGHTS_OFF("lights_off"),
    POSITION_LIGHTS("lights_position"),
    DRIVING_LIGHTS("lights_driving"),
    LONG_RANGE_LIGHTS("lights_long_range"),
    LONG_RANGE_SIGNAL_LIGHTS("lights_long_range_signal");

    companion object {
        // Give the name of the enum which will be the default when needed
        const val DEFAULT = "LIGHTS_OFF"
    }
}

enum class DirectionLight(val id:String){
    DIRECTION_LIGHTS_RIGHT("lights_direction_right"),
    DIRECTION_LIGHTS_LEFT("lights_direction_left"),
    DIRECTION_LIGHTS_STRAIGHT("lights_direction_straight");

    companion object {
        // Give the name of the enum which will be the default when needed
        const val DEFAULT = "DIRECTION_LIGHTS_STRAIGHT"
    }
}

enum class OtherLight(val id:String){
    BRAKING_LIGHTS("lights_braking"),
    REVERSE_LIGHTS("lights_reverse"),
    EMERGENCY_LIGHTS("lights_emergency")
}