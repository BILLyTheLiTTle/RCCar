package car.server

import car.controllers.basic.ElectricsImpl
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

    val initialLightsState = true

    // setDirectionLights
    @Test
    fun `turn on left direction light`() {
        if (ElectricsImpl.leftTurnLightsState){
            restTemplate.getForEntity<String>("/set_direction_lights?direction=turn_lights_left")
        }
        val entity = restTemplate.getForEntity<String>("/set_direction_lights?direction=turn_lights_left")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body).isEqualTo(ElectricSystem.TURN_LIGHTS_LEFT)
        assertThat(ElectricsImpl.leftTurnLightsState).isTrue()
        assertThat(ElectricsImpl.rightTurnLightsState).isFalse()
    }
    @Test
    fun `turn off left direction light`() {
        if (!ElectricsImpl.leftTurnLightsState) {
            restTemplate.getForEntity<String>("/set_direction_lights?direction=turn_lights_left")
        }
        val entity = restTemplate.getForEntity<String>("/set_direction_lights?direction=turn_lights_left")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body).isEqualTo(ElectricSystem.TURN_LIGHTS_STRAIGHT)
        assertThat(ElectricsImpl.leftTurnLightsState).isFalse()
        assertThat(ElectricsImpl.rightTurnLightsState).isFalse()
    }
    @Test
    fun `turn on right direction light`() {
        if (ElectricsImpl.rightTurnLightsState){
            restTemplate.getForEntity<String>("/set_direction_lights?direction=turn_lights_right")
        }
        val entity = restTemplate.getForEntity<String>("/set_direction_lights?direction=turn_lights_right")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body).isEqualTo(ElectricSystem.TURN_LIGHTS_RIGHT)
        assertThat(ElectricsImpl.rightTurnLightsState).isTrue()
        assertThat(ElectricsImpl.leftTurnLightsState).isFalse()
    }
    @Test
    fun `turn off right direction light`() {
        if (!ElectricsImpl.rightTurnLightsState) {
            restTemplate.getForEntity<String>("/set_direction_lights?direction=turn_lights_right")
        }
        val entity = restTemplate.getForEntity<String>("/set_direction_lights?direction=turn_lights_right")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body).isEqualTo(ElectricSystem.TURN_LIGHTS_STRAIGHT)
        assertThat(ElectricsImpl.rightTurnLightsState).isFalse()
        assertThat(ElectricsImpl.leftTurnLightsState).isFalse()
    }
    @Test
    fun `turn off both direction lights when straight from left on`() {
        if (!ElectricsImpl.leftTurnLightsState) {
            restTemplate.getForEntity<String>("/set_direction_lights?direction=turn_lights_right")
        }
        val entity = restTemplate.getForEntity<String>("/set_direction_lights?direction=turn_lights_straight")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body).isEqualTo(ElectricSystem.TURN_LIGHTS_STRAIGHT)
        assertThat(ElectricsImpl.rightTurnLightsState).isFalse()
        assertThat(ElectricsImpl.leftTurnLightsState).isFalse()
    }
    @Test
    fun `turn off both direction lights when straight from right on`() {
        if (!ElectricsImpl.rightTurnLightsState) {
            restTemplate.getForEntity<String>("/set_direction_lights?direction=turn_lights_right")
        }
        val entity = restTemplate.getForEntity<String>("/set_direction_lights?direction=turn_lights_straight")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body).isEqualTo(ElectricSystem.TURN_LIGHTS_STRAIGHT)
        assertThat(ElectricsImpl.rightTurnLightsState).isFalse()
        assertThat(ElectricsImpl.leftTurnLightsState).isFalse()
    }

    // getDirectionLights
    @Test
    fun `get both turn lights when left turn is on`() {
        if (!ElectricsImpl.leftTurnLightsState) {
            restTemplate.getForEntity<String>("/set_direction_lights?direction=turn_lights_left")
        }
        val entity = restTemplate.getForEntity<String>("/get_direction_lights")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body).isEqualTo(ElectricSystem.TURN_LIGHTS_LEFT)
        assertThat(ElectricsImpl.rightTurnLightsState).isFalse()
        assertThat(ElectricsImpl.leftTurnLightsState).isTrue()
    }
    @Test
    fun `get both turn lights when left turn is off`() {
        if (ElectricsImpl.leftTurnLightsState) {
            restTemplate.getForEntity<String>("/set_direction_lights?direction=turn_lights_left")
        }
        val entity = restTemplate.getForEntity<String>("/get_direction_lights")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body).isEqualTo(ElectricSystem.TURN_LIGHTS_STRAIGHT)
        assertThat(ElectricsImpl.rightTurnLightsState).isFalse()
        assertThat(ElectricsImpl.leftTurnLightsState).isFalse()
    }
    @Test
    fun `get both turn lights when right turn is on`() {
        if (!ElectricsImpl.rightTurnLightsState) {
            restTemplate.getForEntity<String>("/set_direction_lights?direction=turn_lights_right")
        }
        val entity = restTemplate.getForEntity<String>("/get_direction_lights")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body).isEqualTo(ElectricSystem.TURN_LIGHTS_RIGHT)
        assertThat(ElectricsImpl.rightTurnLightsState).isTrue()
        assertThat(ElectricsImpl.leftTurnLightsState).isFalse()
    }
    @Test
    fun `get both turn lights when right turn is off`() {
        if (ElectricsImpl.rightTurnLightsState) {
            restTemplate.getForEntity<String>("/set_direction_lights?direction=turn_lights_right")
        }
        val entity = restTemplate.getForEntity<String>("/get_direction_lights")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body).isEqualTo(ElectricSystem.TURN_LIGHTS_STRAIGHT)
        assertThat(ElectricsImpl.rightTurnLightsState).isFalse()
        assertThat(ElectricsImpl.leftTurnLightsState).isFalse()
    }
    @Test
    fun `get both turn lights when straight`() {
        restTemplate.getForEntity<String>("/set_direction_lights?direction=turn_lights_straight")
        val entity = restTemplate.getForEntity<String>("/get_direction_lights")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body).isEqualTo(ElectricSystem.TURN_LIGHTS_STRAIGHT)
        assertThat(ElectricsImpl.rightTurnLightsState).isFalse()
        assertThat(ElectricsImpl.leftTurnLightsState).isFalse()
    }

    // setMainLightsState
    @Test
    fun `turn off main lights`() {
    }
    @Test
    fun `turn on position lights`() {
    }
    @Test
    fun `turn on driving lights`() {
    }
    @Test
    fun `turn on long range lights`() {
    }
    @Test
    fun `signal with headlights`() {
        // TODO not sure what to test
    }

    // getMainLightsState
    @Test
    fun `get all lights state when long range lights are on`() {
    }
    @Test
    fun `get all lights state when long range lights are off`() {
    }
    @Test
    fun `get all lights state when driving lights are on`() {
    }
    @Test
    fun `get all lights state when driving lights are off`() {
    }
    @Test
    fun `get all lights state when position lights are on`() {
    }
    @Test
    fun `get all lights state when position lights are off`() {
    }
    @Test
    fun `get all lights state when main lights are off`() {
    }

    // setReverseLightsState
    @Test
    fun `turn on reverse lights`() {
    }
    @Test
    fun `turn off reverse lights`() {
    }

    // getReverseLightsState
    @Test
    fun `reverse lights should be on`() {
    }
    @Test
    fun `reverse lights should be off`() {
    }

    // setEmergencyLightsState
    @Test
    fun `turn on emergency lights`() {
    }
    @Test
    fun `turn off emergency lights`() {
    }

    // getEmergencyLightsState
    @Test
    fun `emergency lights should be on`() {
    }
    @Test
    fun `emergency lights should be off`() {
    }
}