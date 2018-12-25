package car.cockpit.electrics

import car.cockpit.electrics.ElectricsImpl
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension

@SpringBootTest
@ExtendWith(SpringExtension::class)
internal class ElectricsImplTest {

val initialLightsState = true

    /* For headlights there is no reason to test if the lower (from tested)
        beam is on/off because we cannot handle larger beam without changing
        the lower beams first.
        To ensure that a beam has remained unchanged we use "initialLightsState"

        For example:
        assertThat(ElectricsImpl.positionLightsState).isEqualTo(initialLightsState)
        when the driving light beam is turned on
     */

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
        ElectricsImpl.positionLightsState = false
        assertThat(ElectricsImpl.positionLightsState).isFalse()
        assertThat(ElectricsImpl.drivingLightsState).isFalse()
        assertThat(ElectricsImpl.longRangeLightsState).isFalse()
    }

    // drivingLightsState
    @Test
    fun `turn on driving lights`() {
        ElectricsImpl.drivingLightsState = true
        assertThat(ElectricsImpl.positionLightsState).isEqualTo(initialLightsState)
        assertThat(ElectricsImpl.drivingLightsState).isTrue()
        assertThat(ElectricsImpl.longRangeLightsState).isFalse()
    }
    @Test
    fun `turn off driving lights`() {
        ElectricsImpl.drivingLightsState = false
        assertThat(ElectricsImpl.positionLightsState).isEqualTo(initialLightsState)
        assertThat(ElectricsImpl.drivingLightsState).isFalse()
        assertThat(ElectricsImpl.longRangeLightsState).isFalse()
    }

    // longRangeLightsState
    @Test
    fun `turn on long range lights`() {
        ElectricsImpl.longRangeLightsState = true
        assertThat(ElectricsImpl.positionLightsState).isEqualTo(initialLightsState)
        assertThat(ElectricsImpl.drivingLightsState).isEqualTo(initialLightsState)
        assertThat(ElectricsImpl.longRangeLightsState).isTrue()
    }
    @Test
    fun `turn off long range lights`() {
        ElectricsImpl.longRangeLightsState = false
        assertThat(ElectricsImpl.positionLightsState).isEqualTo(initialLightsState)
        assertThat(ElectricsImpl.drivingLightsState).isEqualTo(initialLightsState)
        assertThat(ElectricsImpl.longRangeLightsState).isFalse()
    }

    // brakingLightsState
    @Test
    fun `turn on braking lights`() {
        ElectricsImpl.brakingLightsState = true
        assertThat(ElectricsImpl.brakingLightsState).isTrue()
    }
    @Test
    fun `turn off braking lights`() {
        ElectricsImpl.brakingLightsState = false
        assertThat(ElectricsImpl.brakingLightsState).isFalse()
    }

    // reverseLightsState
    @Test
    fun `turn on reverse lights`() {
        ElectricsImpl.reverseLightsState = true
        assertThat(ElectricsImpl.reverseLightsState).isTrue()
    }
    @Test
    fun `turn off reverse lights`() {
        ElectricsImpl.reverseLightsState = false
        assertThat(ElectricsImpl.reverseLightsState).isFalse()
    }

    // leftTurnLightsState
    @Test
    fun `turn on left turn lights`() {
        ElectricsImpl.rightTurnLightsState = initialLightsState

        ElectricsImpl.leftTurnLightsState = true
        assertThat(ElectricsImpl.leftTurnLightsState).isTrue()
        assertThat(ElectricsImpl.rightTurnLightsState).isFalse()
    }
    @Test
    fun `turn off left turn lights`() {
        ElectricsImpl.rightTurnLightsState = initialLightsState

        ElectricsImpl.leftTurnLightsState = false
        assertThat(ElectricsImpl.leftTurnLightsState).isFalse()
        assertThat(ElectricsImpl.rightTurnLightsState).isEqualTo(initialLightsState)
    }

    // rightTurnLightsState
    @Test
    fun `turn on right turn lights`() {
        ElectricsImpl.leftTurnLightsState = initialLightsState

        ElectricsImpl.rightTurnLightsState = true
        assertThat(ElectricsImpl.rightTurnLightsState).isTrue()
        assertThat(ElectricsImpl.leftTurnLightsState).isFalse()
    }
    @Test
    fun `turn off right turn lights`() {
        ElectricsImpl.leftTurnLightsState = initialLightsState

        ElectricsImpl.rightTurnLightsState = false
        assertThat(ElectricsImpl.rightTurnLightsState).isFalse()
        assertThat(ElectricsImpl.leftTurnLightsState).isEqualTo(initialLightsState)
    }

    // emergencyLightsState
    @Test
    fun `turn on emergency lights`() {
        ElectricsImpl.emergencyLightsState = true
        assertThat(ElectricsImpl.emergencyLightsState).isTrue()
    }
    @Test
    fun `turn off emergency lights`() {
        ElectricsImpl.emergencyLightsState = false
        assertThat(ElectricsImpl.emergencyLightsState).isFalse()
    }

    // doHeadlightsSignal
    @Test
    fun `do headlights signal`() {
        val ret = ElectricsImpl.doHeadlightsSignal()
        assertThat(ret).isEqualTo("Signal")
        assertThat(ElectricsImpl.positionLightsState).isEqualTo(initialLightsState)
        assertThat(ElectricsImpl.drivingLightsState).isEqualTo(initialLightsState)
        assertThat(ElectricsImpl.longRangeLightsState).isEqualTo(initialLightsState)

    }

    // handleLeds
    @Test
    fun `turn on or off the LEDs`() {
        // TODO needs pins to test it, probably no need to test it and get assertions
    }


    @Test
    fun reset() {
        ElectricsImpl.reset()
        assertThat(ElectricsImpl.longRangeLightsState).isFalse()
        assertThat(ElectricsImpl.drivingLightsState).isFalse()
        assertThat(ElectricsImpl.positionLightsState).isFalse()
        assertThat(ElectricsImpl.brakingLightsState).isFalse()
        assertThat(ElectricsImpl.reverseLightsState).isFalse()
        assertThat(ElectricsImpl.leftTurnLightsState).isFalse()
        assertThat(ElectricsImpl.rightTurnLightsState).isFalse()
        assertThat(ElectricsImpl.emergencyLightsState).isFalse()
    }
}