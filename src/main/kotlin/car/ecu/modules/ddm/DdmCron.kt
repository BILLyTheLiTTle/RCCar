package car.ecu.modules.ddm

/* DDM stands for Driver Detection Module

 */

import car.TYPE_CRITICAL
import car.TYPE_WARNING
import car.cockpit.engine.EngineImpl
import car.cockpit.pedals.ThrottleBrakeImpl
import car.cockpit.engine.EngineController
import car.cockpit.engine.nanohttpClientIp
import car.showMessage
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import java.net.InetAddress


@Configuration
@EnableAsync
@EnableScheduling
class DdmCron {

    private val engineOffState = 1
    private val clientStillOnline = 2
    private val clientCameOnline = 3
    private val clientNotFound = 4
    private val clientStillNotFound = 5

    private var isClientOnline = false
    private var wasClientOnline = false

    private var counter = 0
    private val maxResetCounter = 50

    @Scheduled(initialDelay = 3000, fixedDelay = 400)  // 2 minutes
    fun checkClientStatus(): Int {

        if (EngineImpl.engineState) {
            isClientOnline = try {
                InetAddress.getByName(nanohttpClientIp).isReachable(200)
            } catch (e: Exception) {
                false
            }

            if (isClientOnline) {
                if (wasClientOnline) {
                    if (counter == maxResetCounter) {
                        counter = 0
                        showMessage(title = "CONNECTION CRON JOB",
                            body = "Client is still online\nIP: $nanohttpClientIp")
                    }
                    else {
                        counter++
                    }
                    return clientStillOnline
                }
                else {
                    showMessage(
                        msgType = TYPE_WARNING,
                        title = "CONNECTION CRON JOB",
                        body = "Client came online\nIP: $nanohttpClientIp"
                    )
                    return clientCameOnline
                }
            } else {
                if (wasClientOnline) {
                    ThrottleBrakeImpl.parkingBrake(100)
                    showMessage(msgType = TYPE_CRITICAL,
                        title = "CONNECTION CRON JOB",
                        body = "Client ($nanohttpClientIp) not found." +
                                "Parking brake applied: ${ThrottleBrakeImpl.parkingBrakeState}")
                    return clientNotFound
                } else {
                    if (counter == maxResetCounter) {
                        counter = 0
                        showMessage(msgType = TYPE_CRITICAL,
                            title = "CONNECTION CRON JOB",
                            body = "Client ($nanohttpClientIp) still not found." +
                                    "Parking brake was applied: ${ThrottleBrakeImpl.parkingBrakeState}")
                    } else
                        counter++
                    return clientStillNotFound
                }

            }

        }
        wasClientOnline = isClientOnline
        return engineOffState
    }

}