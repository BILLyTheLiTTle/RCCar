package car.controllers.basic

import car.server.ElectricSystem

object ElectricsImpl:Electrics {
    private val _lights = mutableMapOf<String, Int>()
    override val lights: Map<String, Int> = _lights

    override var positionLightsState = false
        set(value) {
            if (value)
                _lights[ElectricSystem.POSITION_LIGHTS] = 1
            else
                _lights[ElectricSystem.POSITION_LIGHTS] = 0

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


    override fun doHeadlightsSignal(): String {
        // TODO turn on/off the long_range lights twice in a TimerTask for a small period of time

        // TODO call handleLeds(...)

        return "Signal"
    }

    override fun reset() {
        longRangeLightsState =false
        drivingLightsState = false
        positionLightsState = false
        // TODO handleLeds(_lights) to turn LEDs off
    }
}