package car.dummy

/* The purpose of this interface is to simulate events when a pin changes value
    when the software is running on a PC and not on a Pi
 */
interface DummyListener {
    fun onDummyValueChange()
}