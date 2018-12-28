package car.cockpit.dashboard.lights.warning.handling

import car.cockpit.engine.nanohttpClientIp
import car.cockpit.engine.nanohttpClientPort
import car.doNonBlockingRequest
import car.ecu.Ecu

fun cdmClientNotifier(hardwareID: String, value: String) {
    doNonBlockingRequest(
        "http://" +
                "$nanohttpClientIp:" +
                "$nanohttpClientPort" +
                Ecu.ECU_URI +
                "?${Ecu.ECU_PARAM_KEY_ITEM}=$hardwareID" +
                "&${Ecu.ECU_PARAM_KEY_VALUE}=$value"
    )
}