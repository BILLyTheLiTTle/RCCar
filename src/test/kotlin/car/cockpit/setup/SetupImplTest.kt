package car.cockpit.setup

import car.cockpit.setup.SetupImpl
import car.cockpit.setup.SetupSystem
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension

@SpringBootTest
@ExtendWith(SpringExtension::class)
internal class SetupImplTest {


    @Test
    fun reset() {
        SetupImpl.reset()
        assertThat(SetupImpl.handlingAssistanceState)
            .isEqualTo(SetupSystem.ASSISTANCE_NONE)
            .isEqualTo("assistance_none")
        assertThat(SetupImpl.motorSpeedLimiter)
            .isEqualTo(SetupSystem.MOTOR_SPEED_LIMITER_FULL_SPEED)
            .isEqualTo(1.00)

        assertThat(SetupImpl.frontDifferentialSlipperyLimiter)
            .isEqualTo(SetupSystem.DIFFERENTIAL_SLIPPERY_LIMITER_LOCKED)
            .isEqualTo(4)

        assertThat(SetupImpl.rearDifferentialSlipperyLimiter)
            .isEqualTo(SetupSystem.DIFFERENTIAL_SLIPPERY_LIMITER_LOCKED)
            .isEqualTo(4)
    }
}