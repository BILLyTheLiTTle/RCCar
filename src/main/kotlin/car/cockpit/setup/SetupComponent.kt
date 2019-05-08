package car.cockpit.setup

import org.springframework.stereotype.Component

@Component("Setup Component")
class SetupComponent: Setup {
    override var handlingAssistanceState = ASSISTANCE_MANUAL
    override var motorSpeedLimiter = MOTOR_SPEED_LIMITER_FULL_SPEED
    override var frontDifferentialSlipperyLimiter = DIFFERENTIAL_SLIPPERY_LIMITER_LOCKED
    override var rearDifferentialSlipperyLimiter = DIFFERENTIAL_SLIPPERY_LIMITER_LOCKED
}