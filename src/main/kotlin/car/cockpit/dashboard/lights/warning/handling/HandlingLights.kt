package car.cockpit.dashboard.lights.warning.handling

import car.cockpit.engine.nanohttpClientIp
import car.cockpit.engine.nanohttpClientPort
import car.doNonBlockingRequest
import car.ecu.ECU_PARAM_KEY_ITEM
import car.ecu.ECU_PARAM_KEY_VALUE
import car.ecu.ECU_URI
import car.ecu.Ecu

fun cdmClientNotifier(hardwareID: String, value: String) {
    doNonBlockingRequest(
        "http://" +
                "$nanohttpClientIp:" +
                "$nanohttpClientPort" +
                ECU_URI +
                "?$ECU_PARAM_KEY_ITEM=$hardwareID" +
                "&$ECU_PARAM_KEY_VALUE=$value"
    )
}