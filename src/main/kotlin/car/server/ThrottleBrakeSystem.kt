package car.server

import car.controllers.basic.ThrottleBrakeImpl
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class ThrottleBrakeSystem{

    @GetMapping("/set_throttle_brake_system")
    fun setThrottleBrakeAction(@RequestParam(value = "id", defaultValue = "-1") id: Int,
                               @RequestParam(value = "action", defaultValue = "$ACTION_STILL") action: String,
                               @RequestParam(value = "value", defaultValue =  "0") value: Int): String
    {

        lastRequestId = if(id > lastRequestId) id else return "Wrong Request ID: $id"

        println("ID request: $id\nID last request: $lastRequestId")

        //TODO add function for the hardware
        var state = "Unknown"
        synchronized(this){
            if(id == lastRequestId) {
                if (action == ACTION_MOVE_FORWARD || action == ACTION_MOVE_BACKWARD) {
                    state = ThrottleBrakeImpl.throttle(action, value)
                }
                else if (action == ACTION_PARKING_BRAKE) {
                    state = ThrottleBrakeImpl.parkingBrake(value)
                }
                else if (action == ACTION_HANDBRAKE) {
                    state = ThrottleBrakeImpl.handbrake(value)
                }
                else if (action == ACTION_STILL){
                    state = ThrottleBrakeImpl.brake(value)
                }
            }
        }

        return state
    }

    @GetMapping("/get_parking_brake_state")
    fun getParkingBrakeState() = ThrottleBrakeImpl.action == ACTION_PARKING_BRAKE && ThrottleBrakeImpl.value == 100

    companion object {
        var lastRequestId = -1

        const val ACTION_MOVE_FORWARD = "forward"
        const val ACTION_MOVE_BACKWARD = "backwards"
        const val ACTION_STILL = "still"
        const val ACTION_PARKING_BRAKE = "parking_brake"
        const val ACTION_HANDBRAKE = "handbrake"
    }

}