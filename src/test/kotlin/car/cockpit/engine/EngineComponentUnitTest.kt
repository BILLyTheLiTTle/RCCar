package car.cockpit.engine

import car.categories.UnitTest
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension

@SpringBootTest
@ExtendWith(SpringExtension::class)
@UnitTest
internal class EngineComponentUnitTest(@Autowired val engineComponent: Engine) {

    // start
    @Test
    fun `start the engine`() {
        val ret = engineComponent.start()
        assertThat(ret).isEqualTo(SUCCESS)
        assertThat(engineComponent.engineState).isTrue()
    }
    @Test
    fun `start the engine on PC`() {
        engineComponent.start()
        assertThrows(UninitializedPropertyAccessException::class.java) {
            engineComponent.gpio
        }
        assertThat(engineComponent.runOnPi).isFalse()
    }

    // stop
    @Test
    fun `stop the engine`() {
        val ret = engineComponent.stop()
        assertThat(ret).isEqualTo(SUCCESS)
        assertThat(engineComponent.engineState).isFalse()
    }
}