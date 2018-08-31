package car.controllers.basic

import car.server.EngineSystem
import car.server.SteeringSystem
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension

@SpringBootTest
@ExtendWith(SpringExtension::class)
internal class SteeringImplTest {

    // turn
    @Test
    fun `turn right`() {
        val ret = SteeringImpl.turn(SteeringSystem.ACTION_TURN_RIGHT,
            SteeringSystem.STEERING_VALUE_40)
        assertThat(ret).isEqualTo(EngineSystem.SUCCESS)
        assertThat(SteeringImpl.direction)
            .isEqualTo(SteeringImpl::direction.get())
            .isEqualTo(SteeringSystem.ACTION_TURN_RIGHT)
            .isEqualTo("right")
        assertThat(SteeringImpl::value.get())
            .isEqualTo(SteeringSystem.STEERING_VALUE_40)
            .isEqualTo(40)
    }
    @Test
    fun `turn left`() {
        val ret = SteeringImpl.turn(SteeringSystem.ACTION_TURN_LEFT,
            SteeringSystem.STEERING_VALUE_60)
        assertThat(ret).isEqualTo(EngineSystem.SUCCESS)
        assertThat(SteeringImpl.direction)
            .isEqualTo(SteeringImpl::direction.get())
            .isEqualTo(SteeringSystem.ACTION_TURN_LEFT)
            .isEqualTo("left")
        assertThat(SteeringImpl::value.get())
            .isEqualTo(SteeringSystem.STEERING_VALUE_60)
            .isEqualTo(60)
    }
    @Test
    fun `turn straight`() {
        val ret = SteeringImpl.turn(SteeringSystem.ACTION_STRAIGHT)
        assertThat(ret).isEqualTo(EngineSystem.SUCCESS)
        assertThat(SteeringImpl.direction)
            .isEqualTo(SteeringImpl::direction.get())
            .isEqualTo(SteeringSystem.ACTION_STRAIGHT)
            .isEqualTo("straight")
        assertThat(SteeringImpl::value.get())
            .isEqualTo(SteeringSystem.STEERING_VALUE_00)
            .isEqualTo(0)
    }


    @Test
    fun reset() {
        SteeringImpl.reset()
        assertThat(SteeringImpl.direction)
            .isEqualTo(SteeringImpl::direction.get())
            .isEqualTo(SteeringSystem.ACTION_STRAIGHT)
            .isEqualTo("straight")
        assertThat(SteeringImpl::value.get())
            .isEqualTo(SteeringSystem.STEERING_VALUE_00)
            .isEqualTo(0)
    }
}