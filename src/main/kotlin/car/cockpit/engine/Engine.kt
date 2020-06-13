package car.cockpit.engine

import com.pi4j.gpio.extension.mcp.MCP23S17GpioProvider
import com.pi4j.io.gpio.GpioController
import com.pi4j.io.gpio.GpioPinDigitalOutput
import com.pi4j.io.gpio.GpioPinPwmOutput

interface Engine {

    val runOnPi: Boolean
    var engineState: Boolean
    val gpio: GpioController
    val motorsPinsProvider: MCP23S17GpioProvider


    fun start(): String
    fun stop(): String

    fun reset() {
        engineState = false
    }
}