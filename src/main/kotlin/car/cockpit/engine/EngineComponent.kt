package car.cockpit.engine

import car.LoggerTypes.WARNING
import car.cockpit.electrics.Electrics
import car.cockpit.pedals.ThrottleBrake
import car.cockpit.setup.Setup
import car.cockpit.steering.Steering
import car.parts.raspi.pins.BCM_12
import car.parts.raspi.pins.BCM_13
import car.parts.raspi.pins.BCM_18
import car.parts.raspi.pins.BCM_19
import car.showMessage
import com.pi4j.gpio.extension.mcp.MCP23S17GpioProvider
import com.pi4j.gpio.extension.mcp.MCP23S17Pin
import com.pi4j.io.gpio.*
import com.pi4j.io.spi.SpiChannel
import com.pi4j.system.SystemInfo
import com.pi4j.util.CommandArgumentParser
import com.pi4j.wiringpi.Gpio
import org.slf4j.LoggerFactory
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

    private val logger = LoggerFactory.getLogger(EngineComponent::class.java)

    //////
    // Pi related
    override val runOnPi = try {
        SystemInfo.getBoardType().name.contains("raspberry", true)
    }
    catch (e: Exception) { false }

    override lateinit var gpio: GpioController
    override lateinit var motorsPinsProvider: MCP23S17GpioProvider

    //////

    override fun start(): String {
        showMessage(msgType = WARNING,
            logger = logger,
            body = "Software IS ${if (runOnPi) "" else "NOT"} running on Pi.")

        //////
        // Pi related
        steeringComponent.initialize()


        if(runOnPi) {
            // Initialize RasPi hardware
            gpio = GpioFactory.getInstance()
            Gpio.pwmSetMode(Gpio.PWM_MODE_MS)
            Gpio.pwmSetRange(100)
            Gpio.pwmSetClock(500)

            // Initialize extra hardware parts
            motorsPinsProvider = MCP23S17GpioProvider(MCP23S17GpioProvider.ADDRESS_0, SpiChannel.CS0)

            throttleBrake.initialize()
        }
        //////

        engineState = true

        return SUCCESS
    }

    override fun stop(): String {
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