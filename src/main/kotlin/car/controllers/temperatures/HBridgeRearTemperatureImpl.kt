package car.controllers.temperatures


object HBridgeRearTemperatureImpl: TemperatureImpl() {
    override val ID = "h_bridge_rear_temp"
    override val MIN_MEDIUM_TEMP = 1
    override val MAX_MEDIUM_TEMP = 2
}