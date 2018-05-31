package car.controllers.temperatures

import car.server.EngineSystem
import java.util.*


const val WARNING_TYPE_NOTHING = EngineSystem.EMPTY_STRING
const val WARNING_TYPE_NORMAL = "normal"
const val WARNING_TYPE_MEDIUM = "medium"
const val WARNING_TYPE_HIGH = "high"

interface Temperature {

    val value: Int
    val warning: String

    @Synchronized
    fun readSensor(sensorId: String): Int {
        //Thread.sleep(10000)
        return Random().nextInt(4)
    }
    fun reset(){
        //TODO what?!
    }
}