package car.controllers.basic

import car.server.ThrottleBrakeSystem

interface ThrottleBrake {
    var action: String
    var value: Int

    var getParkingBrakeState: Boolean
    var getHandbrakeState: Boolean
    var getMotionState: String
    var isMovingForward: Boolean
    var isMovingBackward: Boolean
    var isBrakingStill: Boolean
    var isNeutral: Boolean

    fun throttle(direction: String, value: Int): String
    fun brake(value: Int): String
    fun parkingBrake(value: Int): String
    fun handbrake(value: Int): String
    fun setNeutral(): String

    fun applyPwmValues(motorFRpin00: Int = 0, motorFRpin01: Int = 0,
                                 motorFLpin00: Int = 0, motorFLpin01: Int = 0,
                                 motorRRpin00: Int = 0, motorRRpin01: Int = 0,
                                 motorRLpin00: Int = 0, motorRLpin01: Int = 0){
        //TODO implement it here
    }

    fun reset() {
        action = ThrottleBrakeSystem.ACTION_NEUTRAL
        value = 0
    }
}