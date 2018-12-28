package car.cockpit.electrics

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension

@SpringBootTest
@ExtendWith(SpringExtension::class)
internal class ElectricsComponentFromLightsOffTest(
    @Autowired override val electricsComponent: Electrics
): ElectricsComponentTest(electricsComponent) {

    override val initialLightsState = false

    @BeforeEach
    internal fun setUp() {
        electricsComponent.positionLightsState = initialLightsState
        electricsComponent.drivingLightsState = initialLightsState
        electricsComponent.longRangeLightsState = initialLightsState
        electricsComponent.brakingLightsState = initialLightsState
        electricsComponent.reverseLightsState = initialLightsState
        electricsComponent.leftTurnLightsState = initialLightsState
        electricsComponent.rightTurnLightsState = initialLightsState
        electricsComponent.emergencyLightsState = initialLightsState
    }

    @AfterEach
    internal fun tearDown() {
        electricsComponent.positionLightsState = initialLightsState
        electricsComponent.drivingLightsState = initialLightsState
        electricsComponent.longRangeLightsState = initialLightsState
        electricsComponent.brakingLightsState = initialLightsState
        electricsComponent.reverseLightsState = initialLightsState
        electricsComponent.leftTurnLightsState = initialLightsState
        electricsComponent.rightTurnLightsState = initialLightsState
        electricsComponent.emergencyLightsState = initialLightsState
    }
}