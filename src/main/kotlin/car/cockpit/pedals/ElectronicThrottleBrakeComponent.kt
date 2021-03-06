package car.cockpit.pedals

import car.cockpit.dashboard.indicators.handling.cdmClientNotifier
import car.ecu.modules.cdm.Cdm
import car.cockpit.setup.*
import car.ecu.Module
import car.ecu.ModuleState
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component

@Component("Electronic Throttle -n Brake Component")
class ElectronicThrottleBrakeComponent(
    @Autowired
    @Qualifier("Throttle -n- Brake Component")
    val throttleBrake: ThrottleBrake
): ThrottleBrake by throttleBrake {

    @Autowired
    private lateinit var setupComponent: Setup

    @Autowired
    private lateinit var cdmComponent: Cdm

    private var reportedCdmState = ModuleState.IDLE_STATE

    override fun throttle(direction: Motion, value: Int): String {
        // check the CDM here also because the car could be still and accidentally throttled
        val throttle = checkCdm(direction, value)

        return when (setupComponent.handlingAssistanceState) {
            HandlingAssistance.FULL -> throttleBrake.throttle(direction, throttle)
            HandlingAssistance.WARNING,
            HandlingAssistance.MANUAL -> throttleBrake.throttle(direction, value)
            else -> throttleBrake.throttle(direction, value)
        }
    }

    override fun setNeutral(): String {
        checkCdm(Motion.NEUTRAL, 0)
        return throttleBrake.setNeutral()
    }

    private fun checkCdm(direction: Motion, value: Int): Int {

        val throttle = cdmComponent.calculateThrottleValue(direction, value)

        val currentCdmState = if (throttle != value) {
                ModuleState.ON_STATE
            } else {
                ModuleState.IDLE_STATE
            }

        when (setupComponent.handlingAssistanceState) {
            HandlingAssistance.FULL,
            HandlingAssistance.WARNING -> {
                if (reportedCdmState != currentCdmState) {
                    reportedCdmState = currentCdmState
                    cdmClientNotifier(
                        Module.COLLISION_DETECTION.name,
                        reportedCdmState.name
                    )
                }
            }
        }

        return when (setupComponent.handlingAssistanceState) {
            HandlingAssistance.FULL -> throttle
            //No actual need to check this condition but do it for completeness!
            HandlingAssistance.WARNING, HandlingAssistance.MANUAL -> value
            else -> value
        }

    }
}