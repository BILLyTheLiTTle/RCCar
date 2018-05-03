package car.server

import car.controllers.basic.EngineImpl
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class EngineSystem {
    @GetMapping("/start_engine")
    fun startEngine(@RequestParam(value = "nanohttp_client_ip", defaultValue = "$EMPTY_STRING") nanohttpClientIp: String,
                  @RequestParam(value = "nanohttp_client_port", defaultValue = "$EMPTY_INT") nanohttpClientPort: Int)
    : String {
        //reset and get ready for new requests
        ThrottleBrakeSystem.lastRequestId = -1
        SteeringSystem.lastRequestId = -1

        EngineSystem.nanohttpClientIp = nanohttpClientIp
        EngineSystem.nanohttpClientPort = nanohttpClientPort

        println("Engine started\nController IP: $nanohttpClientIp\nController Port: $nanohttpClientPort\n")

        return EngineImpl.start()
    }

    @GetMapping("/get_engine_state")
    fun getEngineState() = EngineImpl.engineState

    @GetMapping("/stop_engine")
    fun stopEngine() = EngineImpl.stop()

    companion object {
        const val EMPTY_STRING = "NULL"
        const val EMPTY_INT = -1
        var nanohttpClientIp = EMPTY_STRING
        var nanohttpClientPort = EMPTY_INT
    }
}