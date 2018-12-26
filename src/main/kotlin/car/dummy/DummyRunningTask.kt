package car.dummy

import java.util.*

class DummyRunningTask(private val dummyListener: DummyListener) : Runnable {

    override fun run() {
        try {
            Thread.sleep(100)
            dummyListener.onDummyValueChange(Random().nextInt(50)+1)
        } catch (e: Exception) {
            print(e.message)
        }

    }
}