package car.controllers.temperatures

import car.server.EngineSystem
import java.util.*
import kotlin.math.max

abstract class HardwareItemTemperatureImpl: Temperature {

    open val ID = "parent_of_all_temp"
    protected open val MIN_MEDIUM_TEMP = -1
    protected open val MAX_MEDIUM_TEMP = -1

    override val value: Int
        get() {
            // TODO read the appropriate sensor
            val temp = readSensor()

            warning = when {
                temp < MIN_MEDIUM_TEMP -> WARNING_TYPE_NORMAL
                temp > MAX_MEDIUM_TEMP -> WARNING_TYPE_HIGH
                else -> WARNING_TYPE_MEDIUM
            }

            return temp
        }

    override var warning = EngineSystem.EMPTY_STRING
        protected set

    @Synchronized
    private fun readSensor(): Int {
        //Thread.sleep(10000)

        fun getSensorValue(): Int {
            return Random().nextInt(4)
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
                val gpuTemp = getSensorValue()
                val cpuTemp = getSensorValue()
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
            else -> EngineSystem.EMPTY_INT
        }
    }
}