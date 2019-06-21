package car.cockpit.steering

interface Steering {

    val value: SteeringValues
    val direction: Turn

    fun turn(direction: Turn, value: SteeringValues = SteeringValues.VALUE_00): String

    fun reset()
}

enum class Turn {
    RIGHT, LEFT, STRAIGHT, NOTHING;

    companion object {
        // Give the name of the enum which will be the default when needed
        const val DEFAULT = "STRAIGHT"
    }
}

// Theta angle is the inner front wheel angle in degrees
// Phi angle is the outer front wheel angle in degrees
enum class SteeringValues(val value: Int, val angleTheta: Double, val anglePhi: Double){
    VALUE_00(0, 0.0, 0.0),
    VALUE_20(0, 22.0, 19.9),
    VALUE_40(20, 45.0, 37.9),
    VALUE_60(40, 52.0, 41.7),
    VALUE_80(60, 60.0, 49.2),
    VALUE_100(100, 66.0, 47.8),
    NOTHING(-1, -1.0, -1.0);

    companion object {
        // Give the name of the enum which will be the default when needed
        const val DEFAULT = "VALUE_00"
    }
}