package car.cockpit.pedals

import car.cockpit.engine.EMPTY_STRING
import car.cockpit.engine.EngineController
import car.cockpit.engine.SUCCESS
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible

@SpringBootTest
@ExtendWith(SpringExtension::class)
internal class ThrottleBrakeImplTest {

    private var actionMemProp: KMutableProperty<*>? = null
    private var valueMemProp: KMutableProperty<*>? = null

    @BeforeEach
    internal fun setUp() {
        actionMemProp = ThrottleBrakeImpl::class.memberProperties
            .filterIsInstance<KMutableProperty<*>>()
            .firstOrNull { it.name =="action" }
        actionMemProp?.isAccessible = true
        valueMemProp = ThrottleBrakeImpl::class.memberProperties
            .filterIsInstance<KMutableProperty<*>>()
            .firstOrNull { it.name =="value" }
        valueMemProp?.isAccessible = true
    }

    // getParkingBrakeState
    @Test
    fun `parking brake should be activated`() {
        actionMemProp?.setter?.call(ACTION_PARKING_BRAKE)
        valueMemProp?.setter?.call(ThrottleBrakeImpl, 100)
        assertThat(ThrottleBrakeImpl.parkingBrakeState).isTrue()
    }
    @Test
    fun `parking brake should be deactivated`() {
        actionMemProp?.setter?.call(ACTION_BRAKING_STILL)
        valueMemProp?.setter?.call(ThrottleBrakeImpl, 100)
        assertThat(ThrottleBrakeImpl.parkingBrakeState).isFalse()
    }
    @Test
    fun `parking brake should be deactivated also`() {
        actionMemProp?.setter?.call(ACTION_PARKING_BRAKE)
        valueMemProp?.setter?.call(ThrottleBrakeImpl, 10)
        assertThat(ThrottleBrakeImpl.parkingBrakeState).isFalse()
    }

    // getHandbrakeState
    @Test
    fun `handbrake should be activated`() {
        actionMemProp?.setter?.call(ACTION_HANDBRAKE)
        valueMemProp?.setter?.call(ThrottleBrakeImpl, 100)
        assertThat(ThrottleBrakeImpl.handbrakeState).isTrue()
    }
    @Test
    fun `handbrake should be deactivated`() {
        actionMemProp?.setter?.call(ACTION_BRAKING_STILL)
        valueMemProp?.setter?.call(ThrottleBrakeImpl, 100)
        assertThat(ThrottleBrakeImpl.handbrakeState).isFalse()
    }
    @Test
    fun `handbrake should be deactivated also`() {
        actionMemProp?.setter?.call(ACTION_HANDBRAKE)
        valueMemProp?.setter?.call(ThrottleBrakeImpl, 10)
        assertThat(ThrottleBrakeImpl.handbrakeState).isFalse()
    }

    // isMovingForward
    @Test
    fun `is moving forward`() {
        actionMemProp?.setter?.call(ACTION_MOVE_FORWARD)
        assertThat(ThrottleBrakeImpl.isMovingForward).isTrue()
    }
    @Test
    fun `is not moving forward`() {
        actionMemProp?.setter?.call(ACTION_MOVE_BACKWARD)
        assertThat(ThrottleBrakeImpl.isMovingForward).isFalse()
    }

    // isMovingBackward
    @Test
    fun `is moving backward`() {
        actionMemProp?.setter?.call(ACTION_MOVE_BACKWARD)
        assertThat(ThrottleBrakeImpl.isMovingBackward).isTrue()
    }
    @Test
    fun `is not moving backward`() {
        actionMemProp?.setter?.call(ACTION_MOVE_FORWARD)
        assertThat(ThrottleBrakeImpl.isMovingBackward).isFalse()
    }

    // isBrakingStill
    @Test
    fun `is braking still`() {
        actionMemProp?.setter?.call(ACTION_BRAKING_STILL)
        assertThat(ThrottleBrakeImpl.isBrakingStill).isTrue()
    }
    @Test
    fun `is not braking still`() {
        actionMemProp?.setter?.call(ACTION_MOVE_FORWARD)
        assertThat(ThrottleBrakeImpl.isBrakingStill).isFalse()
    }

    // isNeutral
    @Test
    fun `is neutral`() {
        actionMemProp?.setter?.call(ACTION_NEUTRAL)
        assertThat(ThrottleBrakeImpl.isNeutral).isTrue()
    }
    @Test
    fun `is not neutral`() {
        actionMemProp?.setter?.call(ACTION_MOVE_FORWARD)
        assertThat(ThrottleBrakeImpl.isNeutral).isFalse()
    }

    // getMotionState
    @Test
    fun `neutral motion state`() {
        `is neutral`()
        assertThat(ThrottleBrakeImpl.motionState)
            .isEqualTo(ACTION_NEUTRAL)
            .isEqualTo("neutral")
    }
    @Test
    fun `braking still motion state`() {
        `is braking still`()
        assertThat(ThrottleBrakeImpl.motionState)
            .isEqualTo(ACTION_BRAKING_STILL)
            .isEqualTo("braking_still")
    }
    @Test
    fun `parking brake motion state`() {
        `parking brake should be activated`()
        assertThat(ThrottleBrakeImpl.motionState)
            .isEqualTo(ACTION_PARKING_BRAKE)
            .isEqualTo("parking_brake")
    }
    @Test
    fun `forward motion state`() {
        `is moving forward`()
        assertThat(ThrottleBrakeImpl.motionState)
            .isEqualTo(ACTION_MOVE_FORWARD)
            .isEqualTo("forward")
    }
    @Test
    fun `backward motion state`() {
        `is moving backward`()
        assertThat(ThrottleBrakeImpl.motionState)
            .isEqualTo(ACTION_MOVE_BACKWARD)
            .isEqualTo("backward")
    }
    @Test
    fun `unknown motion state`() {
        `parking brake should be deactivated also`()
        assertThat(ThrottleBrakeImpl.motionState)
            .isEqualTo(EMPTY_STRING)
            .isEqualTo("NULL")
    }

    // throttle
    @Test
    fun `throttle forward`() {
        val ret = ThrottleBrakeImpl.throttle(ACTION_MOVE_FORWARD, -30)
        assertThat(ret).isEqualTo(SUCCESS)
        assertThat(ThrottleBrakeImpl.motionState)
            .isEqualTo(actionMemProp?.getter?.call())
            .isEqualTo(ACTION_MOVE_FORWARD)
            .isEqualTo("forward")
        assertThat(valueMemProp?.getter?.call(ThrottleBrakeImpl)).isEqualTo(-30)
    }
    @Test
    fun `throttle backward`() {
        val ret = ThrottleBrakeImpl.throttle(ACTION_MOVE_BACKWARD, 300)
        assertThat(ret).isEqualTo(SUCCESS)
        assertThat(ThrottleBrakeImpl.motionState)
            .isEqualTo(actionMemProp?.getter?.call())
            .isEqualTo(ACTION_MOVE_BACKWARD)
            .isEqualTo("backward")
        assertThat(valueMemProp?.getter?.call(ThrottleBrakeImpl)).isEqualTo(300)
    }

    @Test
    fun brake() {
        val ret = ThrottleBrakeImpl.brake(10)
        assertThat(ret).isEqualTo(SUCCESS)
        assertThat(ThrottleBrakeImpl.motionState)
            .isEqualTo(actionMemProp?.getter?.call())
            .isEqualTo(ACTION_BRAKING_STILL)
            .isEqualTo("braking_still")
        assertThat(valueMemProp?.getter?.call(ThrottleBrakeImpl)).isEqualTo(10)
    }

    // parkingBrake
    @Test
    fun `client activate parking brake`() {
        val ret = ThrottleBrakeImpl.parkingBrake(100)
        assertThat(ret).isEqualTo(SUCCESS)
        assertThat(ThrottleBrakeImpl.motionState)
            .isEqualTo(actionMemProp?.getter?.call())
            .isEqualTo(ACTION_PARKING_BRAKE)
            .isEqualTo("parking_brake")
        assertThat(valueMemProp?.getter?.call(ThrottleBrakeImpl)).isEqualTo(100)
    }
    @Test
    fun `client deactivate parking brake from ImageView`() {
        val ret = ThrottleBrakeImpl.parkingBrake(0)
        assertThat(ret).isEqualTo(SUCCESS)
        assertThat(ThrottleBrakeImpl.motionState)
            .isEqualTo(actionMemProp?.getter?.call())
            .isEqualTo(ACTION_BRAKING_STILL)
            .isEqualTo("braking_still")
        assertThat(valueMemProp?.getter?.call(ThrottleBrakeImpl)).isEqualTo(0)
    }

    // handbrake
    @Test
    fun `client activate handbrake`() {
        val ret = ThrottleBrakeImpl.handbrake(100)
        assertThat(ret).isEqualTo(SUCCESS)
        assertThat(ThrottleBrakeImpl.motionState)
            .isEqualTo(actionMemProp?.getter?.call())
            .isEqualTo(ACTION_HANDBRAKE)
            .isEqualTo("handbrake")
        assertThat(valueMemProp?.getter?.call(ThrottleBrakeImpl)).isEqualTo(100)
    }
    @Test
    fun `client deactivate handbrake from ImageView`() {
        val ret = ThrottleBrakeImpl.handbrake(0)
        assertThat(ret).isEqualTo(SUCCESS)
        assertThat(ThrottleBrakeImpl.motionState)
            .isEqualTo(actionMemProp?.getter?.call())
            .isEqualTo(ACTION_NEUTRAL)
            .isEqualTo("neutral")
        assertThat(valueMemProp?.getter?.call(ThrottleBrakeImpl)).isEqualTo(0)
    }

    @Test
    fun setNeutral() {
        val ret = ThrottleBrakeImpl.setNeutral()
        assertThat(ret).isEqualTo(SUCCESS)
        assertThat(ThrottleBrakeImpl.motionState)
            .isEqualTo(actionMemProp?.getter?.call())
            .isEqualTo(ACTION_NEUTRAL)
            .isEqualTo("neutral")
        assertThat(valueMemProp?.getter?.call(ThrottleBrakeImpl)).isEqualTo(0)
    }

    @Test
    fun reset() {
        ThrottleBrakeImpl.reset()
        assertThat(ThrottleBrakeImpl.motionState)
            .isEqualTo(actionMemProp?.getter?.call())
            .isEqualTo(ACTION_NEUTRAL)
            .isEqualTo("neutral")
        assertThat(valueMemProp?.getter?.call(ThrottleBrakeImpl)).isEqualTo(0)
    }
}