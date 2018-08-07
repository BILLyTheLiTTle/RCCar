package car.server.cron

import car.TYPE_CRITICAL
import car.TYPE_WARNING
import car.controllers.basic.EngineImpl
import car.controllers.basic.ThrottleBrakeImpl
import car.server.EngineSystem
import car.showMessage
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import java.net.InetAddress
import java.net.SocketException
import java.net.UnknownHostException


@Configuration
@EnableAsync
@EnableScheduling
class ConnectionCronJob {

    private var isClientOnline = false
    private var wasClientOnline = false

    private var counter = 0
    private val maxResetCounter = 50

    @Scheduled(initialDelay = 3000, fixedDelay = 400)  // 2 minutes
    fun checkClientStatus(): Int {

        wasClientOnline = isClientOnline

        if (EngineImpl.engineState) {
            isClientOnline = try {
                InetAddress.getByName(EngineSystem.nanohttpClientIp).isReachable(200)
            } catch (uhe: UnknownHostException) {
                false
            } catch (se: SocketException) {
                false
            }

            if (isClientOnline) {
                if (wasClientOnline) {
                    if (counter == maxResetCounter) {
                        counter = 0
                        showMessage(title = "CONNECTION CRON JOB",
                            body = "Client is still online\nIP: ${EngineSystem.nanohttpClientIp}")
                    }
                    else {
                        counter++
                    }
                    return CLIENT_STILL_ONLINE
                }
                else {
                    showMessage(
                        msgType = TYPE_WARNING,
                        title = "CONNECTION CRON JOB",
                        body = "Client came online\nIP: ${EngineSystem.nanohttpClientIp}"
                    )
                    return CLIENT_CAME_ONLINE
                }
            } else {
                if (wasClientOnline) {
                    ThrottleBrakeImpl.parkingBrake(100)
                    showMessage(msgType = TYPE_CRITICAL,
                        title = "CONNECTION CRON JOB",
                        body = "Client (${EngineSystem.nanohttpClientIp}) not found." +
                                "Parking brake applied: ${ThrottleBrakeImpl.parkingBrakeState}")
                    return CLIENT_NOT_FOUND
                } else {
                    if (counter == maxResetCounter) {
                        counter = 0
                        showMessage(msgType = TYPE_CRITICAL,
                            title = "CONNECTION CRON JOB",
                            body = "Client (${EngineSystem.nanohttpClientIp}) still not found." +
                                    "Parking brake was applied: ${ThrottleBrakeImpl.parkingBrakeState}")
                    } else
                        counter++
                    return CLIENT_STILL_NOT_FOUND
                }

            }

        }
        return ENGINE_OFF_STATE
    }


    companion object {
        private const val ENGINE_OFF_STATE = 0
        private const val CLIENT_STILL_ONLINE = 1
        private const val CLIENT_CAME_ONLINE = 2
        private const val CLIENT_NOT_FOUND = 3
        private const val CLIENT_STILL_NOT_FOUND = 4
    }

}