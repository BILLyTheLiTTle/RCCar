package car.cockpit.electrics

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class ElectricsController{

    @Autowired
    private lateinit var electricsService: ElectricsService

    @Autowired
    private lateinit var electricsComponent: Electrics

    /*
        All possible combinations and their meaning:
        direction = 1 -> Light for right turn
        direction = -1 -> Light for left turn
        direction = 0 -> All turn lights are off
     */
    @GetMapping("/set_direction_lights")
    fun setDirectionLights(
        @RequestParam(value = "direction", defaultValue = TURN_LIGHTS_STRAIGHT) direction: String
    ) = electricsService.setDirectionLights(direction)

    @GetMapping("/get_direction_lights")
    fun getDirectionLights() = electricsService.getDirectionLights()


    @GetMapping("/set_main_lights_state")
    fun setMainLightsState(@RequestParam(value = "value", defaultValue = LIGHTS_OFF) value: String) =
        electricsService.setMainLightsState(value)

    @GetMapping("/get_main_lights_state")
    fun getMainLightsState() = electricsService.getMainLightsState()

    @GetMapping("/set_reverse_lights_state")
    fun setReverseLightsState(@RequestParam(value = "state", defaultValue = "false") state: Boolean): String {
        electricsComponent.reverseLightsState = state
        return electricsComponent.reverseLightsState.toString()
    }

    @GetMapping("/get_reverse_lights_state")
    fun getReverseLightsState() = electricsComponent.reverseLightsState

    @GetMapping("/set_emergency_lights_state")
    fun setEmergencyLightsState(@RequestParam(value = "state", defaultValue = "false") state: Boolean): String {
        electricsComponent.emergencyLightsState = state
        return electricsComponent.emergencyLightsState.toString()
    }

    @GetMapping("/get_emergency_lights_state")
    fun getEmergencyLightsState() = electricsComponent.emergencyLightsState

}