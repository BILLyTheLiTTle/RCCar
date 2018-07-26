package car.controllers.advanced.ecu.sensors

import car.controllers.basic.EngineImpl
import java.util.*

object UltrasonicDistanceMeterImpl: UltrasonicDistanceMeter {

    private val ERROR_SPEED_GENERATOR: Double
        get() =  (Random().nextInt(10) + 1) / 10 as Double

    override val frontDistance: Double
        get() = calculateFrontDistance()

    override val rearDistance: Double
        get() = calculateRearDistance()

    @Synchronized
    private fun calculateFrontDistance(): Double {
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

    @Synchronized
    private fun calculateRearDistance(): Double {
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

    private fun calculateFrontDistanceFromEvent(): Double {
        // TODO implement the listener
            // TODO inform the throttle mechanism reactively

        // but for now
        return frontDistance
    }

    private fun calculateRearDistanceFromEvent(): Double {
        // TODO implement the listener
        // TODO inform the throttle mechanism reactively

        // but for now
        return rearDistance
    }
}