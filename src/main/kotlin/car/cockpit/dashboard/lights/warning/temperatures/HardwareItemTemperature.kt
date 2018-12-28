package car.cockpit.dashboard.lights.warning.temperatures

import car.cockpit.engine.*
import com.pi4j.system.SystemInfo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.*
import kotlin.math.max

open class HardwareItemTemperature: Temperature {

    @Autowired
    private lateinit var engineComponent: Engine

    private val lock = Any()

    open val id = "parent_of_all_hardware_temp"
    protected open val minMediumTemp = -1
    protected open val maxMediumTemp = -1

    private val errorTemp = 1000
    private val errorTempGenerator
        get() =  Random().nextInt(100)

    override val value: Int
        get() {
            // I need synchronization because I use multiplexers
            val temp = synchronized(lock) { readSensor() }

            warning = when {
                temp < minMediumTemp -> Temperature.WARNING_TYPE_NORMAL
                temp > maxMediumTemp -> Temperature.WARNING_TYPE_HIGH
                else -> Temperature.WARNING_TYPE_MEDIUM
            }

            return temp
        }

    override var warning = EMPTY_STRING
        protected set

    private fun readSensor(): Int {

        fun getSensorValue(): Int {
            return errorTempGenerator
        }

        return when(this) {
            is MotorRearRightTemperature -> {
                getSensorValue()
            }
            is MotorRearLeftTemperature -> {
                getSensorValue()
            }
            is MotorFrontRightTemperature -> {
                getSensorValue()
            }
            is MotorFrontLeftTemperature -> {
                getSensorValue()
            }
            is HBridgeRearTemperature -> {
                getSensorValue()
            }
            is HBridgeFrontTemperature -> {
                getSensorValue()
            }
            is RaspberryPiTemperature -> {
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
                max(gpuTemp, cpuTemp)
            }
            is BatteriesTemperature -> {
                val rearMotorBatteryBox = getSensorValue()
                val frontMotorBatteryBox = getSensorValue()
                val stepperMotorBatteryBox = getSensorValue()
                val ledsBatteryBox = getSensorValue()
                max(rearMotorBatteryBox,
                    max(frontMotorBatteryBox,
                        max(stepperMotorBatteryBox, ledsBatteryBox)))
            }
            is ShiftRegistersTemperature -> {
                val mainLightsTemp = getSensorValue()
                val signalLightsTemp = getSensorValue()
                max(mainLightsTemp, signalLightsTemp)
            }
            else -> EMPTY_INT
        }
    }
}