package car.controllers.basic

interface Steering {

    fun turn(direction: String, value: Int = 0): String

    fun reset()
}