package car.cockpit.electrics

import car.cockpit.engine.EngineController
import car.cockpit.engine.UNKNOWN_STATE
import car.showMessage
import org.springframework.stereotype.Service

@Service("Electrics")
class ElectricsService {
    fun setDirectionLights(direction: String): String {

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

    fun getDirectionLights() =
        if (!ElectricsImpl.leftTurnLightsState && !ElectricsImpl.rightTurnLightsState)
            TURN_LIGHTS_STRAIGHT
        else if (ElectricsImpl.leftTurnLightsState)
            TURN_LIGHTS_LEFT
        else if (ElectricsImpl.rightTurnLightsState)
            TURN_LIGHTS_RIGHT
        else
            UNKNOWN_STATE


    fun setMainLightsState(value: String): String {

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
        else UNKNOWN_STATE

    fun setReverseLightsState(state: Boolean): String {
        ElectricsImpl.reverseLightsState = state
        return ElectricsImpl.reverseLightsState.toString()
    }

    fun getReverseLightsState() = ElectricsImpl.reverseLightsState

    fun setEmergencyLightsState(state: Boolean): String {
        ElectricsImpl.emergencyLightsState = state
        return ElectricsImpl.emergencyLightsState.toString()
    }

    fun getEmergencyLightsState() = ElectricsImpl.emergencyLightsState
}

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