package car.controllers.advanced

import car.controllers.advanced.ecu.CdmImpl
import car.controllers.advanced.ecu.Ecu
import car.controllers.basic.SetupImpl
import car.controllers.basic.ThrottleBrake
import car.controllers.basic.ThrottleBrakeImpl
import car.server.EngineSystem
import car.server.SetupSystem
import car.server.doNonBlockingRequest

object ElectronicThrottleBrakeImpl: ThrottleBrake by ThrottleBrakeImpl{

    override fun throttle(direction: String, value: Int): String {
        // check the CDM
        val throttle = CdmImpl.calculateThrottleValue(direction, value)

        return when (SetupImpl.handlingAssistanceState) {
            SetupSystem.ASSISTANCE_FULL -> {
                if (CdmImpl.isActive) {
                    cdmInformClient(Ecu.COLLISION_DETECTION_MODULE, Ecu.MODULE_ON_STATE)
                }
                else {
                    cdmInformClient(Ecu.COLLISION_DETECTION_MODULE, Ecu.MODULE_IDLE_STATE)
                }
                ThrottleBrakeImpl.throttle(direction, throttle)
            }
            SetupSystem.ASSISTANCE_WARNING -> {
                if (CdmImpl.isActive) {
                    cdmInformClient(Ecu.COLLISION_DETECTION_MODULE, Ecu.MODULE_ON_STATE)
                }
                else {
                    cdmInformClient(Ecu.COLLISION_DETECTION_MODULE, Ecu.MODULE_IDLE_STATE)
                }
                ThrottleBrakeImpl.throttle(direction, value)
            }
            SetupSystem.ASSISTANCE_NONE -> ThrottleBrakeImpl.throttle(direction, value)
            else -> ThrottleBrakeImpl.throttle(direction, value)
        }
    }

    private fun cdmInformClient(hardwareID: String, value: String){
        doNonBlockingRequest(
            "http://" +
                    "${EngineSystem.nanohttpClientIp}:" +
                    "${EngineSystem.nanohttpClientPort + 1}" +
                    Ecu.ECU_URI +
                    "?${Ecu.ECU_PARAM_KEY_ITEM}=$hardwareID" +
                    "&${Ecu.ECU_PARAM_KEY_VALUE}=$value"
        )
    }
}