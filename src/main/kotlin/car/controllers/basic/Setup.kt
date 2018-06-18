package car.controllers.basic

import car.server.SetupSystem


interface Setup {
    var handlingAssistanceState: String
    var motorSpeedLimiter: Double

    fun reset(){
        handlingAssistanceState = SetupSystem.ASSISTANCE_NONE
        motorSpeedLimiter = SetupSystem.MOTOR_SPEED_LIMITER_FULL_SPEED
    }
}