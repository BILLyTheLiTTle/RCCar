package car.controllers.basic

import car.server.ElectricSystem

object ElectricsImpl:Electrics {
    private val _lights = mutableMapOf<String, Int>()

    override var positionLightsState = false
        set(value) {
            if (value)
                _lights[ElectricSystem.POSITION_LIGHTS] = 1
            else
                _lights[ElectricSystem.POSITION_LIGHTS] = 0

            //println("${this::class.simpleName} Position Lights: $value\n")

            //reset the bigger light scales
            drivingLightsState = false
            longRangeLightsState = false

            // TODO call handleLeds(...)

            field = value
    }
    override var drivingLightsState = false
        set(value) {
            if (value)
                _lights[ElectricSystem.DRIVING_LIGHTS] = 1
            else
                _lights[ElectricSystem.DRIVING_LIGHTS] = 0

            //println("${this::class.simpleName} Driving Lights: $value\n")

            //reset the bigger light scales
            longRangeLightsState = false

            // TODO call handleLeds(...)

            field = value
        }
    override var longRangeLightsState = false
        set(value) {
            if (value)
                _lights[ElectricSystem.LONG_RANGE_LIGHTS] = 1
            else
                _lights[ElectricSystem.LONG_RANGE_LIGHTS] = 0

            //println("${this::class.simpleName} Long Range Lights: $value\n")

            // TODO call handleLeds(...)

            field = value
        }

    override var brakingLightsState = false
        set(value) {
            if (value)
                _lights[ElectricSystem.BRAKING_LIGHTS] = 1
            else
                _lights[ElectricSystem.BRAKING_LIGHTS] = 0

            println("${this::class.simpleName} Brake Lights: $value\n")

            // TODO call handleLeds(...)

            field = value
        }

    override var reverseLightsState = false
        set(value) {
            if (value)
                _lights[ElectricSystem.REVERSE_LIGHTS] = 1
            else
                _lights[ElectricSystem.REVERSE_LIGHTS] = 0

            println("${this::class.simpleName} Reverse Lights: $value\n")

            // TODO call handleLeds(...)

            field = value
        }

    override var leftTurnLightsState = false
        set(value) {
            if (value)
                _lights[ElectricSystem.TURN_LIGHT_LEFT] = 1
            else
                _lights[ElectricSystem.TURN_LIGHT_LEFT] = 0

            println("${this::class.simpleName} Left Turn Lights: $value\n")

            field = value
        }

    override var rightTurnLightsState = false
        set(value) {
            if (value)
                _lights[ElectricSystem.TURN_LIGHT_RIGHT] = 1
            else
                _lights[ElectricSystem.TURN_LIGHT_RIGHT] = 0

            println("${this::class.simpleName} Right Turn Lights: $value\n")

            field = value
        }


    override fun doHeadlightsSignal(): String {
        // TODO turn on/off the long_range lights twice in a TimerTask for a small period of time

        // TODO call handleLeds(...)

        return "Signal"
    }

    override fun reset() {
        longRangeLightsState =false
        drivingLightsState = false
        positionLightsState = false
        brakingLightsState = false
        reverseLightsState = false
        leftTurnLightsState = false
        rightTurnLightsState = false
        /* No need to run handleLeds(...) cuz it should run
            when we set every value to false, above
        */
        // TODO maybe reset the values of the pins (3 for Shift Registers and 2 for NPN BJTs)
    }
}