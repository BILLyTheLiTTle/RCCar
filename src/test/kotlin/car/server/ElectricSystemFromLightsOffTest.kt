package car.server

import car.controllers.basic.ElectricsImpl
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.getForEntity
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class ElectricSystemFromLightsOffTest(
    @Autowired override val restTemplate: TestRestTemplate
): ElectricSystemTest(restTemplate) {

    override val initialLightsState = false

    @BeforeEach
    internal fun setUp() {
        if (ElectricsImpl.positionLightsState != initialLightsState){
            restTemplate.getForEntity<String>("/set_main_lights_state?value=position_lights")
        }

    }

    @AfterEach
    internal fun tearDown() {
        if (ElectricsImpl.positionLightsState){
            restTemplate.getForEntity<String>("/set_main_lights_state?value=position_lights")
        }
    }
}