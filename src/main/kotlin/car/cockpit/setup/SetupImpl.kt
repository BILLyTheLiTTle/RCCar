package car.cockpit.setup

object SetupImpl: Setup {
    override var handlingAssistanceState = ASSISTANCE_NONE
    override var motorSpeedLimiter = MOTOR_SPEED_LIMITER_FULL_SPEED
    override var frontDifferentialSlipperyLimiter = DIFFERENTIAL_SLIPPERY_LIMITER_LOCKED
    override var rearDifferentialSlipperyLimiter = DIFFERENTIAL_SLIPPERY_LIMITER_LOCKED
}