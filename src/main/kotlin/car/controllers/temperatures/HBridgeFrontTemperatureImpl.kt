package car.controllers.temperatures


object HBridgeFrontTemperatureImpl: HardwareItemTemperatureImpl() {
    override val ID = "h_bridge_front_temp"
    override val MIN_MEDIUM_TEMP = 1
    override val MAX_MEDIUM_TEMP = 2
}