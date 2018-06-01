package car.controllers.temperatures


object MotorRearLeftTemperatureImpl: TemperatureImpl() {
    override val ID = "motor_rear_left_temp"
    override val MIN_MEDIUM_TEMP = 1
    override val MAX_MEDIUM_TEMP = 2
}