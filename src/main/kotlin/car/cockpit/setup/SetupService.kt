package car.cockpit.setup

import car.cockpit.engine.SUCCESS
import car.enumContains
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service("Setup Service")
class SetupService {

    @Autowired
    private lateinit var setupComponent: Setup

    fun setHandlingAssistance(state: String): String {

        val assistance = if (enumContains<HandlingAssistance>(state)) HandlingAssistance.valueOf(state) else HandlingAssistance.NULL

        when (assistance) {
            HandlingAssistance.MANUAL -> setupComponent.handlingAssistanceState =
                HandlingAssistance.MANUAL
            HandlingAssistance.WARNING -> setupComponent.handlingAssistanceState =
                HandlingAssistance.WARNING
            HandlingAssistance.FULL -> setupComponent.handlingAssistanceState =
                HandlingAssistance.FULL
            else -> setupComponent.handlingAssistanceState = HandlingAssistance.NULL
        }
        return SUCCESS
    }

    fun getHandlingAssistanceState() = setupComponent.handlingAssistanceState.name

    fun setMotorSpeedLimiter(value: String): String {
        val limit = if (enumContains<MotorSpeedLimiter>(value)) MotorSpeedLimiter.valueOf(value) else MotorSpeedLimiter.ERROR_SPEED
        setupComponent.motorSpeedLimiter = limit
        return SUCCESS
    }

    fun getMotorSpeedLimiter() = setupComponent.motorSpeedLimiter.name

    fun setFrontDifferentialSlipperyLimiter(value: String): String {
        val slip = if (enumContains<DifferentialSlipperyLimiter>(value)) DifferentialSlipperyLimiter.valueOf(value) else DifferentialSlipperyLimiter.ERROR
        setupComponent.frontDifferentialSlipperyLimiter = slip
        return SUCCESS
    }

    fun getFrontDifferentialSlipperyLimiter() = setupComponent.frontDifferentialSlipperyLimiter.name

    fun setRearDifferentialSlipperyLimiter(value: String): String {
        val slip = if (enumContains<DifferentialSlipperyLimiter>(value)) DifferentialSlipperyLimiter.valueOf(value) else DifferentialSlipperyLimiter.ERROR
        setupComponent.rearDifferentialSlipperyLimiter = slip
        return SUCCESS
    }

    fun getRearDifferentialSlipperyLimiter() = setupComponent.rearDifferentialSlipperyLimiter.name
}