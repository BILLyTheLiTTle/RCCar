package car.cockpit.setup

import car.UNIT_TEST
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension

@SpringBootTest
@ExtendWith(SpringExtension::class)
@Tag(UNIT_TEST)
internal class SetupComponentUnitTest(@Autowired val setupComponent: Setup) {


    @Test
    fun reset() {
        setupComponent.reset()
        assertThat(setupComponent.handlingAssistanceState)
            .isEqualTo(HandlingAssistance.MANUAL)

        assertThat(setupComponent.motorSpeedLimiter)
            .isEqualTo(MotorSpeedLimiter.FULL_SPEED)

        assertThat(setupComponent.frontDifferentialSlipperyLimiter)
            .isEqualTo(DifferentialSlipperyLimiter.LOCKED)

        assertThat(setupComponent.rearDifferentialSlipperyLimiter)
            .isEqualTo(DifferentialSlipperyLimiter.LOCKED)
    }
}