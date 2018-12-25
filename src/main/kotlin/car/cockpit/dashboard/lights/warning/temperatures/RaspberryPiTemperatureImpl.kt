package car.cockpit.dashboard.lights.warning.temperatures


object RaspberryPiTemperatureImpl: HardwareItemTemperatureImpl() {
    override val id = "raspberry_pi_temp"
    /* Declared temps:
        -40 < cpu temp < 85
        ? < gpu temp < ?
     */
    override val minMediumTemp = 50
    override val maxMediumTemp = 70
}