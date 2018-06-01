package car.controllers.temperatures

import car.server.EngineSystem


object MotorRearLeftTemperatureImpl: Temperature {
    const val ID = "motor_rear_left_temp"
    private const val MIN_MEDIUM_TEMP = 1
    private const val MAX_MEDIUM_TEMP = 2

    override val value: Int
        get() {
            // TODO read the appropriate sensor
            val temp = readSensor(ID)

            warning = when {
                temp < MIN_MEDIUM_TEMP -> WARNING_TYPE_NORMAL
                temp > MAX_MEDIUM_TEMP -> WARNING_TYPE_HIGH
                else -> WARNING_TYPE_MEDIUM
            }

            return temp
        }

    override var warning = EngineSystem.EMPTY_STRING
        private set
}