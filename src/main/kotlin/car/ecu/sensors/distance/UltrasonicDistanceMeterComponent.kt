package car.ecu.sensors.distance

import car.cockpit.engine.Engine
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.*

@Component("Ultrasonic Distance Meter Sensor Component")
class UltrasonicDistanceMeterComponent: UltrasonicDistanceMeter {

    @Autowired
    private lateinit var engineComponent: Engine

    private val DUMMY_DISTANCE_GENERATOR: Double
        get() =  ((Random().nextInt(10) + 1)).toDouble() / 10

    override val frontDistance: Double
        get() = calculateFrontDistance()

    override val rearDistance: Double
        get() = calculateRearDistance()

    @Synchronized
    private fun calculateFrontDistance(): Double {
        return if(engineComponent.RunOnPi) {
            // TODO measure distance according to sensor
            DUMMY_DISTANCE_GENERATOR
        }
        else {
            // return a dummy distance
            DUMMY_DISTANCE_GENERATOR
        }
    }

    @Synchronized
    private fun calculateRearDistance(): Double {
        return if(engineComponent.RunOnPi) {
            // TODO measure distance according to sensor
            DUMMY_DISTANCE_GENERATOR
        }
        else {
            // return a dummy distance
            DUMMY_DISTANCE_GENERATOR
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