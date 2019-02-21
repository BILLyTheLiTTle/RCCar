package car.cockpit.setup

import car.categories.IntegrationTest
import car.cockpit.engine.SUCCESS
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
@IntegrationTest
internal class SetupControllerIntegrationTest(
    @Autowired val restTemplate: TestRestTemplate,
    @Autowired val setupComponent: Setup
) {

    // setHandlingAssistance
    @Test
    fun `set handling assistance to nothing`() {
        val entity = restTemplate.getForEntity<String>("/set_handling_assistance")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body).isEqualTo(SUCCESS)
    }
    @Test
    fun `set handling assistance to null`() {
        val entity = restTemplate.getForEntity<String>("/set_handling_assistance?state=NULL")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body).isEqualTo(SUCCESS)
    }
    @Test
    fun `set handling assistance to full`() {
        val entity = restTemplate.getForEntity<String>("/set_handling_assistance?state=assistance_full")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body).isEqualTo(SUCCESS)
    }
    @Test
    fun `set handling assistance to warning`() {
        val entity = restTemplate.getForEntity<String>("/set_handling_assistance?state=assistance_warning")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body).isEqualTo(SUCCESS)
    }
    @Test
    fun `set handling assistance to none`() {
        val entity = restTemplate.getForEntity<String>("/set_handling_assistance?state=assistance_none")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body).isEqualTo(SUCCESS)
    }

    // getHandlingAssistanceState
    @Test
    fun `get handling assistance from nothing`() {
        restTemplate.getForEntity<String>("/set_handling_assistance")
        val entity = restTemplate.getForEntity<String>("/get_handling_assistance_state")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body).isEqualTo(setupComponent.handlingAssistanceState).isEqualTo(ASSISTANCE_NULL)
    }
    @Test
    fun `get handling assistance from null`() {
        restTemplate.getForEntity<String>("/set_handling_assistance?state=NULL")
        val entity = restTemplate.getForEntity<String>("/get_handling_assistance_state")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body).isEqualTo(setupComponent.handlingAssistanceState).isEqualTo(ASSISTANCE_NULL)
    }
    @Test
    fun `get handling assistance from full`() {
        restTemplate.getForEntity<String>("/set_handling_assistance?state=assistance_full")
        val entity = restTemplate.getForEntity<String>("/get_handling_assistance_state")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body).isEqualTo(setupComponent.handlingAssistanceState).isEqualTo(ASSISTANCE_FULL)
    }
    @Test
    fun `get handling assistance from warning`() {
        restTemplate.getForEntity<String>("/set_handling_assistance?state=assistance_warning")
        val entity = restTemplate.getForEntity<String>("/get_handling_assistance_state")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body).isEqualTo(setupComponent.handlingAssistanceState).isEqualTo(ASSISTANCE_WARNING)
    }
    @Test
    fun `get handling assistance from none`() {
        restTemplate.getForEntity<String>("/set_handling_assistance?state=assistance_none")
        val entity = restTemplate.getForEntity<String>("/get_handling_assistance_state")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body).isEqualTo(setupComponent.handlingAssistanceState).isEqualTo(ASSISTANCE_NONE)
    }

    // setMotorSpeedLimiter
    @Test
    fun `set motor speed limiter to char`() {
        val entity = restTemplate.getForEntity<String>("/set_motor_speed_limiter?value=a")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
    }
    @Test
    fun `set motor speed limiter to negative int`() {
        val entity = restTemplate.getForEntity<String>("/set_motor_speed_limiter?value=-16")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body).isEqualTo(SUCCESS)
    }
    @Test
    fun `set motor speed limiter to double`() {
        val entity = restTemplate.getForEntity<String>("/set_motor_speed_limiter?value=16.9")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body).isEqualTo(SUCCESS)
    }

    // getMotorSpeedLimiter
    @Test
    fun `get motor speed limiter from char`() {
        restTemplate.getForEntity<String>("/set_motor_speed_limiter?value=a")
        val entity = restTemplate.getForEntity<String>("/get_motor_speed_limiter")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body?.toDouble()).isEqualTo(setupComponent.motorSpeedLimiter)
    }
    @Test
    fun `get motor speed limiter from negative int`() {
        restTemplate.getForEntity<String>("/set_motor_speed_limiter?value=-16")
        val entity = restTemplate.getForEntity<String>("/get_motor_speed_limiter")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body?.toDouble()).isEqualTo(setupComponent.motorSpeedLimiter).isEqualTo((-16).toDouble())
    }
    @Test
    fun `get motor speed limiter from double`() {
        restTemplate.getForEntity<String>("/set_motor_speed_limiter?value=16.9")
        val entity = restTemplate.getForEntity<String>("/get_motor_speed_limiter")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body?.toDouble()).isEqualTo(setupComponent.motorSpeedLimiter).isEqualTo(16.9)
    }

    // setFrontDifferentialSlipperyLimiter
    @Test
    fun `set front differential slippery limiter to char`() {
        val entity = restTemplate.getForEntity<String>("/set_front_differential_slippery_limiter?value=a")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
    }
    @Test
    fun `set front differential slippery limiter to double`() {
        val entity = restTemplate.getForEntity<String>("/set_front_differential_slippery_limiter?value=16.9")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
    }
    @Test
    fun `set front differential slippery limiter to predefined int`() {
        val entity = restTemplate.getForEntity<String>("/set_front_differential_slippery_limiter?value=2")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body).isEqualTo(SUCCESS)
    }
    @Test
    fun `set front differential slippery limiter to any int`() {
        val entity = restTemplate.getForEntity<String>("/set_front_differential_slippery_limiter?value=5")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body).isEqualTo(SUCCESS)
    }

    // getFrontDifferentialSlipperyLimiter
    @Test
    fun `get front differential slippery limiter from char`() {
        restTemplate.getForEntity<String>("/set_front_differential_slippery_limiter?value=a")
        val entity = restTemplate.getForEntity<String>("/get_front_differential_slippery_limiter")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body?.toInt()).isEqualTo(setupComponent.frontDifferentialSlipperyLimiter)
    }
    @Test
    fun `get front differential slippery limiter from double`() {
        restTemplate.getForEntity<String>("/set_front_differential_slippery_limiter?value=16.9")
        val entity = restTemplate.getForEntity<String>("/get_front_differential_slippery_limiter")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body?.toInt()).isEqualTo(setupComponent.frontDifferentialSlipperyLimiter)
    }
    @Test
    fun `get front differential slippery limiter from predefined int`() {
        restTemplate.getForEntity<String>("/set_front_differential_slippery_limiter?value=2")
        val entity = restTemplate.getForEntity<String>("/get_front_differential_slippery_limiter")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body?.toInt()).isEqualTo(setupComponent.frontDifferentialSlipperyLimiter)
            .isEqualTo(DIFFERENTIAL_SLIPPERY_LIMITER_MEDI_1)
    }
    @Test
    fun `get front differential slippery limiter from any int`() {
        restTemplate.getForEntity<String>("/set_front_differential_slippery_limiter?value=5")
        val entity = restTemplate.getForEntity<String>("/get_front_differential_slippery_limiter")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body?.toInt()).isEqualTo(setupComponent.frontDifferentialSlipperyLimiter).isEqualTo(5)
    }

    // setRearDifferentialSlipperyLimiter
    @Test
    fun `set rear differential slippery limiter to char`() {
        val entity = restTemplate.getForEntity<String>("/set_rear_differential_slippery_limiter?value=c")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
    }
    @Test
    fun `set rear differential slippery limiter to double`() {
        val entity = restTemplate.getForEntity<String>("/set_rear_differential_slippery_limiter?value=14.8")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
    }
    @Test
    fun `set rear differential slippery limiter to predefined int`() {
        val entity = restTemplate.getForEntity<String>("/set_rear_differential_slippery_limiter?value=3")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body).isEqualTo(SUCCESS)
    }
    @Test
    fun `set rear differential slippery limiter to any int`() {
        val entity = restTemplate.getForEntity<String>("/set_rear_differential_slippery_limiter?value=6")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body).isEqualTo(SUCCESS)
    }

    // getRearDifferentialSlipperyLimiter
    @Test
    fun `get rear differential slippery limiter from char`() {
        restTemplate.getForEntity<String>("/set_rear_differential_slippery_limiter?value=b")
        val entity = restTemplate.getForEntity<String>("/get_rear_differential_slippery_limiter")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body?.toInt()).isEqualTo(setupComponent.rearDifferentialSlipperyLimiter)
    }
    @Test
    fun `get rear differential slippery limiter from double`() {
        restTemplate.getForEntity<String>("/set_rear_differential_slippery_limiter?value=14.8")
        val entity = restTemplate.getForEntity<String>("/get_rear_differential_slippery_limiter")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body?.toInt()).isEqualTo(setupComponent.rearDifferentialSlipperyLimiter)
    }
    @Test
    fun `get rear differential slippery limiter from predefined int`() {
        restTemplate.getForEntity<String>("/set_rear_differential_slippery_limiter?value=3")
        val entity = restTemplate.getForEntity<String>("/get_rear_differential_slippery_limiter")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body?.toInt()).isEqualTo(setupComponent.rearDifferentialSlipperyLimiter)
            .isEqualTo(DIFFERENTIAL_SLIPPERY_LIMITER_MEDI_2)
    }
    @Test
    fun `get rear differential slippery limiter from any int`() {
        restTemplate.getForEntity<String>("/set_rear_differential_slippery_limiter?value=6")
        val entity = restTemplate.getForEntity<String>("/get_rear_differential_slippery_limiter")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body?.toInt()).isEqualTo(setupComponent.rearDifferentialSlipperyLimiter).isEqualTo(6)
    }
}