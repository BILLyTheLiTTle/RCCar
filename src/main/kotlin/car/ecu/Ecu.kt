package car.ecu

/* ECU stands for Electronic Control Unit

 */

const val ECU_URI = "/ecu"
const val ECU_PARAM_KEY_ITEM = "item"
const val ECU_PARAM_KEY_VALUE = "value"

enum class ModuleState {
    NOTHING_STATE, OFF_STATE, ON_STATE, IDLE_STATE, UNCHANGED_STATE
}

enum class Module {
    TRACTION_CONTROL, ANTILOCK_BRAKING, ELECTRONIC_STABILITY, UNDERSTEER_DETECTION, OVERSTEER_DETECTION,
    COLLISION_DETECTION
}