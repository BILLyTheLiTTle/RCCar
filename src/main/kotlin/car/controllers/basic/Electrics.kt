package car.controllers.basic

interface Electrics {
    val lights: Map<String, Int>

    var positionLightsState: Boolean
    var drivingLightsState: Boolean
    var longRangeLightsState: Boolean

    var brakingLightsState: Boolean

    fun doHeadlightsSignal(): String

    // TODO the stuff here
    @Synchronized fun handleLeds() {

    }

    fun reset() {
        longRangeLightsState =false
        drivingLightsState = false
        positionLightsState = false
        // TODO handleLeds(_lights) to turn LEDs off
    }
}