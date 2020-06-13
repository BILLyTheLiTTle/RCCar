package car.cockpit.pedals

import car.cockpit.engine.UNKNOWN_STATE
import car.cockpit.setup.*
import car.enumContains
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

        val motion = if (enumContains<Motion>(action)) Motion.valueOf(action) else Motion.NOTHING

        showMessage(logger = logger,
            body = "Action: ${motion.name}\n" +
                    "Primitive User Value: $value\n" +
                    if (motion == Motion.FORWARD || motion == Motion.BACKWARD) {
                        "Limited User Value: " +
                                "${(value * setupComponent.motorSpeedLimiter.value).roundToInt()}\n"
                    }
                    else {
                        ""
                    }
                    +
                    "ID request: $id\n" +
                    "ID last request: $lastRequestId")

        //TODO add function for the raspi
        var state = UNKNOWN_STATE
        // I don't think I need synchronization
        //synchronized(this){
        if(id == lastRequestId) {
            state = when (motion) {
                Motion.FORWARD, Motion.BACKWARD -> {
                    when (setupComponent.handlingAssistanceState) {
                        HandlingAssistance.MANUAL ->
                            throttleBrake.throttle(motion,
                                (value * setupComponent.motorSpeedLimiter.value).roundToInt())
                        HandlingAssistance.NULL ->
                            throttleBrake.parkingBrake(100)
                        else ->
                            electronicThrottleBrake.throttle(motion,
                                (value * setupComponent.motorSpeedLimiter.value).roundToInt())
                    }
                }
                Motion.PARKING_BRAKE ->
                    throttleBrake.parkingBrake(value)
                Motion.HANDBRAKE ->
                    throttleBrake.handbrake(value)
                Motion.BRAKING_STILL ->
                    throttleBrake.brake(value)
                Motion.NEUTRAL -> {
                    when (setupComponent.handlingAssistanceState) {
                        HandlingAssistance.MANUAL, HandlingAssistance.NULL ->
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

    fun getMotionState() = throttleBrake.motionState.name
}

