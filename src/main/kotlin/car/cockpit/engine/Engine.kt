package car.cockpit.engine

import com.pi4j.gpio.extension.mcp.MCP23S17GpioProvider
import com.pi4j.io.gpio.GpioController
import com.pi4j.io.gpio.GpioPinDigitalOutput
import com.pi4j.io.gpio.GpioPinPwmOutput

interface Engine {

    val RunOnPi: Boolean
    var engineState: Boolean
    val gpio: GpioController
    val motorsNledsPinsProvider: MCP23S17GpioProvider
    val motorFrontRightPwmPin: GpioPinPwmOutput
    val motorFrontRightDirPin: GpioPinDigitalOutput
    val motorFrontLeftPwmPin: GpioPinPwmOutput
    val motorFrontLeftDirPin: GpioPinDigitalOutput
    val motorRearRightPwmPin: GpioPinPwmOutput
    val motorRearRightDirPin: GpioPinDigitalOutput
    val motorRearLeftPwmPin: GpioPinPwmOutput
    val motorRearLeftDirPin: GpioPinDigitalOutput

    fun start(): String
    fun stop(): String

    fun reset() {
        engineState = false
    }
}