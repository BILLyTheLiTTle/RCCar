package car.cockpit.dashboard.lights.warning.temperatures


object BatteriesTemperature: HardwareItemTemperature() {
    override val id = "batteries_temp"
    override val minMediumTemp = 30
    override val maxMediumTemp = 70
}