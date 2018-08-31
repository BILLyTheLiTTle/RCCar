package car.controllers.temperatures


object MotorRearLeftTemperatureImpl: HardwareItemTemperatureImpl() {
    override val id = "motor_rear_left_temp"
    override val minMediumTemp = 30
    override val maxMediumTemp = 70
}