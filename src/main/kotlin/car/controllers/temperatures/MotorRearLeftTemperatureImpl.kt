package car.controllers.temperatures


object MotorRearLeftTemperatureImpl: Temperature {
    const val ID = "motor_rear_left_temp"
    private const val MIN_MEDIUM_TEMP = 1
    private const val MAX_MEDIUM_TEMP = 2

    override val value: Int
        get() {
            // TODO read the appropriate sensor
            return readSensor(ID)
        }

    override val warning: String
        get() {
            return when {
                value < MIN_MEDIUM_TEMP -> WARNING_TYPE_NORMAL
                value > MAX_MEDIUM_TEMP -> WARNING_TYPE_HIGH
                else -> WARNING_TYPE_MEDIUM
            }
        }
}