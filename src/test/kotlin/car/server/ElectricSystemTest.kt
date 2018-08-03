package car.server

import org.assertj.core.api.Assertions.*
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
internal class ElectricSystemTest(@Autowired val restTemplate: TestRestTemplate) {

    @Test
    fun setDirectionLights() {
    }

    @Test
    fun getDirectionLights() {
    }

    @Test
    fun setMainLightsState() {
    }

    @Test
    fun getMainLightsState() {
    }

    @Test
    fun setReverseLightsState() {
    }

    @Test
    fun getReverseLightsState() {
    }

    @Test
    fun setEmergencyLightsState() {
    }

    @Test
    fun getEmergencyLightsState() {
    }
}