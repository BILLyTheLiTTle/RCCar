package car.cockpit.electrics

enum class ElectricPart {
    // Wow, what I had implemented here? Is it good, clever or awful?!
    ;
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

    enum class Other(val id:String){
        BRAKING_LIGHTS("lights_braking"),
        REVERSE_LIGHTS("lights_reverse"),
        EMERGENCY_LIGHTS("lights_emergency")
    }
}