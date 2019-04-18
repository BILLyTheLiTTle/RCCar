package car.cockpit.dashboard.lights.warning.temperatures

import java.util.*

interface Temperature {

    val id: String
    val value: Int
    val warning: String

    val minMediumTemp
        get() = -1
    val maxMediumTemp
        get() = -1

    val errorTemp
        get() = 1000
    val errorTempGenerator
        get() =  Random().nextInt(100)

    fun reset(){
        //TODO what?!
    }
}

val lock = Any()