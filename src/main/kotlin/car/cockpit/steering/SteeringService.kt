package car.cockpit.steering

import car.cockpit.engine.UNKNOWN_STATE
import car.cockpit.pedals.ThrottleBrake
import car.cockpit.setup.HandlingAssistance
import car.cockpit.setup.Setup
import car.enumContains
import car.showMessage
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service

@Service("Steering Service")
class SteeringService {

    @Autowired
    @Qualifier("Throttle -n- Brake Component")
    private lateinit var throttleBrake: ThrottleBrake

    @Autowired
    @Qualifier("Electronic Throttle -n Brake Component")
    private lateinit var electronicThrottleBrake: ThrottleBrake

    @Autowired
    private lateinit var setupComponent: Setup

    @Autowired
    private lateinit var steeringComponent: Steering

    private val logger = LoggerFactory.getLogger(SteeringService::class.java)

    var lastRequestId = -1L

    fun setSteeringAction(id: Long, direction: String, value: String): String {

        lastRequestId = if(id > lastRequestId) id else return "Wrong Request ID: $id"

        val turn = if (enumContains<Turn>(direction)) Turn.valueOf(direction) else Turn.NOTHING
        val angle = if (enumContains<SteeringValue>(value)) SteeringValue.valueOf(value) else SteeringValue.NOTHING

        showMessage(logger = logger,
            body = "Direction: $turn\n" +
                    "Value: $angle\n" +
                    "ID request: $id\n" +
                    "ID last request: $lastRequestId")

        //TODO add function for the raspi
        var state = UNKNOWN_STATE
        // I don't think I need synchronization
        //synchronized(this){
        if(id == lastRequestId) {
            state = when (turn) {
                Turn.LEFT ->
                    //TODO turn left with value
                    steeringComponent.turn(Turn.LEFT, angle)
                Turn.RIGHT ->
                    //TODO turn right with value
                    steeringComponent.turn(Turn.RIGHT, angle)
                Turn.STRAIGHT ->
                    //TODO go straight with no value
                    steeringComponent.turn(Turn.STRAIGHT)
                else ->
                    "${this::class.simpleName} ERROR: Entered in else condition"
            }

            /* This function is called because the throttle values, due to differential functionality,
                must be updated also when the throttle is steady but the steering angle is changing.
                For example:
                1). When the user turns more to the same direction
                2). When the user turns to the opposite direction
             */
            when (setupComponent.handlingAssistanceState) {
                HandlingAssistance.MANUAL ->
                    throttleBrake.throttle(throttleBrake.motionState, throttleBrake.value)
                HandlingAssistance.NULL ->
                    throttleBrake.parkingBrake(100)
                else ->
                    electronicThrottleBrake.throttle(throttleBrake.motionState, throttleBrake.value)
            }
        }
        //}

        return state
    }

    fun getSteeringDirection() = steeringComponent.direction.name
}

