package car.cockpit.dashboard.indicators.temperatures

import car.cockpit.engine.EMPTY_STRING
import java.util.*

interface Temperature {

    val id: ThermometerDevice
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

enum class ThermometerDevice(val id: String) {
    BATTERIES("batteries_temp"),
    H_BRIDGE_FRONT("h_bridge_front_temp"),
    H_BRIDGE_REAR("h_bridge_rear_temp"),
    MOTOR_FRONT_LEFT("motor_front_left_temp"),
    MOTOR_FRONT_RIGHT("motor_front_right_temp"),
    MOTOR_REAR_LEFT("motor_rear_left_temp"),
    MOTOR_REAR_RIGHT("motor_rear_right_temp"),
    RASPBERRY_PI("raspberry_pi_temp"),
    SHIFT_REGISTERS("shift_registers_temp")
}

enum class TemperatureWarningType(val id: String) {
    NOTHING(EMPTY_STRING),
    UNCHANGED("unchanged"),
    NORMAL("normal"),
    MEDIUM("medium"),
    HIGH("high")
}