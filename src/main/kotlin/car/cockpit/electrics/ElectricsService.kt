package car.cockpit.electrics

import car.cockpit.engine.UNKNOWN_STATE
import car.enumContains
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

        val part = if (enumContains<CorneringLight>(direction)) CorneringLight.valueOf(direction) else CorneringLight.NOTHING

        synchronized(this){
            when (part) {
                CorneringLight.STRAIGHT_LIGHTS -> {
                    electricsComponent.leftTurnLightsState = false
                    electricsComponent.rightTurnLightsState = false
                }
                CorneringLight.RIGHT_LIGHTS -> {
                    //ElectricsComponent.leftTurnLightsState = false
                    electricsComponent.rightTurnLightsState = !electricsComponent.rightTurnLightsState
                }
                CorneringLight.LEFT_LIGHTS -> {
                    electricsComponent.leftTurnLightsState = !electricsComponent.leftTurnLightsState
                    //ElectricsComponent.rightTurnLightsState = false
                }
            }
        }

        return getDirectionLights()
    }

    fun getDirectionLights() =
        if (!electricsComponent.leftTurnLightsState && !electricsComponent.rightTurnLightsState)
            CorneringLight.STRAIGHT_LIGHTS.name
        else if (electricsComponent.leftTurnLightsState)
            CorneringLight.LEFT_LIGHTS.name
        else if (electricsComponent.rightTurnLightsState)
            CorneringLight.RIGHT_LIGHTS.name
        else
            UNKNOWN_STATE


    fun setMainLightsState(value: String): String {

        showMessage(logger = logger,
            body = "Main Lights State Request: $value")

        val part = if (enumContains<MainLight>(value)) MainLight.valueOf(value) else MainLight.NOTHING
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