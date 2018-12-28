package car.cockpit.dashboard.lights.warning.temperatures


object HBridgeRearTemperature: HardwareItemTemperature() {
    override val id = "h_bridge_rear_temp"
    override val minMediumTemp = 30
    override val maxMediumTemp = 70
}