package car.controllers.temperatures

import car.server.EngineSystem


const val WARNING_TYPE_NOTHING = EngineSystem.EMPTY_STRING
const val WARNING_TYPE_NORMAL = "normal"
const val WARNING_TYPE_MEDIUM = "medium"
const val WARNING_TYPE_HIGH = "high"

interface Temperature {

    val value: Int
    val warning: String

    fun reset(){
        //TODO what?!
    }
}