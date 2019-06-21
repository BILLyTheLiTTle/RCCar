package car.cockpit.steering

import car.UNIT_TEST
import car.cockpit.engine.SUCCESS
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
internal class SteeringComponentUnitTest(
    @Autowired val steeringComponent: Steering
) {

    // turn
    @Test
    fun `turn right`() {
        val ret = steeringComponent.turn(
            Turn.RIGHT,
            STEERING_VALUE_40)
        assertThat(ret).isEqualTo(SUCCESS)
        assertThat(steeringComponent.direction)
            .isEqualTo(steeringComponent::direction.get())
            .isEqualTo(Turn.RIGHT)
        assertThat(steeringComponent::value.get())
            .isEqualTo(STEERING_VALUE_40)
            .isEqualTo(40)
    }
    @Test
    fun `turn left`() {
        val ret = steeringComponent.turn(
            Turn.LEFT,
            STEERING_VALUE_60)
        assertThat(ret).isEqualTo(SUCCESS)
        assertThat(steeringComponent.direction)
            .isEqualTo(steeringComponent::direction.get())
            .isEqualTo(Turn.LEFT)
        assertThat(steeringComponent::value.get())
            .isEqualTo(STEERING_VALUE_60)
            .isEqualTo(60)
    }
    @Test
    fun `turn straight`() {
        val ret = steeringComponent.turn(Turn.STRAIGHT)
        assertThat(ret).isEqualTo(SUCCESS)
        assertThat(steeringComponent.direction)
            .isEqualTo(steeringComponent::direction.get())
            .isEqualTo(Turn.STRAIGHT)
        assertThat(steeringComponent::value.get())
            .isEqualTo(STEERING_VALUE_00)
            .isEqualTo(0)
    }


    @Test
    fun reset() {
        steeringComponent.reset()
        assertThat(steeringComponent.direction)
            .isEqualTo(steeringComponent::direction.get())
            .isEqualTo(Turn.STRAIGHT)
        assertThat(steeringComponent::value.get())
            .isEqualTo(STEERING_VALUE_00)
            .isEqualTo(0)
    }
}