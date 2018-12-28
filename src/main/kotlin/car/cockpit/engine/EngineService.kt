package car.cockpit.engine

import car.cockpit.pedals.ThrottleBrakeService
import car.cockpit.steering.SteeringService
import car.showMessage
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service("Engine Service")
class EngineService {

    @Autowired
    private lateinit var throttleBrakeService: ThrottleBrakeService

    @Autowired
    private lateinit var steeringService: SteeringService

    @Autowired
    private lateinit var engineComponent: Engine

    fun startEngine(clientIp: String, clientPort: Int): String {

        //reset and get ready for new requests
        throttleBrakeService.lastRequestId = -1
        steeringService.lastRequestId = -1

        nanohttpClientIp = if (clientIp == "0.0.0.0") "10.0.2.15" else clientIp
        nanohttpClientPort = clientPort

        showMessage(title = "ENGINE SYSTEM",
            body = "Engine started\n" +
                    "Controller IP: $nanohttpClientIp\n" +
                    "Controller Port: $nanohttpClientPort")

        return engineComponent.start()
    }

    fun getEngineState() = engineComponent.engineState

    fun stopEngine(): String {
        showMessage(title = "ENGINE SYSTEM",
            body = "Engine stopped\n" +
                    "Controller IP: $nanohttpClientIp\n" +
                    "Controller Port: $nanohttpClientPort")
        return engineComponent.stop()
    }
}

const val UNKNOWN_STATE = "Unknown"
const val EMPTY_STRING = "NULL"
const val EMPTY_INT = -1
const val SUCCESS = "OK"
var nanohttpClientIp = EMPTY_STRING
    private set
var nanohttpClientPort = EMPTY_INT
    private set