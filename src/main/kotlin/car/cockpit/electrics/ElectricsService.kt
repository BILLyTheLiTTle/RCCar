package car.cockpit.electrics

import car.cockpit.engine.UNKNOWN_STATE
import car.showMessage
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service("Electrics Service")
class ElectricsService {

    @Autowired
    private lateinit var electricsComponent: Electrics

    private val logger = LoggerFactory.getLogger(ElectricsService::class.java)

    fun setDirectionLights(direction: String): String {

        showMessage(logger = logger,
            body = "Direction: $direction")

        val part = DirectionLight.valueOf(direction)

        synchronized(this){
            when (part) {
                DirectionLight.DIRECTION_LIGHTS_STRAIGHT -> {
                    electricsComponent.leftTurnLightsState = false
                    electricsComponent.rightTurnLightsState = false
                }
                DirectionLight.DIRECTION_LIGHTS_RIGHT -> {
                    //ElectricsComponent.leftTurnLightsState = false
                    electricsComponent.rightTurnLightsState = !electricsComponent.rightTurnLightsState
                }
                DirectionLight.DIRECTION_LIGHTS_LEFT -> {
                    electricsComponent.leftTurnLightsState = !electricsComponent.leftTurnLightsState
                    //ElectricsComponent.rightTurnLightsState = false
                }
            }
        }

        return getDirectionLights()
    }

    fun getDirectionLights() =
        if (!electricsComponent.leftTurnLightsState && !electricsComponent.rightTurnLightsState)
            DirectionLight.DIRECTION_LIGHTS_STRAIGHT.name
        else if (electricsComponent.leftTurnLightsState)
            DirectionLight.DIRECTION_LIGHTS_LEFT.name
        else if (electricsComponent.rightTurnLightsState)
            DirectionLight.DIRECTION_LIGHTS_RIGHT.name
        else
            UNKNOWN_STATE


    fun setMainLightsState(value: String): String {

        showMessage(logger = logger,
            body = "Main Lights State Request: $value")

        val part = MainLight.valueOf(value)
        // I don't think I need synchronization
        //synchronized(this){
        when(part){
            MainLight.LIGHTS_OFF -> electricsComponent.positionLightsState = false
            MainLight.POSITION_LIGHTS -> electricsComponent.positionLightsState = true
            MainLight.DRIVING_LIGHTS -> electricsComponent.drivingLightsState = true
            MainLight.LONG_RANGE_LIGHTS -> electricsComponent.longRangeLightsState = true
            MainLight.LONG_RANGE_SIGNAL_LIGHTS -> electricsComponent.doHeadlightsSignal()
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
        if (electricsComponent.longRangeLightsState) MainLight.LONG_RANGE_LIGHTS.name
        else if (electricsComponent.drivingLightsState) MainLight.DRIVING_LIGHTS.name
        else if (electricsComponent.positionLightsState) MainLight.POSITION_LIGHTS.name
        else if (!electricsComponent.positionLightsState) MainLight.LIGHTS_OFF.name
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