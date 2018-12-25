package car.cockpit.pedals

import car.cockpit.engine.EngineSystem
import car.cockpit.setup.SetupSystem
import car.cockpit.setup.SetupImpl
import car.showMessage
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import kotlin.math.roundToInt

@RestController
class ThrottleBrakePedal{

    @GetMapping("/set_throttle_brake_system")
    fun setThrottleBrakeAction(@RequestParam(value = "id", defaultValue = "-1") id: Long,
                               @RequestParam(value = "action", defaultValue = ACTION_NEUTRAL) action: String,
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

        //TODO add function for the pins
        var state = EngineSystem.UNKNOWN_STATE
        // I don't think I need synchronization
        //synchronized(this){
            if(id == lastRequestId) {
                state = when (action) {
                    ACTION_MOVE_FORWARD, ACTION_MOVE_BACKWARD -> {
                        when (SetupImpl.handlingAssistanceState) {
                            SetupSystem.ASSISTANCE_NONE ->
                                ThrottleBrakeImpl.throttle(action,
                                    (value * SetupImpl.motorSpeedLimiter).roundToInt())
                            SetupSystem.ASSISTANCE_NULL ->
                                ThrottleBrakeImpl.parkingBrake(100)
                            else ->
                                ElectronicThrottleBrakeImpl.throttle(action,
                                    (value * SetupImpl.motorSpeedLimiter).roundToInt())
                        }
                    }
                    ACTION_PARKING_BRAKE ->
                        ThrottleBrakeImpl.parkingBrake(value)
                    ACTION_HANDBRAKE ->
                        ThrottleBrakeImpl.handbrake(value)
                    ACTION_BRAKING_STILL ->
                        ThrottleBrakeImpl.brake(value)
                    ACTION_NEUTRAL -> {
                        when (SetupImpl.handlingAssistanceState) {
                            SetupSystem.ASSISTANCE_NONE, SetupSystem.ASSISTANCE_NULL ->
                                ThrottleBrakeImpl.setNeutral()
                            else ->
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
        var lastRequestId = -1L

        const val ACTION_MOVE_FORWARD = "forward"
        const val ACTION_MOVE_BACKWARD = "backward"
        const val ACTION_NEUTRAL = "neutral"
        const val ACTION_BRAKING_STILL = "braking_still"
        const val ACTION_PARKING_BRAKE = "parking_brake"
        const val ACTION_HANDBRAKE = "handbrake"
    }

}