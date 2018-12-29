package car.cockpit.dashboard.lights.warning.temperatures

import car.cockpit.engine.EMPTY_STRING
import org.springframework.stereotype.Component
import kotlin.math.max

@Component("Shift Registers Temperature Component")
class ShiftRegistersTemperatureComponent: Temperature {

    override val id = "shift_registers_temp"
    override val minMediumTemp = 30
    override val maxMediumTemp = 70

    override val value: Int
        get() {
            /* I need synchronization on each subclass because I use multiplexers
                that's why I chose to lock a static variable*/
            val temp = synchronized(lock) { readSensor() }

            warning = when {
                temp < minMediumTemp -> WARNING_TYPE_NORMAL
                temp > maxMediumTemp -> WARNING_TYPE_HIGH
                else -> WARNING_TYPE_MEDIUM
            }

            return temp
        }

    override var warning = EMPTY_STRING
        protected set

    private fun readSensor(): Int {
        val mainLightsTemp = errorTempGenerator
        val signalLightsTemp = errorTempGenerator
        return max(mainLightsTemp, signalLightsTemp)
    }
}