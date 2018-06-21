package car.controllers.basic

interface Steering {
    val theta: Double
    val phi: Double

    fun turn(direction: String, value: Int = 0): String

    fun reset()
}