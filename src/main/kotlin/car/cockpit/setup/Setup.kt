package car.cockpit.setup

import car.cockpit.setup.SetupSystem


interface Setup {
    var handlingAssistanceState: String
    var motorSpeedLimiter: Double
    var frontDifferentialSlipperyLimiter: Int
    var rearDifferentialSlipperyLimiter: Int

    fun reset(){
        handlingAssistanceState = SetupSystem.ASSISTANCE_NONE
        motorSpeedLimiter = SetupSystem.MOTOR_SPEED_LIMITER_FULL_SPEED
        frontDifferentialSlipperyLimiter = SetupSystem.DIFFERENTIAL_SLIPPERY_LIMITER_LOCKED
        rearDifferentialSlipperyLimiter = SetupSystem.DIFFERENTIAL_SLIPPERY_LIMITER_LOCKED
    }
}