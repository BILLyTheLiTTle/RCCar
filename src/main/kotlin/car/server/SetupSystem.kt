package car.server

import car.controllers.basic.SetupImpl
import car.controllers.basic.SteeringImpl
import car.showMessage
import kotlinx.coroutines.experimental.channels.NULL_VALUE
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class SetupSystem{

    @GetMapping("/set_handling_assistance")
    fun setHandlingAssistance(
        @RequestParam(value = "state", defaultValue =  "$ASSISTANCE_NULL") state: String
    ): String {
        when (state) {
            ASSISTANCE_NONE -> SetupImpl.handlingAssistanceState = ASSISTANCE_NONE
            ASSISTANCE_WARNING -> SetupImpl.handlingAssistanceState = ASSISTANCE_WARNING
            ASSISTANCE_FULL -> SetupImpl.handlingAssistanceState = ASSISTANCE_FULL
            else -> SetupImpl.handlingAssistanceState = ASSISTANCE_NULL
        }
        return EngineSystem.SUCCESS
    }

    @GetMapping("/get_handling_assistance_state")
    fun getHandlingAssistanceState() = SetupImpl.handlingAssistanceState


    companion object {
        const val ASSISTANCE_NULL = EngineSystem.EMPTY_STRING
        const val ASSISTANCE_NONE = "assistance_none"
        const val ASSISTANCE_WARNING = "assistance_warning"
        const val ASSISTANCE_FULL = "assistance_full"
    }

}