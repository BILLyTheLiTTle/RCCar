package car.cockpit.electrics

import car.cockpit.engine.UNKNOWN_STATE
import car.showMessage
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service("Electrics Service")
class ElectricsService {

    @Autowired
    private lateinit var electricsComponent: Electrics

    fun setDirectionLights(direction: String): String {

        showMessage(title = "ELECTRIC SYSTEM",
            body = "{ ${this::class.simpleName} } Direction: $direction")

        synchronized(this){
            when (direction) {
                TURN_LIGHTS_STRAIGHT -> {
                    electricsComponent.leftTurnLightsState = false
                    electricsComponent.rightTurnLightsState = false
                }
                TURN_LIGHTS_RIGHT -> {
                    //ElectricsComponent.leftTurnLightsState = false
                    electricsComponent.rightTurnLightsState = !electricsComponent.rightTurnLightsState
                }
                TURN_LIGHTS_LEFT -> {
                    electricsComponent.leftTurnLightsState = !electricsComponent.leftTurnLightsState
                    //ElectricsComponent.rightTurnLightsState = false
                }
            }
        }

        return getDirectionLights()
    }

    fun getDirectionLights() =
        if (!electricsComponent.leftTurnLightsState && !electricsComponent.rightTurnLightsState)
            TURN_LIGHTS_STRAIGHT
        else if (electricsComponent.leftTurnLightsState)
            TURN_LIGHTS_LEFT
        else if (electricsComponent.rightTurnLightsState)
            TURN_LIGHTS_RIGHT
        else
            UNKNOWN_STATE


    fun setMainLightsState(value: String): String {

        showMessage(title = "ELECTRIC SYSTEM",
            body = "{ ${this::class.simpleName} } Main Lights State Request: $value")

        // I don't think I need synchronization
        //synchronized(this){
        when(value){
            LIGHTS_OFF -> electricsComponent.positionLightsState = false
            POSITION_LIGHTS -> electricsComponent.positionLightsState = true
            DRIVING_LIGHTS -> electricsComponent.drivingLightsState = true
            LONG_RANGE_LIGHTS -> electricsComponent.longRangeLightsState = true
            LONG_RANGE_SIGNAL_LIGHTS -> electricsComponent.doHeadlightsSignal()
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
        if (electricsComponent.longRangeLightsState) LONG_RANGE_LIGHTS
        else if (electricsComponent.drivingLightsState) DRIVING_LIGHTS
        else if (electricsComponent.positionLightsState) POSITION_LIGHTS
        else if (!electricsComponent.positionLightsState) LIGHTS_OFF
        else UNKNOWN_STATE

    fun setReverseLightsState(state: Boolean): String {
        electricsComponent.reverseLightsState = state
        return electricsComponent.reverseLightsState.toString()
    }

    fun getReverseLightsState() = electricsComponent.reverseLightsState

    fun setEmergencyLightsState(state: Boolean): String {
        electricsComponent.emergencyLightsState = state
        return electricsComponent.emergencyLightsState.toString()
    }

    fun getEmergencyLightsState() = electricsComponent.emergencyLightsState
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