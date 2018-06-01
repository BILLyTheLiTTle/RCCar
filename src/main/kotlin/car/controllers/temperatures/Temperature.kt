package car.controllers.temperatures

import car.server.EngineSystem
import java.util.*
import kotlin.math.max


const val WARNING_TYPE_NOTHING = EngineSystem.EMPTY_STRING
const val WARNING_TYPE_NORMAL = "normal"
const val WARNING_TYPE_MEDIUM = "medium"
const val WARNING_TYPE_HIGH = "high"

interface Temperature {

    val value: Int
    val warning: String

    @Synchronized
    fun readSensor(sensorId: String): Int {
        //Thread.sleep(10000)
        fun getSensorValue(): Int {
            return Random().nextInt(4)
        }
        return when(sensorId) {
            MotorRearRightTemperatureImpl.ID -> {
                getSensorValue()
            }
            MotorRearLeftTemperatureImpl.ID -> {
                getSensorValue()
            }
            MotorFrontRightTemperatureImpl.ID -> {
                getSensorValue()
            }
            MotorFrontLeftTemperatureImpl.ID -> {
                getSensorValue()
            }
            HBridgeRearTemperatureImpl.ID -> {
                getSensorValue()
            }
            HBridgeFrontTemperatureImpl.ID -> {
                getSensorValue()
            }
            RaspberryPiTemperatureImpl.ID -> {
                val gpuTemp = getSensorValue()
                val cpuTemp = getSensorValue()
                max(gpuTemp, cpuTemp)
            }
            else -> EngineSystem.EMPTY_INT
        }
    }
    fun reset(){
        //TODO what?!
    }
}