package car.cockpit.dashboard.lights.warning.temperatures

import car.cockpit.engine.EMPTY_INT
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

    override val id = "raspberry_pi_temp"
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
                temp < minMediumTemp -> WARNING_TYPE_NORMAL
                temp > maxMediumTemp -> WARNING_TYPE_HIGH
                else -> WARNING_TYPE_MEDIUM
            }

            return temp
        }

    override var warning = EMPTY_STRING
        protected set

    private fun readSensor(): Int {
        val gpuTemp: Int
        val cpuTemp: Int
        if (engineComponent.RunOnPi) {
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