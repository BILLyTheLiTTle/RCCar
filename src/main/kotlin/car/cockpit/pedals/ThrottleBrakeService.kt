package car.cockpit.pedals

import car.cockpit.engine.UNKNOWN_STATE
import car.cockpit.setup.ASSISTANCE_NONE
import car.cockpit.setup.ASSISTANCE_NULL
import car.cockpit.setup.SetupImpl
import car.cockpit.setup.SetupController
import car.showMessage
import org.springframework.stereotype.Service
import kotlin.math.roundToInt

@Service("Throttle -n- Brake")
class ThrottleBrakeService {

    var lastRequestId = -1L

    fun setThrottleBrakeAction(id: Long, action: String, value: Int): String {

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
        var state = UNKNOWN_STATE
        // I don't think I need synchronization
        //synchronized(this){
        if(id == lastRequestId) {
            state = when (action) {
                ACTION_MOVE_FORWARD, ACTION_MOVE_BACKWARD -> {
                    when (SetupImpl.handlingAssistanceState) {
                        ASSISTANCE_NONE ->
                            ThrottleBrakeImpl.throttle(action,
                                (value * SetupImpl.motorSpeedLimiter).roundToInt())
                        ASSISTANCE_NULL ->
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
                        ASSISTANCE_NONE, ASSISTANCE_NULL ->
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

    fun getParkingBrakeState() = ThrottleBrakeImpl.parkingBrakeState

    fun getHandbrakeState() = ThrottleBrakeImpl.handbrakeState

    fun getMotionState() = ThrottleBrakeImpl.motionState
}

const val ACTION_MOVE_FORWARD = "forward"
const val ACTION_MOVE_BACKWARD = "backward"
const val ACTION_NEUTRAL = "neutral"
const val ACTION_BRAKING_STILL = "braking_still"
const val ACTION_PARKING_BRAKE = "parking_brake"
const val ACTION_HANDBRAKE = "handbrake"