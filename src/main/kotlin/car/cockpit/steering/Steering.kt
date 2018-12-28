package car.cockpit.steering

interface Steering {

    val value: Int
    val direction: String

    fun turn(direction: String, value: Int = 0): String

    fun reset()
}