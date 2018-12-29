package car.cockpit.engine

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class EngineController {

    @Autowired
    private lateinit var service: EngineService

    @GetMapping("/start_engine")
    fun startEngine(
        @RequestParam(value = "nanohttp_client_ip", defaultValue = EMPTY_STRING) nanohttpClientIp: String,
        @RequestParam(value = "nanohttp_client_port", defaultValue = "$EMPTY_INT") nanohttpClientPort: Int
    ): String = service.startEngine(nanohttpClientIp, nanohttpClientPort)

    @GetMapping("/get_engine_state")
    fun getEngineState() = service.getEngineState()

    @GetMapping("/stop_engine")
    fun stopEngine() = service.stopEngine()
}