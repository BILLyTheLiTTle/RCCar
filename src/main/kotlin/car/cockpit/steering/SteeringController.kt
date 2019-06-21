package car.cockpit.steering

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class SteeringController{

    @Autowired
    private lateinit var service: SteeringService

    @GetMapping("/set_steering_system")
    fun setSteeringAction(
        @RequestParam(value = "id", defaultValue = "-1") id: Long,
        @RequestParam(value = "direction", defaultValue = Turn.DEFAULT) direction: String,
        @RequestParam(value = "value", defaultValue = SteeringValues.DEFAULT) value: String
    ) = service.setSteeringAction(id, direction, value)

    @GetMapping("/get_steering_direction")
    fun getSteeringDirection() = service.getSteeringDirection()

}