package car.controllers.basic

interface Engine {
    suspend fun start(): Pair<String,Boolean>
    fun stop(): Pair<String,Boolean>
}