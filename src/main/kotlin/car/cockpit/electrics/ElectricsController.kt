package car.cockpit.electrics

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class ElectricsController{

    @Autowired
    private lateinit var electricsService: ElectricsService

    @GetMapping("/set_direction_lights")
    fun setDirectionLights(
        @RequestParam(value = "direction", defaultValue = DIRECTION_LIGHTS_STRAIGHT) direction: String
    ) = electricsService.setDirectionLights(direction)

    @GetMapping("/get_direction_lights")
    fun getDirectionLights() = electricsService.getDirectionLights()


    @GetMapping("/set_main_lights_state")
    fun setMainLightsState(@RequestParam(value = "state", defaultValue = LIGHTS_OFF) state: String) =
        electricsService.setMainLightsState(state)

    @GetMapping("/get_main_lights_state")
    fun getMainLightsState() = electricsService.getMainLightsState()

    @GetMapping("/set_reverse_lights_state")
    fun setReverseLightsState(@RequestParam(value = "state", defaultValue = "false") state: Boolean) =
        electricsService.setReverseLightsState(state)

    @GetMapping("/get_reverse_lights_state")
    fun getReverseLightsState() = electricsService.getReverseLightsState()

    @GetMapping("/set_emergency_lights_state")
    fun setEmergencyLightsState(@RequestParam(value = "state", defaultValue = "false") state: Boolean) =
        electricsService.setEmergencyLightsState(state)

    @GetMapping("/get_emergency_lights_state")
    fun getEmergencyLightsState() = electricsService.getEmergencyLightsState()

}