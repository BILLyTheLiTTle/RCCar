package car.controllers.basic

import car.server.SteeringSystem.Companion.ACTION_STRAIGHT

interface Steering {
    var direction: String
    var value: Int

    fun turn(direction: String, value: Int = 0): String

    fun reset() {
        direction = ACTION_STRAIGHT
        value = 0
    }
}