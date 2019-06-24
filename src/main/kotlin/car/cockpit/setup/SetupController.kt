package car.cockpit.setup

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class SetupController{

    @Autowired
    private lateinit var service: SetupService

    @GetMapping("/set_handling_assistance")
    fun setHandlingAssistance(
        @RequestParam(value = "state", defaultValue = HandlingAssistance.DEFAULT) state: String
    ) = service.setHandlingAssistance(state)

    @GetMapping("/get_handling_assistance_state")
    fun getHandlingAssistanceState() = service.getHandlingAssistanceState()

    @GetMapping("/set_motor_speed_limiter")
    fun setMotorSpeedLimiter(
        @RequestParam(value = "value", defaultValue =  MotorSpeedLimiter.DEFAULT) value: String
    ) = service.setMotorSpeedLimiter(value)

    @GetMapping("/get_motor_speed_limiter")
    fun getMotorSpeedLimiter() = service.getMotorSpeedLimiter()

    @GetMapping("/set_front_differential_slippery_limiter")
    fun setFrontDifferentialSlipperyLimiter(
        @RequestParam(value = "value", defaultValue =  DifferentialSlipperyLimiter.DEFAULT) value: String
    ) = service.setFrontDifferentialSlipperyLimiter(value)

    @GetMapping("/get_front_differential_slippery_limiter")
    fun getFrontDifferentialSlipperyLimiter() = service.getFrontDifferentialSlipperyLimiter()

    @GetMapping("/set_rear_differential_slippery_limiter")
    fun setRearDifferentialSlipperyLimiter(
        @RequestParam(value = "value", defaultValue =  DifferentialSlipperyLimiter.DEFAULT) value: String
    ) = service.setRearDifferentialSlipperyLimiter(value)

    @GetMapping("/get_rear_differential_slippery_limiter")
    fun getRearDifferentialSlipperyLimiter() = service.getRearDifferentialSlipperyLimiter()

}