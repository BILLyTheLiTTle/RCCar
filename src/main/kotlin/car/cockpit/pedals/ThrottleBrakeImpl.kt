package car.cockpit.pedals

import car.cockpit.electrics.ElectricsImpl
import car.cockpit.engine.EMPTY_STRING
import car.cockpit.engine.EngineImpl
import car.ecu.modules.dcm.DcmImpl
import car.cockpit.engine.SUCCESS
import car.cockpit.setup.*
import car.cockpit.steering.ACTION_TURN_LEFT
import car.cockpit.steering.ACTION_TURN_RIGHT
import car.cockpit.steering.SteeringImpl
import car.cockpit.steering.SteeringController
import car.showMessage
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch

object ThrottleBrakeImpl: ThrottleBrake {

    private var action = ACTION_NEUTRAL
    var value = 0
        private set

    override val parkingBrakeState
        get() = action == ACTION_PARKING_BRAKE && value == 100
    override val handbrakeState
        get() = action == ACTION_HANDBRAKE && value == 100
    override val isMovingForward
        get() = action == ACTION_MOVE_FORWARD
    override val isMovingBackward
        get() = action == ACTION_MOVE_BACKWARD
    override val isBrakingStill
        get() = action == ACTION_BRAKING_STILL
    override val isNeutral
        get() = action == ACTION_NEUTRAL
    override val motionState
        get() = when {
            isNeutral -> ACTION_NEUTRAL
            isBrakingStill -> ACTION_BRAKING_STILL
            isMovingForward -> ACTION_MOVE_FORWARD
            isMovingBackward -> ACTION_MOVE_BACKWARD
            handbrakeState -> ACTION_HANDBRAKE
            parkingBrakeState -> ACTION_PARKING_BRAKE
            else -> EMPTY_STRING
        }

    override fun throttle(direction: String, value: Int): String {
        // turn on/off the braking lights
        if (ThrottleBrakeImpl.value > value)
            launch {
                ElectricsImpl.brakingLightsState = true
                // turn them off after a while
                delay(500)
                ElectricsImpl.brakingLightsState = false
            }
        else
            launch { ElectricsImpl.brakingLightsState = false }


        // inform TCM of the ECU for forward and backward movement
        DcmImpl.valueOuterFront = value

        //////
        // Pi related
        if (EngineImpl.RUN_ON_PI)
            calculateDifferentialValues(value)
        else
            showDifferentialInfo(value)
        //////

        if(direction == ACTION_MOVE_FORWARD){
            //////
            // Pi related
            if (EngineImpl.RUN_ON_PI) {
                applyPinValues(motorFrontRightDigital = true, motorFrontLeftDigital = true,
                    motorRearRightDigital = true, motorRearLeftDigital = true)
            }
        }
        else if (direction == ACTION_MOVE_BACKWARD){
            //////
            // Pi related
            if (EngineImpl.RUN_ON_PI) {
                applyPinValues(motorFrontRightDigital = false, motorFrontLeftDigital = false,
                    motorRearRightDigital = false, motorRearLeftDigital = false)
            }
        }
        action = direction

	    ThrottleBrakeImpl.value = value
        return SUCCESS // or error message from pins
    }

    /* The value param will be used  for identification purposes only and
        will not be used as pin values
     */
    override fun brake(value: Int): String {
        launch { ElectricsImpl.brakingLightsState = true }
        //////
        // Pi related
        if (EngineImpl.RUN_ON_PI) {
            applyPinValues(motorFrontRightPwm = 0 /*value*/, motorFrontRightDigital = false,
                motorFrontLeftPwm = 0 /*value*/, motorFrontLeftDigital = false,
                motorRearRightPwm = 0 /*value*/, motorRearRightDigital = false,
                motorRearLeftPwm = 0 /*value*/, motorRearLeftDigital = false)
        }
        //////

        action = ACTION_BRAKING_STILL
	    ThrottleBrakeImpl.value = value
        return SUCCESS // or error message from pins
    }

    /* The value param will be used  for identification purposes only and
        will not be used as pin values
     */
    override fun parkingBrake(value: Int): String {
        if (value == 100)
            launch { ElectricsImpl.brakingLightsState = true }
        else if (value == 0)
            launch { ElectricsImpl.brakingLightsState = false }
        //////
        // Pi related
        if (EngineImpl.RUN_ON_PI) {
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
            ACTION_PARKING_BRAKE
        }
        else
        {
            brake(100)
            ACTION_BRAKING_STILL
        }
        ThrottleBrakeImpl.value = value

        return SUCCESS // or error message from pins
    }

    /* The value param will be used  for identification purposes only and
        will not be used as pin values
     */
    override fun handbrake(value: Int): String {
        //////
        // Pi related
        if (EngineImpl.RUN_ON_PI && value == 100) {
            // Affects only the rear wheels
            applyPinValues(motorRearRightPwm = 0 /*value*/, motorRearRightDigital = false,
                motorRearLeftPwm = 0 /*value*/, motorRearLeftDigital = false)
        }
        //////

        action = if (value == 100) {
            ACTION_HANDBRAKE
        }
        else {
            /* TODO or NOTTODO: Uncomment the below when neutral state will work as it should
                At the moment the Android client (controller) when the handbrake is
                deactivated throttles according to the current throttle slider progress value.
             */
            // setNeutral()
            ACTION_NEUTRAL
        }
	    ThrottleBrakeImpl.value = value

        return SUCCESS // or error message from pins
    }

    /* Neutral state is not achievable with H-bridge used.
        So, it works almost like the brake function.
    */
    override fun setNeutral(): String {
        launch { ElectricsImpl.brakingLightsState = false }
        //////
        // Pi related
        if (EngineImpl.RUN_ON_PI) {
            applyPinValues(motorFrontRightPwm = 0 /*value*/, motorFrontRightDigital = false,
                motorFrontLeftPwm = 0 /*value*/, motorFrontLeftDigital = false,
                motorRearRightPwm = 0 /*value*/, motorRearRightDigital = false,
                motorRearLeftPwm = 0 /*value*/, motorRearLeftDigital = false)
        }
        //////

        action = ACTION_NEUTRAL
        value = 0
        return SUCCESS // or error message from pins
    }

    override fun reset() {
        action = ACTION_NEUTRAL
        value = 0
    }

    private fun calculateDifferentialValues(userThrottleValue: Int) {
        when (SteeringImpl.direction) {
            ACTION_TURN_LEFT -> {
                when (SetupImpl.frontDifferentialSlipperyLimiter) {
                    DIFFERENTIAL_SLIPPERY_LIMITER_OPEN -> {
                        applyPinValues(motorFrontLeftPwm = DcmImpl.frontOpenDiffValues[0],
                            motorFrontRightPwm = DcmImpl.frontOpenDiffValues[1])
                    }
                    DIFFERENTIAL_SLIPPERY_LIMITER_MEDI_0 -> {
                        applyPinValues(motorFrontLeftPwm = DcmImpl.frontMedi0DiffValues[0],
                            motorFrontRightPwm = DcmImpl.frontMedi0DiffValues[1])
                    }
                    DIFFERENTIAL_SLIPPERY_LIMITER_MEDI_1 -> {
                        applyPinValues(motorFrontLeftPwm = DcmImpl.frontMedi1DiffValues[0],
                            motorFrontRightPwm = DcmImpl.frontMedi1DiffValues[1])
                    }
                    DIFFERENTIAL_SLIPPERY_LIMITER_MEDI_2 -> {
                        applyPinValues(motorFrontLeftPwm = DcmImpl.frontMedi2DiffValues[0],
                            motorFrontRightPwm = DcmImpl.frontMedi2DiffValues[1])
                    }
                    DIFFERENTIAL_SLIPPERY_LIMITER_LOCKED -> {
                        applyPinValues(motorFrontLeftPwm = DcmImpl.frontLockedDiffValues[0],
                            motorFrontRightPwm = DcmImpl.frontLockedDiffValues[1])
                    }
                    DIFFERENTIAL_SLIPPERY_LIMITER_AUTO -> {
                        applyPinValues(motorFrontLeftPwm = DcmImpl.frontAutoDiffValues[0],
                            motorFrontRightPwm = DcmImpl.frontAutoDiffValues[1])
                    }
                    else -> {
                        applyPinValues(motorFrontLeftPwm = 0, motorFrontRightPwm = 0)
                    }
                }
                when (SetupImpl.rearDifferentialSlipperyLimiter) {
                    DIFFERENTIAL_SLIPPERY_LIMITER_OPEN -> {
                        applyPinValues(motorRearLeftPwm = DcmImpl.rearOpenDiffValues[0],
                            motorRearRightPwm = DcmImpl.rearOpenDiffValues[1])
                    }
                    DIFFERENTIAL_SLIPPERY_LIMITER_MEDI_0 -> {
                        applyPinValues(motorRearLeftPwm = DcmImpl.rearMedi0DiffValues[0],
                            motorRearRightPwm = DcmImpl.rearMedi0DiffValues[1])
                    }
                    DIFFERENTIAL_SLIPPERY_LIMITER_MEDI_1 -> {
                        applyPinValues(motorRearLeftPwm = DcmImpl.rearMedi1DiffValues[0],
                            motorRearRightPwm = DcmImpl.rearMedi1DiffValues[1])
                    }
                    DIFFERENTIAL_SLIPPERY_LIMITER_MEDI_2 -> {
                        applyPinValues(motorRearLeftPwm = DcmImpl.rearMedi2DiffValues[0],
                            motorRearRightPwm = DcmImpl.rearMedi2DiffValues[1])
                    }
                    DIFFERENTIAL_SLIPPERY_LIMITER_LOCKED -> {
                        applyPinValues(motorRearLeftPwm = DcmImpl.rearLockedDiffValues[0],
                            motorRearRightPwm = DcmImpl.rearLockedDiffValues[1])
                    }
                    DIFFERENTIAL_SLIPPERY_LIMITER_AUTO -> {
                        applyPinValues(motorRearLeftPwm = DcmImpl.rearAutoDiffValues[0],
                            motorRearRightPwm = DcmImpl.rearAutoDiffValues[1])
                    }
                    else -> {
                        applyPinValues(motorRearLeftPwm = 0, motorRearRightPwm = 0)
                    }
                }
                showMessage(title = "-- ECU -- THROTTLE -N- BRAKE SYSTEM",
                body = "User Throttle Value: $userThrottleValue\n" +
                        "User Steering Value: ${SteeringImpl.value}\n" +
                        "Front Differential is: ${SetupImpl.frontDifferentialSlipperyLimiter}\n" +
                        "Inner (Left) Front Wheel Speed: ${EngineImpl.motorFrontLeftPwmPin}\n" +
                        "Outer (Right) Front Wheel Speed: ${EngineImpl.motorFrontRightPwmPin}\n" +
                        "Rear Differential is: ${SetupImpl.rearDifferentialSlipperyLimiter}\n" +
                        "Inner (Left) Rear Wheel Speed: ${EngineImpl.motorRearLeftPwmPin}\n" +
                        "Outer (Right) Rear Wheel Speed: ${EngineImpl.motorRearRightPwmPin}"
                )
            }
            ACTION_TURN_RIGHT -> {
                when (SetupImpl.frontDifferentialSlipperyLimiter) {
                    DIFFERENTIAL_SLIPPERY_LIMITER_OPEN -> {
                        applyPinValues(motorFrontRightPwm = DcmImpl.frontOpenDiffValues[0],
                            motorFrontLeftPwm = DcmImpl.frontOpenDiffValues[1])
                    }
                    DIFFERENTIAL_SLIPPERY_LIMITER_MEDI_0 -> {
                        applyPinValues(motorFrontRightPwm = DcmImpl.frontMedi0DiffValues[0],
                            motorFrontLeftPwm = DcmImpl.frontMedi0DiffValues[1])
                    }
                    DIFFERENTIAL_SLIPPERY_LIMITER_MEDI_1 -> {
                        applyPinValues(motorFrontRightPwm = DcmImpl.frontMedi1DiffValues[0],
                            motorFrontLeftPwm = DcmImpl.frontMedi1DiffValues[1])
                    }
                    DIFFERENTIAL_SLIPPERY_LIMITER_MEDI_2 -> {
                        applyPinValues(motorFrontRightPwm = DcmImpl.frontMedi2DiffValues[0],
                            motorFrontLeftPwm = DcmImpl.frontMedi2DiffValues[1])
                    }
                    DIFFERENTIAL_SLIPPERY_LIMITER_LOCKED -> {
                        applyPinValues(motorFrontRightPwm = DcmImpl.frontLockedDiffValues[0],
                            motorFrontLeftPwm = DcmImpl.frontLockedDiffValues[1])
                    }
                    DIFFERENTIAL_SLIPPERY_LIMITER_AUTO -> {
                        applyPinValues(motorFrontRightPwm = DcmImpl.frontAutoDiffValues[0],
                            motorFrontLeftPwm = DcmImpl.frontAutoDiffValues[1])
                    }
                    else -> {
                        applyPinValues(motorFrontRightPwm = 0, motorFrontLeftPwm = 0)
                    }
                }
                when (SetupImpl.rearDifferentialSlipperyLimiter) {
                    DIFFERENTIAL_SLIPPERY_LIMITER_OPEN -> {
                        applyPinValues(motorRearRightPwm = DcmImpl.rearOpenDiffValues[0],
                            motorRearLeftPwm = DcmImpl.rearOpenDiffValues[1])
                    }
                    DIFFERENTIAL_SLIPPERY_LIMITER_MEDI_0 -> {
                        applyPinValues(motorRearRightPwm = DcmImpl.rearMedi0DiffValues[0],
                            motorRearLeftPwm = DcmImpl.rearMedi0DiffValues[1])
                    }
                    DIFFERENTIAL_SLIPPERY_LIMITER_MEDI_1 -> {
                        applyPinValues(motorRearRightPwm = DcmImpl.rearMedi1DiffValues[0],
                            motorRearLeftPwm = DcmImpl.rearMedi1DiffValues[1])
                    }
                    DIFFERENTIAL_SLIPPERY_LIMITER_MEDI_2 -> {
                        applyPinValues(motorRearRightPwm = DcmImpl.rearMedi2DiffValues[0],
                            motorRearLeftPwm = DcmImpl.rearMedi2DiffValues[1])
                    }
                    DIFFERENTIAL_SLIPPERY_LIMITER_LOCKED -> {
                        applyPinValues(motorRearRightPwm = DcmImpl.rearLockedDiffValues[0],
                            motorRearLeftPwm = DcmImpl.rearLockedDiffValues[1])
                    }
                    DIFFERENTIAL_SLIPPERY_LIMITER_AUTO -> {
                        applyPinValues(motorRearRightPwm = DcmImpl.rearAutoDiffValues[0],
                            motorRearLeftPwm = DcmImpl.rearAutoDiffValues[1])
                    }
                    else -> {
                        applyPinValues(motorRearRightPwm = 0, motorRearLeftPwm = 0)
                    }
                }
                showMessage(title = "-- ECU -- THROTTLE -N- BRAKE SYSTEM",
                    body = "User Throttle Value: $userThrottleValue\n" +
                            "User Steering Value: ${SteeringImpl.value}\n" +
                            "Front Differential is: ${SetupImpl.frontDifferentialSlipperyLimiter}\n" +
                            "Inner (Right) Front Wheel Speed: ${EngineImpl.motorFrontRightPwmPin}\n" +
                            "Outer (Left) Front Wheel Speed: ${EngineImpl.motorFrontLeftPwmPin}\n" +
                            "Rear Differential is: ${SetupImpl.rearDifferentialSlipperyLimiter}\n" +
                            "Inner (Right) Rear Wheel Speed: ${EngineImpl.motorRearRightPwmPin}\n" +
                            "Outer (Left) Rear Wheel Speed: ${EngineImpl.motorRearLeftPwmPin}"
                )
            }
            else -> {
                applyPinValues(motorFrontRightPwm = userThrottleValue, motorFrontLeftPwm = userThrottleValue,
                    motorRearRightPwm = userThrottleValue, motorRearLeftPwm = userThrottleValue)
                showMessage(title = "-- ECU -- THROTTLE -N- BRAKE SYSTEM",
                    body = "User Throttle Value: $userThrottleValue\n" +
                            "User Steering Value: ${SteeringImpl.value}\n" +
                            "Front Differential is: ${SetupImpl.frontDifferentialSlipperyLimiter}\n" +
                            "Right Front Wheel Speed: ${EngineImpl.motorFrontRightPwmPin}\n" +
                            "Left Front Wheel Speed: ${EngineImpl.motorFrontLeftPwmPin}\n" +
                            "Rear Differential is: ${SetupImpl.rearDifferentialSlipperyLimiter}\n" +
                            "Right Rear Wheel Speed: ${EngineImpl.motorRearRightPwmPin}\n" +
                            "Left Rear Wheel Speed: ${EngineImpl.motorRearLeftPwmPin}"
                )
            }
        }
    }

    private fun showDifferentialInfo(userThrottleValue: Int) {
        showMessage(title = "-- ECU -- THROTTLE -N- BRAKE SYSTEM",
            body = "User Throttle Value: $userThrottleValue\n" +
                "User Steering Value: ${SteeringImpl.value}\n" +
        when (SteeringImpl.direction) {
                ACTION_TURN_LEFT -> {
                    when (SetupImpl.frontDifferentialSlipperyLimiter) {
                        DIFFERENTIAL_SLIPPERY_LIMITER_OPEN ->
                            "Front Differential is \"OPEN\"\n" +
                                    "Inner (Left) Front Wheel Speed: ${DcmImpl.frontOpenDiffValues[0]}\n" +
                                    "Outer (Right) Front Wheel Speed: ${DcmImpl.frontOpenDiffValues[1]}\n"
                        DIFFERENTIAL_SLIPPERY_LIMITER_MEDI_0 ->
                            "Front Differential is \"MEDI 0\"\n" +
                                    "Inner (Left) Front Wheel Speed: ${DcmImpl.frontMedi0DiffValues[0]}\n" +
                                    "Outer (Right) Front Wheel Speed: ${DcmImpl.frontMedi0DiffValues[1]}\n"
                        DIFFERENTIAL_SLIPPERY_LIMITER_MEDI_1 ->
                            "Front Differential is \"MEDI 1\"\n" +
                                    "Inner (Left) Front Wheel Speed: ${DcmImpl.frontMedi1DiffValues[0]}\n" +
                                    "Outer (Right) Front Wheel Speed: ${DcmImpl.frontMedi1DiffValues[1]}\n"
                        DIFFERENTIAL_SLIPPERY_LIMITER_MEDI_2 ->
                            "Front Differential is \"MEDI 2\"\n" +
                                    "Inner (Left) Front Wheel Speed: ${DcmImpl.frontMedi2DiffValues[0]}\n" +
                                    "Outer (Right) Front Wheel Speed: ${DcmImpl.frontMedi2DiffValues[1]}\n"
                        DIFFERENTIAL_SLIPPERY_LIMITER_LOCKED ->
                            "Front Differential is \"LOCKED\"\n" +
                                    "Inner (Left) Front Wheel Speed: ${DcmImpl.frontLockedDiffValues[0]}\n" +
                                    "Outer (Right) Front Wheel Speed: ${DcmImpl.frontLockedDiffValues[1]}\n"
                        DIFFERENTIAL_SLIPPERY_LIMITER_AUTO ->
                            "Front Differential is \"AUTO\"\n" +
                                    "Inner (Left) Front Wheel Speed: ${DcmImpl.frontAutoDiffValues[0]}\n" +
                                    "Outer (Right) Front Wheel Speed: ${DcmImpl.frontAutoDiffValues[1]}\n"
                        else -> "ERROR in Front differential for Left turn\n"
                    } +
                    when (SetupImpl.rearDifferentialSlipperyLimiter) {
                        DIFFERENTIAL_SLIPPERY_LIMITER_OPEN ->
                            "Rear Differential is \"OPEN\"\n" +
                                    "Inner (Left) Rear Wheel Speed: ${DcmImpl.rearOpenDiffValues[0]}\n" +
                                    "Outer (Right) Rear Wheel Speed: ${DcmImpl.rearOpenDiffValues[1]}"
                        DIFFERENTIAL_SLIPPERY_LIMITER_MEDI_0 ->
                            "Rear Differential is \"MEDI 0\"\n" +
                                    "Inner (Left) Rear Wheel Speed: ${DcmImpl.rearMedi0DiffValues[0]}\n" +
                                    "Outer (Right) Rear Wheel Speed: ${DcmImpl.rearMedi0DiffValues[1]}"
                        DIFFERENTIAL_SLIPPERY_LIMITER_MEDI_1 ->
                            "Rear Differential is \"MEDI 1\"\n" +
                                    "Inner (Left) Rear Wheel Speed: ${DcmImpl.rearMedi1DiffValues[0]}\n" +
                                    "Outer (Right) Rear Wheel Speed: ${DcmImpl.rearMedi1DiffValues[1]}"
                        DIFFERENTIAL_SLIPPERY_LIMITER_MEDI_2 ->
                            "Rear Differential is \"MEDI 2\"\n" +
                                    "Inner (Left) Rear Wheel Speed: ${DcmImpl.rearMedi2DiffValues[0]}\n" +
                                    "Outer (Right) Rear Wheel Speed: ${DcmImpl.rearMedi2DiffValues[1]}"
                        DIFFERENTIAL_SLIPPERY_LIMITER_LOCKED ->
                            "Rear Differential is \"LOCKED\"\n" +
                                    "Inner (Left) Rear Wheel Speed: ${DcmImpl.rearLockedDiffValues[0]}\n" +
                                    "Outer (Right) Rear Wheel Speed: ${DcmImpl.rearLockedDiffValues[1]}"
                        DIFFERENTIAL_SLIPPERY_LIMITER_AUTO ->
                            "Rear Differential is \"AUTO\"\n" +
                                    "Inner (Left) Rear Wheel Speed: ${DcmImpl.rearAutoDiffValues[0]}\n" +
                                    "Outer (Right) Rear Wheel Speed: ${DcmImpl.rearAutoDiffValues[1]}"
                        else -> "ERROR in Rear differential for Left turn"
                    }
                }
                ACTION_TURN_RIGHT -> {
                    when (SetupImpl.frontDifferentialSlipperyLimiter) {
                        DIFFERENTIAL_SLIPPERY_LIMITER_OPEN ->
                            "Front Differential is \"OPEN\"\n" +
                                    "Inner (Right) Front Wheel Speed: ${DcmImpl.frontOpenDiffValues[0]}\n" +
                                    "Outer (Left) Front Wheel Speed: ${DcmImpl.frontOpenDiffValues[1]}\n"
                        DIFFERENTIAL_SLIPPERY_LIMITER_MEDI_0 ->
                            "Front Differential is \"MEDI 0\"\n" +
                                    "Inner (Right) Front Wheel Speed: ${DcmImpl.frontMedi0DiffValues[0]}\n" +
                                    "Outer (Left) Front Wheel Speed: ${DcmImpl.frontMedi0DiffValues[1]}\n"
                        DIFFERENTIAL_SLIPPERY_LIMITER_MEDI_1 ->
                            "Front Differential is \"MEDI 1\"\n" +
                                    "Inner (Right) Front Wheel Speed: ${DcmImpl.frontMedi1DiffValues[0]}\n" +
                                    "Outer (Left) Front Wheel Speed: ${DcmImpl.frontMedi1DiffValues[1]}\n"
                        DIFFERENTIAL_SLIPPERY_LIMITER_MEDI_2 ->
                            "Front Differential is \"MEDI 2\"\n" +
                                    "Inner (Right) Front Wheel Speed: ${DcmImpl.frontMedi2DiffValues[0]}\n" +
                                    "Outer (Left) Front Wheel Speed: ${DcmImpl.frontMedi2DiffValues[1]}\n"
                        DIFFERENTIAL_SLIPPERY_LIMITER_LOCKED ->
                            "Front Differential is \"LOCKED\"\n" +
                                    "Inner (Right) Front Wheel Speed: ${DcmImpl.frontLockedDiffValues[0]}\n" +
                                    "Outer (Left) Front Wheel Speed: ${DcmImpl.frontLockedDiffValues[1]}\n"
                        DIFFERENTIAL_SLIPPERY_LIMITER_AUTO ->
                            "Front Differential is \"AUTO\"\n" +
                                    "Inner (Right) Front Wheel Speed: ${DcmImpl.frontAutoDiffValues[0]}\n" +
                                    "Outer (Left) Front Wheel Speed: ${DcmImpl.frontAutoDiffValues[1]}\n"
                        else -> "ERROR in Front differential (${SetupImpl.frontDifferentialSlipperyLimiter}) for Right turn\n"
                    } +
                    when (SetupImpl.rearDifferentialSlipperyLimiter) {
                        DIFFERENTIAL_SLIPPERY_LIMITER_OPEN ->
                            "Rear Differential is \"OPEN\"\n" +
                                    "Inner (Right) Rear Wheel Speed: ${DcmImpl.rearOpenDiffValues[0]}\n" +
                                    "Outer (Left) Rear Wheel Speed: ${DcmImpl.rearOpenDiffValues[1]}"
                        DIFFERENTIAL_SLIPPERY_LIMITER_MEDI_0 ->
                            "Rear Differential is \"MEDI 0\"\n" +
                                    "Inner (Right) Rear Wheel Speed: ${DcmImpl.rearMedi0DiffValues[0]}\n" +
                                    "Outer (Left) Rear Wheel Speed: ${DcmImpl.rearMedi0DiffValues[1]}"
                        DIFFERENTIAL_SLIPPERY_LIMITER_MEDI_1 ->
                            "Rear Differential is \"MEDI 1\"\n" +
                                    "Inner (Right) Rear Wheel Speed: ${DcmImpl.rearMedi1DiffValues[0]}\n" +
                                    "Outer (Left) Rear Wheel Speed: ${DcmImpl.rearMedi1DiffValues[1]}"
                        DIFFERENTIAL_SLIPPERY_LIMITER_MEDI_2 ->
                            "Rear Differential is \"MEDI 2\"\n" +
                                    "Inner (Right) Rear Wheel Speed: ${DcmImpl.rearMedi2DiffValues[0]}\n" +
                                    "Outer (Left) Rear Wheel Speed: ${DcmImpl.rearMedi2DiffValues[1]}"
                        DIFFERENTIAL_SLIPPERY_LIMITER_LOCKED ->
                            "Rear Differential is \"LOCKED\"\n" +
                                    "Inner (Right) Rear Wheel Speed: ${DcmImpl.rearLockedDiffValues[0]}\n" +
                                    "Outer (Left) Rear Wheel Speed: ${DcmImpl.rearLockedDiffValues[1]}"
                        DIFFERENTIAL_SLIPPERY_LIMITER_AUTO ->
                            "Rear Differential is \"AUTO\"\n" +
                                    "Inner (Right) Rear Wheel Speed: ${DcmImpl.rearAutoDiffValues[0]}\n" +
                                    "Outer (Left) Rear Wheel Speed: ${DcmImpl.rearAutoDiffValues[1]}"
                        else -> "ERROR in Rear differential (${SetupImpl.rearDifferentialSlipperyLimiter}) for Right turn"
                    }
                }
            else -> "Steering value: ${SteeringImpl.value}\n"
        })
    }
}