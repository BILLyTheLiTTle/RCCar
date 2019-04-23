package car.cockpit.electrics

import car.showMessage
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component("Electrics Component")
class ElectricsComponent: Electrics {
    private val lights = mutableMapOf<String, Boolean>()

    private val logger = LoggerFactory.getLogger(ElectricsComponent::class.java)

    override var positionLightsState = false
        set(value) {
            lights[ElectricPart.VisionLight.POSITION_LIGHTS.name] = value

            //println("${this::class.simpleName} Position Lights: $value\n")

            //reset the bigger light scales
            drivingLightsState = false
            //longRangeLightsState = false

            // TODO call handleLeds(...)

            field = value
    }
    override var drivingLightsState = false
        set(value) {
            lights[ElectricPart.VisionLight.DRIVING_LIGHTS.name] = value

            //println("${this::class.simpleName} Driving Lights: $value\n")

            //reset the bigger light scales
            longRangeLightsState = false

            // TODO call handleLeds(...)

            field = value
        }
    override var longRangeLightsState = false
        set(value) {
            lights[ElectricPart.VisionLight.LONG_RANGE_LIGHTS.name] = value

            //println("${this::class.simpleName} Long Range Lights: $value\n")

            // TODO call handleLeds(...)

            field = value
        }

    override var brakingLightsState = false
        set(value) {
            lights[ElectricPart.Other.BRAKING_LIGHTS.name] = value

            showMessage(logger = logger,
                body = "Brake Lights: $value")

            // TODO call handleLeds(...)

            field = value
        }

    override var reverseLightsState = false
        set(value) {
            lights[ElectricPart.Other.REVERSE_LIGHTS.name] = value

            showMessage(logger = logger,
                body = "Reverse Lights: $value")

            // TODO call handleLeds(...)

            field = value
        }

    override var leftTurnLightsState = false
        set(value) {
            lights[ElectricPart.DirectionLight.DIRECTION_LIGHTS_LEFT.name] = value

            showMessage(logger = logger,
                body = "Left Turn Lights: $value")

            if(value)
                //reset the right turn light
                rightTurnLightsState = false

            // TODO call handleLeds(...)

            field = value
        }

    override var rightTurnLightsState = false
        set(value) {
            lights[ElectricPart.DirectionLight.DIRECTION_LIGHTS_RIGHT.name] = value

            showMessage(logger = logger,
                body = "Right Turn Lights: $value")

            if(value)
                //reset the left turn light
                leftTurnLightsState = false

            // TODO call handleLeds(...)

            field = value
        }

    override var emergencyLightsState = false
        set(value) {
            lights[ElectricPart.Other.EMERGENCY_LIGHTS.name] = value

            showMessage(logger = logger,
                body = "Emergency Lights: $value")

            // TODO call handleLeds(...)

            field = value
        }

    override fun doHeadlightsSignal(): String {
        // TODO play with the long_range lights twice in a TimerTask for a small period of time

        /* TODO
            If the lights are on, then do off/on, off/on
            But if the lights are off, then do on/off, on/off

            The above actions will put the LEDs on their initial state( on or off)
         */

        // TODO call handleLeds(...)

        return "Signal"
    }

    /* I am not going to touch the same LEDs for different reasons.
        Also, I am not going to change the same key/value of the
        _lights Map for different reasons.
        This means, for example, that I will handle turn lights for turn reasons
        and not when braking.
        So, in my opinion synchronizing this function is not necessary.
     */
    //@Synchronized
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