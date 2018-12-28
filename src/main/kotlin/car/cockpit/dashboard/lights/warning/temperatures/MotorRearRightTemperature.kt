package car.cockpit.dashboard.lights.warning.temperatures


object MotorRearRightTemperature: HardwareItemTemperature() {
    override val id = "motor_rear_right_temp"
    override val minMediumTemp = 30
    override val maxMediumTemp = 70
}