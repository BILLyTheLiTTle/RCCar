package car.cockpit.pedals

import car.ecu.modules.cdm.CdmImpl
import car.cockpit.setup.SetupImpl
import car.cockpit.engine.EngineSystem
import car.cockpit.setup.SetupSystem
import car.ecu.Ecu
import car.doNonBlockingRequest

object ElectronicThrottleBrakeImpl: ThrottleBrake by ThrottleBrakeImpl {

    private var reportedCdmState = Ecu.MODULE_IDLE_STATE

    override fun throttle(direction: String, value: Int): String {
        // check the CDM
        val throttle = checkCdm(direction, value)

        return when (SetupImpl.handlingAssistanceState) {
            SetupSystem.ASSISTANCE_FULL -> ThrottleBrakeImpl.throttle(direction, throttle)
            SetupSystem.ASSISTANCE_WARNING,
            SetupSystem.ASSISTANCE_NONE -> ThrottleBrakeImpl.throttle(direction, value)
            else -> ThrottleBrakeImpl.throttle(direction, value)
        }
    }

    override fun setNeutral(): String {
        checkCdm(ThrottleBrakePedal.ACTION_NEUTRAL, 0)
        return ThrottleBrakeImpl.setNeutral()
    }

    private fun checkCdm(direction: String, value: Int): Int {
        fun cdmInformClient(hardwareID: String, value: String) {
            doNonBlockingRequest(
                "http://" +
                        "${EngineSystem.nanohttpClientIp}:" +
                        "${EngineSystem.nanohttpClientPort}" +
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
            SetupSystem.ASSISTANCE_FULL,
            SetupSystem.ASSISTANCE_WARNING -> {
                if (reportedCdmState != currentCdmState) {
                    reportedCdmState = currentCdmState
                    cdmInformClient(Ecu.COLLISION_DETECTION_MODULE,
                        reportedCdmState
                    )
                }
            }
        }

        return when (SetupImpl.handlingAssistanceState) {
            SetupSystem.ASSISTANCE_FULL -> {
                throttle
            }
            SetupSystem.ASSISTANCE_WARNING -> {
                value
            }
            SetupSystem.ASSISTANCE_NONE -> value
            else -> value
        }

    }
}