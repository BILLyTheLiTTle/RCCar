package car.cockpit.electrics

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
internal class ElectricsControllerFromLightsOnTest(
    @Autowired override val restTemplate: TestRestTemplate
): ElectricsControllerTest(restTemplate) {

    override val initialLightsState = true

    @BeforeEach
    internal fun setUp() {
        if (ElectricsImpl.positionLightsState != initialLightsState){
            restTemplate.getForEntity<String>("/set_main_lights_state?value=position_lights")
        }
        if (ElectricsImpl.drivingLightsState != initialLightsState){
            restTemplate.getForEntity<String>("/set_main_lights_state?value=driving_lights")
        }
        if (ElectricsImpl.longRangeLightsState != initialLightsState){
            restTemplate.getForEntity<String>("/set_main_lights_state?value=long_range_lights")
        }
    }

    @AfterEach
    internal fun tearDown() {
        if (ElectricsImpl.longRangeLightsState){
            restTemplate.getForEntity<String>("/set_main_lights_state?value=long_range_lights")
        }
        if (ElectricsImpl.drivingLightsState){
            restTemplate.getForEntity<String>("/set_main_lights_state?value=driving_lights")
        }
        if (ElectricsImpl.positionLightsState){
            restTemplate.getForEntity<String>("/set_main_lights_state?value=position_lights")
        }
    }
}