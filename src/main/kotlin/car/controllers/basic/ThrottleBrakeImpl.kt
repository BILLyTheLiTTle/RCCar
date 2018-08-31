package car.controllers.basic

import car.controllers.advanced.ecu.TcmImpl
import car.server.EngineSystem
import car.server.ThrottleBrakeSystem.Companion.ACTION_BRAKING_STILL
import car.server.ThrottleBrakeSystem.Companion.ACTION_HANDBRAKE
import car.server.ThrottleBrakeSystem.Companion.ACTION_MOVE_BACKWARD
import car.server.ThrottleBrakeSystem.Companion.ACTION_MOVE_FORWARD
import car.server.ThrottleBrakeSystem.Companion.ACTION_NEUTRAL
import car.server.ThrottleBrakeSystem.Companion.ACTION_PARKING_BRAKE
import car.server.EngineSystem.Companion.EMPTY_STRING
import car.server.SetupSystem
import car.server.SteeringSystem
import car.showMessage
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch

object ThrottleBrakeImpl:ThrottleBrake {

    private var action = ACTION_NEUTRAL
    var value = 0
        private set

    override val parkingBrakeState
        get() = action == ACTION_PARKING_BRAKE && ThrottleBrakeImpl.value == 100
    override val handbrakeState
        get() = action == ACTION_HANDBRAKE && ThrottleBrakeImpl.value == 100
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
        TcmImpl.valueOuterFront = value

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
                EngineImpl.motorFrontRightDirPin.high()
                EngineImpl.motorFrontLeftDirPin.high()
                EngineImpl.motorRearRightDirPin.high()
                EngineImpl.motorRearLeftDirPin.high()
            }
        }
        else if (direction == ACTION_MOVE_BACKWARD){
            //////
            // Pi related
            if (EngineImpl.RUN_ON_PI) {
                EngineImpl.motorFrontRightDirPin.low()
                EngineImpl.motorFrontLeftDirPin.low()
                EngineImpl.motorRearRightDirPin.low()
                EngineImpl.motorRearLeftDirPin.low()
            }
        }
        action = direction

	    ThrottleBrakeImpl.value = value
        return EngineSystem.SUCCESS // or error message from pins
    }

    /* The value param will be used  for identification purposes only and
        will not be used as pin values
     */
    override fun brake(value: Int): String {
        launch { ElectricsImpl.brakingLightsState = true }
        //////
        // Pi related
        if (EngineImpl.RUN_ON_PI) {
            EngineImpl.motorFrontRightPwmPin.pwm = 0 //value
            EngineImpl.motorFrontRightDirPin.low()
            EngineImpl.motorFrontLeftPwmPin.pwm = 0 //value
            EngineImpl.motorFrontLeftDirPin.low()
            EngineImpl.motorRearRightPwmPin.pwm = 0 //value
            EngineImpl.motorRearRightDirPin.low()
            EngineImpl.motorRearLeftPwmPin.pwm = 0 //value
            EngineImpl.motorRearLeftDirPin.low()
        }
        //////

        action = ACTION_BRAKING_STILL
	    ThrottleBrakeImpl.value = value
        return EngineSystem.SUCCESS // or error message from pins
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
            EngineImpl.motorFrontRightPwmPin.pwm = 0 //value
            EngineImpl.motorFrontRightDirPin.low()
            EngineImpl.motorFrontLeftPwmPin.pwm = 0 //value
            EngineImpl.motorFrontLeftDirPin.low()
            EngineImpl.motorRearRightPwmPin.pwm = 0 //value
            EngineImpl.motorRearRightDirPin.low()
            EngineImpl.motorRearLeftPwmPin.pwm = 0 //value
            EngineImpl.motorRearLeftDirPin.low()
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

        return EngineSystem.SUCCESS // or error message from pins
    }

    /* The value param will be used  for identification purposes only and
        will not be used as pin values
     */
    override fun handbrake(value: Int): String {
        //////
        // Pi related
        if (EngineImpl.RUN_ON_PI && value == 100) {
            // Affects only the rear wheels
            EngineImpl.motorRearRightPwmPin.pwm = 0 //value
            EngineImpl.motorRearRightDirPin.low()
            EngineImpl.motorRearLeftPwmPin.pwm = 0 //value
            EngineImpl.motorRearLeftDirPin.low()
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

        return EngineSystem.SUCCESS // or error message from pins
    }

    /* Neutral state is not achievable with H-bridge used.
        So, it works almost like the brake function.
    */
    override fun setNeutral(): String {
        launch { ElectricsImpl.brakingLightsState = false }
        //////
        // Pi related
        if (EngineImpl.RUN_ON_PI) {
            EngineImpl.motorFrontRightPwmPin.pwm = 0 //value
            EngineImpl.motorFrontRightDirPin.low()
            EngineImpl.motorFrontLeftPwmPin.pwm = 0 //value
            EngineImpl.motorFrontLeftDirPin.low()
            EngineImpl.motorRearRightPwmPin.pwm = 0 //value
            EngineImpl.motorRearRightDirPin.low()
            EngineImpl.motorRearLeftPwmPin.pwm = 0 //value
            EngineImpl.motorRearLeftDirPin.low()
        }
        //////

        action = ACTION_NEUTRAL
        value = 0
        return EngineSystem.SUCCESS // or error message from pins
    }

    override fun reset() {
        action = ACTION_NEUTRAL
        value = 0
    }

    private fun calculateDifferentialValues(userThrottleValue: Int) {
        when (SteeringImpl.direction) {
            SteeringSystem.ACTION_TURN_LEFT -> {
                when (SetupImpl.frontDifferentialSlipperyLimiter) {
                    SetupSystem.DIFFERENTIAL_SLIPPERY_LIMITER_OPEN -> {
                        EngineImpl.motorFrontLeftPwmPin.pwm = TcmImpl.frontOpenDiffValues[0]
                        EngineImpl.motorFrontRightPwmPin.pwm = TcmImpl.frontOpenDiffValues[1]
                    }
                    SetupSystem.DIFFERENTIAL_SLIPPERY_LIMITER_MEDI_0 -> {
                        EngineImpl.motorFrontLeftPwmPin.pwm = TcmImpl.frontMedi0DiffValues[0]
                        EngineImpl.motorFrontRightPwmPin.pwm = TcmImpl.frontMedi0DiffValues[1]
                    }
                    SetupSystem.DIFFERENTIAL_SLIPPERY_LIMITER_MEDI_1 -> {
                        EngineImpl.motorFrontLeftPwmPin.pwm = TcmImpl.frontMedi1DiffValues[0]
                        EngineImpl.motorFrontRightPwmPin.pwm = TcmImpl.frontMedi1DiffValues[1]
                    }
                    SetupSystem.DIFFERENTIAL_SLIPPERY_LIMITER_MEDI_2 -> {
                        EngineImpl.motorFrontLeftPwmPin.pwm = TcmImpl.frontMedi2DiffValues[0]
                        EngineImpl.motorFrontRightPwmPin.pwm = TcmImpl.frontMedi2DiffValues[1]
                    }
                    SetupSystem.DIFFERENTIAL_SLIPPERY_LIMITER_LOCKED -> {
                        EngineImpl.motorFrontLeftPwmPin.pwm = TcmImpl.frontLockedDiffValues[0]
                        EngineImpl.motorFrontRightPwmPin.pwm = TcmImpl.frontLockedDiffValues[1]
                    }
                    SetupSystem.DIFFERENTIAL_SLIPPERY_LIMITER_AUTO -> {
                        EngineImpl.motorFrontLeftPwmPin.pwm = TcmImpl.frontAutoDiffValues[0]
                        EngineImpl.motorFrontRightPwmPin.pwm = TcmImpl.frontAutoDiffValues[1]
                    }
                    else -> {
                        EngineImpl.motorFrontLeftPwmPin.pwm = 0
                        EngineImpl.motorFrontRightPwmPin.pwm = 0
                    }
                }
                when (SetupImpl.rearDifferentialSlipperyLimiter) {
                    SetupSystem.DIFFERENTIAL_SLIPPERY_LIMITER_OPEN -> {
                        EngineImpl.motorRearLeftPwmPin.pwm = TcmImpl.rearOpenDiffValues[0]
                        EngineImpl.motorRearRightPwmPin.pwm = TcmImpl.rearOpenDiffValues[1]
                    }
                    SetupSystem.DIFFERENTIAL_SLIPPERY_LIMITER_MEDI_0 -> {
                        EngineImpl.motorRearLeftPwmPin.pwm = TcmImpl.rearMedi0DiffValues[0]
                        EngineImpl.motorRearRightPwmPin.pwm = TcmImpl.rearMedi0DiffValues[1]
                    }
                    SetupSystem.DIFFERENTIAL_SLIPPERY_LIMITER_MEDI_1 -> {
                        EngineImpl.motorRearLeftPwmPin.pwm = TcmImpl.rearMedi1DiffValues[0]
                        EngineImpl.motorRearRightPwmPin.pwm = TcmImpl.rearMedi1DiffValues[1]
                    }
                    SetupSystem.DIFFERENTIAL_SLIPPERY_LIMITER_MEDI_2 -> {
                        EngineImpl.motorRearLeftPwmPin.pwm = TcmImpl.rearMedi2DiffValues[0]
                        EngineImpl.motorRearRightPwmPin.pwm = TcmImpl.rearMedi2DiffValues[1]
                    }
                    SetupSystem.DIFFERENTIAL_SLIPPERY_LIMITER_LOCKED -> {
                        EngineImpl.motorRearLeftPwmPin.pwm = TcmImpl.rearLockedDiffValues[0]
                        EngineImpl.motorRearRightPwmPin.pwm = TcmImpl.rearLockedDiffValues[1]
                    }
                    SetupSystem.DIFFERENTIAL_SLIPPERY_LIMITER_AUTO -> {
                        EngineImpl.motorRearLeftPwmPin.pwm = TcmImpl.rearAutoDiffValues[0]
                        EngineImpl.motorRearRightPwmPin.pwm = TcmImpl.rearAutoDiffValues[1]
                    }
                    else -> {
                        EngineImpl.motorRearLeftPwmPin.pwm = 0
                        EngineImpl.motorRearRightPwmPin.pwm = 0
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
            SteeringSystem.ACTION_TURN_RIGHT -> {
                when (SetupImpl.frontDifferentialSlipperyLimiter) {
                    SetupSystem.DIFFERENTIAL_SLIPPERY_LIMITER_OPEN -> {
                        EngineImpl.motorFrontRightPwmPin.pwm = TcmImpl.frontOpenDiffValues[0]
                        EngineImpl.motorFrontLeftPwmPin.pwm = TcmImpl.frontOpenDiffValues[1]
                    }
                    SetupSystem.DIFFERENTIAL_SLIPPERY_LIMITER_MEDI_0 -> {
                        EngineImpl.motorFrontRightPwmPin.pwm = TcmImpl.frontMedi0DiffValues[0]
                        EngineImpl.motorFrontLeftPwmPin.pwm = TcmImpl.frontMedi0DiffValues[1]
                    }
                    SetupSystem.DIFFERENTIAL_SLIPPERY_LIMITER_MEDI_1 -> {
                        EngineImpl.motorFrontRightPwmPin.pwm = TcmImpl.frontMedi1DiffValues[0]
                        EngineImpl.motorFrontLeftPwmPin.pwm = TcmImpl.frontMedi1DiffValues[1]
                    }
                    SetupSystem.DIFFERENTIAL_SLIPPERY_LIMITER_MEDI_2 -> {
                        EngineImpl.motorFrontRightPwmPin.pwm = TcmImpl.frontMedi2DiffValues[0]
                        EngineImpl.motorFrontLeftPwmPin.pwm = TcmImpl.frontMedi2DiffValues[1]
                    }
                    SetupSystem.DIFFERENTIAL_SLIPPERY_LIMITER_LOCKED -> {
                        EngineImpl.motorFrontRightPwmPin.pwm = TcmImpl.frontLockedDiffValues[0]
                        EngineImpl.motorFrontLeftPwmPin.pwm = TcmImpl.frontLockedDiffValues[1]
                    }
                    SetupSystem.DIFFERENTIAL_SLIPPERY_LIMITER_AUTO -> {
                        EngineImpl.motorFrontRightPwmPin.pwm = TcmImpl.frontAutoDiffValues[0]
                        EngineImpl.motorFrontLeftPwmPin.pwm = TcmImpl.frontAutoDiffValues[1]
                    }
                    else -> {
                        EngineImpl.motorFrontRightPwmPin.pwm = 0
                        EngineImpl.motorFrontLeftPwmPin.pwm = 0
                    }
                }
                when (SetupImpl.rearDifferentialSlipperyLimiter) {
                    SetupSystem.DIFFERENTIAL_SLIPPERY_LIMITER_OPEN -> {
                        EngineImpl.motorRearRightPwmPin.pwm = TcmImpl.rearOpenDiffValues[0]
                        EngineImpl.motorRearLeftPwmPin.pwm = TcmImpl.rearOpenDiffValues[1]
                    }
                    SetupSystem.DIFFERENTIAL_SLIPPERY_LIMITER_MEDI_0 -> {
                        EngineImpl.motorRearRightPwmPin.pwm = TcmImpl.rearMedi0DiffValues[0]
                        EngineImpl.motorRearLeftPwmPin.pwm = TcmImpl.rearMedi0DiffValues[1]
                    }
                    SetupSystem.DIFFERENTIAL_SLIPPERY_LIMITER_MEDI_1 -> {
                        EngineImpl.motorRearRightPwmPin.pwm = TcmImpl.rearMedi1DiffValues[0]
                        EngineImpl.motorRearLeftPwmPin.pwm = TcmImpl.rearMedi1DiffValues[1]
                    }
                    SetupSystem.DIFFERENTIAL_SLIPPERY_LIMITER_MEDI_2 -> {
                        EngineImpl.motorRearRightPwmPin.pwm = TcmImpl.rearMedi2DiffValues[0]
                        EngineImpl.motorRearLeftPwmPin.pwm = TcmImpl.rearMedi2DiffValues[1]
                    }
                    SetupSystem.DIFFERENTIAL_SLIPPERY_LIMITER_LOCKED -> {
                        EngineImpl.motorRearRightPwmPin.pwm = TcmImpl.rearLockedDiffValues[0]
                        EngineImpl.motorRearLeftPwmPin.pwm = TcmImpl.rearLockedDiffValues[1]
                    }
                    SetupSystem.DIFFERENTIAL_SLIPPERY_LIMITER_AUTO -> {
                        EngineImpl.motorRearRightPwmPin.pwm = TcmImpl.rearAutoDiffValues[0]
                        EngineImpl.motorRearLeftPwmPin.pwm = TcmImpl.rearAutoDiffValues[1]
                    }
                    else -> {
                        EngineImpl.motorRearRightPwmPin.pwm = 0
                        EngineImpl.motorRearLeftPwmPin.pwm = 0
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
                EngineImpl.motorFrontRightPwmPin.pwm = userThrottleValue
                EngineImpl.motorFrontLeftPwmPin.pwm = userThrottleValue
                EngineImpl.motorRearRightPwmPin.pwm = userThrottleValue
                EngineImpl.motorRearLeftPwmPin.pwm = userThrottleValue
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
                SteeringSystem.ACTION_TURN_LEFT -> {
                    when (SetupImpl.frontDifferentialSlipperyLimiter) {
                        SetupSystem.DIFFERENTIAL_SLIPPERY_LIMITER_OPEN ->
                            "Front Differential is \"OPEN\"\n" +
                                    "Inner (Left) Front Wheel Speed: ${TcmImpl.frontOpenDiffValues[0]}\n" +
                                    "Outer (Right) Front Wheel Speed: ${TcmImpl.frontOpenDiffValues[1]}\n"
                        SetupSystem.DIFFERENTIAL_SLIPPERY_LIMITER_MEDI_0 ->
                            "Front Differential is \"MEDI 0\"\n" +
                                    "Inner (Left) Front Wheel Speed: ${TcmImpl.frontMedi0DiffValues[0]}\n" +
                                    "Outer (Right) Front Wheel Speed: ${TcmImpl.frontMedi0DiffValues[1]}\n"
                        SetupSystem.DIFFERENTIAL_SLIPPERY_LIMITER_MEDI_1 ->
                            "Front Differential is \"MEDI 1\"\n" +
                                    "Inner (Left) Front Wheel Speed: ${TcmImpl.frontMedi1DiffValues[0]}\n" +
                                    "Outer (Right) Front Wheel Speed: ${TcmImpl.frontMedi1DiffValues[1]}\n"
                        SetupSystem.DIFFERENTIAL_SLIPPERY_LIMITER_MEDI_2 ->
                            "Front Differential is \"MEDI 2\"\n" +
                                    "Inner (Left) Front Wheel Speed: ${TcmImpl.frontMedi2DiffValues[0]}\n" +
                                    "Outer (Right) Front Wheel Speed: ${TcmImpl.frontMedi2DiffValues[1]}\n"
                        SetupSystem.DIFFERENTIAL_SLIPPERY_LIMITER_LOCKED ->
                            "Front Differential is \"LOCKED\"\n" +
                                    "Inner (Left) Front Wheel Speed: ${TcmImpl.frontLockedDiffValues[0]}\n" +
                                    "Outer (Right) Front Wheel Speed: ${TcmImpl.frontLockedDiffValues[1]}\n"
                        SetupSystem.DIFFERENTIAL_SLIPPERY_LIMITER_AUTO ->
                            "Front Differential is \"AUTO\"\n" +
                                    "Inner (Left) Front Wheel Speed: ${TcmImpl.frontAutoDiffValues[0]}\n" +
                                    "Outer (Right) Front Wheel Speed: ${TcmImpl.frontAutoDiffValues[1]}\n"
                        else -> "ERROR in Front differential for Left turn\n"
                    } +
                    when (SetupImpl.rearDifferentialSlipperyLimiter) {
                        SetupSystem.DIFFERENTIAL_SLIPPERY_LIMITER_OPEN ->
                            "Rear Differential is \"OPEN\"\n" +
                                    "Inner (Left) Rear Wheel Speed: ${TcmImpl.rearOpenDiffValues[0]}\n" +
                                    "Outer (Right) Rear Wheel Speed: ${TcmImpl.rearOpenDiffValues[1]}"
                        SetupSystem.DIFFERENTIAL_SLIPPERY_LIMITER_MEDI_0 ->
                            "Rear Differential is \"MEDI 0\"\n" +
                                    "Inner (Left) Rear Wheel Speed: ${TcmImpl.rearMedi0DiffValues[0]}\n" +
                                    "Outer (Right) Rear Wheel Speed: ${TcmImpl.rearMedi0DiffValues[1]}"
                        SetupSystem.DIFFERENTIAL_SLIPPERY_LIMITER_MEDI_1 ->
                            "Rear Differential is \"MEDI 1\"\n" +
                                    "Inner (Left) Rear Wheel Speed: ${TcmImpl.rearMedi1DiffValues[0]}\n" +
                                    "Outer (Right) Rear Wheel Speed: ${TcmImpl.rearMedi1DiffValues[1]}"
                        SetupSystem.DIFFERENTIAL_SLIPPERY_LIMITER_MEDI_2 ->
                            "Rear Differential is \"MEDI 2\"\n" +
                                    "Inner (Left) Rear Wheel Speed: ${TcmImpl.rearMedi2DiffValues[0]}\n" +
                                    "Outer (Right) Rear Wheel Speed: ${TcmImpl.rearMedi2DiffValues[1]}"
                        SetupSystem.DIFFERENTIAL_SLIPPERY_LIMITER_LOCKED ->
                            "Rear Differential is \"LOCKED\"\n" +
                                    "Inner (Left) Rear Wheel Speed: ${TcmImpl.rearLockedDiffValues[0]}\n" +
                                    "Outer (Right) Rear Wheel Speed: ${TcmImpl.rearLockedDiffValues[1]}"
                        SetupSystem.DIFFERENTIAL_SLIPPERY_LIMITER_AUTO ->
                            "Rear Differential is \"AUTO\"\n" +
                                    "Inner (Left) Rear Wheel Speed: ${TcmImpl.rearAutoDiffValues[0]}\n" +
                                    "Outer (Right) Rear Wheel Speed: ${TcmImpl.rearAutoDiffValues[1]}"
                        else -> "ERROR in Rear differential for Left turn"
                    }
                }
                SteeringSystem.ACTION_TURN_RIGHT -> {
                    when (SetupImpl.frontDifferentialSlipperyLimiter) {
                        SetupSystem.DIFFERENTIAL_SLIPPERY_LIMITER_OPEN ->
                            "Front Differential is \"OPEN\"\n" +
                                    "Inner (Right) Front Wheel Speed: ${TcmImpl.frontOpenDiffValues[0]}\n" +
                                    "Outer (Left) Front Wheel Speed: ${TcmImpl.frontOpenDiffValues[1]}\n"
                        SetupSystem.DIFFERENTIAL_SLIPPERY_LIMITER_MEDI_0 ->
                            "Front Differential is \"MEDI 0\"\n" +
                                    "Inner (Right) Front Wheel Speed: ${TcmImpl.frontMedi0DiffValues[0]}\n" +
                                    "Outer (Left) Front Wheel Speed: ${TcmImpl.frontMedi0DiffValues[1]}\n"
                        SetupSystem.DIFFERENTIAL_SLIPPERY_LIMITER_MEDI_1 ->
                            "Front Differential is \"MEDI 1\"\n" +
                                    "Inner (Right) Front Wheel Speed: ${TcmImpl.frontMedi1DiffValues[0]}\n" +
                                    "Outer (Left) Front Wheel Speed: ${TcmImpl.frontMedi1DiffValues[1]}\n"
                        SetupSystem.DIFFERENTIAL_SLIPPERY_LIMITER_MEDI_2 ->
                            "Front Differential is \"MEDI 2\"\n" +
                                    "Inner (Right) Front Wheel Speed: ${TcmImpl.frontMedi2DiffValues[0]}\n" +
                                    "Outer (Left) Front Wheel Speed: ${TcmImpl.frontMedi2DiffValues[1]}\n"
                        SetupSystem.DIFFERENTIAL_SLIPPERY_LIMITER_LOCKED ->
                            "Front Differential is \"LOCKED\"\n" +
                                    "Inner (Right) Front Wheel Speed: ${TcmImpl.frontLockedDiffValues[0]}\n" +
                                    "Outer (Left) Front Wheel Speed: ${TcmImpl.frontLockedDiffValues[1]}\n"
                        SetupSystem.DIFFERENTIAL_SLIPPERY_LIMITER_AUTO ->
                            "Front Differential is \"AUTO\"\n" +
                                    "Inner (Right) Front Wheel Speed: ${TcmImpl.frontAutoDiffValues[0]}\n" +
                                    "Outer (Left) Front Wheel Speed: ${TcmImpl.frontAutoDiffValues[1]}\n"
                        else -> "ERROR in Front differential (${SetupImpl.frontDifferentialSlipperyLimiter}) for Right turn\n"
                    } +
                    when (SetupImpl.rearDifferentialSlipperyLimiter) {
                        SetupSystem.DIFFERENTIAL_SLIPPERY_LIMITER_OPEN ->
                            "Rear Differential is \"OPEN\"\n" +
                                    "Inner (Right) Rear Wheel Speed: ${TcmImpl.rearOpenDiffValues[0]}\n" +
                                    "Outer (Left) Rear Wheel Speed: ${TcmImpl.rearOpenDiffValues[1]}"
                        SetupSystem.DIFFERENTIAL_SLIPPERY_LIMITER_MEDI_0 ->
                            "Rear Differential is \"MEDI 0\"\n" +
                                    "Inner (Right) Rear Wheel Speed: ${TcmImpl.rearMedi0DiffValues[0]}\n" +
                                    "Outer (Left) Rear Wheel Speed: ${TcmImpl.rearMedi0DiffValues[1]}"
                        SetupSystem.DIFFERENTIAL_SLIPPERY_LIMITER_MEDI_1 ->
                            "Rear Differential is \"MEDI 1\"\n" +
                                    "Inner (Right) Rear Wheel Speed: ${TcmImpl.rearMedi1DiffValues[0]}\n" +
                                    "Outer (Left) Rear Wheel Speed: ${TcmImpl.rearMedi1DiffValues[1]}"
                        SetupSystem.DIFFERENTIAL_SLIPPERY_LIMITER_MEDI_2 ->
                            "Rear Differential is \"MEDI 2\"\n" +
                                    "Inner (Right) Rear Wheel Speed: ${TcmImpl.rearMedi2DiffValues[0]}\n" +
                                    "Outer (Left) Rear Wheel Speed: ${TcmImpl.rearMedi2DiffValues[1]}"
                        SetupSystem.DIFFERENTIAL_SLIPPERY_LIMITER_LOCKED ->
                            "Rear Differential is \"LOCKED\"\n" +
                                    "Inner (Right) Rear Wheel Speed: ${TcmImpl.rearLockedDiffValues[0]}\n" +
                                    "Outer (Left) Rear Wheel Speed: ${TcmImpl.rearLockedDiffValues[1]}"
                        SetupSystem.DIFFERENTIAL_SLIPPERY_LIMITER_AUTO ->
                            "Rear Differential is \"AUTO\"\n" +
                                    "Inner (Right) Rear Wheel Speed: ${TcmImpl.rearAutoDiffValues[0]}\n" +
                                    "Outer (Left) Rear Wheel Speed: ${TcmImpl.rearAutoDiffValues[1]}"
                        else -> "ERROR in Rear differential (${SetupImpl.rearDifferentialSlipperyLimiter}) for Right turn"
                    }
                }
            else -> "Steering value: ${SteeringImpl.value}\n"
        })
    }
}