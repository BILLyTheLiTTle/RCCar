package car.controllers.temperatures


object MotorFrontLeftTemperatureImpl: HardwareItemTemperatureImpl() {
    override val id = "motor_front_left_temp"
    override val minMediumTemp = 30
    override val maxMediumTemp = 70
}