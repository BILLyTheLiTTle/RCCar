package car.controllers.basic

import car.TYPE_WARNING
import car.showMessage
import com.pi4j.io.gpio.*
import com.pi4j.system.SystemInfo
import com.pi4j.util.CommandArgumentParser
import com.pi4j.wiringpi.Gpio

object EngineImpl:Engine {

    const val SUCCESS = "OK"

    override var engineState = false

    //////
    // Pi related
    val RUN_ON_PI: Boolean = try {
        SystemInfo.getBoardType().name.contains("raspberry", true)
    }
    catch (e: Exception) { false }

    lateinit var gpio: GpioController
    lateinit var pwmPinEnable: GpioPinPwmOutput
    lateinit var pinInput1: GpioPinDigitalOutput
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
            val pinEnable = CommandArgumentParser.getPin(
                RaspiPin::class.java, // pin provider class to obtain pin instance from
                RaspiPin.GPIO_01, // default pin if no pin argument found
                null
            )
            pwmPinEnable = gpio.provisionPwmOutputPin(pinEnable)

            Gpio.pwmSetMode(Gpio.PWM_MODE_MS);
            Gpio.pwmSetRange(100);
            Gpio.pwmSetClock(500);

            pinInput1 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_06, "Pin_Input_1")
            pinInput2 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_05, "Pin_Input_2")
        }
        //////

        engineState = true

        return SUCCESS
    }

    override fun stop(): String {
        //TODO("Shutdown & unprovision GPIOs, PWM, etc")

        //////
        // Pi related
        if(RUN_ON_PI) {
            pinInput1.low()
            pinInput2.low()
            pwmPinEnable.pwm = 0
            gpio.apply {
                shutdown()
                unprovisionPin(pwmPinEnable)
                unprovisionPin(pinInput1)
                unprovisionPin(pinInput2)
            }
        }
        //////

        // TODO reset every significant variable
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

        return SUCCESS
    }
}