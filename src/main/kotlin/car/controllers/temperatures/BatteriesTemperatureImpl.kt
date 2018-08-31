package car.controllers.temperatures


object BatteriesTemperatureImpl: HardwareItemTemperatureImpl() {
    override val id = "batteries_temp"
    override val minMediumTemp = 30
    override val maxMediumTemp = 70
}