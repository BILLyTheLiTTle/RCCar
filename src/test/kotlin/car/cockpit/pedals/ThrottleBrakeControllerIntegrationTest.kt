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
        val entity = restTemplate.getForEntity<String>("/set_throttle_brake_system?id=$id&action=FORWARD")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body).isEqualTo(SUCCESS)
    }
    @Test
    fun `apply moving backward`() {
        val entity = restTemplate.getForEntity<String>("/set_throttle_brake_system?id=$id&action=BACKWARD")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body).isEqualTo(SUCCESS)
    }
    @Test
    fun `apply parking brake`() {
        val entity = restTemplate.getForEntity<String>("/set_throttle_brake_system?id=$id&action=PARKING_BRAKE")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body).isEqualTo(SUCCESS)
    }
    @Test
    fun `apply handbrake`() {
        val entity = restTemplate.getForEntity<String>("/set_throttle_brake_system?id=$id&action=HANDBRAKE")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body).isEqualTo(SUCCESS)
    }
    @Test
    fun `apply braking still`() {
        val entity = restTemplate.getForEntity<String>("/set_throttle_brake_system?id=$id&action=BRAKING_STILL")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body).isEqualTo(SUCCESS)
    }
    @Test
    fun `apply neutral`() {
        val entity = restTemplate.getForEntity<String>("/set_throttle_brake_system?id=$id&action=NEUTRAL")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body).isEqualTo(SUCCESS)
    }
    @Test
    fun `apply throttle is successful no matter handling assistance`() {
        restTemplate.getForEntity<String>("/set_handling_assistance?state=MANUAL")
        val entity = restTemplate.getForEntity<String>("/set_throttle_brake_system?id=$id&action=FORWARD")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body).isEqualTo(SUCCESS)

        id++
        restTemplate.getForEntity<String>("/set_handling_assistance?state=WARNING")
        val entity1 = restTemplate.getForEntity<String>("/set_throttle_brake_system?id=$id&action=FORWARD")
        assertThat(entity1.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity1.body).isEqualTo(SUCCESS)

        id++
        restTemplate.getForEntity<String>("/set_handling_assistance?state=FULL")
        val entity2 = restTemplate.getForEntity<String>("/set_throttle_brake_system?id=$id&action=FORWARD")
        assertThat(entity2.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity2.body).isEqualTo(SUCCESS)

        id++
        restTemplate.getForEntity<String>("/set_handling_assistance?state=NULL")
        val entity3 = restTemplate.getForEntity<String>("/set_throttle_brake_system?id=$id&action=FORWARD")
        assertThat(entity3.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity3.body).isEqualTo(SUCCESS)

        id++
        restTemplate.getForEntity<String>("/set_handling_assistance?state=MANUAL")
        val entity4 = restTemplate.getForEntity<String>("/set_throttle_brake_system?id=$id&action=NEUTRAL")
        assertThat(entity4.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity4.body).isEqualTo(SUCCESS)

        id++
        restTemplate.getForEntity<String>("/set_handling_assistance?state=WARNING")
        val entity5 = restTemplate.getForEntity<String>("/set_throttle_brake_system?id=$id&action=NEUTRAL")
        assertThat(entity5.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity5.body).isEqualTo(SUCCESS)

        id++
        restTemplate.getForEntity<String>("/set_handling_assistance?state=FULL")
        val entity6 = restTemplate.getForEntity<String>("/set_throttle_brake_system?id=$id&action=NEUTRAL")
        assertThat(entity6.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity6.body).isEqualTo(SUCCESS)

        id++
        restTemplate.getForEntity<String>("/set_handling_assistance?state=NULL")
        val entity7 = restTemplate.getForEntity<String>("/set_throttle_brake_system?id=$id&action=NEUTRAL")
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
        restTemplate.getForEntity<String>("/set_throttle_brake_system?id=$id&action=PARKING_BRAKE&value=100")
        val entity = restTemplate.getForEntity<String>("/get_parking_brake_state")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body?.toBoolean()).isEqualTo(throttleBrake.parkingBrakeState).isTrue()
    }
    @Test
    fun `parking brake should be off`() {
        restTemplate.getForEntity<String>("/set_throttle_brake_system?id=$id&action=PARKING_BRAKE&value=0")
        val entity = restTemplate.getForEntity<String>("/get_parking_brake_state")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body?.toBoolean()).isEqualTo(throttleBrake.parkingBrakeState).isFalse()
    }
    @Test
    fun `no value means parking brake should be off`() {
        restTemplate.getForEntity<String>("/set_throttle_brake_system?id=$id&action=PARKING_BRAKE")
        val entity = restTemplate.getForEntity<String>("/get_parking_brake_state")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body?.toBoolean()).isEqualTo(throttleBrake.parkingBrakeState).isFalse()
    }

    // getHandbrakeState
    @Test
    fun `handbrake should be on`() {
        restTemplate.getForEntity<String>("/set_throttle_brake_system?id=$id&action=HANDBRAKE&value=100")
        val entity = restTemplate.getForEntity<String>("/get_handbrake_state")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body?.toBoolean()).isEqualTo(throttleBrake.handbrakeState).isTrue()
    }
    @Test
    fun `handbrake should be off`() {
        restTemplate.getForEntity<String>("/set_throttle_brake_system?id=$id&action=HANDBRAKE&value=0")
        val entity = restTemplate.getForEntity<String>("/get_handbrake_state")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body?.toBoolean()).isEqualTo(throttleBrake.handbrakeState).isFalse()
    }
    @Test
    fun `no value means handbrake should be off`() {
        restTemplate.getForEntity<String>("/set_throttle_brake_system?id=$id&action=HANDBRAKE")
        val entity = restTemplate.getForEntity<String>("/get_handbrake_state")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body?.toBoolean()).isEqualTo(throttleBrake.handbrakeState).isFalse()
    }

    // getMotionState
    @Test
    fun `should be moving forward`() {
        restTemplate.getForEntity<String>("/set_handling_assistance?state=MANUAL")
        restTemplate.getForEntity<String>("/set_throttle_brake_system?id=$id&action=FORWARD")
        val entity = restTemplate.getForEntity<String>("/get_motion_state")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body).isEqualTo(Motion.FORWARD.name)

        id++
        restTemplate.getForEntity<String>("/set_handling_assistance?state=WARNING")
        restTemplate.getForEntity<String>("/set_throttle_brake_system?id=$id&action=FORWARD")
        val entity1 = restTemplate.getForEntity<String>("/get_motion_state")
        assertThat(entity1.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity1.body).isEqualTo(Motion.FORWARD.name)

        id++
        restTemplate.getForEntity<String>("/set_handling_assistance?state=FULL")
        restTemplate.getForEntity<String>("/set_throttle_brake_system?id=$id&action=FORWARD")
        val entity2 = restTemplate.getForEntity<String>("/get_motion_state")
        assertThat(entity2.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity2.body).isEqualTo(Motion.FORWARD.name)
    }
    @Test
    fun `should be moving backward`() {
        restTemplate.getForEntity<String>("/set_handling_assistance?state=MANUAL")
        restTemplate.getForEntity<String>("/set_throttle_brake_system?id=$id&action=BACKWARD")
        val entity = restTemplate.getForEntity<String>("/get_motion_state")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body).isEqualTo(Motion.BACKWARD.name)

        id++
        restTemplate.getForEntity<String>("/set_handling_assistance?state=WARNING")
        restTemplate.getForEntity<String>("/set_throttle_brake_system?id=$id&action=BACKWARD")
        val entity1 = restTemplate.getForEntity<String>("/get_motion_state")
        assertThat(entity1.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity1.body).isEqualTo(Motion.BACKWARD.name)

        id++
        restTemplate.getForEntity<String>("/set_handling_assistance?state=FULL")
        restTemplate.getForEntity<String>("/set_throttle_brake_system?id=$id&action=BACKWARD")
        val entity2 = restTemplate.getForEntity<String>("/get_motion_state")
        assertThat(entity2.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity2.body).isEqualTo(Motion.BACKWARD.name)
    }
    @Test
    fun `should be parking brake with null handling assistance`(){
        restTemplate.getForEntity<String>("/set_handling_assistance?state=NULL")
        restTemplate.getForEntity<String>("/set_throttle_brake_system?id=$id&action=FORWARD")
        val entity = restTemplate.getForEntity<String>("/get_motion_state")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body).isEqualTo(Motion.PARKING_BRAKE.name)

        id++
        restTemplate.getForEntity<String>("/set_handling_assistance?state=NULL")
        restTemplate.getForEntity<String>("/set_throttle_brake_system?id=$id&action=BACKWARD")
        val entity2 = restTemplate.getForEntity<String>("/get_motion_state")
        assertThat(entity2.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity2.body).isEqualTo(Motion.PARKING_BRAKE.name)
    }
    @Test
    fun `should be neutral`() {
        restTemplate.getForEntity<String>("/set_throttle_brake_system?id=$id&action=NEUTRAL")
        val entity = restTemplate.getForEntity<String>("/get_motion_state")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body).isEqualTo(Motion.NEUTRAL.name)
    }
    @Test
    fun `should be braking still`() {
        restTemplate.getForEntity<String>("/set_throttle_brake_system?id=$id&action=BRAKING_STILL")
        val entity = restTemplate.getForEntity<String>("/get_motion_state")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body).isEqualTo(Motion.BRAKING_STILL.name)
    }
    @Test
    fun `should be empty`() {
        restTemplate.getForEntity<String>("/set_throttle_brake_system?id=$id&action=oops")
        val entity = restTemplate.getForEntity<String>("/get_motion_state")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body).isEqualTo(Motion.NEUTRAL.name)
    }
}

private var id = -1