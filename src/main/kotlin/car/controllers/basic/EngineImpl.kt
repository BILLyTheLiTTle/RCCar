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
    lateinit var pinInput2: GpioPinDigitalOutput
    //////

    override fun start(): String {
        //TODO("mode=input/output & and pin = false for GPIOs, PWM, etc")
        //////
        // Pi related
        showMessage(msgType = TYPE_WARNING,
            title = "ENGINE",
            body = "Software IS ${if (RUN_ON_PI) "" else "NOT"} running on Pi.")

        if(RUN_ON_PI) {
            gpio = GpioFactory.getInstance()
            motorsNledsPinsProvider = MCP23S17GpioProvider(MCP23S17GpioProvider.ADDRESS_0, SpiChannel.CS0)

            Gpio.pwmSetMode(Gpio.PWM_MODE_MS);
            Gpio.pwmSetRange(100);
            Gpio.pwmSetClock(500);

            // Front Right Motor
            val motorFrontRightPin = CommandArgumentParser.getPin(
                RaspiPin::class.java, // pin provider class to obtain pin instance from
                BcmPins.BCM_09, // default pin if no pin argument found
                null
            )
            motorFrontRightPwmPin = gpio.provisionPwmOutputPin(motorFrontRightPin)
            motorFrontRightDirPin = gpio.provisionDigitalOutputPin(motorsNledsPinsProvider, MCP23S17Pin.GPIO_A1,
                "Front Right Motor Dir Pin", PinState.LOW)

            pinInput2 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_05, "Pin_Input_2")
        }
        //////

        engineState = true

        return EngineSystem.SUCCESS
    }

    override fun stop(): String {
        //TODO("Shutdown & unprovision GPIOs, PWM, etc")

        //////
        // Pi related
        if(RUN_ON_PI) {
            motorFrontRightPwmPin.pwm = 0
            motorFrontRightDirPin.low()

            pinInput2.low()
            gpio.apply {
                shutdown()
                unprovisionPin(motorFrontRightPwmPin)
                unprovisionPin(motorFrontRightDirPin)

                unprovisionPin(pinInput2)
            }
        }
        //////

        // TODO reset every significant variable
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