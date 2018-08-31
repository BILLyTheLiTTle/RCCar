package car.controllers.basic

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension

@SpringBootTest
@ExtendWith(SpringExtension::class)
internal class ElectricsImplFromLightsOffTest: ElectricsImplTest() {

    override val initialLightsState = false

    @BeforeEach
    internal fun setUp() {
        ElectricsImpl.positionLightsState = initialLightsState
        ElectricsImpl.drivingLightsState = initialLightsState
        ElectricsImpl.longRangeLightsState = initialLightsState
        ElectricsImpl.brakingLightsState = initialLightsState
        ElectricsImpl.reverseLightsState = initialLightsState
        ElectricsImpl.leftTurnLightsState = initialLightsState
        ElectricsImpl.rightTurnLightsState = initialLightsState
        ElectricsImpl.emergencyLightsState = initialLightsState
    }

    @AfterEach
    internal fun tearDown() {
        ElectricsImpl.positionLightsState = initialLightsState
        ElectricsImpl.drivingLightsState = initialLightsState
        ElectricsImpl.longRangeLightsState = initialLightsState
        ElectricsImpl.brakingLightsState = initialLightsState
        ElectricsImpl.reverseLightsState = initialLightsState
        ElectricsImpl.leftTurnLightsState = initialLightsState
        ElectricsImpl.rightTurnLightsState = initialLightsState
        ElectricsImpl.emergencyLightsState = initialLightsState
    }
}