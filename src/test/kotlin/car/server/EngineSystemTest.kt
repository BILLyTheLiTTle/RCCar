package car.server

import car.controllers.basic.EngineImpl
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.getForEntity
import org.springframework.http.HttpStatus
import org.springframework.test.context.junit.jupiter.SpringExtension


@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class EngineSystemTest(@Autowired val restTemplate: TestRestTemplate) {

    @Test
    fun `rc controller server should be empty`() {
        val entity = restTemplate.getForEntity<String>("/start_engine")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(EngineSystem.nanohttpClientIp).isEqualTo(EngineSystem.EMPTY_STRING)
        assertThat(EngineSystem.nanohttpClientPort).isEqualTo(EngineSystem.EMPTY_INT)
    }
    @Test
    fun `rc controller server should have a value`() {
        val entity = restTemplate.getForEntity<String>("/start_engine?nanohttp_client_ip=ip&nanohttp_client_port=1")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(EngineSystem.nanohttpClientIp).isEqualTo("ip")
        assertThat(EngineSystem.nanohttpClientPort).isEqualTo(1)
    }

    @Test
    fun `start engine without parameters`() {
        val entity = restTemplate.getForEntity<String>("/start_engine")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body).isEqualTo(EngineSystem.SUCCESS)
    }

    @Test
    fun `engine state should be on`() {
        restTemplate.getForEntity<String>("/start_engine")
        val entity = restTemplate.getForEntity<String>("/get_engine_state")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body?.toBoolean()).isEqualTo(EngineImpl.engineState).isTrue()
    }
    @Test
    fun `engine state should be off`() {
        restTemplate.getForEntity<String>("/stop_engine")
        val entity = restTemplate.getForEntity<String>("/get_engine_state")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(EngineImpl.engineState).isFalse()
    }

    @Test
    fun `stop the engine`() {
        val entity = restTemplate.getForEntity<String>("/stop_engine")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body).isEqualTo(EngineSystem.SUCCESS)
    }
}