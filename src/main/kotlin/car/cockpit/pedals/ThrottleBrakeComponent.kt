package car.cockpit.pedals

import car.cockpit.electrics.Electrics
import car.cockpit.engine.Engine
import car.cockpit.engine.SUCCESS
import car.cockpit.setup.*
import car.cockpit.steering.Steering
import car.cockpit.steering.Turn
import car.ecu.modules.dcm.Dcm
import car.parts.*
import car.parts.raspi.*
import car.showMessage
import com.pi4j.io.gpio.GpioPinDigitalOutput
import com.pi4j.io.gpio.GpioPinPwmOutput
import com.pi4j.io.gpio.PinState
import com.pi4j.io.gpio.RaspiPin
import com.pi4j.util.CommandArgumentParser
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

    private lateinit var motorFrontRightPwmPin: GpioPinPwmOutput
    private lateinit var motorFrontRightDirPin: GpioPinDigitalOutput
    private lateinit var motorFrontLeftPwmPin: GpioPinPwmOutput
    private lateinit var motorFrontLeftDirPin: GpioPinDigitalOutput
    private lateinit var motorRearRightPwmPin: GpioPinPwmOutput
    private lateinit var motorRearRightDirPin: GpioPinDigitalOutput
    private lateinit var motorRearLeftPwmPin: GpioPinPwmOutput
    private lateinit var motorRearLeftDirPin: GpioPinDigitalOutput
    private lateinit var motorFrontRightEnablerPin: GpioPinDigitalOutput
    private lateinit var motorFrontLeftEnablerPin: GpioPinDigitalOutput
    private lateinit var motorRearRightEnablerPin: GpioPinDigitalOutput
    private lateinit var motorRearLeftEnablerPin: GpioPinDigitalOutput

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
                applyPinValues(motorFrontRightMovingForward = true, motorFrontRightEnable = true,
                    motorFrontLeftMovingForward = true, motorFrontLeftEnable = true,
                    motorRearRightMovingForward = true, motorRearRightEnable = true,
                    motorRearLeftMovingForward = true, motorRearLeftEnable = true)
            }
        }
        else if (direction == Motion.BACKWARD){
            //////
            // Pi related
            if (engineComponent.runOnPi) {
                applyPinValues(motorFrontRightMovingForward = false, motorFrontRightEnable = true,
                    motorFrontLeftMovingForward = false, motorFrontLeftEnable = true,
                    motorRearRightMovingForward = false, motorRearRightEnable = true,
                    motorRearLeftMovingForward = false, motorRearLeftEnable = true)
            }
        }
        action = direction

	    this.value = value
        return SUCCESS // or error message from raspi
    }

    /* The value param will be used  for identification purposes only and
        will not be used as pin values
     */
    override fun brake(value: Int): String {
        CoroutineScope(Dispatchers.Default).launch { electricsComponent.brakingLightsState = true }
        //////
        // Pi related
        if (engineComponent.runOnPi) {
            applyPinValues(
                motorFrontRightPwm = 0 /*value*/, motorFrontRightMovingForward = false, motorFrontRightEnable = true,
                motorFrontLeftPwm = 0 /*value*/, motorFrontLeftMovingForward = false, motorFrontLeftEnable = true,
                motorRearRightPwm = 0 /*value*/, motorRearRightMovingForward = false, motorRearRightEnable = true,
                motorRearLeftPwm = 0 /*value*/, motorRearLeftMovingForward = false, motorRearLeftEnable = true)
        }
        //////

        action = Motion.BRAKING_STILL
        this.value = value
        return SUCCESS // or error message from raspi
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
            applyPinValues(
                motorFrontRightPwm = 0 /*value*/, motorFrontRightMovingForward = false, motorFrontRightEnable = true,
                motorFrontLeftPwm = 0 /*value*/, motorFrontLeftMovingForward = false, motorFrontLeftEnable = true,
                motorRearRightPwm = 0 /*value*/, motorRearRightMovingForward = false, motorRearRightEnable = true,
                motorRearLeftPwm = 0 /*value*/, motorRearLeftMovingForward = false, motorRearLeftEnable = true)
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

        return SUCCESS // or error message from raspi
    }

    /* The value param will be used  for identification purposes only and
        will not be used as pin values
     */
    override fun handbrake(value: Int): String {
        //////
        // Pi related
        if (engineComponent.runOnPi && value == 100) {
            // Affects only the rear wheels
            applyPinValues(
                motorRearRightPwm = 0 /*value*/, motorRearRightMovingForward = false, motorRearRightEnable = true,
                motorRearLeftPwm = 0 /*value*/, motorRearLeftMovingForward = false, motorRearLeftEnable = true)
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

        return SUCCESS // or error message from raspi
    }

    /* Neutral state is not achievable with H-bridge used.
        So, it works almost like the brake function.
    */
    override fun setNeutral(): String {
        CoroutineScope(Dispatchers.Default).launch { electricsComponent.brakingLightsState = false }
        //////
        // Pi related
        if (engineComponent.runOnPi) {
            applyPinValues(
                motorFrontRightPwm = 0 /*value*/, motorFrontRightMovingForward = false, motorFrontRightEnable = false,
                motorFrontLeftPwm = 0 /*value*/, motorFrontLeftMovingForward = false, motorFrontLeftEnable = false,
                motorRearRightPwm = 0 /*value*/, motorRearRightMovingForward = false, motorRearRightEnable = false,
                motorRearLeftPwm = 0 /*value*/, motorRearLeftMovingForward = false, motorRearLeftEnable = false)
        }
        //////

        action = Motion.NEUTRAL
        value = 0
        return SUCCESS // or error message from raspi
    }

    override fun initialize() {

        /* == Front Right Motor == */
        val motorFrontRightPin = CommandArgumentParser.getPin(
            RaspiPin::class.java, // pin provider class to obtain pin instance from
            MOTOR_FRONT_RIGHT_PWM_PIN, // default pin if no pin argument found
            null
        )
        motorFrontRightPwmPin = engineComponent.gpio.provisionPwmOutputPin(motorFrontRightPin)

        motorFrontRightDirPin = engineComponent.gpio.provisionDigitalOutputPin(
            engineComponent.motorsPinsProvider, FRONT_MULTIPLEXER_SELECTOR_2,
            "Front Right Motor Dir Pin", PinState.LOW)

        motorFrontRightEnablerPin = engineComponent.gpio.provisionDigitalOutputPin(
            engineComponent.motorsPinsProvider, FRONT_RIGHT_BRIDGE_ENABLER_PIN,
            "Front Right Motor En Pin", PinState.HIGH)

        /* == Front Left Motor == */
        val motorFrontLeftPin = CommandArgumentParser.getPin(
            RaspiPin::class.java, // pin provider class to obtain pin instance from
            MOTOR_FRONT_LEFT_PWM_PIN, // default pin if no pin argument found
            null
        )
        motorFrontLeftPwmPin = engineComponent.gpio.provisionPwmOutputPin(motorFrontLeftPin)

        motorFrontLeftDirPin = engineComponent.gpio.provisionDigitalOutputPin(
            engineComponent.motorsPinsProvider, FRONT_MULTIPLEXER_SELECTOR_1,
            "Front Left Motor Dir Pin", PinState.LOW)

        motorFrontLeftEnablerPin = engineComponent.gpio.provisionDigitalOutputPin(
            engineComponent.motorsPinsProvider, FRONT_LEFT_BRIDGE_ENABLER_PIN,
            "Front Right Motor En Pin", PinState.HIGH)

        /* == Rear Right Motor == */
        val motorRearRightPin = CommandArgumentParser.getPin(
            RaspiPin::class.java, // pin provider class to obtain pin instance from
            MOTOR_REAR_RIGHT_PWM_PIN, // default pin if no pin argument found
            null
        )
        motorRearRightPwmPin = engineComponent.gpio.provisionPwmOutputPin(motorRearRightPin)

        motorRearRightDirPin = engineComponent.gpio.provisionDigitalOutputPin(
            engineComponent.motorsPinsProvider, REAR_MULTIPLEXER_SELECTOR_2,
            "Rear Right Motor Dir Pin", PinState.LOW)

        motorRearRightEnablerPin = engineComponent.gpio.provisionDigitalOutputPin(
            engineComponent.motorsPinsProvider, REAR_RIGHT_BRIDGE_ENABLER_PIN,
            "Rear Right Motor En Pin", PinState.HIGH)

        /* == Rear Left Motor == */
        val motorRearLeftPin = CommandArgumentParser.getPin(
            RaspiPin::class.java, // pin provider class to obtain pin instance from
            MOTOR_REAR_LEFT_PWM_PIN, // default pin if no pin argument found
            null
        )
        motorRearLeftPwmPin = engineComponent.gpio.provisionPwmOutputPin(motorRearLeftPin)

        motorRearLeftDirPin = engineComponent.gpio.provisionDigitalOutputPin(
            engineComponent.motorsPinsProvider, REAR_MULTIPLEXER_SELECTOR_1,
            "Rear Left Motor Dir Pin", PinState.LOW)

        motorRearLeftEnablerPin = engineComponent.gpio.provisionDigitalOutputPin(
            engineComponent.motorsPinsProvider, REAR_LEFT_BRIDGE_ENABLER_PIN,
            "Rear Left Motor En Pin", PinState.HIGH)
    }

    override fun reset() {
        action = Motion.NEUTRAL
        value = 0

        ////// Shutdown & unprovision GPIOs, PWM, etc
        // Pi related
        if(engineComponent.runOnPi) {
            applyPinValues(0, false, false,
                0, false, false,
                0, false, false,
                0, false, false)

            engineComponent.gpio.apply {
                shutdown()
                unprovisionPin(motorFrontRightPwmPin)
                unprovisionPin(motorFrontRightDirPin)
                unprovisionPin(motorFrontRightEnablerPin)

                unprovisionPin(motorFrontLeftPwmPin)
                unprovisionPin(motorFrontLeftDirPin)
                unprovisionPin(motorFrontLeftEnablerPin)

                unprovisionPin(motorRearRightPwmPin)
                unprovisionPin(motorRearRightDirPin)
                unprovisionPin(motorRearRightEnablerPin)

                unprovisionPin(motorRearLeftPwmPin)
                unprovisionPin(motorRearLeftDirPin)
                unprovisionPin(motorRearLeftEnablerPin)
            }
        }
        //////
    }

    private fun applyPinValues(
        motorFrontRightPwm: Int? = null, motorFrontRightMovingForward: Boolean? = null, motorFrontRightEnable: Boolean? = null,
        motorFrontLeftPwm: Int? = null, motorFrontLeftMovingForward: Boolean? = null, motorFrontLeftEnable: Boolean? = null,
        motorRearRightPwm: Int? = null, motorRearRightMovingForward: Boolean? = null, motorRearRightEnable: Boolean? = null,
        motorRearLeftPwm: Int? = null, motorRearLeftMovingForward: Boolean? = null, motorRearLeftEnable: Boolean? = null){

        motorFrontRightPwm?.let { motorFrontRightPwmPin.pwm = it }
        motorFrontRightMovingForward?.let { motorFrontRightDirPin.setState(it) }
        motorFrontRightEnable?.let { motorFrontRightEnablerPin.setState(it) }
        motorFrontLeftPwm?.let { motorFrontLeftPwmPin.pwm = it }
        motorFrontLeftMovingForward?.let { motorFrontLeftDirPin.setState(it) }
        motorFrontLeftEnable?.let { motorFrontLeftEnablerPin.setState(it) }
        motorRearRightPwm?.let { motorRearRightPwmPin.pwm = it }
        motorRearRightMovingForward?.let { motorRearRightDirPin.setState(it) }
        motorRearRightEnable?.let { motorRearRightEnablerPin.setState(it) }
        motorRearLeftPwm?.let { motorRearLeftPwmPin.pwm = it }
        motorRearLeftMovingForward?.let { motorRearLeftDirPin.setState(it) }
        motorRearLeftEnable?.let { motorRearLeftEnablerPin.setState(it) }
    }

    private fun calculateDifferentialValues(userThrottleValue: Int) {
        when (steeringComponent.direction) {
            Turn.LEFT -> {
                when (setupComponent.frontDifferentialSlipperyLimiter) {
                    DifferentialSlipperyLimiter.OPEN -> {
                        applyPinValues(motorFrontLeftPwm = dcmComponent.frontOpenDiffValues[0],
                            motorFrontRightPwm = dcmComponent.frontOpenDiffValues[1])
                    }
                    DifferentialSlipperyLimiter.MEDI_0 -> {
                        applyPinValues(motorFrontLeftPwm = dcmComponent.frontMedi0DiffValues[0],
                            motorFrontRightPwm = dcmComponent.frontMedi0DiffValues[1])
                    }
                    DifferentialSlipperyLimiter.MEDI_1 -> {
                        applyPinValues(motorFrontLeftPwm = dcmComponent.frontMedi1DiffValues[0],
                            motorFrontRightPwm = dcmComponent.frontMedi1DiffValues[1])
                    }
                    DifferentialSlipperyLimiter.MEDI_2 -> {
                        applyPinValues(motorFrontLeftPwm = dcmComponent.frontMedi2DiffValues[0],
                            motorFrontRightPwm = dcmComponent.frontMedi2DiffValues[1])
                    }
                    DifferentialSlipperyLimiter.LOCKED -> {
                        applyPinValues(motorFrontLeftPwm = dcmComponent.frontLockedDiffValues[0],
                            motorFrontRightPwm = dcmComponent.frontLockedDiffValues[1])
                    }
                    DifferentialSlipperyLimiter.AUTO -> {
                        applyPinValues(motorFrontLeftPwm = dcmComponent.frontAutoDiffValues[0],
                            motorFrontRightPwm = dcmComponent.frontAutoDiffValues[1])
                    }
                    else -> {
                        applyPinValues(motorFrontLeftPwm = 0, motorFrontRightPwm = 0)
                    }
                }
                when (setupComponent.rearDifferentialSlipperyLimiter) {
                    DifferentialSlipperyLimiter.OPEN -> {
                        applyPinValues(motorRearLeftPwm = dcmComponent.rearOpenDiffValues[0],
                            motorRearRightPwm = dcmComponent.rearOpenDiffValues[1])
                    }
                    DifferentialSlipperyLimiter.MEDI_0 -> {
                        applyPinValues(motorRearLeftPwm = dcmComponent.rearMedi0DiffValues[0],
                            motorRearRightPwm = dcmComponent.rearMedi0DiffValues[1])
                    }
                    DifferentialSlipperyLimiter.MEDI_1 -> {
                        applyPinValues(motorRearLeftPwm = dcmComponent.rearMedi1DiffValues[0],
                            motorRearRightPwm = dcmComponent.rearMedi1DiffValues[1])
                    }
                    DifferentialSlipperyLimiter.MEDI_2 -> {
                        applyPinValues(motorRearLeftPwm = dcmComponent.rearMedi2DiffValues[0],
                            motorRearRightPwm = dcmComponent.rearMedi2DiffValues[1])
                    }
                    DifferentialSlipperyLimiter.LOCKED -> {
                        applyPinValues(motorRearLeftPwm = dcmComponent.rearLockedDiffValues[0],
                            motorRearRightPwm = dcmComponent.rearLockedDiffValues[1])
                    }
                    DifferentialSlipperyLimiter.AUTO -> {
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
                        "Inner (Left) Front Wheel Speed: ${motorFrontLeftPwmPin}\n" +
                        "Outer (Right) Front Wheel Speed: ${motorFrontRightPwmPin}\n" +
                        "Rear Differential is: ${setupComponent.rearDifferentialSlipperyLimiter}\n" +
                        "Inner (Left) Rear Wheel Speed: ${motorRearLeftPwmPin}\n" +
                        "Outer (Right) Rear Wheel Speed: ${motorRearRightPwmPin}"
                )
            }
            Turn.RIGHT -> {
                when (setupComponent.frontDifferentialSlipperyLimiter) {
                    DifferentialSlipperyLimiter.OPEN -> {
                        applyPinValues(motorFrontRightPwm = dcmComponent.frontOpenDiffValues[0],
                            motorFrontLeftPwm = dcmComponent.frontOpenDiffValues[1])
                    }
                    DifferentialSlipperyLimiter.MEDI_0 -> {
                        applyPinValues(motorFrontRightPwm = dcmComponent.frontMedi0DiffValues[0],
                            motorFrontLeftPwm = dcmComponent.frontMedi0DiffValues[1])
                    }
                    DifferentialSlipperyLimiter.MEDI_1 -> {
                        applyPinValues(motorFrontRightPwm = dcmComponent.frontMedi1DiffValues[0],
                            motorFrontLeftPwm = dcmComponent.frontMedi1DiffValues[1])
                    }
                    DifferentialSlipperyLimiter.MEDI_2 -> {
                        applyPinValues(motorFrontRightPwm = dcmComponent.frontMedi2DiffValues[0],
                            motorFrontLeftPwm = dcmComponent.frontMedi2DiffValues[1])
                    }
                    DifferentialSlipperyLimiter.LOCKED -> {
                        applyPinValues(motorFrontRightPwm = dcmComponent.frontLockedDiffValues[0],
                            motorFrontLeftPwm = dcmComponent.frontLockedDiffValues[1])
                    }
                    DifferentialSlipperyLimiter.AUTO -> {
                        applyPinValues(motorFrontRightPwm = dcmComponent.frontAutoDiffValues[0],
                            motorFrontLeftPwm = dcmComponent.frontAutoDiffValues[1])
                    }
                    else -> {
                        applyPinValues(motorFrontRightPwm = 0, motorFrontLeftPwm = 0)
                    }
                }
                when (setupComponent.rearDifferentialSlipperyLimiter) {
                    DifferentialSlipperyLimiter.OPEN -> {
                        applyPinValues(motorRearRightPwm = dcmComponent.rearOpenDiffValues[0],
                            motorRearLeftPwm = dcmComponent.rearOpenDiffValues[1])
                    }
                    DifferentialSlipperyLimiter.MEDI_0 -> {
                        applyPinValues(motorRearRightPwm = dcmComponent.rearMedi0DiffValues[0],
                            motorRearLeftPwm = dcmComponent.rearMedi0DiffValues[1])
                    }
                    DifferentialSlipperyLimiter.MEDI_1 -> {
                        applyPinValues(motorRearRightPwm = dcmComponent.rearMedi1DiffValues[0],
                            motorRearLeftPwm = dcmComponent.rearMedi1DiffValues[1])
                    }
                    DifferentialSlipperyLimiter.MEDI_2 -> {
                        applyPinValues(motorRearRightPwm = dcmComponent.rearMedi2DiffValues[0],
                            motorRearLeftPwm = dcmComponent.rearMedi2DiffValues[1])
                    }
                    DifferentialSlipperyLimiter.LOCKED -> {
                        applyPinValues(motorRearRightPwm = dcmComponent.rearLockedDiffValues[0],
                            motorRearLeftPwm = dcmComponent.rearLockedDiffValues[1])
                    }
                    DifferentialSlipperyLimiter.AUTO -> {
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
                            "Inner (Right) Front Wheel Speed: ${motorFrontRightPwmPin}\n" +
                            "Outer (Left) Front Wheel Speed: ${motorFrontLeftPwmPin}\n" +
                            "Rear Differential is: ${setupComponent.rearDifferentialSlipperyLimiter}\n" +
                            "Inner (Right) Rear Wheel Speed: ${motorRearRightPwmPin}\n" +
                            "Outer (Left) Rear Wheel Speed: ${motorRearLeftPwmPin}"
                )
            }
            else -> {
                applyPinValues(motorFrontRightPwm = userThrottleValue, motorFrontLeftPwm = userThrottleValue,
                    motorRearRightPwm = userThrottleValue, motorRearLeftPwm = userThrottleValue)
                showMessage(logger = logger,
                    body = "User Throttle Value: $userThrottleValue\n" +
                            "User Steering Value: ${steeringComponent.value}\n" +
                            "Front Differential is: ${setupComponent.frontDifferentialSlipperyLimiter}\n" +
                            "Right Front Wheel Speed: ${motorFrontRightPwmPin}\n" +
                            "Left Front Wheel Speed: ${motorFrontLeftPwmPin}\n" +
                            "Rear Differential is: ${setupComponent.rearDifferentialSlipperyLimiter}\n" +
                            "Right Rear Wheel Speed: ${motorRearRightPwmPin}\n" +
                            "Left Rear Wheel Speed: ${motorRearLeftPwmPin}"
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
                    DifferentialSlipperyLimiter.OPEN ->
                        "Front Differential is \"OPEN\"\n" +
                                "Inner (Left) Front Wheel Speed: ${dcmComponent.frontOpenDiffValues[0]}\n" +
                                "Outer (Right) Front Wheel Speed: ${dcmComponent.frontOpenDiffValues[1]}\n"
                    DifferentialSlipperyLimiter.MEDI_0 ->
                        "Front Differential is \"MEDI 0\"\n" +
                                "Inner (Left) Front Wheel Speed: ${dcmComponent.frontMedi0DiffValues[0]}\n" +
                                "Outer (Right) Front Wheel Speed: ${dcmComponent.frontMedi0DiffValues[1]}\n"
                    DifferentialSlipperyLimiter.MEDI_1 ->
                        "Front Differential is \"MEDI 1\"\n" +
                                "Inner (Left) Front Wheel Speed: ${dcmComponent.frontMedi1DiffValues[0]}\n" +
                                "Outer (Right) Front Wheel Speed: ${dcmComponent.frontMedi1DiffValues[1]}\n"
                    DifferentialSlipperyLimiter.MEDI_2 ->
                        "Front Differential is \"MEDI 2\"\n" +
                                "Inner (Left) Front Wheel Speed: ${dcmComponent.frontMedi2DiffValues[0]}\n" +
                                "Outer (Right) Front Wheel Speed: ${dcmComponent.frontMedi2DiffValues[1]}\n"
                    DifferentialSlipperyLimiter.LOCKED ->
                        "Front Differential is \"LOCKED\"\n" +
                                "Inner (Left) Front Wheel Speed: ${dcmComponent.frontLockedDiffValues[0]}\n" +
                                "Outer (Right) Front Wheel Speed: ${dcmComponent.frontLockedDiffValues[1]}\n"
                    DifferentialSlipperyLimiter.AUTO ->
                        "Front Differential is \"AUTO\"\n" +
                                "Inner (Left) Front Wheel Speed: ${dcmComponent.frontAutoDiffValues[0]}\n" +
                                "Outer (Right) Front Wheel Speed: ${dcmComponent.frontAutoDiffValues[1]}\n"
                    else -> "ERROR in Front differential for Left turn\n"
                } +
                when (setupComponent.rearDifferentialSlipperyLimiter) {
                    DifferentialSlipperyLimiter.OPEN ->
                        "Rear Differential is \"OPEN\"\n" +
                                "Inner (Left) Rear Wheel Speed: ${dcmComponent.rearOpenDiffValues[0]}\n" +
                                "Outer (Right) Rear Wheel Speed: ${dcmComponent.rearOpenDiffValues[1]}"
                    DifferentialSlipperyLimiter.MEDI_0 ->
                        "Rear Differential is \"MEDI 0\"\n" +
                                "Inner (Left) Rear Wheel Speed: ${dcmComponent.rearMedi0DiffValues[0]}\n" +
                                "Outer (Right) Rear Wheel Speed: ${dcmComponent.rearMedi0DiffValues[1]}"
                    DifferentialSlipperyLimiter.MEDI_1 ->
                        "Rear Differential is \"MEDI 1\"\n" +
                                "Inner (Left) Rear Wheel Speed: ${dcmComponent.rearMedi1DiffValues[0]}\n" +
                                "Outer (Right) Rear Wheel Speed: ${dcmComponent.rearMedi1DiffValues[1]}"
                    DifferentialSlipperyLimiter.MEDI_2 ->
                        "Rear Differential is \"MEDI 2\"\n" +
                                "Inner (Left) Rear Wheel Speed: ${dcmComponent.rearMedi2DiffValues[0]}\n" +
                                "Outer (Right) Rear Wheel Speed: ${dcmComponent.rearMedi2DiffValues[1]}"
                    DifferentialSlipperyLimiter.LOCKED ->
                        "Rear Differential is \"LOCKED\"\n" +
                                "Inner (Left) Rear Wheel Speed: ${dcmComponent.rearLockedDiffValues[0]}\n" +
                                "Outer (Right) Rear Wheel Speed: ${dcmComponent.rearLockedDiffValues[1]}"
                    DifferentialSlipperyLimiter.AUTO ->
                        "Rear Differential is \"AUTO\"\n" +
                                "Inner (Left) Rear Wheel Speed: ${dcmComponent.rearAutoDiffValues[0]}\n" +
                                "Outer (Right) Rear Wheel Speed: ${dcmComponent.rearAutoDiffValues[1]}"
                    else -> "ERROR in Rear differential for Left turn"
                }
            }
            Turn.RIGHT -> {
                when (setupComponent.frontDifferentialSlipperyLimiter) {
                    DifferentialSlipperyLimiter.OPEN ->
                        "Front Differential is \"OPEN\"\n" +
                                "Inner (Right) Front Wheel Speed: ${dcmComponent.frontOpenDiffValues[0]}\n" +
                                "Outer (Left) Front Wheel Speed: ${dcmComponent.frontOpenDiffValues[1]}\n"
                    DifferentialSlipperyLimiter.MEDI_0 ->
                        "Front Differential is \"MEDI 0\"\n" +
                                "Inner (Right) Front Wheel Speed: ${dcmComponent.frontMedi0DiffValues[0]}\n" +
                                "Outer (Left) Front Wheel Speed: ${dcmComponent.frontMedi0DiffValues[1]}\n"
                    DifferentialSlipperyLimiter.MEDI_1 ->
                        "Front Differential is \"MEDI 1\"\n" +
                                "Inner (Right) Front Wheel Speed: ${dcmComponent.frontMedi1DiffValues[0]}\n" +
                                "Outer (Left) Front Wheel Speed: ${dcmComponent.frontMedi1DiffValues[1]}\n"
                    DifferentialSlipperyLimiter.MEDI_2 ->
                        "Front Differential is \"MEDI 2\"\n" +
                                "Inner (Right) Front Wheel Speed: ${dcmComponent.frontMedi2DiffValues[0]}\n" +
                                "Outer (Left) Front Wheel Speed: ${dcmComponent.frontMedi2DiffValues[1]}\n"
                    DifferentialSlipperyLimiter.LOCKED ->
                        "Front Differential is \"LOCKED\"\n" +
                                "Inner (Right) Front Wheel Speed: ${dcmComponent.frontLockedDiffValues[0]}\n" +
                                "Outer (Left) Front Wheel Speed: ${dcmComponent.frontLockedDiffValues[1]}\n"
                    DifferentialSlipperyLimiter.AUTO ->
                        "Front Differential is \"AUTO\"\n" +
                                "Inner (Right) Front Wheel Speed: ${dcmComponent.frontAutoDiffValues[0]}\n" +
                                "Outer (Left) Front Wheel Speed: ${dcmComponent.frontAutoDiffValues[1]}\n"
                    else -> "ERROR in Front differential (${setupComponent.frontDifferentialSlipperyLimiter}) for Right turn\n"
                } +
                when (setupComponent.rearDifferentialSlipperyLimiter) {
                    DifferentialSlipperyLimiter.OPEN ->
                        "Rear Differential is \"OPEN\"\n" +
                                "Inner (Right) Rear Wheel Speed: ${dcmComponent.rearOpenDiffValues[0]}\n" +
                                "Outer (Left) Rear Wheel Speed: ${dcmComponent.rearOpenDiffValues[1]}"
                    DifferentialSlipperyLimiter.MEDI_0 ->
                        "Rear Differential is \"MEDI 0\"\n" +
                                "Inner (Right) Rear Wheel Speed: ${dcmComponent.rearMedi0DiffValues[0]}\n" +
                                "Outer (Left) Rear Wheel Speed: ${dcmComponent.rearMedi0DiffValues[1]}"
                    DifferentialSlipperyLimiter.MEDI_1 ->
                        "Rear Differential is \"MEDI 1\"\n" +
                                "Inner (Right) Rear Wheel Speed: ${dcmComponent.rearMedi1DiffValues[0]}\n" +
                                "Outer (Left) Rear Wheel Speed: ${dcmComponent.rearMedi1DiffValues[1]}"
                    DifferentialSlipperyLimiter.MEDI_2 ->
                        "Rear Differential is \"MEDI 2\"\n" +
                                "Inner (Right) Rear Wheel Speed: ${dcmComponent.rearMedi2DiffValues[0]}\n" +
                                "Outer (Left) Rear Wheel Speed: ${dcmComponent.rearMedi2DiffValues[1]}"
                    DifferentialSlipperyLimiter.LOCKED ->
                        "Rear Differential is \"LOCKED\"\n" +
                                "Inner (Right) Rear Wheel Speed: ${dcmComponent.rearLockedDiffValues[0]}\n" +
                                "Outer (Left) Rear Wheel Speed: ${dcmComponent.rearLockedDiffValues[1]}"
                    DifferentialSlipperyLimiter.AUTO ->
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