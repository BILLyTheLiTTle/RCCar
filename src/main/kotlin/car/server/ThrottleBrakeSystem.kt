package car.server

import car.controllers.advanced.ElectronicThrottleBrakeImpl
import car.controllers.basic.SetupImpl
import car.controllers.basic.ThrottleBrakeImpl
import car.showMessage
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import kotlin.math.roundToInt

@RestController
class ThrottleBrakeSystem{

    @GetMapping("/set_throttle_brake_system")
    fun setThrottleBrakeAction(@RequestParam(value = "id", defaultValue = "-1") id: Int,
                               @RequestParam(value = "action", defaultValue = "$ACTION_NEUTRAL") action: String,
                               @RequestParam(value = "value", defaultValue =  "0") value: Int): String
    {

        lastRequestId = if(id > lastRequestId) id else return "Wrong Request ID: $id"

        showMessage(title = "THROTTLE -N- BRAKE SYSTEM",
            body = "Action: $action\n" +
                    "Primitive User Value: $value\n" +
                    if (action == ACTION_MOVE_FORWARD || action == ACTION_MOVE_BACKWARD) {
                        "Limited User Value: " +
                                "${(value * SetupImpl.motorSpeedLimiter).roundToInt()}\n"
                    }
                    else {
                        ""
                    }
                    +
                    "{ ${this::class.simpleName} } ID request: $id\n" +
                    "{ ${this::class.simpleName} } ID last request: $lastRequestId")

        //TODO add function for the hardware
        var state = EngineSystem.UNKNOWN_STATE
        // I don't think I need synchronization
        //synchronized(this){
            if(id == lastRequestId) {
                state = when (action) {
                    ACTION_MOVE_FORWARD, ACTION_MOVE_BACKWARD -> {
                        if (SetupImpl.handlingAssistanceState == SetupSystem.ASSISTANCE_NONE) {
                            ThrottleBrakeImpl.throttle(action, (value * SetupImpl.motorSpeedLimiter).roundToInt())
                        }
                        else {
                            ElectronicThrottleBrakeImpl.throttle(action, (value * SetupImpl.motorSpeedLimiter).roundToInt())
                        }
                    }
                    ACTION_PARKING_BRAKE ->
                        ThrottleBrakeImpl.parkingBrake(value)
                    ACTION_HANDBRAKE ->
                        ThrottleBrakeImpl.handbrake(value)
                    ACTION_BRAKING_STILL ->
                        ThrottleBrakeImpl.brake(value)
                    ACTION_NEUTRAL -> {
                        if (SetupImpl.handlingAssistanceState == SetupSystem.ASSISTANCE_NONE) {
                        ThrottleBrakeImpl.setNeutral()
                        }
                        else {
                            ElectronicThrottleBrakeImpl.setNeutral()
                        }
                    }
                    else ->
                        "${this::class.simpleName} ERROR: Entered in else condition"
                }
            }
        //}

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