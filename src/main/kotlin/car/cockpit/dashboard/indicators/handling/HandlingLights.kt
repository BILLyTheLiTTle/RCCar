package car.cockpit.dashboard.indicators.handling

import car.cockpit.engine.nanohttpClientIp
import car.cockpit.engine.nanohttpClientPort
import car.launchRequest
import car.ecu.ECU_PARAM_KEY_ITEM
import car.ecu.ECU_PARAM_KEY_VALUE
import car.ecu.ECU_URI

fun cdmClientNotifier(hardwareID: String, value: String) {
    launchRequest(
        "http://" +
                "$nanohttpClientIp:" +
                "$nanohttpClientPort" +
                ECU_URI +
                "?$ECU_PARAM_KEY_ITEM=$hardwareID" +
                "&$ECU_PARAM_KEY_VALUE=$value"
    )
}