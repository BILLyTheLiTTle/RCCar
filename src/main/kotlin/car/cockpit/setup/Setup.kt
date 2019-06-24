package car.cockpit.setup


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
    NO_SPEED(0.00),
    SLOW_SPEED_1(0.20), SLOW_SPEED_2(0.40),
    MEDIUM_SPEED_1(0.60), MEDIUM_SPEED_2(0.70),
    FAST_SPEED_1(0.80), FAST_SPEED_2(0.90),
    FULL_SPEED(1.00);

    companion object {
        // Give the name of the enum which will be the default when needed
        const val DEFAULT = "NO_SPEED"
    }
}

enum class DifferentialSlipperyLimiter(val value: Int) {
    ERROR(-1),
    OPEN(0),
    MEDI_0(1), MEDI_1(2), MEDI_2(3),
    LOCKED(4),
    AUTO(10);

    companion object {
        // Give the name of the enum which will be the default when needed
        const val DEFAULT = "LOCKED"
    }
}