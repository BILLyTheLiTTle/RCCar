package car.controllers.basic

import car.server.ThrottleBrakeSystem

object EngineImpl:Engine {

    const val SUCCESS = "OK"

    override var engineState = false

    override fun start(): String {
        //TODO("mode=input/output & and pin = false for GPIOs, PWM, etc")
        engineState = true
        return SUCCESS
    }

    override fun stop(): String {
        //TODO("Shutdown & unprovision GPIOs, PWM, etc")

        // TODO reset every significant variable
        // ThrottleBrake
        ThrottleBrakeImpl.reset()

        // Engine
        reset()

        return SUCCESS
    }
}