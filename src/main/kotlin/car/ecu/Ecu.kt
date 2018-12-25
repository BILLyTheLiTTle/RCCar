package car.ecu

/* ECU stands for Electronic Control Unit

 */

import car.cockpit.engine.EngineSystem

interface Ecu {
    companion object {
        const val TRACTION_CONTROL_MODULE = "TCM"
        const val ANTILOCK_BRAKING_MODULE = "ABM"
        const val ELECTRONIC_STABILITY_MODULE = "ESM"
        const val UNDERSTEER_DETECTION_MODULE = "UDM"
        const val OVERSTEER_DETECTION_MODULE = "ODM"
        const val COLLISION_DETECTION_MODULE = "CDM"

        const val ECU_URI = "/ecu"
        const val ECU_PARAM_KEY_ITEM = "item"
        const val ECU_PARAM_KEY_VALUE = "value"
        const val MODULE_NOTHING_STATE = EngineSystem.EMPTY_STRING
        const val MODULE_OFF_STATE = "module_off_state"
        const val MODULE_ON_STATE = "module_on_state"
        const val MODULE_IDLE_STATE = "module_idle_state"
        const val MODULE_UNCHANGED_STATE = "module_unchanged_state"
    }
}