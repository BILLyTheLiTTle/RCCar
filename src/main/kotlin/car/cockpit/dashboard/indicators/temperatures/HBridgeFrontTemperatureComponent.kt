package car.cockpit.dashboard.indicators.temperatures

import car.cockpit.engine.EMPTY_STRING
import org.springframework.stereotype.Component

@Component("H-Bridge Front Temperature Component")
class HBridgeFrontTemperatureComponent: Temperature {

    override val id = TemperatureDevice.H_BRIDGE_FRONT_TEMP
    override val minMediumTemp = 30
    override val maxMediumTemp = 70

    override val value: Int
        get() {
            /* I need synchronization on each subclass because I use multiplexers
                that's why I chose to lock a static variable*/
            val temp = synchronized(lock) { readSensor() }

            warning = when {
                temp < minMediumTemp -> TemperatureWarning.NORMAL_TEMPERATURE.name
                temp > maxMediumTemp -> TemperatureWarning.HIGH_TEMPERATURE.name
                else -> TemperatureWarning.MEDIUM_TEMPERATURE.name
            }

            return temp
        }

    override var warning = EMPTY_STRING
        protected set

    private fun readSensor(): Int {
        return errorTempGenerator
    }
}