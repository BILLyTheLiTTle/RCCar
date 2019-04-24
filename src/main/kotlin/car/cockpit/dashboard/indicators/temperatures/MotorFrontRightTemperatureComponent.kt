package car.cockpit.dashboard.indicators.temperatures

import car.cockpit.engine.EMPTY_STRING
import org.springframework.stereotype.Component

@Component("Motor Front Right Temperature Component")
class MotorFrontRightTemperatureComponent: Temperature {

    override val id = ThermometerDevice.MOTOR_FRONT_RIGHT
    override val minMediumTemp = 30
    override val maxMediumTemp = 70

    override val value: Int
        get() {
            /* I need synchronization on each subclass because I use multiplexers
                that's why I chose to lock a static variable*/
            val temp = synchronized(lock) { readSensor() }

            warning = when {
                temp < minMediumTemp -> TemperatureWarningType.NORMAL.name
                temp > maxMediumTemp -> TemperatureWarningType.HIGH.name
                else -> TemperatureWarningType.MEDIUM.name
            }

            return temp
        }

    override var warning = EMPTY_STRING
        protected set

    private fun readSensor(): Int {
        return errorTempGenerator
    }
}