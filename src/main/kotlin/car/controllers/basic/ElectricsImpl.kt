package car.controllers.basic

import car.server.ElectricSystem
import car.showMessage

object ElectricsImpl:Electrics {
    private val _lights = mutableMapOf<String, Boolean>()

    override var positionLightsState = false
        set(value) {
            _lights[ElectricSystem.POSITION_LIGHTS] = value

            //println("${this::class.simpleName} Position Lights: $value\n")

            //reset the bigger light scales
            drivingLightsState = false
            longRangeLightsState = false

            // TODO call handleLeds(...)

            field = value
    }
    override var drivingLightsState = false
        set(value) {
            _lights[ElectricSystem.DRIVING_LIGHTS] = value

            //println("${this::class.simpleName} Driving Lights: $value\n")

            //reset the bigger light scales
            longRangeLightsState = false

            // TODO call handleLeds(...)

            field = value
        }
    override var longRangeLightsState = false
        set(value) {
            _lights[ElectricSystem.LONG_RANGE_LIGHTS] = value

            //println("${this::class.simpleName} Long Range Lights: $value\n")

            // TODO call handleLeds(...)

            field = value
        }

    override var brakingLightsState = false
        set(value) {
            _lights[ElectricSystem.BRAKING_LIGHTS] = value

            showMessage(title = "LIGHTS",
                body = "{ ${this::class.simpleName} } Brake Lights: $value")

            // TODO call handleLeds(...)

            field = value
        }

    override var reverseLightsState = false
        set(value) {
            _lights[ElectricSystem.REVERSE_LIGHTS] = value

            showMessage(title = "LIGHTS",
                body = "{ ${this::class.simpleName} } Reverse Lights: $value")

            // TODO call handleLeds(...)

            field = value
        }

    override var leftTurnLightsState = false
        set(value) {
            _lights[ElectricSystem.TURN_LIGHTS_LEFT] = value

            showMessage(title = "LIGHTS",
                body = "{ ${this::class.simpleName} } Left Turn Lights: $value")

            field = value
        }

    override var rightTurnLightsState = false
        set(value) {
            _lights[ElectricSystem.TURN_LIGHTS_RIGHT] = value

            showMessage(title = "LIGHTS",
                body = "{ ${this::class.simpleName} } Right Turn Lights: $value")

            field = value
        }

    override var emergencyLightsState = false
        set(value) {
            _lights[ElectricSystem.EMERGENCY_LIGHTS] = value

            showMessage(title = "LIGHTS",
                body = "{ ${this::class.simpleName} } Emergency Lights: $value")

            field = value
        }

    override fun doHeadlightsSignal(): String {
        // TODO turn on/off the long_range lights twice in a TimerTask for a small period of time

        // TODO call handleLeds(...)

        return "Signal"
    }

    // TODO the stuff here
    @Synchronized
    override fun handleLeds() {
        
    }

    override fun reset() {
        longRangeLightsState =false
        drivingLightsState = false
        positionLightsState = false
        brakingLightsState = false
        reverseLightsState = false
        leftTurnLightsState = false
        rightTurnLightsState = false
        emergencyLightsState = false
        /* No need to run handleLeds(...) cuz it should run
            when we set every value to false, above
        */
        // TODO maybe reset the values of the pins (3 for Shift Registers and 2 for NPN BJTs)
    }
}