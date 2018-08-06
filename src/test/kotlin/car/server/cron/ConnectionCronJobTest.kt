package car.server.cron

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


@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class ConnectionCronJobTest {

    @SpyBean
    private val myTask: ConnectionCronJob? = null

    // checkClientStatus
    @Test
    fun `function called from scheduler`() {
        // Is this really working?
        Awaitility.await().atMost(Duration.FIVE_HUNDRED_MILLISECONDS)
            .untilAsserted {
                verify(myTask, atLeast(1))
            }
    }
    @Test
    fun `engine state on with wrong ip format`() {
        // TODO it some time
    }
}