package car.cockpit.pedals

import car.UNIT_TEST
import car.cockpit.engine.SUCCESS
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible

@SpringBootTest
@ExtendWith(SpringExtension::class)
@Tag(UNIT_TEST)
internal class ThrottleBrakeComponentUnitTest (
    @Autowired
    @Qualifier("Throttle -n- Brake Component")
    val throttleBrake: ThrottleBrake
) {

    private var actionMemProp: KMutableProperty<*>? = null
    private var valueMemProp: KMutableProperty<*>? = null

    @BeforeEach
    internal fun setUp() {
        actionMemProp = throttleBrake::class.memberProperties
            .filterIsInstance<KMutableProperty<*>>()
            .firstOrNull { it.name =="action" }
        actionMemProp?.isAccessible = true
        valueMemProp = throttleBrake::class.memberProperties
            .filterIsInstance<KMutableProperty<*>>()
            .firstOrNull { it.name =="value" }
        valueMemProp?.isAccessible = true
    }

    // getParkingBrakeState
    @Test
    fun `parking brake should be activated`() {
        actionMemProp?.setter?.call(throttleBrake, Motion.PARKING_BRAKE)
        valueMemProp?.setter?.call(throttleBrake, 100)
        assertThat(throttleBrake.parkingBrakeState).isTrue()
    }
    @Test
    fun `parking brake should be deactivated`() {
        actionMemProp?.setter?.call(throttleBrake, Motion.BRAKING_STILL)
        valueMemProp?.setter?.call(throttleBrake, 100)
        assertThat(throttleBrake.parkingBrakeState).isFalse()
    }
    @Test
    fun `parking brake should be deactivated also`() {
        actionMemProp?.setter?.call(throttleBrake, Motion.PARKING_BRAKE)
        valueMemProp?.setter?.call(throttleBrake, 10)
        assertThat(throttleBrake.parkingBrakeState).isFalse()
    }

    // getHandbrakeState
    @Test
    fun `handbrake should be activated`() {
        actionMemProp?.setter?.call(throttleBrake, Motion.HANDBRAKE)
        valueMemProp?.setter?.call(throttleBrake, 100)
        assertThat(throttleBrake.handbrakeState).isTrue()
    }
    @Test
    fun `handbrake should be deactivated`() {
        actionMemProp?.setter?.call(throttleBrake, Motion.BRAKING_STILL)
        valueMemProp?.setter?.call(throttleBrake, 100)
        assertThat(throttleBrake.handbrakeState).isFalse()
    }
    @Test
    fun `handbrake should be deactivated also`() {
        actionMemProp?.setter?.call(throttleBrake, Motion.HANDBRAKE)
        valueMemProp?.setter?.call(throttleBrake, 10)
        assertThat(throttleBrake.handbrakeState).isFalse()
    }

    // isMovingForward
    @Test
    fun `is moving forward`() {
        actionMemProp?.setter?.call(throttleBrake, Motion.FORWARD)
        assertThat(throttleBrake.isMovingForward).isTrue()
    }
    @Test
    fun `is not moving forward`() {
        actionMemProp?.setter?.call(throttleBrake, Motion.BACKWARD)
        assertThat(throttleBrake.isMovingForward).isFalse()
    }

    // isMovingBackward
    @Test
    fun `is moving backward`() {
        actionMemProp?.setter?.call(throttleBrake, Motion.BACKWARD)
        assertThat(throttleBrake.isMovingBackward).isTrue()
    }
    @Test
    fun `is not moving backward`() {
        actionMemProp?.setter?.call(throttleBrake, Motion.FORWARD)
        assertThat(throttleBrake.isMovingBackward).isFalse()
    }

    // isBrakingStill
    @Test
    fun `is braking still`() {
        actionMemProp?.setter?.call(throttleBrake, Motion.BRAKING_STILL)
        assertThat(throttleBrake.isBrakingStill).isTrue()
    }
    @Test
    fun `is not braking still`() {
        actionMemProp?.setter?.call(throttleBrake, Motion.FORWARD)
        assertThat(throttleBrake.isBrakingStill).isFalse()
    }

    // isNeutral
    @Test
    fun `is neutral`() {
        actionMemProp?.setter?.call(throttleBrake, Motion.NEUTRAL)
        assertThat(throttleBrake.isNeutral).isTrue()
    }
    @Test
    fun `is not neutral`() {
        actionMemProp?.setter?.call(throttleBrake, Motion.FORWARD)
        assertThat(throttleBrake.isNeutral).isFalse()
    }

    // getMotionState
    @Test
    fun `neutral motion state`() {
        `is neutral`()
        assertThat(throttleBrake.motionState)
            .isEqualTo(Motion.NEUTRAL)
    }
    @Test
    fun `braking still motion state`() {
        `is braking still`()
        assertThat(throttleBrake.motionState)
            .isEqualTo(Motion.BRAKING_STILL)
    }
    @Test
    fun `parking brake motion state`() {
        `parking brake should be activated`()
        assertThat(throttleBrake.motionState)
            .isEqualTo(Motion.PARKING_BRAKE)
    }
    @Test
    fun `forward motion state`() {
        `is moving forward`()
        assertThat(throttleBrake.motionState)
            .isEqualTo(Motion.FORWARD)
    }
    @Test
    fun `backward motion state`() {
        `is moving backward`()
        assertThat(throttleBrake.motionState)
            .isEqualTo(Motion.BACKWARD)
    }
    @Test
    fun `unknown motion state`() {
        `parking brake should be deactivated also`()
        assertThat(throttleBrake.motionState)
            .isEqualTo(Motion.NOTHING)
    }

    // throttle
    @Test
    fun `throttle forward`() {
        val ret = throttleBrake.throttle(Motion.FORWARD, -30)
        assertThat(ret).isEqualTo(SUCCESS)
        assertThat(throttleBrake.motionState)
            .isEqualTo(actionMemProp?.getter?.call(throttleBrake))
            .isEqualTo(Motion.FORWARD)
        assertThat(valueMemProp?.getter?.call(throttleBrake)).isEqualTo(-30)
    }
    @Test
    fun `throttle backward`() {
        val ret = throttleBrake.throttle(Motion.BACKWARD, 300)
        assertThat(ret).isEqualTo(SUCCESS)
        assertThat(throttleBrake.motionState)
            .isEqualTo(actionMemProp?.getter?.call(throttleBrake))
            .isEqualTo(Motion.BACKWARD)
        assertThat(valueMemProp?.getter?.call(throttleBrake)).isEqualTo(300)
    }

    @Test
    fun brake() {
        val ret = throttleBrake.brake(10)
        assertThat(ret).isEqualTo(SUCCESS)
        assertThat(throttleBrake.motionState)
            .isEqualTo(actionMemProp?.getter?.call(throttleBrake))
            .isEqualTo(Motion.BRAKING_STILL)
        assertThat(valueMemProp?.getter?.call(throttleBrake)).isEqualTo(10)
    }

    // parkingBrake
    @Test
    fun `client activate parking brake`() {
        val ret = throttleBrake.parkingBrake(100)
        assertThat(ret).isEqualTo(SUCCESS)
        assertThat(throttleBrake.motionState)
            .isEqualTo(actionMemProp?.getter?.call(throttleBrake))
            .isEqualTo(Motion.PARKING_BRAKE)
        assertThat(valueMemProp?.getter?.call(throttleBrake)).isEqualTo(100)
    }
    @Test
    fun `client deactivate parking brake from ImageView`() {
        val ret = throttleBrake.parkingBrake(0)
        assertThat(ret).isEqualTo(SUCCESS)
        assertThat(throttleBrake.motionState)
            .isEqualTo(actionMemProp?.getter?.call(throttleBrake))
            .isEqualTo(Motion.BRAKING_STILL)
        assertThat(valueMemProp?.getter?.call(throttleBrake)).isEqualTo(0)
    }

    // handbrake
    @Test
    fun `client activate handbrake`() {
        val ret = throttleBrake.handbrake(100)
        assertThat(ret).isEqualTo(SUCCESS)
        assertThat(throttleBrake.motionState)
            .isEqualTo(actionMemProp?.getter?.call(throttleBrake))
            .isEqualTo(Motion.HANDBRAKE)
        assertThat(valueMemProp?.getter?.call(throttleBrake)).isEqualTo(100)
    }
    @Test
    fun `client deactivate handbrake from ImageView`() {
        val ret = throttleBrake.handbrake(0)
        assertThat(ret).isEqualTo(SUCCESS)
        assertThat(throttleBrake.motionState)
            .isEqualTo(actionMemProp?.getter?.call(throttleBrake))
            .isEqualTo(Motion.NEUTRAL)
        assertThat(valueMemProp?.getter?.call(throttleBrake)).isEqualTo(0)
    }

    @Test
    fun setNeutral() {
        val ret = throttleBrake.setNeutral()
        assertThat(ret).isEqualTo(SUCCESS)
        assertThat(throttleBrake.motionState)
            .isEqualTo(actionMemProp?.getter?.call(throttleBrake))
            .isEqualTo(Motion.NEUTRAL)
        assertThat(valueMemProp?.getter?.call(throttleBrake)).isEqualTo(0)
    }

    @Test
    fun reset() {
        throttleBrake.reset()
        assertThat(throttleBrake.motionState)
            .isEqualTo(actionMemProp?.getter?.call(throttleBrake))
            .isEqualTo(Motion.NEUTRAL)
        assertThat(valueMemProp?.getter?.call(throttleBrake)).isEqualTo(0)
    }
}