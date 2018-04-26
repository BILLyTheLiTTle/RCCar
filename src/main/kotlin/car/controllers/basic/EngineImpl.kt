package car.controllers.basic

object EngineImpl:Engine {

    private const val SUCCESS = "OK"

    override var engineState = false

    override fun start(): String {
        //TODO("mode=input/output & and pin = false for GPIOs, PWM, etc")
        engineState = true
        return SUCCESS
    }

    override fun stop(): String {
        //TODO("Shutdown & unprovision GPIOs, PWM, etc")
        engineState = false
        return SUCCESS
    }
}