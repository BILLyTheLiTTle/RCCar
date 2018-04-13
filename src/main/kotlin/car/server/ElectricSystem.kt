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
    @GetMapping("/turn_light")
    fun setTurnLightAction(@RequestParam(value = "direction", defaultValue = "0") direction: Int): String {

        //TODO add function for the hardware
        synchronized(this){
            Thread.sleep(1000)
            println("Light for $direction turn")
        }

        return "Light for $direction turn"
    }

    /*
        All possible combinations and their meaning:
        value = 0 -> All vision lights (front/back) are off
        value = 0.5 -> Position scale vision lights (front/back)
        direction = 1 -> Driving scale vision lights (front/back)
     */
    @GetMapping("/vision_light")
    fun setVisionLightAction(@RequestParam(value = "value", defaultValue = "0") value: Int): String {

        //TODO add function for the hardware
        synchronized(this){
            Thread.sleep(1000)
            println("Vision lights scale is $value")
        }

        return "Vision lights scale is $value"
    }

    companion object {
        const val DIRECTION_LIGHT_RIGHT = 1
        const val DIRECTION_LIGHT_LEFT = -1
        const val DIRECTION_LIGHT_STRAIGHT = 0

        const val LIGHTS_OFF = 0
        const val POSITION_SCALE = 0.5
        const val DRIVING_SCALE = 1
    }

}