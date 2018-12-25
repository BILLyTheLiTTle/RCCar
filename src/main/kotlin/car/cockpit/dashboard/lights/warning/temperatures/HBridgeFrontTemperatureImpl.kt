package car.cockpit.dashboard.lights.warning.temperatures


object HBridgeFrontTemperatureImpl: HardwareItemTemperatureImpl() {
    override val id = "h_bridge_front_temp"
    override val minMediumTemp = 30
    override val maxMediumTemp = 70
}