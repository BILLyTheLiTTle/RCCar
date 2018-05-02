package car.server

import car.controllers.basic.ThrottleBrakeImpl
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class ThrottleBrakeSystem{

    @GetMapping("/set_throttle_brake_system")
    fun setThrottleBrakeAction(@RequestParam(value = "id", defaultValue = "-1") id: Int,
                               @RequestParam(value = "action", defaultValue = "$ACTION_NEUTRAL") action: String,
                               @RequestParam(value = "value", defaultValue =  "0") value: Int): String
    {

        lastRequestId = if(id > lastRequestId) id else return "Wrong Request ID: $id"

        println("Action: $action\n" +
                "Value: $value\n" +
                "ID request: $id\n" +
                "ID last request: $lastRequestId\n")

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
                else if (action == ACTION_BRAKING_STILL){
                    state = ThrottleBrakeImpl.brake(value)
                }
                else if (action == ACTION_NEUTRAL){
                    state = ThrottleBrakeImpl.setNeutral()
                }
            }
        }

        return state
    }

    @GetMapping("/get_parking_brake_state")
    fun getParkingBrakeState() = ThrottleBrakeImpl.parkingBrakeState

    @GetMapping("/get_handbrake_state")
    fun getHandbrakeState() = ThrottleBrakeImpl.handbrakeState

    @GetMapping("/get_motion_state")
    fun getMotionState() = ThrottleBrakeImpl.motionState

    companion object {
        var lastRequestId = -1

        const val ACTION_MOVE_FORWARD = "forward"
        const val ACTION_MOVE_BACKWARD = "backward"
        const val ACTION_NEUTRAL = "neutral"
        const val ACTION_BRAKING_STILL = "braking_still"
        const val ACTION_PARKING_BRAKE = "parking_brake"
        const val ACTION_HANDBRAKE = "handbrake"
    }

}