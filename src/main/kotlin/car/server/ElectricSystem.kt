package car.server

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class ElectricSystem{

    /*
        All possible combinations and their meaning:
        direction = 1 -> Light for right turn
        direction = -1 -> Light for left turn
        direction = 0 -> All turn lights are off
     */
    @GetMapping("/set_turn_light")
    fun setTurnLightAction(
        @RequestParam(value = "direction", defaultValue = "$DIRECTION_LIGHT_STRAIGHT") direction: Int)
            : String {

        //TODO add function for the hardware
        synchronized(this){
            Thread.sleep(1000)
            println("Light for $direction turn")
        }

        return "Light for $direction turn"
    }

    @GetMapping("/get_turn_light")
    fun getTurnLightAction(): String {
        return "Read the hardware an show me what is going on"
    }

    /*
        All possible combinations and their meaning:
        value = 0 -> All vision lights (front/back) are off
        value = 0.5 front & 0.5 back -> Position scale vision lights (front/back) *1
        direction = 1 front & 0.5 back -> Driving scale vision lights (front/back) *1
        *1: Back vision light is 1 when braking
     */
    @GetMapping("/set_vision_light")
    fun setVisionLightAction(@RequestParam(value = "value", defaultValue = "$VISION_LIGHTS_OFF") value: Int): String {

        //TODO add function for the hardware
        synchronized(this){
            Thread.sleep(1000)
            println("Vision lights scale is $value")
        }

        return "Vision lights scale is $value"
    }

    @GetMapping("/get_vision_light")
    fun setVisionLightAction(): String {
        return "Read the hardware an show me what is going on"
    }

    companion object {
        const val DIRECTION_LIGHT_RIGHT = 1
        const val DIRECTION_LIGHT_LEFT = -1
        const val DIRECTION_LIGHT_STRAIGHT = 0

        const val VISION_LIGHTS_OFF = 0
        const val VISION_LIGHTS_POSITION_SCALE = 0.5
        const val VISION_LIGHTS_DRIVING_SCALE = 1
    }

}