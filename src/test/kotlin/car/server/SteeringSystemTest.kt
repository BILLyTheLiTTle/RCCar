package car.server

import car.controllers.basic.SteeringImpl
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
internal class SteeringSystemTest(@Autowired val restTemplate: TestRestTemplate) {


    // setSteeringAction
    @Test
    fun `turn steering to nothing is successful`() {
        val entity = restTemplate.getForEntity<String>("/set_steering_system")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body).isEqualTo(EngineSystem.SUCCESS)
    }
    @Test
    fun `turn steering to unknown direction should be an error`() {
        val entity = restTemplate.getForEntity<String>("/set_steering_system?direction=oops")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body).contains("ERROR: Entered in else condition")
    }

    // getSteeringDirection
    @Test
    fun `steering direction when no steering action happened is straight`() {
        restTemplate.getForEntity<String>("/set_steering_system")
        val entity = restTemplate.getForEntity<String>("/get_steering_direction")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body).isEqualTo(SteeringImpl.direction).isEqualTo(SteeringSystem.ACTION_STRAIGHT)
    }
    @Test
    fun `steering direction should be left`() {
        restTemplate.getForEntity<String>("/set_steering_system?direction=left")
        val entity = restTemplate.getForEntity<String>("/get_steering_direction")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body).isEqualTo(SteeringImpl.direction).isEqualTo(SteeringSystem.ACTION_TURN_LEFT)
    }
    @Test
    fun `steering direction should be right`() {
        restTemplate.getForEntity<String>("/set_steering_system?direction=right")
        val entity = restTemplate.getForEntity<String>("/get_steering_direction")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body).isEqualTo(SteeringImpl.direction).isEqualTo(SteeringSystem.ACTION_TURN_RIGHT)
    }
}