package car.controllers.temperatures


object BatteriesTemperatureImpl: HardwareItemTemperatureImpl() {
    override val ID = "batteries_temp"
    override val MIN_MEDIUM_TEMP = 1
    override val MAX_MEDIUM_TEMP = 2
}