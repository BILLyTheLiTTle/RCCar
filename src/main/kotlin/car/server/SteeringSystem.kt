package car.server

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class SteeringSystem{

    var lastRequestId = -1

    /*
        All possible combinations and their meaning:
        direction = 1 & value = x (x>0) -> Turn right with x tension
        direction = -1 & value = x (x>0) -> Turn left with x tension
        direction = 0 & value = 0 -> Set steering to straight
     */
    @GetMapping("/set_steering_system")
    fun setSteeringAction(@RequestParam(value = "id", defaultValue = "-1") id: Int,
                          @RequestParam(value = "direction", defaultValue = "$DIRECTION_STRAIGHT") direction: Int,
                          @RequestParam(value = "value", defaultValue =  "0") value: Int): String
    {

        lastRequestId = if(id > lastRequestId) id else lastRequestId

        //TODO add function for the hardware
        var state = "Not Executed"
        synchronized(this){
            Thread.sleep(1000)
            state = "Steering $direction with $value tension"
        }

        return state
    }

    @GetMapping("/get_steering_system")
    fun setSteeringAction(): String {
        return "Read the hardware an show me what is going on"
    }

    companion object {
        const val DIRECTION_RIGHT = 1
        const val DIRECTION_LEFT = -1
        const val DIRECTION_STRAIGHT = 0
    }

}