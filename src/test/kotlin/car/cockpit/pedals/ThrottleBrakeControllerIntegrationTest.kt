package car.cockpit.pedals

import car.INTEGRATION_TEST
import car.cockpit.engine.SUCCESS
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.getForEntity
import org.springframework.http.HttpStatus
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Tag(INTEGRATION_TEST)
internal class ThrottleBrakeControllerTest(
    @Autowired val restTemplate: TestRestTemplate,
    @Autowired
    @Qualifier("Throttle -n- Brake Component")
    val throttleBrake: ThrottleBrake
) {

    @BeforeEach
    fun setup(){
        id++
    }
    // setThrottleBrakeAction
    @Test
    fun `apply moving forward`() {
        val entity = restTemplate.getForEntity<String>("/set_throttle_brake_system?id=$id&action=forward")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body).isEqualTo(SUCCESS)
    }
    @Test
    fun `apply moving backward`() {
        val entity = restTemplate.getForEntity<String>("/set_throttle_brake_system?id=$id&action=backward")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body).isEqualTo(SUCCESS)
    }
    @Test
    fun `apply parking brake`() {
        val entity = restTemplate.getForEntity<String>("/set_throttle_brake_system?id=$id&action=parking_brake")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body).isEqualTo(SUCCESS)
    }
    @Test
    fun `apply handbrake`() {
        val entity = restTemplate.getForEntity<String>("/set_throttle_brake_system?id=$id&action=handbrake")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body).isEqualTo(SUCCESS)
    }
    @Test
    fun `apply braking still`() {
        val entity = restTemplate.getForEntity<String>("/set_throttle_brake_system?id=$id&action=braking_still")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body).isEqualTo(SUCCESS)
    }
    @Test
    fun `apply neutral`() {
        val entity = restTemplate.getForEntity<String>("/set_throttle_brake_system?id=$id&action=neutral")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body).isEqualTo(SUCCESS)
    }
    @Test
    fun `apply throttle is successful no matter handling assistance`() {
        restTemplate.getForEntity<String>("/set_handling_assistance?state=assistance_none")
        val entity = restTemplate.getForEntity<String>("/set_throttle_brake_system?id=$id&action=forward")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body).isEqualTo(SUCCESS)

        id++
        restTemplate.getForEntity<String>("/set_handling_assistance?state=assistance_warning")
        val entity1 = restTemplate.getForEntity<String>("/set_throttle_brake_system?id=$id&action=forward")
        assertThat(entity1.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity1.body).isEqualTo(SUCCESS)

        id++
        restTemplate.getForEntity<String>("/set_handling_assistance?state=assistance_full")
        val entity2 = restTemplate.getForEntity<String>("/set_throttle_brake_system?id=$id&action=forward")
        assertThat(entity2.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity2.body).isEqualTo(SUCCESS)

        id++
        restTemplate.getForEntity<String>("/set_handling_assistance?state=NULL")
        val entity3 = restTemplate.getForEntity<String>("/set_throttle_brake_system?id=$id&action=forward")
        assertThat(entity3.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity3.body).isEqualTo(SUCCESS)

        id++
        restTemplate.getForEntity<String>("/set_handling_assistance?state=assistance_none")
        val entity4 = restTemplate.getForEntity<String>("/set_throttle_brake_system?id=$id&action=neutral")
        assertThat(entity4.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity4.body).isEqualTo(SUCCESS)

        id++
        restTemplate.getForEntity<String>("/set_handling_assistance?state=assistance_warning")
        val entity5 = restTemplate.getForEntity<String>("/set_throttle_brake_system?id=$id&action=neutral")
        assertThat(entity5.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity5.body).isEqualTo(SUCCESS)

        id++
        restTemplate.getForEntity<String>("/set_handling_assistance?state=assistance_full")
        val entity6 = restTemplate.getForEntity<String>("/set_throttle_brake_system?id=$id&action=neutral")
        assertThat(entity6.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity6.body).isEqualTo(SUCCESS)

        id++
        restTemplate.getForEntity<String>("/set_handling_assistance?state=NULL")
        val entity7 = restTemplate.getForEntity<String>("/set_throttle_brake_system?id=$id&action=neutral")
        assertThat(entity7.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity7.body).isEqualTo(SUCCESS)
    }
    @Test
    fun `apply wrong throttle action successfully`() {
        val entity = restTemplate.getForEntity<String>("/set_throttle_brake_system?id=$id&action=oops")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body).contains("ERROR: Entered in else condition")
    }

    // getParkingBrakeState
    @Test
    fun `parking brake should be on`() {
        restTemplate.getForEntity<String>("/set_throttle_brake_system?id=$id&action=parking_brake&value=100")
        val entity = restTemplate.getForEntity<String>("/get_parking_brake_state")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body?.toBoolean()).isEqualTo(throttleBrake.parkingBrakeState).isTrue()
    }
    @Test
    fun `parking brake should be off`() {
        restTemplate.getForEntity<String>("/set_throttle_brake_system?id=$id&action=parking_brake&value=0")
        val entity = restTemplate.getForEntity<String>("/get_parking_brake_state")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body?.toBoolean()).isEqualTo(throttleBrake.parkingBrakeState).isFalse()
    }
    @Test
    fun `no value means parking brake should be off`() {
        restTemplate.getForEntity<String>("/set_throttle_brake_system?id=$id&action=parking_brake")
        val entity = restTemplate.getForEntity<String>("/get_parking_brake_state")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body?.toBoolean()).isEqualTo(throttleBrake.parkingBrakeState).isFalse()
    }

    // getHandbrakeState
    @Test
    fun `handbrake should be on`() {
        restTemplate.getForEntity<String>("/set_throttle_brake_system?id=$id&action=handbrake&value=100")
        val entity = restTemplate.getForEntity<String>("/get_handbrake_state")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body?.toBoolean()).isEqualTo(throttleBrake.handbrakeState).isTrue()
    }
    @Test
    fun `handbrake should be off`() {
        restTemplate.getForEntity<String>("/set_throttle_brake_system?id=$id&action=handbrake&value=0")
        val entity = restTemplate.getForEntity<String>("/get_handbrake_state")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body?.toBoolean()).isEqualTo(throttleBrake.handbrakeState).isFalse()
    }
    @Test
    fun `no value means handbrake should be off`() {
        restTemplate.getForEntity<String>("/set_throttle_brake_system?id=$id&action=handbrake")
        val entity = restTemplate.getForEntity<String>("/get_handbrake_state")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body?.toBoolean()).isEqualTo(throttleBrake.handbrakeState).isFalse()
    }

    // getMotionState
    @Test
    fun `should be moving forward`() {
        restTemplate.getForEntity<String>("/set_handling_assistance?state=assistance_manual")
        restTemplate.getForEntity<String>("/set_throttle_brake_system?id=$id&action=forward")
        val entity = restTemplate.getForEntity<String>("/get_motion_state")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body).isEqualTo(ACTION_MOVE_FORWARD).isEqualTo("forward")

        id++
        restTemplate.getForEntity<String>("/set_handling_assistance?state=assistance_warning")
        restTemplate.getForEntity<String>("/set_throttle_brake_system?id=$id&action=forward")
        val entity1 = restTemplate.getForEntity<String>("/get_motion_state")
        assertThat(entity1.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity1.body).isEqualTo(ACTION_MOVE_FORWARD).isEqualTo("forward")

        id++
        restTemplate.getForEntity<String>("/set_handling_assistance?state=assistance_full")
        restTemplate.getForEntity<String>("/set_throttle_brake_system?id=$id&action=forward")
        val entity2 = restTemplate.getForEntity<String>("/get_motion_state")
        assertThat(entity2.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity2.body).isEqualTo(ACTION_MOVE_FORWARD).isEqualTo("forward")
    }
    @Test
    fun `should be moving backward`() {
        restTemplate.getForEntity<String>("/set_handling_assistance?state=assistance_manual")
        restTemplate.getForEntity<String>("/set_throttle_brake_system?id=$id&action=backward")
        val entity = restTemplate.getForEntity<String>("/get_motion_state")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body).isEqualTo(ACTION_MOVE_BACKWARD).isEqualTo("backward")

        id++
        restTemplate.getForEntity<String>("/set_handling_assistance?state=assistance_warning")
        restTemplate.getForEntity<String>("/set_throttle_brake_system?id=$id&action=backward")
        val entity1 = restTemplate.getForEntity<String>("/get_motion_state")
        assertThat(entity1.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity1.body).isEqualTo(ACTION_MOVE_BACKWARD).isEqualTo("backward")

        id++
        restTemplate.getForEntity<String>("/set_handling_assistance?state=assistance_full")
        restTemplate.getForEntity<String>("/set_throttle_brake_system?id=$id&action=backward")
        val entity2 = restTemplate.getForEntity<String>("/get_motion_state")
        assertThat(entity2.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity2.body).isEqualTo(ACTION_MOVE_BACKWARD).isEqualTo("backward")
    }
    @Test
    fun `should be parking brake with null handling assistance`(){
        restTemplate.getForEntity<String>("/set_handling_assistance?state=NULL")
        restTemplate.getForEntity<String>("/set_throttle_brake_system?id=$id&action=forward")
        val entity = restTemplate.getForEntity<String>("/get_motion_state")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body).isEqualTo(ACTION_PARKING_BRAKE).isEqualTo("parking_brake")

        id++
        restTemplate.getForEntity<String>("/set_handling_assistance?state=NULL")
        restTemplate.getForEntity<String>("/set_throttle_brake_system?id=$id&action=backward")
        val entity2 = restTemplate.getForEntity<String>("/get_motion_state")
        assertThat(entity2.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity2.body).isEqualTo(ACTION_PARKING_BRAKE).isEqualTo("parking_brake")
    }
    @Test
    fun `should be neutral`() {
        restTemplate.getForEntity<String>("/set_throttle_brake_system?id=$id&action=neutral")
        val entity = restTemplate.getForEntity<String>("/get_motion_state")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body).isEqualTo(ACTION_NEUTRAL).isEqualTo("neutral")
    }
    @Test
    fun `should be braking still`() {
        restTemplate.getForEntity<String>("/set_throttle_brake_system?id=$id&action=braking_still")
        val entity = restTemplate.getForEntity<String>("/get_motion_state")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body).isEqualTo(ACTION_BRAKING_STILL).isEqualTo("braking_still")
    }
    @Test
    fun `should be empty`() {
        restTemplate.getForEntity<String>("/set_throttle_brake_system?id=$id&action=oops")
        val entity = restTemplate.getForEntity<String>("/get_motion_state")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body).isEqualTo(ACTION_NEUTRAL).isEqualTo("neutral")
    }
}

private var id = -1