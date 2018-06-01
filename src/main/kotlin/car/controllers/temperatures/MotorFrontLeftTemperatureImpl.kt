package car.controllers.temperatures


object MotorFrontLeftTemperatureImpl: HardwareItemTemperatureImpl() {
    override val ID = "motor_front_left_temp"
    override val MIN_MEDIUM_TEMP = 1
    override val MAX_MEDIUM_TEMP = 2
}