package car.controllers.advanced.ecu.sensors

import car.controllers.basic.EngineImpl
import java.util.*

object UltrasonicDistanceMeterImpl: UltrasonicDistanceMeter {

    private val ERROR_SPEED_GENERATOR: Double
        get() =  (Random().nextInt(10) + 1) / 10 as Double

    override val value: Double
        get() = calculateDistance()

    @Synchronized
    private fun calculateDistance(): Double {
        return if(EngineImpl.RUN_ON_PI) {
            // TODO measure distance according to sensor
            ERROR_SPEED_GENERATOR
        }
        else {
            // return a dummy distance
            Thread.sleep(500)
            ERROR_SPEED_GENERATOR
        }
    }

    private fun calculateDistanceFromEvent(): Double {
        // TODO implement the listener
            // TODO inform the throttle mechanism reactively

        // but for now
        return value
    }
}