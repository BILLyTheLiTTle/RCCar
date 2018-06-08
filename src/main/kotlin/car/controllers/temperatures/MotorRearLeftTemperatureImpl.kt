package car.controllers.temperatures


object MotorRearLeftTemperatureImpl: HardwareItemTemperatureImpl() {
    override val ID = "motor_rear_left_temp"
    override val MIN_MEDIUM_TEMP = 30
    override val MAX_MEDIUM_TEMP = 70
}