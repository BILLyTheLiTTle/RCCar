package car.ecu.sensors.speed

import org.springframework.stereotype.Component
import java.util.*

@Component("Speedometer Sensor Component")
class SpeedometerComponent: Speedometer {

    private val DUMMY_SPEED_GENERATOR
        get() =  Random().nextInt(200)

    override val travelSpeed: Int
        get() = calculateSpeed()

    private fun calculateSpeed(): Int {
        var totalSpeed = 0
        for (i in 0..3)
            totalSpeed += DUMMY_SPEED_GENERATOR
        return totalSpeed/4
    }
}