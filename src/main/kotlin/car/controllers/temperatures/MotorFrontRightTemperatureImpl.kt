package car.controllers.temperatures


object MotorFrontRightTemperatureImpl: HardwareItemTemperatureImpl() {
    override val ID = "motor_front_right_temp"
    override val MIN_MEDIUM_TEMP = 30
    override val MAX_MEDIUM_TEMP = 70
}