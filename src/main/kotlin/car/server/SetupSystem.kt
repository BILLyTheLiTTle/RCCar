package car.server

import car.controllers.basic.SetupImpl
import car.controllers.basic.SteeringImpl
import car.showMessage
import kotlinx.coroutines.experimental.channels.NULL_VALUE
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import kotlin.math.roundToInt

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

    @GetMapping("/set_motor_speed_limiter")
    fun setMotorSpeedLimiter(
        @RequestParam(value = "value", defaultValue =  "$MOTOR_SPEED_LIMITER_NO_SPEED") value: Double
    ): String {
        SetupImpl.motorSpeedLimiter = value
        return EngineSystem.SUCCESS
    }

    @GetMapping("/get_motor_speed_limiter")
    fun getMotorSpeedLimiter() = SetupImpl.motorSpeedLimiter


    companion object {
        const val ASSISTANCE_NULL = EngineSystem.EMPTY_STRING
        const val ASSISTANCE_NONE = "assistance_none"
        const val ASSISTANCE_WARNING = "assistance_warning"
        const val ASSISTANCE_FULL = "assistance_full"

        const val MOTOR_SPEED_LIMITER_NO_SPEED = 0.00
        const val MOTOR_SPEED_LIMITER_FULL_SPEED = 1.00
    }

}