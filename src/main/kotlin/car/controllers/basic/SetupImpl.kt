package car.controllers.basic

import car.server.SetupSystem

object SetupImpl: Setup {
    override var handlingAssistanceState = SetupSystem.ASSISTANCE_NONE
    override var motorSpeedLimiter = SetupSystem.MOTOR_SPEED_LIMITER_FULL_SPEED
}