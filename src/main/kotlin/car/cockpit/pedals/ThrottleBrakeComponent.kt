package car.cockpit.pedals

import car.cockpit.electrics.Electrics
import car.cockpit.engine.Engine
import car.cockpit.engine.SUCCESS
import car.cockpit.setup.*
import car.cockpit.steering.Steering
import car.cockpit.steering.Turn
import car.ecu.modules.dcm.Dcm
import car.showMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.*
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component("Throttle -n- Brake Component")
class ThrottleBrakeComponent: ThrottleBrake {

    @Autowired
    private lateinit var setupComponent: Setup

    @Autowired
    private lateinit var dcmComponent: Dcm

    @Autowired
    private lateinit var steeringComponent: Steering

    @Autowired
    private lateinit var engineComponent: Engine

    @Autowired
    private lateinit var electricsComponent: Electrics

    private val logger = LoggerFactory.getLogger(ThrottleBrakeComponent::class.java)

    private var action = Motion.NEUTRAL
    override var value = 0
        protected set
        /*private set -
            achieved by using val in the interface and use the interface instead of this class at the outside*/

    override val parkingBrakeState
        get() = action == Motion.PARKING_BRAKE && value == 100
    override val handbrakeState
        get() = action == Motion.HANDBRAKE && value == 100
    override val isMovingForward
        get() = action == Motion.FORWARD
    override val isMovingBackward
        get() = action == Motion.BACKWARD
    override val isBrakingStill
        get() = action == Motion.BRAKING_STILL
    override val isNeutral
        get() = action == Motion.NEUTRAL
    override val motionState:Motion
        get() = when {
            isNeutral -> Motion.NEUTRAL
            isBrakingStill -> Motion.BRAKING_STILL
            isMovingForward -> Motion.FORWARD
            isMovingBackward -> Motion.BACKWARD
            handbrakeState -> Motion.HANDBRAKE
            parkingBrakeState -> Motion.PARKING_BRAKE
            else -> Motion.NOTHING
        }

    override fun throttle(direction: Motion, value: Int): String {
        // turn on/off the braking lights
        if (this.value > value)
            CoroutineScope(Dispatchers.Default).launch {
                electricsComponent.brakingLightsState = true
                // turn them off after a while
                delay(500)
                electricsComponent.brakingLightsState = false
            }
        else
            CoroutineScope(Dispatchers.Default).launch { electricsComponent.brakingLightsState = false }


        // inform DCM of the ECU for forward and backward movement
        dcmComponent.valueOuterFront = value

        //////
        // Pi related
        if (engineComponent.runOnPi)
            calculateDifferentialValues(value)
        else
            showDifferentialInfo(value)
        //////

        if(direction == Motion.FORWARD){
            //////
            // Pi related
            if (engineComponent.runOnPi) {
                applyPinValues(motorFrontRightDigital = true, motorFrontLeftDigital = true,
                    motorRearRightDigital = true, motorRearLeftDigital = true)
            }
        }
        else if (direction == Motion.BACKWARD){
            //////
            // Pi related
            if (engineComponent.runOnPi) {
                applyPinValues(motorFrontRightDigital = false, motorFrontLeftDigital = false,
                    motorRearRightDigital = false, motorRearLeftDigital = false)
            }
        }
        action = direction

	    this.value = value
        return SUCCESS // or error message from pins
    }

    /* The value param will be used  for identification purposes only and
        will not be used as pin values
     */
    override fun brake(value: Int): String {
        CoroutineScope(Dispatchers.Default).launch { electricsComponent.brakingLightsState = true }
        //////
        // Pi related
        if (engineComponent.runOnPi) {
            applyPinValues(motorFrontRightPwm = 0 /*value*/, motorFrontRightDigital = false,
                motorFrontLeftPwm = 0 /*value*/, motorFrontLeftDigital = false,
                motorRearRightPwm = 0 /*value*/, motorRearRightDigital = false,
                motorRearLeftPwm = 0 /*value*/, motorRearLeftDigital = false)
        }
        //////

        action = Motion.BRAKING_STILL
        this.value = value
        return SUCCESS // or error message from pins
    }

    /* The value param will be used  for identification purposes only and
        will not be used as pin values
     */
    override fun parkingBrake(value: Int): String {
        if (value == 100)
            CoroutineScope(Dispatchers.Default).launch { electricsComponent.brakingLightsState = true }
        else if (value == 0)
            CoroutineScope(Dispatchers.Default).launch { electricsComponent.brakingLightsState = false }
        //////
        // Pi related
        if (engineComponent.runOnPi) {
            applyPinValues(motorFrontRightPwm = 0 /*value*/, motorFrontRightDigital = false,
                motorFrontLeftPwm = 0 /*value*/, motorFrontLeftDigital = false,
                motorRearRightPwm = 0 /*value*/, motorRearRightDigital = false,
                motorRearLeftPwm = 0 /*value*/, motorRearLeftDigital = false)
        }
        //////

        /* In else condition is better to let the car be at braking still than neutral
            because the car may be stopped in uphill or downhill and could slide down
            when the user deactivates parking brake from ImageView
         */
        action = if (value == 100) {
            Motion.PARKING_BRAKE
        }
        else
        {
            brake(100)
            Motion.BRAKING_STILL
        }
        this.value = value

        return SUCCESS // or error message from pins
    }

    /* The value param will be used  for identification purposes only and
        will not be used as pin values
     */
    override fun handbrake(value: Int): String {
        //////
        // Pi related
        if (engineComponent.runOnPi && value == 100) {
            // Affects only the rear wheels
            applyPinValues(motorRearRightPwm = 0 /*value*/, motorRearRightDigital = false,
                motorRearLeftPwm = 0 /*value*/, motorRearLeftDigital = false)
        }
        //////

        action = if (value == 100) {
            Motion.HANDBRAKE
        }
        else {
            /* TODO or NOTTODO: Uncomment the below when neutral state will work as it should
                At the moment the Android client (controller) when the handbrake is
                deactivated throttles according to the current throttle slider progress value.
             */
            // setNeutral()
            Motion.NEUTRAL
        }
        this.value = value

        return SUCCESS // or error message from pins
    }

    /* Neutral state is not achievable with H-bridge used.
        So, it works almost like the brake function.
    */
    override fun setNeutral(): String {
        CoroutineScope(Dispatchers.Default).launch { electricsComponent.brakingLightsState = false }
        //////
        // Pi related
        if (engineComponent.runOnPi) {
            applyPinValues(motorFrontRightPwm = 0 /*value*/, motorFrontRightDigital = false,
                motorFrontLeftPwm = 0 /*value*/, motorFrontLeftDigital = false,
                motorRearRightPwm = 0 /*value*/, motorRearRightDigital = false,
                motorRearLeftPwm = 0 /*value*/, motorRearLeftDigital = false)
        }
        //////

        action = Motion.NEUTRAL
        value = 0
        return SUCCESS // or error message from pins
    }

    override fun reset() {
        action = Motion.NEUTRAL
        value = 0
    }

    private fun applyPinValues(motorFrontRightPwm: Int? = null, motorFrontRightDigital: Boolean? = null,
                       motorFrontLeftPwm: Int? = null, motorFrontLeftDigital: Boolean? = null,
                       motorRearRightPwm: Int? = null, motorRearRightDigital: Boolean? = null,
                       motorRearLeftPwm: Int? = null, motorRearLeftDigital: Boolean? = null){

        motorFrontRightPwm?.let { engineComponent.motorFrontRightPwmPin.pwm = it }
        motorFrontRightDigital?.let { engineComponent.motorFrontRightDirPin.setState(it) }
        motorFrontLeftPwm?.let { engineComponent.motorFrontLeftPwmPin.pwm = it }
        motorFrontLeftDigital?.let { engineComponent.motorFrontLeftDirPin.setState(it) }
        motorRearRightPwm?.let { engineComponent.motorRearRightPwmPin.pwm = it }
        motorRearRightDigital?.let { engineComponent.motorRearRightDirPin.setState(it) }
        motorRearLeftPwm?.let { engineComponent.motorRearLeftPwmPin.pwm = it }
        motorRearLeftDigital?.let { engineComponent.motorRearLeftDirPin.setState(it) }
    }

    private fun calculateDifferentialValues(userThrottleValue: Int) {
        when (steeringComponent.direction) {
            Turn.LEFT -> {
                when (setupComponent.frontDifferentialSlipperyLimiter) {
                    DIFFERENTIAL_SLIPPERY_LIMITER_OPEN -> {
                        applyPinValues(motorFrontLeftPwm = dcmComponent.frontOpenDiffValues[0],
                            motorFrontRightPwm = dcmComponent.frontOpenDiffValues[1])
                    }
                    DIFFERENTIAL_SLIPPERY_LIMITER_MEDI_0 -> {
                        applyPinValues(motorFrontLeftPwm = dcmComponent.frontMedi0DiffValues[0],
                            motorFrontRightPwm = dcmComponent.frontMedi0DiffValues[1])
                    }
                    DIFFERENTIAL_SLIPPERY_LIMITER_MEDI_1 -> {
                        applyPinValues(motorFrontLeftPwm = dcmComponent.frontMedi1DiffValues[0],
                            motorFrontRightPwm = dcmComponent.frontMedi1DiffValues[1])
                    }
                    DIFFERENTIAL_SLIPPERY_LIMITER_MEDI_2 -> {
                        applyPinValues(motorFrontLeftPwm = dcmComponent.frontMedi2DiffValues[0],
                            motorFrontRightPwm = dcmComponent.frontMedi2DiffValues[1])
                    }
                    DIFFERENTIAL_SLIPPERY_LIMITER_LOCKED -> {
                        applyPinValues(motorFrontLeftPwm = dcmComponent.frontLockedDiffValues[0],
                            motorFrontRightPwm = dcmComponent.frontLockedDiffValues[1])
                    }
                    DIFFERENTIAL_SLIPPERY_LIMITER_AUTO -> {
                        applyPinValues(motorFrontLeftPwm = dcmComponent.frontAutoDiffValues[0],
                            motorFrontRightPwm = dcmComponent.frontAutoDiffValues[1])
                    }
                    else -> {
                        applyPinValues(motorFrontLeftPwm = 0, motorFrontRightPwm = 0)
                    }
                }
                when (setupComponent.rearDifferentialSlipperyLimiter) {
                    DIFFERENTIAL_SLIPPERY_LIMITER_OPEN -> {
                        applyPinValues(motorRearLeftPwm = dcmComponent.rearOpenDiffValues[0],
                            motorRearRightPwm = dcmComponent.rearOpenDiffValues[1])
                    }
                    DIFFERENTIAL_SLIPPERY_LIMITER_MEDI_0 -> {
                        applyPinValues(motorRearLeftPwm = dcmComponent.rearMedi0DiffValues[0],
                            motorRearRightPwm = dcmComponent.rearMedi0DiffValues[1])
                    }
                    DIFFERENTIAL_SLIPPERY_LIMITER_MEDI_1 -> {
                        applyPinValues(motorRearLeftPwm = dcmComponent.rearMedi1DiffValues[0],
                            motorRearRightPwm = dcmComponent.rearMedi1DiffValues[1])
                    }
                    DIFFERENTIAL_SLIPPERY_LIMITER_MEDI_2 -> {
                        applyPinValues(motorRearLeftPwm = dcmComponent.rearMedi2DiffValues[0],
                            motorRearRightPwm = dcmComponent.rearMedi2DiffValues[1])
                    }
                    DIFFERENTIAL_SLIPPERY_LIMITER_LOCKED -> {
                        applyPinValues(motorRearLeftPwm = dcmComponent.rearLockedDiffValues[0],
                            motorRearRightPwm = dcmComponent.rearLockedDiffValues[1])
                    }
                    DIFFERENTIAL_SLIPPERY_LIMITER_AUTO -> {
                        applyPinValues(motorRearLeftPwm = dcmComponent.rearAutoDiffValues[0],
                            motorRearRightPwm = dcmComponent.rearAutoDiffValues[1])
                    }
                    else -> {
                        applyPinValues(motorRearLeftPwm = 0, motorRearRightPwm = 0)
                    }
                }
                showMessage(logger = logger,
                body = "User Throttle Value: $userThrottleValue\n" +
                        "User Steering Value: ${steeringComponent.value}\n" +
                        "Front Differential is: ${setupComponent.frontDifferentialSlipperyLimiter}\n" +
                        "Inner (Left) Front Wheel Speed: ${engineComponent.motorFrontLeftPwmPin}\n" +
                        "Outer (Right) Front Wheel Speed: ${engineComponent.motorFrontRightPwmPin}\n" +
                        "Rear Differential is: ${setupComponent.rearDifferentialSlipperyLimiter}\n" +
                        "Inner (Left) Rear Wheel Speed: ${engineComponent.motorRearLeftPwmPin}\n" +
                        "Outer (Right) Rear Wheel Speed: ${engineComponent.motorRearRightPwmPin}"
                )
            }
            Turn.RIGHT -> {
                when (setupComponent.frontDifferentialSlipperyLimiter) {
                    DIFFERENTIAL_SLIPPERY_LIMITER_OPEN -> {
                        applyPinValues(motorFrontRightPwm = dcmComponent.frontOpenDiffValues[0],
                            motorFrontLeftPwm = dcmComponent.frontOpenDiffValues[1])
                    }
                    DIFFERENTIAL_SLIPPERY_LIMITER_MEDI_0 -> {
                        applyPinValues(motorFrontRightPwm = dcmComponent.frontMedi0DiffValues[0],
                            motorFrontLeftPwm = dcmComponent.frontMedi0DiffValues[1])
                    }
                    DIFFERENTIAL_SLIPPERY_LIMITER_MEDI_1 -> {
                        applyPinValues(motorFrontRightPwm = dcmComponent.frontMedi1DiffValues[0],
                            motorFrontLeftPwm = dcmComponent.frontMedi1DiffValues[1])
                    }
                    DIFFERENTIAL_SLIPPERY_LIMITER_MEDI_2 -> {
                        applyPinValues(motorFrontRightPwm = dcmComponent.frontMedi2DiffValues[0],
                            motorFrontLeftPwm = dcmComponent.frontMedi2DiffValues[1])
                    }
                    DIFFERENTIAL_SLIPPERY_LIMITER_LOCKED -> {
                        applyPinValues(motorFrontRightPwm = dcmComponent.frontLockedDiffValues[0],
                            motorFrontLeftPwm = dcmComponent.frontLockedDiffValues[1])
                    }
                    DIFFERENTIAL_SLIPPERY_LIMITER_AUTO -> {
                        applyPinValues(motorFrontRightPwm = dcmComponent.frontAutoDiffValues[0],
                            motorFrontLeftPwm = dcmComponent.frontAutoDiffValues[1])
                    }
                    else -> {
                        applyPinValues(motorFrontRightPwm = 0, motorFrontLeftPwm = 0)
                    }
                }
                when (setupComponent.rearDifferentialSlipperyLimiter) {
                    DIFFERENTIAL_SLIPPERY_LIMITER_OPEN -> {
                        applyPinValues(motorRearRightPwm = dcmComponent.rearOpenDiffValues[0],
                            motorRearLeftPwm = dcmComponent.rearOpenDiffValues[1])
                    }
                    DIFFERENTIAL_SLIPPERY_LIMITER_MEDI_0 -> {
                        applyPinValues(motorRearRightPwm = dcmComponent.rearMedi0DiffValues[0],
                            motorRearLeftPwm = dcmComponent.rearMedi0DiffValues[1])
                    }
                    DIFFERENTIAL_SLIPPERY_LIMITER_MEDI_1 -> {
                        applyPinValues(motorRearRightPwm = dcmComponent.rearMedi1DiffValues[0],
                            motorRearLeftPwm = dcmComponent.rearMedi1DiffValues[1])
                    }
                    DIFFERENTIAL_SLIPPERY_LIMITER_MEDI_2 -> {
                        applyPinValues(motorRearRightPwm = dcmComponent.rearMedi2DiffValues[0],
                            motorRearLeftPwm = dcmComponent.rearMedi2DiffValues[1])
                    }
                    DIFFERENTIAL_SLIPPERY_LIMITER_LOCKED -> {
                        applyPinValues(motorRearRightPwm = dcmComponent.rearLockedDiffValues[0],
                            motorRearLeftPwm = dcmComponent.rearLockedDiffValues[1])
                    }
                    DIFFERENTIAL_SLIPPERY_LIMITER_AUTO -> {
                        applyPinValues(motorRearRightPwm = dcmComponent.rearAutoDiffValues[0],
                            motorRearLeftPwm = dcmComponent.rearAutoDiffValues[1])
                    }
                    else -> {
                        applyPinValues(motorRearRightPwm = 0, motorRearLeftPwm = 0)
                    }
                }
                showMessage(logger = logger,
                    body = "User Throttle Value: $userThrottleValue\n" +
                            "User Steering Value: ${steeringComponent.value}\n" +
                            "Front Differential is: ${setupComponent.frontDifferentialSlipperyLimiter}\n" +
                            "Inner (Right) Front Wheel Speed: ${engineComponent.motorFrontRightPwmPin}\n" +
                            "Outer (Left) Front Wheel Speed: ${engineComponent.motorFrontLeftPwmPin}\n" +
                            "Rear Differential is: ${setupComponent.rearDifferentialSlipperyLimiter}\n" +
                            "Inner (Right) Rear Wheel Speed: ${engineComponent.motorRearRightPwmPin}\n" +
                            "Outer (Left) Rear Wheel Speed: ${engineComponent.motorRearLeftPwmPin}"
                )
            }
            else -> {
                applyPinValues(motorFrontRightPwm = userThrottleValue, motorFrontLeftPwm = userThrottleValue,
                    motorRearRightPwm = userThrottleValue, motorRearLeftPwm = userThrottleValue)
                showMessage(logger = logger,
                    body = "User Throttle Value: $userThrottleValue\n" +
                            "User Steering Value: ${steeringComponent.value}\n" +
                            "Front Differential is: ${setupComponent.frontDifferentialSlipperyLimiter}\n" +
                            "Right Front Wheel Speed: ${engineComponent.motorFrontRightPwmPin}\n" +
                            "Left Front Wheel Speed: ${engineComponent.motorFrontLeftPwmPin}\n" +
                            "Rear Differential is: ${setupComponent.rearDifferentialSlipperyLimiter}\n" +
                            "Right Rear Wheel Speed: ${engineComponent.motorRearRightPwmPin}\n" +
                            "Left Rear Wheel Speed: ${engineComponent.motorRearLeftPwmPin}"
                )
            }
        }
    }

    private fun showDifferentialInfo(userThrottleValue: Int) {
        showMessage(logger = logger,
            body = "User Throttle Value: $userThrottleValue\n" +
                "User Steering Value: ${steeringComponent.value}\n" +
        when (steeringComponent.direction) {
            Turn.LEFT -> {
                when (setupComponent.frontDifferentialSlipperyLimiter) {
                    DIFFERENTIAL_SLIPPERY_LIMITER_OPEN ->
                        "Front Differential is \"OPEN\"\n" +
                                "Inner (Left) Front Wheel Speed: ${dcmComponent.frontOpenDiffValues[0]}\n" +
                                "Outer (Right) Front Wheel Speed: ${dcmComponent.frontOpenDiffValues[1]}\n"
                    DIFFERENTIAL_SLIPPERY_LIMITER_MEDI_0 ->
                        "Front Differential is \"MEDI 0\"\n" +
                                "Inner (Left) Front Wheel Speed: ${dcmComponent.frontMedi0DiffValues[0]}\n" +
                                "Outer (Right) Front Wheel Speed: ${dcmComponent.frontMedi0DiffValues[1]}\n"
                    DIFFERENTIAL_SLIPPERY_LIMITER_MEDI_1 ->
                        "Front Differential is \"MEDI 1\"\n" +
                                "Inner (Left) Front Wheel Speed: ${dcmComponent.frontMedi1DiffValues[0]}\n" +
                                "Outer (Right) Front Wheel Speed: ${dcmComponent.frontMedi1DiffValues[1]}\n"
                    DIFFERENTIAL_SLIPPERY_LIMITER_MEDI_2 ->
                        "Front Differential is \"MEDI 2\"\n" +
                                "Inner (Left) Front Wheel Speed: ${dcmComponent.frontMedi2DiffValues[0]}\n" +
                                "Outer (Right) Front Wheel Speed: ${dcmComponent.frontMedi2DiffValues[1]}\n"
                    DIFFERENTIAL_SLIPPERY_LIMITER_LOCKED ->
                        "Front Differential is \"LOCKED\"\n" +
                                "Inner (Left) Front Wheel Speed: ${dcmComponent.frontLockedDiffValues[0]}\n" +
                                "Outer (Right) Front Wheel Speed: ${dcmComponent.frontLockedDiffValues[1]}\n"
                    DIFFERENTIAL_SLIPPERY_LIMITER_AUTO ->
                        "Front Differential is \"AUTO\"\n" +
                                "Inner (Left) Front Wheel Speed: ${dcmComponent.frontAutoDiffValues[0]}\n" +
                                "Outer (Right) Front Wheel Speed: ${dcmComponent.frontAutoDiffValues[1]}\n"
                    else -> "ERROR in Front differential for Left turn\n"
                } +
                when (setupComponent.rearDifferentialSlipperyLimiter) {
                    DIFFERENTIAL_SLIPPERY_LIMITER_OPEN ->
                        "Rear Differential is \"OPEN\"\n" +
                                "Inner (Left) Rear Wheel Speed: ${dcmComponent.rearOpenDiffValues[0]}\n" +
                                "Outer (Right) Rear Wheel Speed: ${dcmComponent.rearOpenDiffValues[1]}"
                    DIFFERENTIAL_SLIPPERY_LIMITER_MEDI_0 ->
                        "Rear Differential is \"MEDI 0\"\n" +
                                "Inner (Left) Rear Wheel Speed: ${dcmComponent.rearMedi0DiffValues[0]}\n" +
                                "Outer (Right) Rear Wheel Speed: ${dcmComponent.rearMedi0DiffValues[1]}"
                    DIFFERENTIAL_SLIPPERY_LIMITER_MEDI_1 ->
                        "Rear Differential is \"MEDI 1\"\n" +
                                "Inner (Left) Rear Wheel Speed: ${dcmComponent.rearMedi1DiffValues[0]}\n" +
                                "Outer (Right) Rear Wheel Speed: ${dcmComponent.rearMedi1DiffValues[1]}"
                    DIFFERENTIAL_SLIPPERY_LIMITER_MEDI_2 ->
                        "Rear Differential is \"MEDI 2\"\n" +
                                "Inner (Left) Rear Wheel Speed: ${dcmComponent.rearMedi2DiffValues[0]}\n" +
                                "Outer (Right) Rear Wheel Speed: ${dcmComponent.rearMedi2DiffValues[1]}"
                    DIFFERENTIAL_SLIPPERY_LIMITER_LOCKED ->
                        "Rear Differential is \"LOCKED\"\n" +
                                "Inner (Left) Rear Wheel Speed: ${dcmComponent.rearLockedDiffValues[0]}\n" +
                                "Outer (Right) Rear Wheel Speed: ${dcmComponent.rearLockedDiffValues[1]}"
                    DIFFERENTIAL_SLIPPERY_LIMITER_AUTO ->
                        "Rear Differential is \"AUTO\"\n" +
                                "Inner (Left) Rear Wheel Speed: ${dcmComponent.rearAutoDiffValues[0]}\n" +
                                "Outer (Right) Rear Wheel Speed: ${dcmComponent.rearAutoDiffValues[1]}"
                    else -> "ERROR in Rear differential for Left turn"
                }
            }
            Turn.RIGHT -> {
                when (setupComponent.frontDifferentialSlipperyLimiter) {
                    DIFFERENTIAL_SLIPPERY_LIMITER_OPEN ->
                        "Front Differential is \"OPEN\"\n" +
                                "Inner (Right) Front Wheel Speed: ${dcmComponent.frontOpenDiffValues[0]}\n" +
                                "Outer (Left) Front Wheel Speed: ${dcmComponent.frontOpenDiffValues[1]}\n"
                    DIFFERENTIAL_SLIPPERY_LIMITER_MEDI_0 ->
                        "Front Differential is \"MEDI 0\"\n" +
                                "Inner (Right) Front Wheel Speed: ${dcmComponent.frontMedi0DiffValues[0]}\n" +
                                "Outer (Left) Front Wheel Speed: ${dcmComponent.frontMedi0DiffValues[1]}\n"
                    DIFFERENTIAL_SLIPPERY_LIMITER_MEDI_1 ->
                        "Front Differential is \"MEDI 1\"\n" +
                                "Inner (Right) Front Wheel Speed: ${dcmComponent.frontMedi1DiffValues[0]}\n" +
                                "Outer (Left) Front Wheel Speed: ${dcmComponent.frontMedi1DiffValues[1]}\n"
                    DIFFERENTIAL_SLIPPERY_LIMITER_MEDI_2 ->
                        "Front Differential is \"MEDI 2\"\n" +
                                "Inner (Right) Front Wheel Speed: ${dcmComponent.frontMedi2DiffValues[0]}\n" +
                                "Outer (Left) Front Wheel Speed: ${dcmComponent.frontMedi2DiffValues[1]}\n"
                    DIFFERENTIAL_SLIPPERY_LIMITER_LOCKED ->
                        "Front Differential is \"LOCKED\"\n" +
                                "Inner (Right) Front Wheel Speed: ${dcmComponent.frontLockedDiffValues[0]}\n" +
                                "Outer (Left) Front Wheel Speed: ${dcmComponent.frontLockedDiffValues[1]}\n"
                    DIFFERENTIAL_SLIPPERY_LIMITER_AUTO ->
                        "Front Differential is \"AUTO\"\n" +
                                "Inner (Right) Front Wheel Speed: ${dcmComponent.frontAutoDiffValues[0]}\n" +
                                "Outer (Left) Front Wheel Speed: ${dcmComponent.frontAutoDiffValues[1]}\n"
                    else -> "ERROR in Front differential (${setupComponent.frontDifferentialSlipperyLimiter}) for Right turn\n"
                } +
                when (setupComponent.rearDifferentialSlipperyLimiter) {
                    DIFFERENTIAL_SLIPPERY_LIMITER_OPEN ->
                        "Rear Differential is \"OPEN\"\n" +
                                "Inner (Right) Rear Wheel Speed: ${dcmComponent.rearOpenDiffValues[0]}\n" +
                                "Outer (Left) Rear Wheel Speed: ${dcmComponent.rearOpenDiffValues[1]}"
                    DIFFERENTIAL_SLIPPERY_LIMITER_MEDI_0 ->
                        "Rear Differential is \"MEDI 0\"\n" +
                                "Inner (Right) Rear Wheel Speed: ${dcmComponent.rearMedi0DiffValues[0]}\n" +
                                "Outer (Left) Rear Wheel Speed: ${dcmComponent.rearMedi0DiffValues[1]}"
                    DIFFERENTIAL_SLIPPERY_LIMITER_MEDI_1 ->
                        "Rear Differential is \"MEDI 1\"\n" +
                                "Inner (Right) Rear Wheel Speed: ${dcmComponent.rearMedi1DiffValues[0]}\n" +
                                "Outer (Left) Rear Wheel Speed: ${dcmComponent.rearMedi1DiffValues[1]}"
                    DIFFERENTIAL_SLIPPERY_LIMITER_MEDI_2 ->
                        "Rear Differential is \"MEDI 2\"\n" +
                                "Inner (Right) Rear Wheel Speed: ${dcmComponent.rearMedi2DiffValues[0]}\n" +
                                "Outer (Left) Rear Wheel Speed: ${dcmComponent.rearMedi2DiffValues[1]}"
                    DIFFERENTIAL_SLIPPERY_LIMITER_LOCKED ->
                        "Rear Differential is \"LOCKED\"\n" +
                                "Inner (Right) Rear Wheel Speed: ${dcmComponent.rearLockedDiffValues[0]}\n" +
                                "Outer (Left) Rear Wheel Speed: ${dcmComponent.rearLockedDiffValues[1]}"
                    DIFFERENTIAL_SLIPPERY_LIMITER_AUTO ->
                        "Rear Differential is \"AUTO\"\n" +
                                "Inner (Right) Rear Wheel Speed: ${dcmComponent.rearAutoDiffValues[0]}\n" +
                                "Outer (Left) Rear Wheel Speed: ${dcmComponent.rearAutoDiffValues[1]}"
                    else -> "ERROR in Rear differential (${setupComponent.rearDifferentialSlipperyLimiter}) for Right turn"
                }
            }
            else -> "Steering value: ${steeringComponent.value}\n"
        })
    }
}