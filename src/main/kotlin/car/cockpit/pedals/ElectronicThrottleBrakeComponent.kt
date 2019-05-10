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

    private var reportedCdmState = ModuleState.IDLE

    override fun throttle(direction: String, value: Int): String {
        // check the CDM
        val throttle = checkCdm(direction, value)

        return when (setupComponent.handlingAssistanceState) {
            ASSISTANCE_FULL -> throttleBrake.throttle(direction, throttle)
            ASSISTANCE_WARNING,
            ASSISTANCE_MANUAL -> throttleBrake.throttle(direction, value)
            else -> throttleBrake.throttle(direction, value)
        }
    }

    override fun setNeutral(): String {
        checkCdm(ACTION_NEUTRAL, 0)
        return throttleBrake.setNeutral()
    }

    private fun checkCdm(direction: String, value: Int): Int {
        
        val throttle = cdmComponent.calculateThrottleValue(direction, value)

        val currentCdmState = if (throttle != value) {
                ModuleState.ON
            } else {
                ModuleState.IDLE
            }

        when (setupComponent.handlingAssistanceState) {
            ASSISTANCE_FULL,
            ASSISTANCE_WARNING -> {
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
            ASSISTANCE_FULL -> throttle
            //No actual need to check this condition but do it for completeness!
            ASSISTANCE_WARNING, ASSISTANCE_MANUAL -> value
            else -> value
        }

    }
}