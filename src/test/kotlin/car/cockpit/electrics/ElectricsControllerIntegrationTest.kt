package car.cockpit.electrics

import car.PARENT_TEST
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
@Tag(PARENT_TEST)
internal class ElectricsControllerIntegrationTest(
    @Autowired val restTemplate: TestRestTemplate,
    @Autowired val electricsComponent: Electrics
) {

    val initialLightsState = true

    // setDirectionLights
    @Test
    fun `turn on left direction light`() {
        if (electricsComponent.leftTurnLightsState){
            restTemplate.getForEntity<String>("/set_direction_lights?direction=LEFT_LIGHTS")
        }
        val entity = restTemplate.getForEntity<String>("/set_direction_lights?direction=LEFT_LIGHTS")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body).isEqualTo(CorneringLight.LEFT_LIGHTS.name)
        assertThat(electricsComponent.leftTurnLightsState).isTrue()
        assertThat(electricsComponent.rightTurnLightsState).isFalse()
    }
    @Test
    fun `turn off left direction light`() {
        if (!electricsComponent.leftTurnLightsState) {
            restTemplate.getForEntity<String>("/set_direction_lights?direction=LEFT_LIGHTS")
        }
        val entity = restTemplate.getForEntity<String>("/set_direction_lights?direction=LEFT_LIGHTS")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body).isEqualTo(CorneringLight.STRAIGHT_LIGHTS.name)
        assertThat(electricsComponent.leftTurnLightsState).isFalse()
        assertThat(electricsComponent.rightTurnLightsState).isFalse()
    }
    @Test
    fun `turn on right direction light`() {
        if (electricsComponent.rightTurnLightsState){
            restTemplate.getForEntity<String>("/set_direction_lights?direction=RIGHT_LIGHTS")
        }
        val entity = restTemplate.getForEntity<String>("/set_direction_lights?direction=RIGHT_LIGHTS")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body).isEqualTo(CorneringLight.RIGHT_LIGHTS.name)
        assertThat(electricsComponent.rightTurnLightsState).isTrue()
        assertThat(electricsComponent.leftTurnLightsState).isFalse()
    }
    @Test
    fun `turn off right direction light`() {
        if (!electricsComponent.rightTurnLightsState) {
            restTemplate.getForEntity<String>("/set_direction_lights?direction=RIGHT_LIGHTS")
        }
        val entity = restTemplate.getForEntity<String>("/set_direction_lights?direction=RIGHT_LIGHTS")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body).isEqualTo(CorneringLight.STRAIGHT_LIGHTS.name)
        assertThat(electricsComponent.rightTurnLightsState).isFalse()
        assertThat(electricsComponent.leftTurnLightsState).isFalse()
    }
    @Test
    fun `turn off both direction lights when straight from left on`() {
        if (!electricsComponent.leftTurnLightsState) {
            restTemplate.getForEntity<String>("/set_direction_lights?direction=RIGHT_LIGHTS")
        }
        val entity = restTemplate.getForEntity<String>("/set_direction_lights?direction=STRAIGHT_LIGHTS")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body).isEqualTo(CorneringLight.STRAIGHT_LIGHTS.name)
        assertThat(electricsComponent.rightTurnLightsState).isFalse()
        assertThat(electricsComponent.leftTurnLightsState).isFalse()
    }
    @Test
    fun `turn off both direction lights when straight from right on`() {
        if (!electricsComponent.rightTurnLightsState) {
            restTemplate.getForEntity<String>("/set_direction_lights?direction=RIGHT_LIGHTS")
        }
        val entity = restTemplate.getForEntity<String>("/set_direction_lights?direction=STRAIGHT_LIGHTS")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body).isEqualTo(CorneringLight.STRAIGHT_LIGHTS.name)
        assertThat(electricsComponent.rightTurnLightsState).isFalse()
        assertThat(electricsComponent.leftTurnLightsState).isFalse()
    }

    // getDirectionLights
    @Test
    fun `get both turn lights when left turn is on`() {
        if (!electricsComponent.leftTurnLightsState) {
            restTemplate.getForEntity<String>("/set_direction_lights?direction=LEFT_LIGHTS")
        }
        val entity = restTemplate.getForEntity<String>("/get_direction_lights")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body).isEqualTo(CorneringLight.LEFT_LIGHTS.name)
        assertThat(electricsComponent.rightTurnLightsState).isFalse()
        assertThat(electricsComponent.leftTurnLightsState).isTrue()
    }
    @Test
    fun `get both turn lights when left turn is off`() {
        if (electricsComponent.leftTurnLightsState) {
            restTemplate.getForEntity<String>("/set_direction_lights?direction=LEFT_LIGHTS")
        }
        val entity = restTemplate.getForEntity<String>("/get_direction_lights")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body).isEqualTo(CorneringLight.STRAIGHT_LIGHTS.name)
        assertThat(electricsComponent.rightTurnLightsState).isFalse()
        assertThat(electricsComponent.leftTurnLightsState).isFalse()
    }
    @Test
    fun `get both turn lights when right turn is on`() {
        if (!electricsComponent.rightTurnLightsState) {
            restTemplate.getForEntity<String>("/set_direction_lights?direction=RIGHT_LIGHTS")
        }
        val entity = restTemplate.getForEntity<String>("/get_direction_lights")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body).isEqualTo(CorneringLight.RIGHT_LIGHTS.name)
        assertThat(electricsComponent.rightTurnLightsState).isTrue()
        assertThat(electricsComponent.leftTurnLightsState).isFalse()
    }
    @Test
    fun `get both turn lights when right turn is off`() {
        if (electricsComponent.rightTurnLightsState) {
            restTemplate.getForEntity<String>("/set_direction_lights?direction=RIGHT_LIGHTS")
        }
        val entity = restTemplate.getForEntity<String>("/get_direction_lights")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body).isEqualTo(CorneringLight.STRAIGHT_LIGHTS.name)
        assertThat(electricsComponent.rightTurnLightsState).isFalse()
        assertThat(electricsComponent.leftTurnLightsState).isFalse()
    }
    @Test
    fun `get both turn lights when straight`() {
        restTemplate.getForEntity<String>("/set_direction_lights?direction=STRAIGHT_LIGHTS")
        val entity = restTemplate.getForEntity<String>("/get_direction_lights")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body).isEqualTo(CorneringLight.STRAIGHT_LIGHTS.name)
        assertThat(electricsComponent.rightTurnLightsState).isFalse()
        assertThat(electricsComponent.leftTurnLightsState).isFalse()
    }

    // setMainLightsState
    @Test
    fun `turn off main lights`() {
        val entity = restTemplate.getForEntity<String>("/set_main_lights_state?value=LIGHTS_OFF")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body).isEqualTo(MainLight.LIGHTS_OFF.name)
        assertThat(electricsComponent.positionLightsState).isFalse()
        assertThat(electricsComponent.drivingLightsState).isFalse()
        assertThat(electricsComponent.longRangeLightsState).isFalse()
    }
    @Test
    fun `turn on position lights`() {
        val entity = restTemplate.getForEntity<String>("/set_main_lights_state?state=POSITION_LIGHTS")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body).isEqualTo(MainLight.POSITION_LIGHTS.name)
        assertThat(electricsComponent.positionLightsState).isTrue()
        assertThat(electricsComponent.drivingLightsState).isFalse()
        assertThat(electricsComponent.longRangeLightsState).isFalse()
    }
    @Test
    fun `turn on driving lights`() {
        `turn on position lights`()

        val entity = restTemplate.getForEntity<String>("/set_main_lights_state?state=DRIVING_LIGHTS")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body).isEqualTo(MainLight.DRIVING_LIGHTS.name)
        assertThat(electricsComponent.positionLightsState).isTrue()
        assertThat(electricsComponent.drivingLightsState).isTrue()
        assertThat(electricsComponent.longRangeLightsState).isFalse()
    }
    @Test
    fun `turn on long range lights`() {
        `turn on driving lights`()

        val entity = restTemplate.getForEntity<String>("/set_main_lights_state?state=LONG_RANGE_LIGHTS")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body).isEqualTo(MainLight.LONG_RANGE_LIGHTS.name)
        assertThat(electricsComponent.positionLightsState).isTrue()
        assertThat(electricsComponent.drivingLightsState).isTrue()
        assertThat(electricsComponent.longRangeLightsState).isTrue()
    }
    @Test
    fun `signal with headlights`() {
        // TODO not sure what to test
    }

    // getMainLightsState
    @Test
    fun `get all lights state when till long range lights are on`() {
        restTemplate.getForEntity<String>("/set_main_lights_state?state=POSITION_LIGHTS")
        restTemplate.getForEntity<String>("/set_main_lights_state?state=DRIVING_LIGHTS")
        restTemplate.getForEntity<String>("/set_main_lights_state?state=LONG_RANGE_LIGHTS")

        val entity = restTemplate.getForEntity<String>("/get_main_lights_state")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body).isEqualTo(MainLight.LONG_RANGE_LIGHTS.name)
        assertThat(electricsComponent.positionLightsState).isTrue()
        assertThat(electricsComponent.drivingLightsState).isTrue()
        assertThat(electricsComponent.longRangeLightsState).isTrue()
    }
    @Test
    fun `get all lights state when till driving lights are on`() {
        restTemplate.getForEntity<String>("/set_main_lights_state?state=POSITION_LIGHTS")
        restTemplate.getForEntity<String>("/set_main_lights_state?state=DRIVING_LIGHTS")

        val entity = restTemplate.getForEntity<String>("/get_main_lights_state")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body).isEqualTo(MainLight.DRIVING_LIGHTS.name)
        assertThat(electricsComponent.positionLightsState).isTrue()
        assertThat(electricsComponent.drivingLightsState).isTrue()
        assertThat(electricsComponent.longRangeLightsState).isFalse()
    }
    @Test
    fun `get all lights state when till position lights are on`() {
        restTemplate.getForEntity<String>("/set_main_lights_state?state=POSITION_LIGHTS")

        val entity = restTemplate.getForEntity<String>("/get_main_lights_state")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body).isEqualTo(MainLight.POSITION_LIGHTS.name)
        assertThat(electricsComponent.positionLightsState).isTrue()
        assertThat(electricsComponent.drivingLightsState).isFalse()
        assertThat(electricsComponent.longRangeLightsState).isFalse()
    }
    @Test
    fun `get all lights state when main lights are off`() {
        restTemplate.getForEntity<String>("/set_main_lights_state?value=LIGHTS_OFF")

        val entity = restTemplate.getForEntity<String>("/get_main_lights_state")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body).isEqualTo(MainLight.LIGHTS_OFF.name)
        assertThat(electricsComponent.positionLightsState).isFalse()
        assertThat(electricsComponent.drivingLightsState).isFalse()
        assertThat(electricsComponent.longRangeLightsState).isFalse()
    }

    // setReverseLightsState
    @Test
    fun `turn on reverse lights`() {
        val entity = restTemplate.getForEntity<String>("/set_reverse_lights_state?state=true")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body).isEqualTo("true")
        assertThat(electricsComponent.reverseLightsState).isTrue()
    }
    @Test
    fun `turn off reverse lights`() {
        val entity = restTemplate.getForEntity<String>("/set_reverse_lights_state?state=false")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body).isEqualTo("false")
        assertThat(electricsComponent.reverseLightsState).isFalse()
    }

    // getReverseLightsState
    @Test
    fun `reverse lights should be on`() {
        restTemplate.getForEntity<String>("/set_reverse_lights_state?state=true")

        val entity = restTemplate.getForEntity<String>("/get_reverse_lights_state")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body).isEqualTo("true")
        assertThat(electricsComponent.reverseLightsState).isTrue()
    }
    @Test
    fun `reverse lights should be off`() {
        restTemplate.getForEntity<String>("/set_reverse_lights_state?state=false")

        val entity = restTemplate.getForEntity<String>("/get_reverse_lights_state")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body).isEqualTo("false")
        assertThat(electricsComponent.reverseLightsState).isFalse()
    }

    // setEmergencyLightsState
    @Test
    fun `turn on emergency lights`() {
        val entity = restTemplate.getForEntity<String>("/set_emergency_lights_state?state=true")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body).isEqualTo("true")
        assertThat(electricsComponent.emergencyLightsState).isTrue()
    }
    @Test
    fun `turn off emergency lights`() {
        val entity = restTemplate.getForEntity<String>("/set_emergency_lights_state?state=false")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body).isEqualTo("false")
        assertThat(electricsComponent.emergencyLightsState).isFalse()
    }

    // getEmergencyLightsState
    @Test
    fun `emergency lights should be on`() {
        restTemplate.getForEntity<String>("/set_emergency_lights_state?state=true")

        val entity = restTemplate.getForEntity<String>("/get_emergency_lights_state")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body).isEqualTo("true")
        assertThat(electricsComponent.emergencyLightsState).isTrue()
    }
    @Test
    fun `emergency lights should be off`() {
        restTemplate.getForEntity<String>("/set_emergency_lights_state?state=false")

        val entity = restTemplate.getForEntity<String>("/get_emergency_lights_state")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body).isEqualTo("false")
        assertThat(electricsComponent.emergencyLightsState).isFalse()
    }
}