package car.server

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class ThrottleBrakeSystem{

    var lastRequestId = -1

    /*
        All possible combinations and their meaning:
        direction = 1 & value = x (x>0) -> Move forward with x speed
        direction = -1 & value = x (x>0) -> Move backward with x speed
        direction = 0 & value = x (0<x<1) -> Apply braking with x force
        direction = 0 & value = 0 -> Stay in neutral
        direction = 0 & value = 1 -> Stay still with handbrake
     */
    @GetMapping("/set_drive_brake_system")
    fun setDriveBrakeAction(@RequestParam(value = "id", defaultValue = "-1") id: Int,
                            @RequestParam(value = "direction", defaultValue = "$DIRECTION_STILL") direction: Int,
                            @RequestParam(value = "value", defaultValue =  "0") value: Int): String
    {

        lastRequestId = if(id > lastRequestId) id else lastRequestId


        //TODO add function for the hardware
        var state = "Not Executed"
        synchronized(this){
            if(id == lastRequestId) {
                Thread.sleep(1000)
                state = "Moving at $direction with $value acceleration"
            }
        }

        return state
    }

    @GetMapping("/get_drive_brake_system")
    fun getDriveBrakeAction(): String {
        return "Read the hardware an show me what is going on"
    }

    companion object {
        const val DIRECTION_FORWARD = 1
        const val DIRECTION_BACKWARD = -1
        const val DIRECTION_STILL = 0
    }

}