package car.dummy

/* Provides a default functionality in case you don't want to implement the interface in your code.
 */
class DummyHandler: DummyListener {
    override fun onDummyValueChange(value: Int) = value
}