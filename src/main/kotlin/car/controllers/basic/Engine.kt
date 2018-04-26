package car.controllers.basic

interface Engine {
    fun start(): Pair<String,Boolean>
    fun stop(): Pair<String,Boolean>
}