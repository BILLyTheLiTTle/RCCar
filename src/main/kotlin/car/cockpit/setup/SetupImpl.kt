package car.cockpit.setup

import car.cockpit.setup.Setup
import car.cockpit.setup.SetupSystem

object SetupImpl: Setup {
    override var handlingAssistanceState = SetupSystem.ASSISTANCE_NONE
    override var motorSpeedLimiter = SetupSystem.MOTOR_SPEED_LIMITER_FULL_SPEED
    override var frontDifferentialSlipperyLimiter = SetupSystem.DIFFERENTIAL_SLIPPERY_LIMITER_LOCKED
    override var rearDifferentialSlipperyLimiter = SetupSystem.DIFFERENTIAL_SLIPPERY_LIMITER_LOCKED
}