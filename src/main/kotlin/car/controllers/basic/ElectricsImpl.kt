package car.controllers.basic

import car.server.ElectricSystem

object ElectricsImpl:Electrics {
    private val _lights = mutableMapOf<String, Int>()
    override val lights: Map<String, Int> = _lights

    override var positionLightsState: Boolean = false
        set(value) {
            if (value)
                _lights[ElectricSystem.POSITION_LIGHTS] = 1
            else
                _lights[ElectricSystem.POSITION_LIGHTS] = 0

            //reset the bigger light scales
            drivingLightsState = false
            longRangeLightsState = false

            field = value
    }
    override var drivingLightsState: Boolean = false
        set(value) {
            if (value)
                _lights[ElectricSystem.DRIVING_LIGHTS] = 1
            else
                _lights[ElectricSystem.DRIVING_LIGHTS] = 0

            //reset the bigger light scales
            longRangeLightsState = false

            field = value
        }
    override var longRangeLightsState: Boolean = false
        set(value) {
            if (value)
                _lights[ElectricSystem.LONG_RANGE_LIGHTS] = 1
            else
                _lights[ElectricSystem.LONG_RANGE_LIGHTS] = 0
            field = value
        }

    override fun doHeadlightsSignal(): String {
        // TODO turn on/off the long_range lights twice in a TimerTask for a small period of time

        return "Signal"
    }
}