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
            .isEqualTo(ASSISTANCE_NONE)
            .isEqualTo("assistance_none")
        assertThat(setupComponent.motorSpeedLimiter)
            .isEqualTo(MOTOR_SPEED_LIMITER_FULL_SPEED)
            .isEqualTo(1.00)

        assertThat(setupComponent.frontDifferentialSlipperyLimiter)
            .isEqualTo(DIFFERENTIAL_SLIPPERY_LIMITER_LOCKED)
            .isEqualTo(4)

        assertThat(setupComponent.rearDifferentialSlipperyLimiter)
            .isEqualTo(DIFFERENTIAL_SLIPPERY_LIMITER_LOCKED)
            .isEqualTo(4)
    }
}