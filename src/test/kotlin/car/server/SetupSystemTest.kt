package car.server

import car.controllers.basic.SteeringImpl
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
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
internal class SetupSystemTest(@Autowired val restTemplate: TestRestTemplate) {

    // setHandlingAssistance
    @Test
    fun `set handling assistance to null`() {
    }
    @Test
    fun `set handling assistance to full`() {
    }
    @Test
    fun `set handling assistance to warning`() {
    }
    @Test
    fun `set handling assistance to none`() {
    }

    // getHandlingAssistanceState
    @Test
    fun `get null handling assistance`() {
    }
    @Test
    fun `get full handling assistance`() {
    }
    @Test
    fun `get warning handling assistance`() {
    }
    @Test
    fun `get none handling assistance`() {
    }

    // setMotorSpeedLimiter
    @Test
    fun `set motor speed limiter to char`() {
    }
    @Test
    fun `set motor speed limiter to negative int`() {
    }
    @Test
    fun `set motor speed limiter to double`() {
    }

    // getMotorSpeedLimiter
    @Test
    fun `get motor speed limiter from char`() {
    }
    @Test
    fun `get motor speed limiter from negative int`() {
    }
    @Test
    fun `get motor speed limiter from double`() {
    }

    // setFrontDifferentialSlipperyLimiter
    @Test
    fun `set front differential slippery limiter to char`() {
    }
    @Test
    fun `set front differential slippery limiter to double`() {
    }
    @Test
    fun `set front differential slippery limiter to predefined int`() {
    }
    @Test
    fun `set front differential slippery limiter to any int`() {
    }

    // getFrontDifferentialSlipperyLimiter
    @Test
    fun `get front differential slippery limiter from char`() {
    }
    @Test
    fun `get front differential slippery limiter from double`() {
    }
    @Test
    fun `get front differential slippery limiter from predefined int`() {
    }
    @Test
    fun `get front differential slippery limiter from any int`() {
    }

    // setRearDifferentialSlipperyLimiter
    @Test
    fun `set rear differential slippery limiter to char`() {
    }
    @Test
    fun `set rear differential slippery limiter to double`() {
    }
    @Test
    fun `set rear differential slippery limiter to predefined int`() {
    }
    @Test
    fun `set rear differential slippery limiter to any int`() {
    }

    // getRearDifferentialSlipperyLimiter
    @Test
    fun `get rear differential slippery limiter from char`() {
    }
    @Test
    fun `get rear differential slippery limiter from double`() {
    }
    @Test
    fun `get rear differential slippery limiter from predefined int`() {
    }
    @Test
    fun `get rear differential slippery limiter from any int`() {
    }
}