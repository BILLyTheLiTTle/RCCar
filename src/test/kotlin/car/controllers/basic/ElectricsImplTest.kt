package car.controllers.basic

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension

@SpringBootTest
@ExtendWith(SpringExtension::class)
internal class ElectricsImplTest {


    // positionLightsState
    @Test
    fun `turn on position lights`() {
        ElectricsImpl.positionLightsState = true
        assertThat(ElectricsImpl.positionLightsState).isTrue()
        assertThat(ElectricsImpl.drivingLightsState).isFalse()
        assertThat(ElectricsImpl.longRangeLightsState).isFalse()

    }
    @Test
    fun `turn off position lights`() {

    }

    // drivingLightsState
    @Test
    fun `turn on driving lights`() {

    }
    @Test
    fun `turn off driving lights`() {

    }

    // longRangeLightsState
    @Test
    fun `turn on long range lights`() {

    }
    @Test
    fun `turn off long range lights`() {

    }

    // brakingLightsState
    @Test
    fun `turn on braking lights`() {

    }
    @Test
    fun `turn off braking lights`() {

    }

    // reverseLightsState
    @Test
    fun `turn on reverse lights`() {

    }
    @Test
    fun `turn off reverse lights`() {

    }

    // leftTurnLightsState
    @Test
    fun `turn on left turn lights`() {

    }
    @Test
    fun `turn off left turn lights`() {

    }

    // rightTurnLightsState
    @Test
    fun `turn on right turn lights`() {

    }
    @Test
    fun `turn off right turn lights`() {

    }

    // emergencyLightsState
    @Test
    fun `turn on emergency lights`() {

    }
    @Test
    fun `turn off emergency lights`() {

    }

    // doHeadlightsSignal
    @Test
    fun `do headlights signal`() {

    }

    // handleLeds
    @Test
    fun `turn on or off the LEDs`() {
        // TODO needs hardware to test it, probably no need to test it and get assertions
    }


    @Test
    fun reset() {

    }
}