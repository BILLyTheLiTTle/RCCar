package car.server.cron

import car.controllers.basic.EngineImpl
import car.server.EngineSystem
import org.assertj.core.api.Assertions.*
import org.awaitility.Awaitility
import org.awaitility.Duration
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.getForEntity
import org.springframework.http.HttpStatus
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.boot.test.mock.mockito.SpyBean
import kotlin.reflect.full.declaredMembers
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.javaField


@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class ConnectionCronJobTest {

    @SpyBean
    private val task: ConnectionCronJob? = null

    // checkClientStatus
    @Test
    fun `function called from scheduler`() {
        // Is this really working?
        Awaitility.await().atMost(Duration.FIVE_HUNDRED_MILLISECONDS)
            .untilAsserted {
                verify(task, atLeast(1))
            }
    }
    @Test
    fun `engine state on with wrong ip format`() {
        EngineImpl.engineState = true
        val field = EngineSystem.Companion::nanohttpClientIp.javaField
        field?.isAccessible = true
        field?.set(String::class, "oops")
        val ret = task?.checkClientStatus()
        assertThat(ret).isEqualTo(ConnectionCronJob.IP_ERROR).isEqualTo(0)
    }
    @Test
    fun `engine state on with client still online`() {

    }
    @Test
    fun `engine state on with client came online`() {

    }
    @Test
    fun `engine state on with client not found`() {

    }
    @Test
    fun `engine state on with client still not found`() {

    }
    @Test
    fun `engine state off`() {

    }
}