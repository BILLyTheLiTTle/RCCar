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

    /* When I wrote @Synchronized here, the following error was thrown on the Pi

        java.lang.ClassFormatError:
        Method handleLeds in class car/controllers/basic/Electrics has illegal modifiers: 0x421

        I don't understand why, but implement this function in the child class
    */
    fun handleLeds()

    fun reset()
}