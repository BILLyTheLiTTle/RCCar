package car.controllers.basic

import car.server.EngineSystem
import org.assertj.core.api.Assertions
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
        Assertions.assertThat(ret).isEqualTo(EngineSystem.SUCCESS)
    }
    @Test
    fun `start the engine on PC`() {
    }

    @Test
    fun stop() {
    }
}