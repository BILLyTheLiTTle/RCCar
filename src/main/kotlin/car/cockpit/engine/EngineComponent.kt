package car.cockpit.engine

import car.TYPE_WARNING
import car.cockpit.electrics.Electrics
import car.cockpit.pedals.ThrottleBrake
import car.cockpit.setup.Setup
import car.cockpit.steering.Steering
import car.raspi.pins.BCM_12
import car.raspi.pins.BCM_13
import car.raspi.pins.BCM_18
import car.raspi.pins.BCM_19
import car.showMessage
import com.pi4j.gpio.extension.mcp.MCP23S17GpioProvider
import com.pi4j.gpio.extension.mcp.MCP23S17Pin
import com.pi4j.io.gpio.*
import com.pi4j.io.spi.SpiChannel
import com.pi4j.system.SystemInfo
import com.pi4j.util.CommandArgumentParser
import com.pi4j.wiringpi.Gpio
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component

@Component("Engine Component")
class EngineComponent: Engine {

    @Autowired
    @Qualifier("Throttle -n- Brake Component")
    private lateinit var throttleBrake: ThrottleBrake

    @Autowired
    private lateinit var setupComponent: Setup

    @Autowired
    private lateinit var steeringComponent: Steering

    @Autowired
    private lateinit var electricsComponent: Electrics

    override var engineState = false

    //////
    // Pi related
    override val runOnPi = try {
        SystemInfo.getBoardType().name.contains("raspberry", true)
    }
    catch (e: Exception) { false }

    override lateinit var gpio: GpioController
    override lateinit var motorsNledsPinsProvider: MCP23S17GpioProvider
    override lateinit var motorFrontRightPwmPin: GpioPinPwmOutput
    override lateinit var motorFrontRightDirPin: GpioPinDigitalOutput
    override lateinit var motorFrontLeftPwmPin: GpioPinPwmOutput
    override lateinit var motorFrontLeftDirPin: GpioPinDigitalOutput
    override lateinit var motorRearRightPwmPin: GpioPinPwmOutput
    override lateinit var motorRearRightDirPin: GpioPinDigitalOutput
    override lateinit var motorRearLeftPwmPin: GpioPinPwmOutput
    override lateinit var motorRearLeftDirPin: GpioPinDigitalOutput
    //////

    override fun start(): String {
        showMessage(msgType = TYPE_WARNING,
            title = "ENGINE",
            body = "Software IS ${if (runOnPi) "" else "NOT"} running on Pi.")

        //////
        // Pi related
        if(runOnPi) {
            gpio = GpioFactory.getInstance()
            motorsNledsPinsProvider = MCP23S17GpioProvider(MCP23S17GpioProvider.ADDRESS_0, SpiChannel.CS0)

            Gpio.pwmSetMode(Gpio.PWM_MODE_MS)
            Gpio.pwmSetRange(100)
            Gpio.pwmSetClock(500)

            initializeMotors()
        }
        //////

        engineState = true

        return SUCCESS
    }

    private fun initializeMotors(){
        // Front Right Motor
        val motorFrontRightPin = CommandArgumentParser.getPin(
            RaspiPin::class.java, // pin provider class to obtain pin instance from
            BCM_13, // default pin if no pin argument found
            null
        )
        motorFrontRightPwmPin = gpio.provisionPwmOutputPin(motorFrontRightPin)
        motorFrontRightDirPin = gpio.provisionDigitalOutputPin(
            motorsNledsPinsProvider, MCP23S17Pin.GPIO_A1,
            "Front Right Motor Dir Pin", PinState.LOW)

        // Front Left Motor
        val motorFrontLeftPin = CommandArgumentParser.getPin(
            RaspiPin::class.java, // pin provider class to obtain pin instance from
            BCM_19, // default pin if no pin argument found
            null
        )
        motorFrontLeftPwmPin = gpio.provisionPwmOutputPin(motorFrontLeftPin)
        motorFrontLeftDirPin = gpio.provisionDigitalOutputPin(
            motorsNledsPinsProvider, MCP23S17Pin.GPIO_A0,
            "Front Left Motor Dir Pin", PinState.LOW)

        // Rear Right Motor
        val motorRearRightPin = CommandArgumentParser.getPin(
            RaspiPin::class.java, // pin provider class to obtain pin instance from
            BCM_12, // default pin if no pin argument found
            null
        )
        motorRearRightPwmPin = gpio.provisionPwmOutputPin(motorRearRightPin)
        motorRearRightDirPin = gpio.provisionDigitalOutputPin(
            motorsNledsPinsProvider, MCP23S17Pin.GPIO_A3,
            "Rear Right Motor Dir Pin", PinState.LOW)

        // Rear Left Motor
        val motorRearLeftPin = CommandArgumentParser.getPin(
            RaspiPin::class.java, // pin provider class to obtain pin instance from
            BCM_18, // default pin if no pin argument found
            null
        )
        motorRearLeftPwmPin = gpio.provisionPwmOutputPin(motorRearLeftPin)
        motorRearLeftDirPin = gpio.provisionDigitalOutputPin(
            motorsNledsPinsProvider, MCP23S17Pin.GPIO_A2,
            "Rear Left Motor Dir Pin", PinState.LOW)
    }

    override fun stop(): String {
        ////// Shutdown & unprovision GPIOs, PWM, etc
        // Pi related
        if(runOnPi) {
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
        setupComponent.reset()

        // Temperatures
        //TemperaturesImpl.reset()

        // Electrics
        electricsComponent.reset()

        // Steering
        steeringComponent.reset()

        // ThrottleBrake
        throttleBrake.reset()

        // Engine
        reset()

        return SUCCESS
    }
}