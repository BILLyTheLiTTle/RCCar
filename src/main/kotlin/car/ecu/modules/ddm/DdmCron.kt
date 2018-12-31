package car.ecu.modules.ddm

/* DDM stands for Driver Detection Module

 */

import car.TYPE_CRITICAL
import car.TYPE_WARNING
import car.cockpit.engine.Engine
import car.cockpit.engine.nanohttpClientIp
import car.cockpit.pedals.ThrottleBrake
import car.showMessage
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import java.net.InetAddress


@Configuration
@EnableAsync
@EnableScheduling
class DdmCron {

    @Autowired
    @Qualifier("Throttle -n- Brake Component")
    private lateinit var throttleBrakeComponent: ThrottleBrake

    @Autowired
    private lateinit var engineComponent: Engine

    private val engineOffState = 1
    private val clientStillOnline = 2
    private val clientCameOnline = 3
    private val clientNotFound = 4
    private val clientStillNotFound = 5

    private var isClientOnline = false
    private var wasClientOnline = false

    private var counter = 0
    private val maxResetCounter = 5

    @Scheduled(initialDelay = 3000, fixedDelay = 450)  // 2 minutes
    fun checkClientStatus(): Int {

        if (engineComponent.engineState) {
            isClientOnline = try {
                InetAddress.getByName(nanohttpClientIp).isReachable(200)
            } catch (e: Exception) {
                false
            }

            if (isClientOnline) {
                if (wasClientOnline) {
                    if (counter == maxResetCounter) {
                        counter = 0
                        showMessage(klass = this::class,
                            body = "Client is still online\nIP: $nanohttpClientIp")
                    }
                    else {
                        counter++
                    }
                    wasClientOnline = isClientOnline
                    return clientStillOnline
                }
                else {
                    showMessage(
                        msgType = TYPE_WARNING,
                        klass = this::class,
                        body = "Client came online\nIP: $nanohttpClientIp"
                    )
                    wasClientOnline = isClientOnline
                    return clientCameOnline
                }
            }
            else {
                if (wasClientOnline) {
                    throttleBrakeComponent.parkingBrake(100)
                    showMessage(msgType = TYPE_CRITICAL,
                        klass = this::class,
                        body = "Client ($nanohttpClientIp) not found." +
                                "Parking brake applied: ${throttleBrakeComponent.parkingBrakeState}")
                    wasClientOnline = isClientOnline
                    return clientNotFound
                }
                else {
                    if (counter == maxResetCounter) {
                        counter = 0
                        showMessage(msgType = TYPE_CRITICAL,
                            klass = this::class,
                            body = "Client ($nanohttpClientIp) still not found." +
                                    "Parking brake was applied: ${throttleBrakeComponent.parkingBrakeState}")
                    }
                    else {
                        counter++
                    }
                    wasClientOnline = isClientOnline
                    return clientStillNotFound
                }
            }
        }
        return engineOffState
    }
}

const val DRIVER_DETECTION_MODULE = "DDM"