package car.cockpit.dashboard.lights.warning.temperatures


object MotorRearLeftTemperature: HardwareItemTemperature() {
    override val id = "motor_rear_left_temp"
    override val minMediumTemp = 30
    override val maxMediumTemp = 70
}