package car.cockpit.dashboard.lights.warning.temperatures


object MotorFrontRightTemperature: HardwareItemTemperature() {
    override val id = "motor_front_right_temp"
    override val minMediumTemp = 30
    override val maxMediumTemp = 70
}