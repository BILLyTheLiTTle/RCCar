package car.controllers.temperatures


object ShiftRegistersTemperatureImpl: HardwareItemTemperatureImpl() {
    override val ID = "shift_registers_temp"
    override val MIN_MEDIUM_TEMP = 1
    override val MAX_MEDIUM_TEMP = 2
}