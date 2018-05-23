package car.server

import car.controllers.basic.Electrics
import car.controllers.basic.ElectricsImpl
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class ElectricSystem{

    /*
        All possible combinations and their meaning:
        direction = 1 -> Light for right turn
        direction = -1 -> Light for left turn
        direction = 0 -> All turn lights are off
     */
    @GetMapping("/set_turn_light")
    fun setTurnLightAction(
        @RequestParam(value = "direction", defaultValue = "$DIRECTION_LIGHT_STRAIGHT") direction: Int)
            : String {

        //TODO add function for the hardware
        synchronized(this){
            println("Light for $direction turn")
        }

        return "Light for $direction turn"
    }

    @GetMapping("/get_turn_light")
    fun getTurnLightAction(): String {
        return "Read the hardware and show me what is going on"
    }


    @GetMapping("/set_main_lights_state")
    fun setMainLightsState(@RequestParam(value = "value", defaultValue = "$LIGHTS_OFF") value: String): String {

        println("${this::class.simpleName} Main Lights State Request: $value\n")

        synchronized(this){
            when(value){
                LIGHTS_OFF -> ElectricsImpl.positionLightsState = false
                POSITION_LIGHTS -> ElectricsImpl.positionLightsState = true
                DRIVING_LIGHTS -> ElectricsImpl.drivingLightsState = true
                LONG_RANGE_LIGHTS -> ElectricsImpl.longRangeLightsState = true
                LONG_RANGE_SIGNAL_LIGHTS -> ElectricsImpl.doHeadlightsSignal()
                else -> "${this::class.simpleName} ERROR: Entered in else condition"
            }
        }

        return getMainLightsState()
    }

    @GetMapping("/get_main_lights_state")
    fun getMainLightsState() =
        /* Condition should be executed this way cuz if I check
            first for position lights, then driving lights, then long range lights
            if the user has the long range beam the if clause will enter at
            position lights condition.
         */
        if (ElectricsImpl.longRangeLightsState) LONG_RANGE_LIGHTS
        else if (ElectricsImpl.drivingLightsState) DRIVING_LIGHTS
        else if (ElectricsImpl.positionLightsState) POSITION_LIGHTS
        else if (!ElectricsImpl.positionLightsState) LIGHTS_OFF
        else EngineSystem.EMPTY_STRING

    @GetMapping("/set_reverse_lights_state")
    fun setReverseLightsState(@RequestParam(value = "state", defaultValue = "true") state: Boolean): String {
        ElectricsImpl.reverseLightsState = state
        return ElectricsImpl.reverseLightsState.toString()
    }

    @GetMapping("/get_reverse_lights_state")
    fun getReverseLightsState() = ElectricsImpl.reverseLightsState

    companion object {
        const val DIRECTION_LIGHT_RIGHT = 1
        const val DIRECTION_LIGHT_LEFT = -1
        const val DIRECTION_LIGHT_STRAIGHT = 0

        const val LIGHTS_OFF = "lights_off"
        const val POSITION_LIGHTS = "position_lights"
        const val DRIVING_LIGHTS = "driving_lights"
        const val LONG_RANGE_LIGHTS = "long_range_lights"
        const val LONG_RANGE_SIGNAL_LIGHTS = "long_range_signal_lights"

        const val BRAKING_LIGHTS = "braking_lights"

        const val REVERSE_LIGHTS = "reverse_lights"
    }

}