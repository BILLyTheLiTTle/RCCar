package car.ecu

/* ECU stands for Electronic Control Unit

 */

import car.cockpit.engine.EMPTY_STRING

const val ECU_URI = "/ecu"
const val ECU_PARAM_KEY_ITEM = "item"
const val ECU_PARAM_KEY_VALUE = "value"

enum class ModuleState(val id: String) {
    NOTHING(EMPTY_STRING), // TODO NOTHING_STATE, etc for the others
    OFF("module_off_state"),
    ON("module_on_state"),
    IDLE("module_idle_state"),
    UNCHANGED("module_unchanged_state")
}

enum class Module(val id: String) {
    TRACTION_CONTROL("TCM"),
    ANTILOCK_BRAKING("ABM"),
    ELECTRONIC_STABILITY("ESM"),
    UNDERSTEER_DETECTION("UDM"),
    OVERSTEER_DETECTION("ODM"),
    COLLISION_DETECTION("CDM")
}