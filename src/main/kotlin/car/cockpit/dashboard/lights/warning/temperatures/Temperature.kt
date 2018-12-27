package car.cockpit.dashboard.lights.warning.temperatures

import car.cockpit.engine.EMPTY_STRING
import car.cockpit.engine.EngineController

interface Temperature {

    val value: Int
    val warning: String

    fun reset(){
        //TODO what?!
    }

    companion object {
        const val WARNING_TYPE_NOTHING = EMPTY_STRING
        const val WARNING_TYPE_NORMAL = "normal"
        const val WARNING_TYPE_MEDIUM = "medium"
        const val WARNING_TYPE_HIGH = "high"
    }
}