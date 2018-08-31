package car.server.cron


import car.controllers.basic.EngineImpl
import car.controllers.basic.sensors.SpeedometerImpl
import car.server.EngineSystem
import car.server.doNonBlockingRequest
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled

@Configuration
@EnableAsync
@EnableScheduling
class SpeedometerCronJob {

    private val speedUri = "/speed"
    private val paramKeyValue = "value"

    private var primarySpeed = EngineSystem.EMPTY_INT
    private var reportedSpeed = EngineSystem.EMPTY_INT

    @Scheduled(initialDelay = 5000, fixedDelay = 600)
    fun checkSpeed(){
        if (EngineImpl.engineState) {
            primarySpeed = SpeedometerImpl.value
            if (primarySpeed != reportedSpeed) {
                reportedSpeed = primarySpeed
                informClient(reportedSpeed)
            }
        }
    }

    private fun informClient(value: Int){
        doNonBlockingRequest(
            "http://" +
                    "${EngineSystem.nanohttpClientIp}:" +
                    "${EngineSystem.nanohttpClientPort}" +
                    speedUri +
                    "?$paramKeyValue=$value"
        )
    }
}