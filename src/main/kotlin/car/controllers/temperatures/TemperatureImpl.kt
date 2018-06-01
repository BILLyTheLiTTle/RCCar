package car.controllers.temperatures

import car.server.EngineSystem

open class TemperatureImpl: Temperature {

    open val ID = "parent_of_all_temp"
    protected open val MIN_MEDIUM_TEMP = 1
    protected open val MAX_MEDIUM_TEMP = 2

    override val value: Int
        get() {
            // TODO read the appropriate sensor
            val temp = readSensor()

            warning = when {
                temp < MIN_MEDIUM_TEMP -> WARNING_TYPE_NORMAL
                temp > MAX_MEDIUM_TEMP -> WARNING_TYPE_HIGH
                else -> WARNING_TYPE_MEDIUM
            }

            return temp
        }

    override var warning = EngineSystem.EMPTY_STRING
        protected set
}