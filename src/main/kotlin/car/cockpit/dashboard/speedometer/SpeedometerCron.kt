package car.cockpit.dashboard.speedometer


import car.cockpit.engine.EngineImpl
import car.ecu.sensors.speed.SpeedometerImpl
import car.cockpit.engine.EngineSystem
import car.doNonBlockingRequest
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled

@Configuration
@EnableAsync
@EnableScheduling
class SpeedometerCron {

    private val speedUri = "/speed"
    private val paramKeyValue = "value"

    private var primarySpeed = EngineSystem.EMPTY_INT
    private var reportedSpeed = EngineSystem.EMPTY_INT

    @Scheduled(initialDelay = 5000, fixedDelay = 600)
    fun checkSpeed(){
        if (EngineImpl.engineState) {
            primarySpeed = SpeedometerImpl.travelSpeed
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