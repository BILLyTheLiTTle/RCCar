package car.controllers.temperatures

import car.controllers.basic.EngineImpl
import car.server.EngineSystem
import com.pi4j.system.SystemInfo
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.*
import kotlin.math.max

abstract class HardwareItemTemperatureImpl: Temperature {

    private val lock = Any()

    open val ID = "parent_of_all_temp"
    protected open val MIN_MEDIUM_TEMP = -1
    protected open val MAX_MEDIUM_TEMP = -1

    private val ERROR_TEMP = 1000
    private val ERROR_TEMP_GENERATOR
        get() =  Random().nextInt(100)

    override val value: Int
        get() {
            // I need synchronization because I use multiplexers
            val temp = synchronized(lock) { readSensor() }

            warning = when {
                temp < MIN_MEDIUM_TEMP -> WARNING_TYPE_NORMAL
                temp > MAX_MEDIUM_TEMP -> WARNING_TYPE_HIGH
                else -> WARNING_TYPE_MEDIUM
            }

            return temp
        }

    override var warning = EngineSystem.EMPTY_STRING
        protected set

    private fun readSensor(): Int {

        fun getSensorValue(): Int {
            return ERROR_TEMP_GENERATOR
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
                var gpuTemp: Int
                var cpuTemp: Int
                if (EngineImpl.RUN_ON_PI) {
                    cpuTemp = try {
                        SystemInfo.getCpuTemperature().toInt()
                    } catch (e: Exception) {
                        ERROR_TEMP
                    }

                    gpuTemp = try {
                        val args = arrayOf("vcgencmd", "measure_temp")
                        val proc = ProcessBuilder(*args).start()
                        // Read the output
                        val reader = BufferedReader(InputStreamReader(proc.inputStream))
                        var line = reader.readLine()
                        line.subSequence(5, 7).toString().toInt()
                    }
                    catch (e: Exception){ERROR_TEMP}
                }
                else {
                    gpuTemp = ERROR_TEMP_GENERATOR
                    cpuTemp = ERROR_TEMP_GENERATOR
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