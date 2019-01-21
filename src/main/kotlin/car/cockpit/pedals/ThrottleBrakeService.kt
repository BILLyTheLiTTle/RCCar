package car.cockpit.pedals

import car.cockpit.engine.UNKNOWN_STATE
import car.cockpit.setup.*
import car.showMessage
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import kotlin.math.roundToInt

@Service("Throttle -n- Brake Service")
class ThrottleBrakeService {

    @Autowired
    @Qualifier("Throttle -n- Brake Component")
    private lateinit var throttleBrake: ThrottleBrake

    @Autowired
    @Qualifier("Electronic Throttle -n Brake Component")
    private lateinit var electronicThrottleBrake: ThrottleBrake

    @Autowired
    private lateinit var setupComponent: Setup

    private val logger = LoggerFactory.getLogger(ThrottleBrakeService::class.java)

    var lastRequestId = -1L

    fun setThrottleBrakeAction(id: Long, action: String, value: Int): String {

        lastRequestId = if(id > lastRequestId) id else return "Wrong Request ID: $id"

        showMessage(logger = logger,
            body = "Action: $action\n" +
                    "Primitive User Value: $value\n" +
                    if (action == ACTION_MOVE_FORWARD || action == ACTION_MOVE_BACKWARD) {
                        "Limited User Value: " +
                                "${(value * setupComponent.motorSpeedLimiter).roundToInt()}\n"
                    }
                    else {
                        ""
                    }
                    +
                    "ID request: $id\n" +
                    "ID last request: $lastRequestId")

        //TODO add function for the pins
        var state = UNKNOWN_STATE
        // I don't think I need synchronization
        //synchronized(this){
        if(id == lastRequestId) {
            state = when (action) {
                ACTION_MOVE_FORWARD, ACTION_MOVE_BACKWARD -> {
                    when (setupComponent.handlingAssistanceState) {
                        ASSISTANCE_NONE ->
                            throttleBrake.throttle(action,
                                (value * setupComponent.motorSpeedLimiter).roundToInt())
                        ASSISTANCE_NULL ->
                            throttleBrake.parkingBrake(100)
                        else ->
                            electronicThrottleBrake.throttle(action,
                                (value * setupComponent.motorSpeedLimiter).roundToInt())
                    }
                }
                ACTION_PARKING_BRAKE ->
                    throttleBrake.parkingBrake(value)
                ACTION_HANDBRAKE ->
                    throttleBrake.handbrake(value)
                ACTION_BRAKING_STILL ->
                    throttleBrake.brake(value)
                ACTION_NEUTRAL -> {
                    when (setupComponent.handlingAssistanceState) {
                        ASSISTANCE_NONE, ASSISTANCE_NULL ->
                            throttleBrake.setNeutral()
                        else ->
                            electronicThrottleBrake.setNeutral()
                    }
                }
                else ->
                    "${this::class.simpleName} ERROR: Entered in else condition"
            }
        }
        //}

        return state
    }

    fun getParkingBrakeState() = throttleBrake.parkingBrakeState

    fun getHandbrakeState() = throttleBrake.handbrakeState

    fun getMotionState() = throttleBrake.motionState
}

const val ACTION_MOVE_FORWARD = "forward"
const val ACTION_MOVE_BACKWARD = "backward"
const val ACTION_NEUTRAL = "neutral"
const val ACTION_BRAKING_STILL = "braking_still"
const val ACTION_PARKING_BRAKE = "parking_brake"
const val ACTION_HANDBRAKE = "handbrake"