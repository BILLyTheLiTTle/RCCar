package car.cockpit.setup

import org.springframework.stereotype.Component

@Component("Setup Component")
class SetupComponent: Setup {
    override var handlingAssistanceState = HandlingAssistance.MANUAL
    override var motorSpeedLimiter = MotorSpeedLimiter.FULL_SPEED
    override var frontDifferentialSlipperyLimiter = DifferentialSlipperyLimiter.LOCKED
    override var rearDifferentialSlipperyLimiter = DifferentialSlipperyLimiter.LOCKED
}