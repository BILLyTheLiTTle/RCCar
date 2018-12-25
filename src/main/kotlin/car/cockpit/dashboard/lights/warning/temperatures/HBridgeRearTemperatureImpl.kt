package car.cockpit.dashboard.lights.warning.temperatures


object HBridgeRearTemperatureImpl: HardwareItemTemperatureImpl() {
    override val id = "h_bridge_rear_temp"
    override val minMediumTemp = 30
    override val maxMediumTemp = 70
}