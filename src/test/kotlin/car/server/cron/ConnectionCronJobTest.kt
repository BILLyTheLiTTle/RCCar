package car.server.cron

import car.controllers.basic.SetupImpl
import org.assertj.core.api.Assertions.*
import org.awaitility.Duration
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.getForEntity
import org.springframework.http.HttpStatus
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.boot.test.mock.mockito.SpyBean
import org.awaitility.Duration.FIVE_SECONDS
import org.awaitility.kotlin.await


@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class ConnectionCronJobTest {

    @SpyBean
    private val myTask: ConnectionCronJob? = null

    // checkClientStatus
    @Test
    fun `has checkClientStatus run in schedule`() {
        await.atMost(Duration.FIVE_SECONDS)
            .untilAsserted {
                verify(myTask, times(1)).work()
            }
    }
}