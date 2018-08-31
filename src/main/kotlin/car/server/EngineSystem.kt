package car.server

import car.controllers.basic.EngineImpl
import car.showMessage
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class EngineSystem {
    @GetMapping("/start_engine")
    fun startEngine(
        @RequestParam(value = "nanohttp_client_ip", defaultValue = EMPTY_STRING) nanohttpClientIp: String,
        @RequestParam(value = "nanohttp_client_port", defaultValue = "$EMPTY_INT") nanohttpClientPort: Int
    ): String {

        //reset and get ready for new requests
        ThrottleBrakeSystem.lastRequestId = -1
        SteeringSystem.lastRequestId = -1

        EngineSystem.nanohttpClientIp = if (nanohttpClientIp == "0.0.0.0") "10.0.2.15" else nanohttpClientIp
        EngineSystem.nanohttpClientPort = nanohttpClientPort

        showMessage(title = "ENGINE SYSTEM",
            body = "Engine started\n" +
                    "Controller IP: $nanohttpClientIp\n" +
                    "Controller Port: $nanohttpClientPort")

        return EngineImpl.start()
    }

    @GetMapping("/get_engine_state")
    fun getEngineState() = EngineImpl.engineState

    @GetMapping("/stop_engine")
    fun stopEngine(): String {
        showMessage(title = "ENGINE SYSTEM",
            body = "Engine stopped\n" +
                    "Controller IP: $nanohttpClientIp\n" +
                    "Controller Port: $nanohttpClientPort")
        return EngineImpl.stop()
    }

    companion object {
        const val UNKNOWN_STATE = "Unknown"
        const val EMPTY_STRING = "NULL"
        const val EMPTY_INT = -1
        const val SUCCESS = "OK"
        var nanohttpClientIp = EMPTY_STRING
            private set
        var nanohttpClientPort = EMPTY_INT
            private set
    }
}