package car.cockpit.steering

import car.cockpit.engine.EngineSystem
import car.cockpit.setup.SetupSystem
import car.cockpit.pedals.ElectronicThrottleBrakeImpl
import car.cockpit.setup.SetupImpl
import car.cockpit.pedals.ThrottleBrakeImpl
import car.showMessage
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class SteeringWheel{

    @GetMapping("/set_steering_system")
    fun setSteeringAction(
        @RequestParam(value = "id", defaultValue = "-1") id: Long,
        @RequestParam(value = "direction", defaultValue = ACTION_STRAIGHT) direction: String,
        @RequestParam(value = "value", defaultValue =  "$STEERING_VALUE_00") value: Int
    ): String {

        lastRequestId = if(id > lastRequestId) id else return "Wrong Request ID: $id"

        showMessage(title = "STEERING SYSTEM",
            body = "Direction: $direction\n" +
                    "Value: $value\n" +
                    "{ ${this::class.simpleName} } ID request: $id\n" +
                    "{ ${this::class.simpleName} } ID last request: $lastRequestId")

        //TODO add function for the pins
        var state = EngineSystem.UNKNOWN_STATE
        // I don't think I need synchronization
        //synchronized(this){
            if(id == lastRequestId) {
                state = when (direction) {
                    ACTION_TURN_LEFT ->
                        //TODO turn left with value
                        SteeringImpl.turn(ACTION_TURN_LEFT, value)
                    ACTION_TURN_RIGHT ->
                        //TODO turn right with value
                        SteeringImpl.turn(ACTION_TURN_RIGHT, value)
                    ACTION_STRAIGHT ->
                        //TODO go straight with no value
                        SteeringImpl.turn(ACTION_STRAIGHT)
                    else ->
                        "${this::class.simpleName} ERROR: Entered in else condition"
                }

                /* This function is called because the throttle values, due to differential functionality,
                    must be updated also when the throttle is steady but the steering angle is changing.
                    For example:
                    1). When the user turns more to the same direction
                    2). When the user turns to the opposite direction
                 */
                when (SetupImpl.handlingAssistanceState) {
                    SetupSystem.ASSISTANCE_NONE ->
                        ThrottleBrakeImpl.throttle(ThrottleBrakeImpl.motionState, ThrottleBrakeImpl.value)
                    SetupSystem.ASSISTANCE_NULL ->
                        ThrottleBrakeImpl.parkingBrake(100)
                    else ->
                        ElectronicThrottleBrakeImpl.throttle(ThrottleBrakeImpl.motionState, ThrottleBrakeImpl.value)
                }
            }
        //}

        return state
    }

    @GetMapping("/get_steering_direction")
    fun getSteeringDirection() = SteeringImpl.direction

    companion object {
        var lastRequestId = -1L

        const val ACTION_TURN_RIGHT = "right"
        const val ACTION_TURN_LEFT = "left"
        const val ACTION_STRAIGHT = "straight"

        const val STEERING_VALUE_00 = 0
        const val STEERING_VALUE_20 = 20
        const val STEERING_VALUE_40 = 40
        const val STEERING_VALUE_60 = 60
        const val STEERING_VALUE_80 = 80
        const val STEERING_VALUE_100 = 100
    }

}