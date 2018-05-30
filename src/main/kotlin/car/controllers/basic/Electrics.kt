package car.controllers.basic

interface Electrics {
    var positionLightsState: Boolean
    var drivingLightsState: Boolean
    var longRangeLightsState: Boolean

    var brakingLightsState: Boolean

    var reverseLightsState: Boolean

    var leftTurnLightsState: Boolean
    var rightTurnLightsState: Boolean

    var emergencyLightsState: Boolean

    fun doHeadlightsSignal(): String

    // TODO the stuff here
    @Synchronized fun handleLeds() {

    }

    fun reset()
}