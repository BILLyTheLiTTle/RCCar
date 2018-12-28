package car.cockpit.dashboard.speedometer


import car.cockpit.engine.*
import car.doNonBlockingRequest
import car.ecu.sensors.speed.Speedometer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled

@Configuration
@EnableAsync
@EnableScheduling
class SpeedometerCron {

    @Autowired
    private lateinit var speedometerComponent: Speedometer

    @Autowired
    private lateinit var engineComponent: Engine

    private val speedUri = "/speed"
    private val paramKeyValue = "value"

    private var primarySpeed = EMPTY_INT
    private var reportedSpeed = EMPTY_INT

    @Scheduled(initialDelay = 5000, fixedDelay = 600)
    fun checkSpeed(){
        if (engineComponent.engineState) {
            primarySpeed = speedometerComponent.travelSpeed
            if (primarySpeed != reportedSpeed) {
                reportedSpeed = primarySpeed
                informClient(reportedSpeed)
            }
        }
    }

    private fun informClient(value: Int){
        doNonBlockingRequest(
            "http://" +
                    "$nanohttpClientIp:" +
                    "$nanohttpClientPort" +
                    speedUri +
                    "?$paramKeyValue=$value"
        )
    }
}