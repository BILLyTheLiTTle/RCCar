package car.cockpit.electrics

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension

@SpringBootTest
@ExtendWith(SpringExtension::class)
internal class ElectricsComponentTest(@Autowired val electricsComponent: Electrics) {

val initialLightsState = true

    /* For headlights there is no reason to test if the lower (from tested)
        beam is on/off because we cannot handle larger beam without changing
        the lower beams first.
        To ensure that a beam has remained unchanged we use "initialLightsState"

        For example:
        assertThat(ElectricsComponent.positionLightsState).isEqualTo(initialLightsState)
        when the driving light beam is turned on
     */

    // positionLightsState
    @Test
    fun `turn on position lights`() {
        electricsComponent.positionLightsState = true
        assertThat(electricsComponent.positionLightsState).isTrue()
        assertThat(electricsComponent.drivingLightsState).isFalse()
        assertThat(electricsComponent.longRangeLightsState).isFalse()

    }
    @Test
    fun `turn off position lights`() {
        electricsComponent.positionLightsState = false
        assertThat(electricsComponent.positionLightsState).isFalse()
        assertThat(electricsComponent.drivingLightsState).isFalse()
        assertThat(electricsComponent.longRangeLightsState).isFalse()
    }

    // drivingLightsState
    @Test
    fun `turn on driving lights`() {
        electricsComponent.drivingLightsState = true
        assertThat(electricsComponent.positionLightsState).isEqualTo(initialLightsState)
        assertThat(electricsComponent.drivingLightsState).isTrue()
        assertThat(electricsComponent.longRangeLightsState).isFalse()
    }
    @Test
    fun `turn off driving lights`() {
        electricsComponent.drivingLightsState = false
        assertThat(electricsComponent.positionLightsState).isEqualTo(initialLightsState)
        assertThat(electricsComponent.drivingLightsState).isFalse()
        assertThat(electricsComponent.longRangeLightsState).isFalse()
    }

    // longRangeLightsState
    @Test
    fun `turn on long range lights`() {
        electricsComponent.longRangeLightsState = true
        assertThat(electricsComponent.positionLightsState).isEqualTo(initialLightsState)
        assertThat(electricsComponent.drivingLightsState).isEqualTo(initialLightsState)
        assertThat(electricsComponent.longRangeLightsState).isTrue()
    }
    @Test
    fun `turn off long range lights`() {
        electricsComponent.longRangeLightsState = false
        assertThat(electricsComponent.positionLightsState).isEqualTo(initialLightsState)
        assertThat(electricsComponent.drivingLightsState).isEqualTo(initialLightsState)
        assertThat(electricsComponent.longRangeLightsState).isFalse()
    }

    // brakingLightsState
    @Test
    fun `turn on braking lights`() {
        electricsComponent.brakingLightsState = true
        assertThat(electricsComponent.brakingLightsState).isTrue()
    }
    @Test
    fun `turn off braking lights`() {
        electricsComponent.brakingLightsState = false
        assertThat(electricsComponent.brakingLightsState).isFalse()
    }

    // reverseLightsState
    @Test
    fun `turn on reverse lights`() {
        electricsComponent.reverseLightsState = true
        assertThat(electricsComponent.reverseLightsState).isTrue()
    }
    @Test
    fun `turn off reverse lights`() {
        electricsComponent.reverseLightsState = false
        assertThat(electricsComponent.reverseLightsState).isFalse()
    }

    // leftTurnLightsState
    @Test
    fun `turn on left turn lights`() {
        electricsComponent.rightTurnLightsState = initialLightsState

        electricsComponent.leftTurnLightsState = true
        assertThat(electricsComponent.leftTurnLightsState).isTrue()
        assertThat(electricsComponent.rightTurnLightsState).isFalse()
    }
    @Test
    fun `turn off left turn lights`() {
        electricsComponent.rightTurnLightsState = initialLightsState

        electricsComponent.leftTurnLightsState = false
        assertThat(electricsComponent.leftTurnLightsState).isFalse()
        assertThat(electricsComponent.rightTurnLightsState).isEqualTo(initialLightsState)
    }

    // rightTurnLightsState
    @Test
    fun `turn on right turn lights`() {
        electricsComponent.leftTurnLightsState = initialLightsState

        electricsComponent.rightTurnLightsState = true
        assertThat(electricsComponent.rightTurnLightsState).isTrue()
        assertThat(electricsComponent.leftTurnLightsState).isFalse()
    }
    @Test
    fun `turn off right turn lights`() {
        electricsComponent.leftTurnLightsState = initialLightsState

        electricsComponent.rightTurnLightsState = false
        assertThat(electricsComponent.rightTurnLightsState).isFalse()
        assertThat(electricsComponent.leftTurnLightsState).isEqualTo(initialLightsState)
    }

    // emergencyLightsState
    @Test
    fun `turn on emergency lights`() {
        electricsComponent.emergencyLightsState = true
        assertThat(electricsComponent.emergencyLightsState).isTrue()
    }
    @Test
    fun `turn off emergency lights`() {
        electricsComponent.emergencyLightsState = false
        assertThat(electricsComponent.emergencyLightsState).isFalse()
    }

    // doHeadlightsSignal
    @Test
    fun `do headlights signal`() {
        val ret = electricsComponent.doHeadlightsSignal()
        assertThat(ret).isEqualTo("Signal")
        assertThat(electricsComponent.positionLightsState).isEqualTo(initialLightsState)
        assertThat(electricsComponent.drivingLightsState).isEqualTo(initialLightsState)
        assertThat(electricsComponent.longRangeLightsState).isEqualTo(initialLightsState)

    }

    // handleLeds
    @Test
    fun `turn on or off the LEDs`() {
        // TODO needs pins to test it, probably no need to test it and get assertions
    }


    @Test
    fun reset() {
        electricsComponent.reset()
        assertThat(electricsComponent.longRangeLightsState).isFalse()
        assertThat(electricsComponent.drivingLightsState).isFalse()
        assertThat(electricsComponent.positionLightsState).isFalse()
        assertThat(electricsComponent.brakingLightsState).isFalse()
        assertThat(electricsComponent.reverseLightsState).isFalse()
        assertThat(electricsComponent.leftTurnLightsState).isFalse()
        assertThat(electricsComponent.rightTurnLightsState).isFalse()
        assertThat(electricsComponent.emergencyLightsState).isFalse()
    }
}