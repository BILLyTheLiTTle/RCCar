package car.server

import car.controllers.basic.ElectricsImpl
import car.showMessage
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
    @GetMapping("/set_direction_lights")
    fun setDirectionLights(
        @RequestParam(value = "direction", defaultValue = TURN_LIGHTS_STRAIGHT) direction: String
    ): String {

        showMessage(title = "ELECTRIC SYSTEM",
            body = "{ ${this::class.simpleName} } Direction: $direction")

        synchronized(this){
            when (direction) {
                TURN_LIGHTS_STRAIGHT -> {
                    ElectricsImpl.leftTurnLightsState = false
                    ElectricsImpl.rightTurnLightsState = false
                }
                TURN_LIGHTS_RIGHT -> {
                    //ElectricsImpl.leftTurnLightsState = false
                    ElectricsImpl.rightTurnLightsState = !ElectricsImpl.rightTurnLightsState
                }
                TURN_LIGHTS_LEFT -> {
                    ElectricsImpl.leftTurnLightsState = !ElectricsImpl.leftTurnLightsState
                    //ElectricsImpl.rightTurnLightsState = false
                }
            }
        }

        return getDirectionLights()
    }

    @GetMapping("/get_direction_lights")
    fun getDirectionLights() =
        if (!ElectricsImpl.leftTurnLightsState && !ElectricsImpl.rightTurnLightsState)
            TURN_LIGHTS_STRAIGHT
        else if (ElectricsImpl.leftTurnLightsState)
            TURN_LIGHTS_LEFT
        else if (ElectricsImpl.rightTurnLightsState)
            TURN_LIGHTS_RIGHT
        else
            EngineSystem.UNKNOWN_STATE


    @GetMapping("/set_main_lights_state")
    fun setMainLightsState(@RequestParam(value = "value", defaultValue = LIGHTS_OFF) value: String): String {

        showMessage(title = "ELECTRIC SYSTEM",
            body = "{ ${this::class.simpleName} } Main Lights State Request: $value")

        // I don't think I need synchronization
        //synchronized(this){
            when(value){
                LIGHTS_OFF -> ElectricsImpl.positionLightsState = false
                POSITION_LIGHTS -> ElectricsImpl.positionLightsState = true
                DRIVING_LIGHTS -> ElectricsImpl.drivingLightsState = true
                LONG_RANGE_LIGHTS -> ElectricsImpl.longRangeLightsState = true
                LONG_RANGE_SIGNAL_LIGHTS -> ElectricsImpl.doHeadlightsSignal()
                else -> "${this::class.simpleName} ERROR: Entered in else condition"
            }
        //}

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
        else EngineSystem.UNKNOWN_STATE

    @GetMapping("/set_reverse_lights_state")
    fun setReverseLightsState(@RequestParam(value = "state", defaultValue = "false") state: Boolean): String {
        ElectricsImpl.reverseLightsState = state
        return ElectricsImpl.reverseLightsState.toString()
    }

    @GetMapping("/get_reverse_lights_state")
    fun getReverseLightsState() = ElectricsImpl.reverseLightsState

    @GetMapping("/set_emergency_lights_state")
    fun setEmergencyLightsState(@RequestParam(value = "state", defaultValue = "false") state: Boolean): String {
        ElectricsImpl.emergencyLightsState = state
        return ElectricsImpl.emergencyLightsState.toString()
    }

    @GetMapping("/get_emergency_lights_state")
    fun getEmergencyLightsState() = ElectricsImpl.emergencyLightsState

    companion object {
        const val TURN_LIGHTS_RIGHT = "turn_lights_right"
        const val TURN_LIGHTS_LEFT = "turn_lights_left"
        const val TURN_LIGHTS_STRAIGHT = "turn_lights_straight"

        const val LIGHTS_OFF = "lights_off"
        const val POSITION_LIGHTS = "position_lights"
        const val DRIVING_LIGHTS = "driving_lights"
        const val LONG_RANGE_LIGHTS = "long_range_lights"
        const val LONG_RANGE_SIGNAL_LIGHTS = "long_range_signal_lights"

        const val BRAKING_LIGHTS = "braking_lights"

        const val REVERSE_LIGHTS = "reverse_lights"

        const val EMERGENCY_LIGHTS = "emergency_lights"
    }

}