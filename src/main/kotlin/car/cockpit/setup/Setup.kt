package car.cockpit.setup

import car.*


interface Setup {
    var handlingAssistanceState: HandlingAssistance
    var motorSpeedLimiter: MotorSpeedLimiter
    var frontDifferentialSlipperyLimiter: DifferentialSlipperyLimiter
    var rearDifferentialSlipperyLimiter: DifferentialSlipperyLimiter

    fun reset(){
        handlingAssistanceState = HandlingAssistance.MANUAL
        motorSpeedLimiter = MotorSpeedLimiter.FULL_SPEED
        frontDifferentialSlipperyLimiter = DifferentialSlipperyLimiter.LOCKED
        rearDifferentialSlipperyLimiter = DifferentialSlipperyLimiter.LOCKED
    }
}

enum class HandlingAssistance {
    NULL, MANUAL, WARNING, FULL;

    companion object {
        // Give the name of the enum which will be the default when needed
        const val DEFAULT = "NULL"
    }
}

enum class MotorSpeedLimiter(val value: Double) {
    ERROR_SPEED(-1.00),
    NO_SPEED(NO_SPEED_PERCENTAGE),
    SLOW_SPEED_1(SLOW_SPEED_1_PERCENTAGE), SLOW_SPEED_2(SLOW_SPEED_2_PERCENTAGE),
    MEDIUM_SPEED_1(MEDIUM_SPEED_1_PERCENTAGE), MEDIUM_SPEED_2(MEDIUM_SPEED_2_PERCENTAGE),
    FAST_SPEED_1(FAST_SPEED_1_PERCENTAGE), FAST_SPEED_2(FAST_SPEED_2_PERCENTAGE),
    FULL_SPEED(FULL_SPEED_PERCENTAGE);

    companion object {
        // Give the name of the enum which will be the default when needed
        const val DEFAULT = "NO_SPEED"
    }
}

enum class DifferentialSlipperyLimiter {
    ERROR, OPEN, MEDI_0, MEDI_1, MEDI_2, LOCKED, AUTO;

    companion object {
        // Give the name of the enum which will be the default when needed
        const val DEFAULT = "LOCKED"
    }
}