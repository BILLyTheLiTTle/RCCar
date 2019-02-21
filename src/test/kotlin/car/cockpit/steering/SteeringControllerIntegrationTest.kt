package car.cockpit.steering

import car.categories.IntegrationTest
import car.cockpit.engine.SUCCESS
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
@IntegrationTest
internal class SteeringControllerTest(
    @Autowired val restTemplate: TestRestTemplate,
    @Autowired val steeringComponent: Steering
) {

    @BeforeEach
    fun setup(){
        id++
    }

    // setSteeringAction
    @Test
    fun `turn steering to nothing is successful`() {
        val entity = restTemplate.getForEntity<String>("/set_steering_system?id=$id")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body).isEqualTo(SUCCESS)
    }
    @Test
    fun `turn steering is successful no matter handling assistance`() {
        restTemplate.getForEntity<String>("/set_handling_assistance?state=assistance_none")
        val entity = restTemplate.getForEntity<String>("/set_steering_system?id=$id")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body).isEqualTo(SUCCESS)

        id++
        restTemplate.getForEntity<String>("/set_handling_assistance?state=assistance_warning")
        val entity1 = restTemplate.getForEntity<String>("/set_steering_system?id=$id")
        assertThat(entity1.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity1.body).isEqualTo(SUCCESS)

        id++
        restTemplate.getForEntity<String>("/set_handling_assistance?state=assistance_full")
        val entity2 = restTemplate.getForEntity<String>("/set_steering_system?id=$id")
        assertThat(entity2.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity2.body).isEqualTo(SUCCESS)

        id++
        restTemplate.getForEntity<String>("/set_handling_assistance?state=NULL")
        val entity3 = restTemplate.getForEntity<String>("/set_steering_system?id=$id")
        assertThat(entity3.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity3.body).isEqualTo(SUCCESS)
    }
    @Test
    fun `turn steering with wrong id is not successful`() {
        val entity = restTemplate.getForEntity<String>("/set_steering_system?id=${id -1}")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body).contains("Wrong Request ID")
    }
    @Test
    fun `turn steering to unknown direction should be an error`() {
        val entity = restTemplate.getForEntity<String>("/set_steering_system?id=$id&direction=oops")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body).contains("ERROR: Entered in else condition")
    }

    // getSteeringDirection
    @Test
    fun `steering direction when no steering action happened is straight`() {
        restTemplate.getForEntity<String>("/set_steering_system?id=$id")
        val entity = restTemplate.getForEntity<String>("/get_steering_direction")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body).isEqualTo(steeringComponent.direction).isEqualTo(ACTION_STRAIGHT)
    }
    @Test
    fun `steering direction should be left`() {
        restTemplate.getForEntity<String>("/set_steering_system?id=$id&direction=left")
        val entity = restTemplate.getForEntity<String>("/get_steering_direction")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body).isEqualTo(steeringComponent.direction).isEqualTo(ACTION_TURN_LEFT)
    }
    @Test
    fun `steering direction should be right`() {
        restTemplate.getForEntity<String>("/set_steering_system?id=$id&direction=right")
        val entity = restTemplate.getForEntity<String>("/get_steering_direction")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body).isEqualTo(steeringComponent.direction).isEqualTo(ACTION_TURN_RIGHT)
    }
}

private var id = -1