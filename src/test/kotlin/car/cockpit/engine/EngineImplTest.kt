package car.cockpit.engine

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension

@SpringBootTest
@ExtendWith(SpringExtension::class)
internal class EngineImplTest {

    // start
    @Test
    fun `start the engine`() {
        val ret = EngineImpl.start()
        assertThat(ret).isEqualTo(EngineSystem.SUCCESS)
        assertThat(EngineImpl.engineState).isTrue()
    }
    @Test
    fun `start the engine on PC`() {
        EngineImpl.start()
        assertThrows(UninitializedPropertyAccessException::class.java) {
            EngineImpl.gpio
        }
        assertThat(EngineImpl.RUN_ON_PI).isFalse()
    }

    // stop
    @Test
    fun `stop the engine`() {
        val ret = EngineImpl.stop()
        assertThat(ret).isEqualTo(EngineSystem.SUCCESS)
        assertThat(EngineImpl.engineState).isFalse()
    }
}