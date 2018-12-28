package car.cockpit.setup

import car.cockpit.engine.EMPTY_STRING
import car.cockpit.engine.SUCCESS
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service("Setup Service")
class SetupService {

    @Autowired
    private lateinit var setupComponent: Setup

    fun setHandlingAssistance(state: String): String {
        when (state) {
            ASSISTANCE_NONE -> setupComponent.handlingAssistanceState =
                    ASSISTANCE_NONE
            ASSISTANCE_WARNING -> setupComponent.handlingAssistanceState =
                    ASSISTANCE_WARNING
            ASSISTANCE_FULL -> setupComponent.handlingAssistanceState =
                    ASSISTANCE_FULL
            else -> setupComponent.handlingAssistanceState = ASSISTANCE_NULL
        }
        return SUCCESS
    }

    fun getHandlingAssistanceState() = setupComponent.handlingAssistanceState

    fun setMotorSpeedLimiter(value: Double): String {
        setupComponent.motorSpeedLimiter = value
        return SUCCESS
    }

    fun getMotorSpeedLimiter() = setupComponent.motorSpeedLimiter

    fun setFrontDifferentialSlipperyLimiter(value: Int): String {
        setupComponent.frontDifferentialSlipperyLimiter = value
        return SUCCESS
    }

    fun getFrontDifferentialSlipperyLimiter() = setupComponent.frontDifferentialSlipperyLimiter

    fun setRearDifferentialSlipperyLimiter(value: Int): String {
        setupComponent.rearDifferentialSlipperyLimiter = value
        return SUCCESS
    }

    fun getRearDifferentialSlipperyLimiter() = setupComponent.rearDifferentialSlipperyLimiter
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