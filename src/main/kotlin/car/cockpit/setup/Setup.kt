package car.cockpit.setup


interface Setup {
    var handlingAssistanceState: String
    var motorSpeedLimiter: Double
    var frontDifferentialSlipperyLimiter: Int
    var rearDifferentialSlipperyLimiter: Int

    fun reset(){
        handlingAssistanceState = ASSISTANCE_NONE
        motorSpeedLimiter = MOTOR_SPEED_LIMITER_FULL_SPEED
        frontDifferentialSlipperyLimiter = DIFFERENTIAL_SLIPPERY_LIMITER_LOCKED
        rearDifferentialSlipperyLimiter = DIFFERENTIAL_SLIPPERY_LIMITER_LOCKED
    }
}