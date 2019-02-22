package car.cockpit.electrics

import car.UNIT_TEST
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension

@SpringBootTest
@ExtendWith(SpringExtension::class)
@Tag(UNIT_TEST)
internal class ElectricsComponentFromLightsOffUnitTest(
    @Autowired override val electricsComponent: Electrics
): ElectricsComponentUnitTest(electricsComponent) {

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