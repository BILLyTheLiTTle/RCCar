package car.cockpit.electrics

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension


@SpringBootTest
@ExtendWith(SpringExtension::class)
internal class ElectricsComponentFromLightsOnTest(
    @Autowired override val electricsComponent: Electrics
): ElectricsComponentTest(electricsComponent) {

    override val initialLightsState = true

    @BeforeEach
    internal fun setUp() {
        electricsComponent.positionLightsState = initialLightsState
        electricsComponent.drivingLightsState = initialLightsState
        electricsComponent.longRangeLightsState = initialLightsState
        electricsComponent.brakingLightsState = initialLightsState
        electricsComponent.reverseLightsState = initialLightsState
        /* The turn lights will bi initialized in the test function
            because when we set on turn light to true
            the opposite will turn to false and will mess up the test.
         */
        //ElectricsComponent.leftTurnLightsState = initialLightsState
        //ElectricsComponent.rightTurnLightsState = initialLightsState
        electricsComponent.emergencyLightsState = initialLightsState
    }

    @AfterEach
    internal fun tearDown() {
        electricsComponent.positionLightsState = !initialLightsState
        electricsComponent.drivingLightsState = !initialLightsState
        electricsComponent.longRangeLightsState = !initialLightsState
        electricsComponent.brakingLightsState = !initialLightsState
        electricsComponent.reverseLightsState = !initialLightsState
        electricsComponent.leftTurnLightsState = !initialLightsState
        electricsComponent.rightTurnLightsState = !initialLightsState
        electricsComponent.emergencyLightsState = !initialLightsState
    }
}