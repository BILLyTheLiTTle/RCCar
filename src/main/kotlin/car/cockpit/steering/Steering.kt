package car.cockpit.steering

interface Steering {

    val value: Int
    val direction: Turn

    fun turn(direction: Turn, value: Int = 0): String

    fun reset()
}

enum class Turn {
    RIGHT, LEFT, STRAIGHT, NOTHING;

    companion object {
        // Give the name of the enum which will be the default when needed
        const val DEFAULT = "STRAIGHT"
    }
}