package car.controllers.basic

object EngineImpl:Engine {

    const val SUCCESS = "OK"

    var engineState = false

    override fun start(): Pair<String, Boolean> {
        //TODO("mode=input/output & and pin = false for GPIOs, PWM, etc")
        engineState = true
        return Pair(SUCCESS, engineState)
    }

    override fun stop(): Pair<String, Boolean> {
        //TODO("Shutdown & unprovision GPIOs, PWM, etc")
        engineState = false
        return Pair(SUCCESS, engineState)
    }
}