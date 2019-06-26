package car.cockpit.dashboard.indicators.temperatures

import java.util.*

interface Temperature {

    val id: TemperatureDevice
    val value: Int
    val warning: String

    val minMediumTemp
        get() = -1
    val maxMediumTemp
        get() = -1

    val errorTemp
        get() = 1000
    val errorTempGenerator
        get() =  Random().nextInt(100)

    fun reset(){
        //TODO what?!
    }
}

val lock = Any()

enum class TemperatureDevice {
    BATTERIES_TEMP,
    H_BRIDGE_FRONT_TEMP, H_BRIDGE_REAR_TEMP,
    MOTOR_FRONT_LEFT_TEMP, MOTOR_FRONT_RIGHT_TEMP, MOTOR_REAR_LEFT_TEMP, MOTOR_REAR_RIGHT_TEMP,
    RASPBERRY_PI_TEMP,
    SHIFT_REGISTERS_TEMP
}

enum class TemperatureWarning {
    NOTHING_TEMPERATURE, UNCHANGED_TEMPERATURE, NORMAL_TEMPERATURE, MEDIUM_TEMPERATURE, HIGH_TEMPERATURE
}