package car.cockpit.pedals

import car.cockpit.dashboard.lights.warning.handling.cdmClientNotifier
import car.ecu.modules.cdm.Cdm
import car.cockpit.engine.nanohttpClientIp
import car.cockpit.engine.nanohttpClientPort
import car.cockpit.setup.*
import car.ecu.Ecu
import car.doNonBlockingRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component("Electronic Throttle -n Brake Component")
class ElectronicThrottleBrakeComponent(@Autowired val throttleBrake: ThrottleBrakeComponent): ThrottleBrake by throttleBrake {

    @Autowired
    private lateinit var setupComponent: Setup

    @Autowired
    private lateinit var cdmComponent: Cdm

    private var reportedCdmState = Ecu.MODULE_IDLE_STATE

    override fun throttle(direction: String, value: Int): String {
        // check the CDM
        val throttle = checkCdm(direction, value)

        return when (setupComponent.handlingAssistanceState) {
            ASSISTANCE_FULL -> throttleBrake.throttle(direction, throttle)
            ASSISTANCE_WARNING,
            ASSISTANCE_NONE -> throttleBrake.throttle(direction, value)
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
                Ecu.MODULE_ON_STATE
            } else {
                Ecu.MODULE_IDLE_STATE
            }

        when (setupComponent.handlingAssistanceState) {
            ASSISTANCE_FULL,
            ASSISTANCE_WARNING -> {
                if (reportedCdmState != currentCdmState) {
                    reportedCdmState = currentCdmState
                    cdmClientNotifier(Ecu.COLLISION_DETECTION_MODULE,
                        reportedCdmState
                    )
                }
            }
        }

        return when (setupComponent.handlingAssistanceState) {
            ASSISTANCE_FULL -> {
                throttle
            }
            ASSISTANCE_WARNING -> {
                value
            }
            ASSISTANCE_NONE -> value
            else -> value
        }

    }
}