package car.controllers.temperatures


object RaspberryPiTemperatureImpl: HardwareItemTemperatureImpl() {
    override val ID = "raspberry_pi_temp"
    /* Declared temps:
        -40 < cpu temp < 85
        ? < gpu temp < ?
     */
    override val MIN_MEDIUM_TEMP = 50
    override val MAX_MEDIUM_TEMP = 70
}