package car.cockpit.engine

interface Engine {
    var engineState: Boolean
    fun start(): String
    fun stop(): String

    fun reset() {
        engineState = false
    }
}