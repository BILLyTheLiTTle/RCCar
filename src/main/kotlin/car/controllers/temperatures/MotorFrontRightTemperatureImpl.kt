package car.controllers.temperatures


object MotorFrontRightTemperatureImpl: HardwareItemTemperatureImpl() {
    override val id = "motor_front_right_temp"
    override val minMediumTemp = 30
    override val maxMediumTemp = 70
}