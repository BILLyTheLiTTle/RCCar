package car.server

import car.controllers.basic.EngineImpl
import kotlinx.coroutines.experimental.runBlocking
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class EngineSystem {
    @GetMapping("/handshake")
    fun handshake(@RequestParam(value = "nanohttp_client_ip", defaultValue = "$EMPTY_STRING") nanohttpClientIp: String,
                  @RequestParam(value = "nanohttp_client_port", defaultValue = "$EMPTY_INT") nanohttpClientPort: Int)
    : String {
        EngineSystem.nanohttpClientIp = nanohttpClientIp
        EngineSystem.nanohttpClientPort = nanohttpClientPort

        var returnMsg = EMPTY_STRING
        runBlocking<Unit> {
            returnMsg = EngineImpl.start().first
            }

        return returnMsg
    }

    companion object {
        const val EMPTY_STRING = "NULL"
        const val EMPTY_INT = -1
        var nanohttpClientIp = EMPTY_STRING
        var nanohttpClientPort = EMPTY_INT
    }
}