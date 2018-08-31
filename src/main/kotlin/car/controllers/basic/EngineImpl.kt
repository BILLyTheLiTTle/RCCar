package car.controllers.basic

import car.TYPE_WARNING
import car.raspi.hardware.BcmPins
import car.server.EngineSystem
import car.showMessage
import com.pi4j.gpio.extension.mcp.MCP23S17GpioProvider
import com.pi4j.gpio.extension.mcp.MCP23S17Pin
import com.pi4j.io.gpio.*
import com.pi4j.io.spi.SpiChannel
import com.pi4j.system.SystemInfo
import com.pi4j.util.CommandArgumentParser
import com.pi4j.wiringpi.Gpio

object EngineImpl:Engine {

    override var engineState = false

    //////
    // Pi related
    val RUN_ON_PI: Boolean = try {
        SystemInfo.getBoardType().name.contains("raspberry", true)
    }
    catch (e: Exception) { false }

    lateinit var gpio: GpioController
    lateinit var motorsNledsPinsProvider: MCP23S17GpioProvider
    lateinit var motorFrontRightPwmPin: GpioPinPwmOutput
    lateinit var motorFrontRightDirPin: GpioPinDigitalOutput
    lateinit var motorFrontLeftPwmPin: GpioPinPwmOutput
    lateinit var motorFrontLeftDirPin: GpioPinDigitalOutput
    lateinit var motorRearRightPwmPin: GpioPinPwmOutput
    lateinit var motorRearRightDirPin: GpioPinDigitalOutput
    lateinit var motorRearLeftPwmPin: GpioPinPwmOutput
    lateinit var motorRearLeftDirPin: GpioPinDigitalOutput
    //////

    override fun start(): String {
        showMessage(msgType = TYPE_WARNING,
            title = "ENGINE",
            body = "Software IS ${if (RUN_ON_PI) "" else "NOT"} running on Pi.")

        //////
        // Pi related
        if(RUN_ON_PI) {
            gpio = GpioFactory.getInstance()
            motorsNledsPinsProvider = MCP23S17GpioProvider(MCP23S17GpioProvider.ADDRESS_0, SpiChannel.CS0)

            Gpio.pwmSetMode(Gpio.PWM_MODE_MS)
            Gpio.pwmSetRange(100)
            Gpio.pwmSetClock(500)

            initializeMotors()
        }
        //////

        engineState = true

        return EngineSystem.SUCCESS
    }

    private fun initializeMotors(){
        // Front Right Motor
        val motorFrontRightPin = CommandArgumentParser.getPin(
            RaspiPin::class.java, // pin provider class to obtain pin instance from
            BcmPins.BCM_13, // default pin if no pin argument found
            null
        )
        motorFrontRightPwmPin = gpio.provisionPwmOutputPin(motorFrontRightPin)
        motorFrontRightDirPin = gpio.provisionDigitalOutputPin(motorsNledsPinsProvider, MCP23S17Pin.GPIO_A1,
            "Front Right Motor Dir Pin", PinState.LOW)

        // Front Left Motor
        val motorFrontLeftPin = CommandArgumentParser.getPin(
            RaspiPin::class.java, // pin provider class to obtain pin instance from
            BcmPins.BCM_19, // default pin if no pin argument found
            null
        )
        motorFrontLeftPwmPin = gpio.provisionPwmOutputPin(motorFrontLeftPin)
        motorFrontLeftDirPin = gpio.provisionDigitalOutputPin(motorsNledsPinsProvider, MCP23S17Pin.GPIO_A0,
            "Front Left Motor Dir Pin", PinState.LOW)

        // Rear Right Motor
        val motorRearRightPin = CommandArgumentParser.getPin(
            RaspiPin::class.java, // pin provider class to obtain pin instance from
            BcmPins.BCM_12, // default pin if no pin argument found
            null
        )
        motorRearRightPwmPin = gpio.provisionPwmOutputPin(motorRearRightPin)
        motorRearRightDirPin = gpio.provisionDigitalOutputPin(motorsNledsPinsProvider, MCP23S17Pin.GPIO_A3,
            "Rear Right Motor Dir Pin", PinState.LOW)

        // Rear Left Motor
        val motorRearLeftPin = CommandArgumentParser.getPin(
            RaspiPin::class.java, // pin provider class to obtain pin instance from
            BcmPins.BCM_18, // default pin if no pin argument found
            null
        )
        motorRearLeftPwmPin = gpio.provisionPwmOutputPin(motorRearLeftPin)
        motorRearLeftDirPin = gpio.provisionDigitalOutputPin(motorsNledsPinsProvider, MCP23S17Pin.GPIO_A2,
            "Rear Left Motor Dir Pin", PinState.LOW)
    }

    override fun stop(): String {
        ////// Shutdown & unprovision GPIOs, PWM, etc
        // Pi related
        if(RUN_ON_PI) {
            motorFrontRightPwmPin.pwm = 0
            motorFrontRightDirPin.low()

            motorFrontLeftPwmPin.pwm = 0
            motorFrontLeftDirPin.low()

            motorRearRightPwmPin.pwm = 0
            motorRearRightDirPin.low()

            motorRearLeftPwmPin.pwm = 0
            motorRearLeftDirPin.low()

            gpio.apply {
                shutdown()
                unprovisionPin(motorFrontRightPwmPin)
                unprovisionPin(motorFrontRightDirPin)

                unprovisionPin(motorFrontLeftPwmPin)
                unprovisionPin(motorFrontLeftDirPin)

                unprovisionPin(motorRearRightPwmPin)
                unprovisionPin(motorRearRightDirPin)

                unprovisionPin(motorRearLeftPwmPin)
                unprovisionPin(motorRearLeftDirPin)
            }
        }
        //////

        // reset every significant variable
        // Setup
        SetupImpl.reset()

        // Temperatures
        //TemperaturesImpl.reset()

        // Electrics
        ElectricsImpl.reset()

        // Steering
        SteeringImpl.reset()

        // ThrottleBrake
        ThrottleBrakeImpl.reset()

        // Engine
        reset()

        return EngineSystem.SUCCESS
    }
}