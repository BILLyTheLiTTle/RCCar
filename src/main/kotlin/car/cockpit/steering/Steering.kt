package car.cockpit.steering

import car.*

interface Steering {

    val value: SteeringValue
    val direction: Turn

    fun turn(direction: Turn, value: SteeringValue = SteeringValue.VALUE_00): String

    fun initialize()
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
enum class SteeringValue(val value: Int, val angleTheta: Double, val anglePhi: Double){
    VALUE_00(0, THETA_ANGLE_00, PHI_ANGLE_00),
    VALUE_20(20, THETA_ANGLE_20, PHI_ANGLE_20),
    VALUE_40(40, THETA_ANGLE_40, PHI_ANGLE_40),
    VALUE_60(60, THETA_ANGLE_60, PHI_ANGLE_60),
    VALUE_80(80, THETA_ANGLE_80, PHI_ANGLE_80),
    VALUE_100(100, THETA_ANGLE_100, PHI_ANGLE_100),
    NOTHING(-1, -1.0, -1.0);

    companion object {
        // Give the name of the enum which will be the default when needed
        const val DEFAULT = "VALUE_00"
    }
}