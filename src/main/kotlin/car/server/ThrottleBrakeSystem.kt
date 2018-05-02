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
                "${this::class.simpleName} ID request: $id\n" +
                "${this::class.simpleName} ID last request: $lastRequestId\n")

        //TODO add function for the hardware
        var state = "Unknown"
        synchronized(this){
            if(id == lastRequestId) {
                state = when (action) {
                    ACTION_MOVE_FORWARD, ACTION_MOVE_BACKWARD ->
                        ThrottleBrakeImpl.throttle(action, value)
                    ACTION_PARKING_BRAKE ->
                        ThrottleBrakeImpl.parkingBrake(value)
                    ACTION_HANDBRAKE ->
                        ThrottleBrakeImpl.handbrake(value)
                    ACTION_BRAKING_STILL ->
                        ThrottleBrakeImpl.brake(value)
                    ACTION_NEUTRAL ->
                        ThrottleBrakeImpl.setNeutral()
                    else ->
                        "${this::class.simpleName} ERROR: Entered in else condition"
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