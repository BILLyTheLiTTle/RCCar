package car.dummy

class DummyRunningTask: Runnable {
    private lateinit var dummyListener: DummyListener

    fun setDummyListener(dummyListener: DummyListener) {
        this.dummyListener = dummyListener
    }

    override fun run() {
        try {
            Thread.sleep(100)
            dummyListener.onDummyValueChange()
        } catch (e: Exception) {
            print(e.message)
        }

    }
}