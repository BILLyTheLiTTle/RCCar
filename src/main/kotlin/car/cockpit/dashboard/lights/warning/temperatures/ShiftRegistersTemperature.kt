package car.cockpit.dashboard.lights.warning.temperatures


object ShiftRegistersTemperature: HardwareItemTemperature() {
    override val id = "shift_registers_temp"
    override val minMediumTemp = 30
    override val maxMediumTemp = 70
}