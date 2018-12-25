package car.cockpit.steering

interface Steering {

    fun turn(direction: String, value: Int = 0): String

    fun reset()
}