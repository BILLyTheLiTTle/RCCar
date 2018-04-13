package car.server

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class SteeringSystem{

    /*
        All possible combinations and their meaning:
        direction = 1 & value = x (x>0) -> Turn right with x tension
        direction = -1 & value = x (x>0) -> Turn left with x tension
        direction = 0 & value = 0 -> Set steering to straight
     */
    @GetMapping("/steering_system")
    fun setSteeringAction(@RequestParam(value = "direction", defaultValue = "0") direction: Int,
                    @RequestParam(value = "value", defaultValue =  "0") value: Int): String
    {

        //TODO add function for the hardware
        synchronized(this){
            Thread.sleep(1000)
            println("Steering $direction with $value tension")
        }

        return "Steering $direction with $value tension"
    }

    companion object {
        const val DIRECTION_RIGHT = 1
        const val DIRECTION_LEFT = -1
        const val DIRECTION_STRAIGHT = 0
    }

}