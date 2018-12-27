package car.cockpit.setup

import car.cockpit.engine.EMPTY_STRING
import car.cockpit.engine.SUCCESS
import org.springframework.stereotype.Service

@Service("Setup")
class SetupService {
    fun setHandlingAssistance(state: String): String {
        when (state) {
            ASSISTANCE_NONE -> SetupImpl.handlingAssistanceState =
                    ASSISTANCE_NONE
            ASSISTANCE_WARNING -> SetupImpl.handlingAssistanceState =
                    ASSISTANCE_WARNING
            ASSISTANCE_FULL -> SetupImpl.handlingAssistanceState =
                    ASSISTANCE_FULL
            else -> SetupImpl.handlingAssistanceState = ASSISTANCE_NULL
        }
        return SUCCESS
    }

    fun getHandlingAssistanceState() = SetupImpl.handlingAssistanceState

    fun setMotorSpeedLimiter(value: Double): String {
        SetupImpl.motorSpeedLimiter = value
        return SUCCESS
    }

    fun getMotorSpeedLimiter() = SetupImpl.motorSpeedLimiter

    fun setFrontDifferentialSlipperyLimiter(value: Int): String {
        SetupImpl.frontDifferentialSlipperyLimiter = value
        return SUCCESS
    }

    fun getFrontDifferentialSlipperyLimiter() = SetupImpl.frontDifferentialSlipperyLimiter

    fun setRearDifferentialSlipperyLimiter(value: Int): String {
        SetupImpl.rearDifferentialSlipperyLimiter = value
        return SUCCESS
    }

    fun getRearDifferentialSlipperyLimiter() = SetupImpl.rearDifferentialSlipperyLimiter
}

const val ASSISTANCE_NULL = EMPTY_STRING
const val ASSISTANCE_NONE = "assistance_none"
const val ASSISTANCE_WARNING = "assistance_warning"
const val ASSISTANCE_FULL = "assistance_full"

const val MOTOR_SPEED_LIMITER_NO_SPEED = 0.00
const val MOTOR_SPEED_LIMITER_FULL_SPEED = 1.00

const val DIFFERENTIAL_SLIPPERY_LIMITER_OPEN = 0
const val DIFFERENTIAL_SLIPPERY_LIMITER_MEDI_0 = 1
const val DIFFERENTIAL_SLIPPERY_LIMITER_MEDI_1 = 2
const val DIFFERENTIAL_SLIPPERY_LIMITER_MEDI_2 = 3
const val DIFFERENTIAL_SLIPPERY_LIMITER_LOCKED = 4
const val DIFFERENTIAL_SLIPPERY_LIMITER_AUTO = 10