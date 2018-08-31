package car.controllers.temperatures


object ShiftRegistersTemperatureImpl: HardwareItemTemperatureImpl() {
    override val id = "shift_registers_temp"
    override val minMediumTemp = 30
    override val maxMediumTemp = 70
}