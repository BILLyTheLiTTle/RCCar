package car.cockpit.dashboard.indicators.temperatures

import car.cockpit.engine.EMPTY_STRING
import car.cockpit.engine.Engine
import com.pi4j.system.SystemInfo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.io.BufferedReader
import java.io.InputStreamReader
import kotlin.math.max

@Component("Raspberry Pi Temperature Component")
class RaspberryPiTemperatureComponent: Temperature {

    @Autowired
    private lateinit var engineComponent: Engine

    override val id = TemperatureDevice.RASPBERRY_PI_TEMP
    /* Declared temps:
        -40 < cpu temp < 85
        ? < gpu temp < ?
     */
    override val minMediumTemp = 50
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
        val gpuTemp: Int
        val cpuTemp: Int
        if (engineComponent.runOnPi) {
            cpuTemp = try {
                SystemInfo.getCpuTemperature().toInt()
            } catch (e: Exception) {
                errorTemp
            }

            gpuTemp = try {
                val args = arrayOf("vcgencmd", "measure_temp")
                val proc = ProcessBuilder(*args).start()
                // Read the output
                val reader = BufferedReader(InputStreamReader(proc.inputStream))
                val line = reader.readLine()
                line.subSequence(5, 7).toString().toInt()
            }
            catch (e: Exception){errorTemp}
        }
        else {
            gpuTemp = errorTempGenerator
            cpuTemp = errorTempGenerator
        }
        return max(gpuTemp, cpuTemp)
    }
}