package car.cockpit.pedals

import car.ecu.modules.cdm.CdmImpl
import car.cockpit.engine.nanohttpClientIp
import car.cockpit.engine.nanohttpClientPort
import car.cockpit.setup.*
import car.ecu.Ecu
import car.doNonBlockingRequest

object ElectronicThrottleBrakeImpl: ThrottleBrake by ThrottleBrakeImpl {

    private var reportedCdmState = Ecu.MODULE_IDLE_STATE

    override fun throttle(direction: String, value: Int): String {
        // check the CDM
        val throttle = checkCdm(direction, value)

        return when (SetupImpl.handlingAssistanceState) {
            ASSISTANCE_FULL -> ThrottleBrakeImpl.throttle(direction, throttle)
            ASSISTANCE_WARNING,
            ASSISTANCE_NONE -> ThrottleBrakeImpl.throttle(direction, value)
            else -> ThrottleBrakeImpl.throttle(direction, value)
        }
    }

    override fun setNeutral(): String {
        checkCdm(ACTION_NEUTRAL, 0)
        return ThrottleBrakeImpl.setNeutral()
    }

    private fun checkCdm(direction: String, value: Int): Int {
        fun cdmInformClient(hardwareID: String, value: String) {
            doNonBlockingRequest(
                "http://" +
                        "$nanohttpClientIp:" +
                        "$nanohttpClientPort" +
                        Ecu.ECU_URI +
                        "?${Ecu.ECU_PARAM_KEY_ITEM}=$hardwareID" +
                        "&${Ecu.ECU_PARAM_KEY_VALUE}=$value"
            )
        }

        val throttle = CdmImpl.calculateThrottleValue(direction, value)

        val currentCdmState = if (throttle != value) {
                Ecu.MODULE_ON_STATE
            } else {
                Ecu.MODULE_IDLE_STATE
            }

        when (SetupImpl.handlingAssistanceState) {
            ASSISTANCE_FULL,
            ASSISTANCE_WARNING -> {
                if (reportedCdmState != currentCdmState) {
                    reportedCdmState = currentCdmState
                    cdmInformClient(Ecu.COLLISION_DETECTION_MODULE,
                        reportedCdmState
                    )
                }
            }
        }

        return when (SetupImpl.handlingAssistanceState) {
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