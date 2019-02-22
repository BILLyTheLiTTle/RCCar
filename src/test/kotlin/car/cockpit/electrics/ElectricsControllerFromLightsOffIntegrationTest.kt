package car.cockpit.electrics

import car.INTEGRATION_TEST
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.getForEntity
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Tag(INTEGRATION_TEST)
internal class ElectricsControllerFromLightsOffIntegrationTest(
    @Autowired override val restTemplate: TestRestTemplate,
    @Autowired override val electricsComponent: Electrics
): ElectricsControllerIntegrationTest(restTemplate, electricsComponent) {

    override val initialLightsState = false

    @BeforeEach
    internal fun setUp() {
        if (electricsComponent.positionLightsState != initialLightsState){
            restTemplate.getForEntity<String>("/set_main_lights_state?value=position_lights")
        }
        if (electricsComponent.drivingLightsState != initialLightsState){
            restTemplate.getForEntity<String>("/set_main_lights_state?value=driving_lights")
        }
        if (electricsComponent.longRangeLightsState != initialLightsState){
            restTemplate.getForEntity<String>("/set_main_lights_state?value=long_range_lights")
        }
    }

    @AfterEach
    internal fun tearDown() {
        if (electricsComponent.longRangeLightsState){
            restTemplate.getForEntity<String>("/set_main_lights_state?value=long_range_lights")
        }
        if (electricsComponent.drivingLightsState){
            restTemplate.getForEntity<String>("/set_main_lights_state?value=driving_lights")
        }
        if (electricsComponent.positionLightsState){
            restTemplate.getForEntity<String>("/set_main_lights_state?value=position_lights")
        }
    }
}