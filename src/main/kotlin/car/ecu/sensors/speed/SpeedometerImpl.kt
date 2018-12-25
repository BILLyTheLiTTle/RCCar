package car.ecu.sensors.speed

import java.util.*

object SpeedometerImpl: Speedometer {

    private val DUMMY_SPEED_GENERATOR
        get() =  Random().nextInt(200)

    override val value: Int
        get() = calculateSpeed()

    private fun calculateSpeed(): Int {
        var totalSpeed = 0
        for (i in 0..3)
            totalSpeed += DUMMY_SPEED_GENERATOR
        return totalSpeed/4
    }
}