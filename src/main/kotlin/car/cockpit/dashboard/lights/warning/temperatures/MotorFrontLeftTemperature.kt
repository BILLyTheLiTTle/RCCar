package car.cockpit.dashboard.lights.warning.temperatures


object MotorFrontLeftTemperature: HardwareItemTemperature() {
    override val id = "motor_front_left_temp"
    override val minMediumTemp = 30
    override val maxMediumTemp = 70
}