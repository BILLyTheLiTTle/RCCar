package car.cockpit.setup

import car.INTEGRATION_TEST
import car.cockpit.engine.SUCCESS
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Tag
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
@Tag(INTEGRATION_TEST)
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
        val entity = restTemplate.getForEntity<String>("/set_handling_assistance?state=FULL")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body).isEqualTo(SUCCESS)
    }
    @Test
    fun `set handling assistance to warning`() {
        val entity = restTemplate.getForEntity<String>("/set_handling_assistance?state=WARNING")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body).isEqualTo(SUCCESS)
    }
    @Test
    fun `set handling assistance to manual`() {
        val entity = restTemplate.getForEntity<String>("/set_handling_assistance?state=MANUAL")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body).isEqualTo(SUCCESS)
    }

    // getHandlingAssistanceState
    @Test
    fun `get handling assistance from nothing`() {
        restTemplate.getForEntity<String>("/set_handling_assistance")
        val entity = restTemplate.getForEntity<String>("/get_handling_assistance_state")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body).isEqualTo(setupComponent.handlingAssistanceState.name)
            .isEqualTo(HandlingAssistance.NULL.name)
    }
    @Test
    fun `get handling assistance from null`() {
        restTemplate.getForEntity<String>("/set_handling_assistance?state=NULL")
        val entity = restTemplate.getForEntity<String>("/get_handling_assistance_state")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body).isEqualTo(setupComponent.handlingAssistanceState.name)
            .isEqualTo(HandlingAssistance.NULL.name)
    }
    @Test
    fun `get handling assistance from full`() {
        restTemplate.getForEntity<String>("/set_handling_assistance?state=FULL")
        val entity = restTemplate.getForEntity<String>("/get_handling_assistance_state")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body).isEqualTo(setupComponent.handlingAssistanceState.name)
            .isEqualTo(HandlingAssistance.FULL.name)
    }
    @Test
    fun `get handling assistance from warning`() {
        restTemplate.getForEntity<String>("/set_handling_assistance?state=WARNING")
        val entity = restTemplate.getForEntity<String>("/get_handling_assistance_state")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body).isEqualTo(setupComponent.handlingAssistanceState.name)
            .isEqualTo(HandlingAssistance.WARNING.name)
    }
    @Test
    fun `get handling assistance from manual`() {
        restTemplate.getForEntity<String>("/set_handling_assistance?state=MANUAL")
        val entity = restTemplate.getForEntity<String>("/get_handling_assistance_state")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body).isEqualTo(setupComponent.handlingAssistanceState.name)
            .isEqualTo(HandlingAssistance.MANUAL.name)
    }

    // setMotorSpeedLimiter
    @Test
    fun `set motor speed limiter to char`() {
        val entity = restTemplate.getForEntity<String>("/set_motor_speed_limiter?value=a")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
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
        assertThat(entity.body).isEqualTo(setupComponent.motorSpeedLimiter.name)
            .isEqualTo(MotorSpeedLimiter.ERROR_SPEED.name)
    }
    @Test
    fun `get motor speed limiter from negative int`() {
        restTemplate.getForEntity<String>("/set_motor_speed_limiter?value=-16")
        val entity = restTemplate.getForEntity<String>("/get_motor_speed_limiter")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body).isEqualTo(setupComponent.motorSpeedLimiter.name)
            .isEqualTo(MotorSpeedLimiter.ERROR_SPEED.name)
    }
    @Test
    fun `get motor speed limiter from double`() {
        restTemplate.getForEntity<String>("/set_motor_speed_limiter?value=16.9")
        val entity = restTemplate.getForEntity<String>("/get_motor_speed_limiter")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body).isEqualTo(setupComponent.motorSpeedLimiter.name)
            .isEqualTo(MotorSpeedLimiter.ERROR_SPEED.name)
    }

    // setFrontDifferentialSlipperyLimiter
    @Test
    fun `set front differential slippery limiter to char`() {
        val entity = restTemplate.getForEntity<String>("/set_front_differential_slippery_limiter?value=a")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
    }
    @Test
    fun `set front differential slippery limiter to double`() {
        val entity = restTemplate.getForEntity<String>("/set_front_differential_slippery_limiter?value=16.9")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
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
        assertThat(entity.body).isEqualTo(setupComponent.frontDifferentialSlipperyLimiter.name)
            .isEqualTo(DifferentialSlipperyLimiter.ERROR.name)
    }
    @Test
    fun `get front differential slippery limiter from double`() {
        restTemplate.getForEntity<String>("/set_front_differential_slippery_limiter?value=16.9")
        val entity = restTemplate.getForEntity<String>("/get_front_differential_slippery_limiter")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body).isEqualTo(setupComponent.frontDifferentialSlipperyLimiter.name)
            .isEqualTo(DifferentialSlipperyLimiter.ERROR.name)
    }
    @Test
    fun `get front differential slippery limiter from predefined int`() {
        restTemplate.getForEntity<String>("/set_front_differential_slippery_limiter?value=MEDI_1")
        val entity = restTemplate.getForEntity<String>("/get_front_differential_slippery_limiter")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body).isEqualTo(setupComponent.frontDifferentialSlipperyLimiter.name)
            .isEqualTo(DifferentialSlipperyLimiter.MEDI_1.name)
    }
    @Test
    fun `get front differential slippery limiter from any int`() {
        restTemplate.getForEntity<String>("/set_front_differential_slippery_limiter?value=5")
        val entity = restTemplate.getForEntity<String>("/get_front_differential_slippery_limiter")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body).isEqualTo(setupComponent.frontDifferentialSlipperyLimiter.name)
            .isEqualTo(DifferentialSlipperyLimiter.ERROR.name)
    }

    // setRearDifferentialSlipperyLimiter
    @Test
    fun `set rear differential slippery limiter to char`() {
        val entity = restTemplate.getForEntity<String>("/set_rear_differential_slippery_limiter?value=c")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
    }
    @Test
    fun `set rear differential slippery limiter to double`() {
        val entity = restTemplate.getForEntity<String>("/set_rear_differential_slippery_limiter?value=14.8")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
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
        assertThat(entity.body).isEqualTo(setupComponent.rearDifferentialSlipperyLimiter.name)
            .isEqualTo(DifferentialSlipperyLimiter.ERROR.name)
    }
    @Test
    fun `get rear differential slippery limiter from double`() {
        restTemplate.getForEntity<String>("/set_rear_differential_slippery_limiter?value=14.8")
        val entity = restTemplate.getForEntity<String>("/get_rear_differential_slippery_limiter")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body).isEqualTo(setupComponent.rearDifferentialSlipperyLimiter.name)
            .isEqualTo(DifferentialSlipperyLimiter.ERROR.name)
    }
    @Test
    fun `get rear differential slippery limiter from predefined value`() {
        restTemplate.getForEntity<String>("/set_rear_differential_slippery_limiter?value=MEDI_2")
        val entity = restTemplate.getForEntity<String>("/get_rear_differential_slippery_limiter")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body).isEqualTo(setupComponent.rearDifferentialSlipperyLimiter.name)
            .isEqualTo(DifferentialSlipperyLimiter.MEDI_2.name)
    }
    @Test
    fun `get rear differential slippery limiter from int`() {
        restTemplate.getForEntity<String>("/set_rear_differential_slippery_limiter?value=6")
        val entity = restTemplate.getForEntity<String>("/get_rear_differential_slippery_limiter")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body).isEqualTo(setupComponent.rearDifferentialSlipperyLimiter.name)
            .isEqualTo(DifferentialSlipperyLimiter.ERROR.name)
    }
}