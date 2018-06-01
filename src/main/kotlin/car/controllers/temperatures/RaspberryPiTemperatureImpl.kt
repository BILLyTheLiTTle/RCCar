package car.controllers.temperatures


object RaspberryPiTemperatureImpl: TemperatureImpl() {
    override val ID = "raspberry_pi_temp"
    override val MIN_MEDIUM_TEMP = 1
    override val MAX_MEDIUM_TEMP = 2
}