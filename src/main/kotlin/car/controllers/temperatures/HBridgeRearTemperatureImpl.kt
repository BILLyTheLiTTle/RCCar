package car.controllers.temperatures


object HBridgeRearTemperatureImpl: HardwareItemTemperatureImpl() {
    override val ID = "h_bridge_rear_temp"
    override val MIN_MEDIUM_TEMP = 30
    override val MAX_MEDIUM_TEMP = 70
}