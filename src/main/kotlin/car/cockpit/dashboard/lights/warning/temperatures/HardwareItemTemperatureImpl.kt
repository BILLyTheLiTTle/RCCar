package car.cockpit.dashboard.lights.warning.temperatures

import car.cockpit.engine.EngineImpl
import car.cockpit.engine.EngineSystem
import com.pi4j.system.SystemInfo
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.*
import kotlin.math.max

open class HardwareItemTemperatureImpl: Temperature {

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

    override var warning = EngineSystem.EMPTY_STRING
        protected set

    private fun readSensor(): Int {

        fun getSensorValue(): Int {
            return errorTempGenerator
        }

        return when(this) {
            is MotorRearRightTemperatureImpl -> {
                getSensorValue()
            }
            is MotorRearLeftTemperatureImpl -> {
                getSensorValue()
            }
            is MotorFrontRightTemperatureImpl -> {
                getSensorValue()
            }
            is MotorFrontLeftTemperatureImpl -> {
                getSensorValue()
            }
            is HBridgeRearTemperatureImpl -> {
                getSensorValue()
            }
            is HBridgeFrontTemperatureImpl -> {
                getSensorValue()
            }
            is RaspberryPiTemperatureImpl -> {
                val gpuTemp: Int
                val cpuTemp: Int
                if (EngineImpl.RUN_ON_PI) {
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
            is BatteriesTemperatureImpl -> {
                val rearMotorBatterBox = getSensorValue()
                val frontMotorBatteryBox = getSensorValue()
                val stepperMotorBatteryBox = getSensorValue()
                val ledsBatteryBox = getSensorValue()
                max(rearMotorBatterBox,
                    max(frontMotorBatteryBox,
                        max(stepperMotorBatteryBox, ledsBatteryBox)))
            }
            is ShiftRegistersTemperatureImpl -> {
                val mainLightsTemp = getSensorValue()
                val signalLightsTemp = getSensorValue()
                max(mainLightsTemp, signalLightsTemp)
            }
            else -> EngineSystem.EMPTY_INT
        }
    }
}