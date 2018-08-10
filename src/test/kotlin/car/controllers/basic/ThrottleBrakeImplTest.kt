package car.controllers.basic

import car.server.EngineSystem
import car.server.ThrottleBrakeSystem
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.lang.reflect.Field

@SpringBootTest
@ExtendWith(SpringExtension::class)
internal class ThrottleBrakeImplTest {

    var action: Field? = null
    var value: Field? = null

    @BeforeEach
    internal fun setUp() {
        action = ThrottleBrakeImpl::class.java.getDeclaredField("action")
        action?.isAccessible = true
        value = ThrottleBrakeImpl::class.java.getDeclaredField("value")
        value?.isAccessible = true
    }

    // getParkingBrakeState
    @Test
    fun `parking brake should be activated`() {
        action?.set(ThrottleBrakeImpl, ThrottleBrakeSystem.ACTION_PARKING_BRAKE)
        value?.setInt(ThrottleBrakeImpl, 100)
        assertThat(ThrottleBrakeImpl.parkingBrakeState).isTrue()
    }
    @Test
    fun `parking brake should be deactivated`() {
        action?.set(ThrottleBrakeImpl, ThrottleBrakeSystem.ACTION_BRAKING_STILL)
        value?.setInt(ThrottleBrakeImpl, 100)
        assertThat(ThrottleBrakeImpl.parkingBrakeState).isFalse()
    }
    @Test
    fun `parking brake should be deactivated also`() {
        action?.set(ThrottleBrakeImpl, ThrottleBrakeSystem.ACTION_PARKING_BRAKE)
        value?.setInt(ThrottleBrakeImpl, 10)
        assertThat(ThrottleBrakeImpl.parkingBrakeState).isFalse()
    }

    // getHandbrakeState
    @Test
    fun `handbrake should be activated`() {
        action?.set(ThrottleBrakeImpl, ThrottleBrakeSystem.ACTION_HANDBRAKE)
        value?.setInt(ThrottleBrakeImpl, 100)
        assertThat(ThrottleBrakeImpl.handbrakeState).isTrue()
    }
    @Test
    fun `handbrake should be deactivated`() {
        action?.set(ThrottleBrakeImpl, ThrottleBrakeSystem.ACTION_BRAKING_STILL)
        value?.setInt(ThrottleBrakeImpl, 100)
        assertThat(ThrottleBrakeImpl.handbrakeState).isFalse()
    }
    @Test
    fun `handbrake should be deactivated also`() {
        action?.set(ThrottleBrakeImpl, ThrottleBrakeSystem.ACTION_HANDBRAKE)
        value?.setInt(ThrottleBrakeImpl, 10)
        assertThat(ThrottleBrakeImpl.handbrakeState).isFalse()
    }

    // isMovingForward
    @Test
    fun `is moving forward`() {
        action?.set(ThrottleBrakeImpl, ThrottleBrakeSystem.ACTION_MOVE_FORWARD)
        assertThat(ThrottleBrakeImpl.isMovingForward).isTrue()
    }
    @Test
    fun `is not moving forward`() {
        action?.set(ThrottleBrakeImpl, ThrottleBrakeSystem.ACTION_MOVE_BACKWARD)
        assertThat(ThrottleBrakeImpl.isMovingForward).isFalse()
    }

    // isMovingBackward
    @Test
    fun `is moving backward`() {
        action?.set(ThrottleBrakeImpl, ThrottleBrakeSystem.ACTION_MOVE_BACKWARD)
        assertThat(ThrottleBrakeImpl.isMovingBackward).isTrue()
    }
    @Test
    fun `is not moving backward`() {
        action?.set(ThrottleBrakeImpl, ThrottleBrakeSystem.ACTION_MOVE_FORWARD)
        assertThat(ThrottleBrakeImpl.isMovingBackward).isFalse()
    }

    // isBrakingStill
    @Test
    fun `is braking still`() {
        action?.set(ThrottleBrakeImpl, ThrottleBrakeSystem.ACTION_BRAKING_STILL)
        assertThat(ThrottleBrakeImpl.isBrakingStill).isTrue()
    }
    @Test
    fun `is not braking still`() {
        action?.set(ThrottleBrakeImpl, ThrottleBrakeSystem.ACTION_MOVE_FORWARD)
        assertThat(ThrottleBrakeImpl.isBrakingStill).isFalse()
    }

    // isNeutral
    @Test
    fun `is neutral`() {
        action?.set(ThrottleBrakeImpl, ThrottleBrakeSystem.ACTION_NEUTRAL)
        assertThat(ThrottleBrakeImpl.isNeutral).isTrue()
    }
    @Test
    fun `is not neutral`() {
        action?.set(ThrottleBrakeImpl, ThrottleBrakeSystem.ACTION_MOVE_FORWARD)
        assertThat(ThrottleBrakeImpl.isNeutral).isFalse()
    }

    // getMotionState
    @Test
    fun `neutral motion state`() {
        `is neutral`()
        assertThat(ThrottleBrakeImpl.motionState)
            .isEqualTo(ThrottleBrakeSystem.ACTION_NEUTRAL)
            .isEqualTo("neutral")
    }
    @Test
    fun `braking still motion state`() {
        `is braking still`()
        assertThat(ThrottleBrakeImpl.motionState)
            .isEqualTo(ThrottleBrakeSystem.ACTION_BRAKING_STILL)
            .isEqualTo("braking_still")
    }
    @Test
    fun `parking brake motion state`() {
        `parking brake should be activated`()
        assertThat(ThrottleBrakeImpl.motionState)
            .isEqualTo(ThrottleBrakeSystem.ACTION_PARKING_BRAKE)
            .isEqualTo("parking_brake")
    }
    @Test
    fun `forward motion state`() {
        `is moving forward`()
        assertThat(ThrottleBrakeImpl.motionState)
            .isEqualTo(ThrottleBrakeSystem.ACTION_MOVE_FORWARD)
            .isEqualTo("forward")
    }
    @Test
    fun `backward motion state`() {
        `is moving backward`()
        assertThat(ThrottleBrakeImpl.motionState)
            .isEqualTo(ThrottleBrakeSystem.ACTION_MOVE_BACKWARD)
            .isEqualTo("backward")
    }
    @Test
    fun `unknown motion state`() {
        `parking brake should be deactivated also`()
        assertThat(ThrottleBrakeImpl.motionState)
            .isEqualTo(EngineSystem.EMPTY_STRING)
            .isEqualTo("NULL")
    }

    // throttle
    @Test
    fun `throttle forward`() {
        val ret = ThrottleBrakeImpl.throttle(ThrottleBrakeSystem.ACTION_MOVE_FORWARD, -30)
        assertThat(ret).isEqualTo(EngineSystem.SUCCESS)
        assertThat(ThrottleBrakeImpl.motionState)
            .isEqualTo(action?.get(ThrottleBrakeImpl).toString())
            .isEqualTo(ThrottleBrakeSystem.ACTION_MOVE_FORWARD)
            .isEqualTo("forward")
        assertThat(value?.getInt(ThrottleBrakeImpl)).isEqualTo(-30)
    }
    @Test
    fun `throttle backward`() {
        val ret = ThrottleBrakeImpl.throttle(ThrottleBrakeSystem.ACTION_MOVE_BACKWARD, 300)
        assertThat(ret).isEqualTo(EngineSystem.SUCCESS)
        assertThat(ThrottleBrakeImpl.motionState)
            .isEqualTo(action?.get(ThrottleBrakeImpl).toString())
            .isEqualTo(ThrottleBrakeSystem.ACTION_MOVE_BACKWARD)
            .isEqualTo("backward")
        assertThat(value?.getInt(ThrottleBrakeImpl)).isEqualTo(300)
    }

    @Test
    fun brake() {
        val ret = ThrottleBrakeImpl.brake(10)
        assertThat(ret).isEqualTo(EngineSystem.SUCCESS)
        assertThat(ThrottleBrakeImpl.motionState)
            .isEqualTo(action?.get(ThrottleBrakeImpl).toString())
            .isEqualTo(ThrottleBrakeSystem.ACTION_BRAKING_STILL)
            .isEqualTo("braking_still")
        assertThat(value?.getInt(ThrottleBrakeImpl)).isEqualTo(10)
    }

    // parkingBrake
    @Test
    fun `client activate parking brake`() {
        val ret = ThrottleBrakeImpl.parkingBrake(100)
        assertThat(ret).isEqualTo(EngineSystem.SUCCESS)
        assertThat(ThrottleBrakeImpl.motionState)
            .isEqualTo(action?.get(ThrottleBrakeImpl).toString())
            .isEqualTo(ThrottleBrakeSystem.ACTION_PARKING_BRAKE)
            .isEqualTo("parking_brake")
        assertThat(value?.getInt(ThrottleBrakeImpl)).isEqualTo(100)
    }
    @Test
    fun `client deactivate parking brake from ImageView`() {
        val ret = ThrottleBrakeImpl.parkingBrake(0)
        assertThat(ret).isEqualTo(EngineSystem.SUCCESS)
        assertThat(ThrottleBrakeImpl.motionState)
            .isEqualTo(action?.get(ThrottleBrakeImpl).toString())
            .isEqualTo(ThrottleBrakeSystem.ACTION_BRAKING_STILL)
            .isEqualTo("braking_still")
        assertThat(value?.getInt(ThrottleBrakeImpl)).isEqualTo(0)
    }

    // handbrake
    @Test
    fun `client activate handbrake`() {
        val ret = ThrottleBrakeImpl.handbrake(100)
        assertThat(ret).isEqualTo(EngineSystem.SUCCESS)
        assertThat(ThrottleBrakeImpl.motionState)
            .isEqualTo(action?.get(ThrottleBrakeImpl).toString())
            .isEqualTo(ThrottleBrakeSystem.ACTION_HANDBRAKE)
            .isEqualTo("handbrake")
        assertThat(value?.getInt(ThrottleBrakeImpl)).isEqualTo(100)
    }
    @Test
    fun `client deactivate handbrake from ImageView`() {
        val ret = ThrottleBrakeImpl.handbrake(0)
        assertThat(ret).isEqualTo(EngineSystem.SUCCESS)
        assertThat(ThrottleBrakeImpl.motionState)
            .isEqualTo(action?.get(ThrottleBrakeImpl).toString())
            .isEqualTo(ThrottleBrakeSystem.ACTION_NEUTRAL)
            .isEqualTo("neutral")
        assertThat(value?.getInt(ThrottleBrakeImpl)).isEqualTo(0)
    }

    @Test
    fun setNeutral() {
        val ret = ThrottleBrakeImpl.setNeutral()
        assertThat(ret).isEqualTo(EngineSystem.SUCCESS)
        assertThat(ThrottleBrakeImpl.motionState)
            .isEqualTo(action?.get(ThrottleBrakeImpl).toString())
            .isEqualTo(ThrottleBrakeSystem.ACTION_NEUTRAL)
            .isEqualTo("neutral")
        assertThat(value?.getInt(ThrottleBrakeImpl)).isEqualTo(0)
    }

    @Test
    fun reset() {
        ThrottleBrakeImpl.reset()
        assertThat(ThrottleBrakeImpl.motionState)
            .isEqualTo(action?.get(ThrottleBrakeImpl).toString())
            .isEqualTo(ThrottleBrakeSystem.ACTION_NEUTRAL)
            .isEqualTo("neutral")
        assertThat(value?.getInt(ThrottleBrakeImpl)).isEqualTo(0)
    }
}