package car.server

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class DriveBrakeSystem{

    /*
        All possible combinations and their meaning:
        direction = 1 & value = x (x>0) -> Move forward with x speed
        direction = -1 & value = x (x>0) -> Move backward with x speed
        direction = 0 & value = x (0<x<1) -> Apply braking with x force
        direction = 0 & value = 0 -> Stay in neutral
        direction = 0 & value = 1 -> Stay still with handbrake
     */
    @GetMapping("/drive_brake_system")
    fun setDriveBrakeAction(@RequestParam(value = "direction", defaultValue = "DIRECTION_STILL") direction: Int,
                 @RequestParam(value = "value", defaultValue =  "0") value: Int): String
    {

        //TODO add function for the hardware
        synchronized(this){
            Thread.sleep(1000)
            println("Moving at $direction with $value acceleration")
        }

        return "Moving at $direction with $value acceleration"
    }

    companion object {
        const val DIRECTION_FORWARD = 1
        const val DIRECTION_BACKWARD = -1
        const val DIRECTION_STILL = 0
    }

}