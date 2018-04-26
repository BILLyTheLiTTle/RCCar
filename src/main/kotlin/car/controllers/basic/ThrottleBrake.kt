package car.controllers.basic

import car.server.ThrottleBrakeSystem

interface ThrottleBrake {
    var action: String
    var value: Int
    fun throttle(direction: String, value: Int): String
    fun brake(value: Int): String
    fun parkingBrake(value: Int): String
    fun handbrake(value: Int): String

    fun reset() {
        action = ThrottleBrakeSystem.ACTION_STILL
        value = 0
    }
}