package car.cockpit.dashboard.lights.warning.temperatures

import car.cockpit.engine.Engine
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.Spy
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.SpyBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible



@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class TemperatureCronJobTest(
    @Autowired
    val engineComponent: Engine,
    @Spy
    @Autowired
    @Qualifier("Raspberry Pi Temperature Component")
    val raspberryPiTempComponent: Temperature,
    @Spy
    @Autowired
    @Qualifier("Shift Registers Temperature Component")
    val shiftRegistersTempComponent: Temperature,
    @Spy
    @Autowired
    @Qualifier("Motor Rear Right Temperature Component")
    val motorRearRightTempComponent: Temperature,
    @Spy
    @Autowired
    @Qualifier("Motor Rear Left Temperature Component")
    val motorRearLeftTempComponent: Temperature,
    @Spy
    @Autowired
    @Qualifier("Motor Front Right Temperature Component")
    val motorFrontRightTempComponent: Temperature,
    @Spy
    @Autowired
    @Qualifier("Motor Front Left Temperature Component")
    val motorFrontLeftTempComponent: Temperature,
    @Spy
    @Autowired
    @Qualifier("H-Bridge Rear Temperature Component")
    val hBridgeRearTempComponent: Temperature,
    @Spy
    @Autowired
    @Qualifier("H-Bridge Front Temperature Component")
    val hBridgeFrontTempComponent: Temperature,
    @Spy
    @Autowired
    @Qualifier("Batteries Temperature Component")
    val batteriesTempComponent: Temperature
) {

    @SpyBean
    private val task: TemperaturesCron? = null

    @BeforeEach
    internal fun setUp() {
        engineComponent.engineState = true
    }

    @Test
    fun testMocksCreation() {
        assertNotNull(motorRearLeftTempComponent)
        assertNotNull(motorRearRightTempComponent)
        assertNotNull(motorFrontLeftTempComponent)
        assertNotNull(motorFrontRightTempComponent)
        assertNotNull(batteriesTempComponent)
        assertNotNull(hBridgeRearTempComponent)
        assertNotNull(hBridgeFrontTempComponent)
        assertNotNull(raspberryPiTempComponent)
        assertNotNull(shiftRegistersTempComponent)
    }

    // checkTemps
    @Test
    fun `report normal temperature for rear left motor`() {
        val minMediumTemp = MotorRearLeftTemperatureComponent::class.memberProperties
            .firstOrNull { it.name =="minMediumTemp" }
        minMediumTemp?.isAccessible = true
        val temp = minMediumTemp?.getter?.call(motorRearLeftTempComponent) as Int

        `when`(motorRearLeftTempComponent.value).thenReturn(temp-5)
        `when`(motorRearLeftTempComponent.warning).thenReturn(WARNING_TYPE_NORMAL)

        task?.hardwareItems = arrayOf(motorRearLeftTempComponent)

        task?.checkTemps()
        assertThat(task?.reportedTempWarnings?.get(0)).isEqualTo(WARNING_TYPE_NORMAL)
        assertThat(task?.primaryTempValues?.get(0)).isEqualTo(temp-5)
    }
    @Test
    fun `report medium temperature for rear left motor`() {
        val minMediumTemp = MotorRearLeftTemperatureComponent::class.memberProperties
            .firstOrNull { it.name =="minMediumTemp" }
        minMediumTemp?.isAccessible = true
        val temp = minMediumTemp?.getter?.call(motorRearLeftTempComponent) as Int

        `when`(motorRearLeftTempComponent.value).thenReturn(temp + 5)
        `when`(motorRearLeftTempComponent.warning).thenReturn(WARNING_TYPE_MEDIUM)

        task?.hardwareItems = arrayOf(motorRearLeftTempComponent)

        task?.checkTemps()
        assertThat(task?.reportedTempWarnings?.get(0)).isEqualTo(WARNING_TYPE_MEDIUM)
        assertThat(task?.primaryTempValues?.get(0)).isEqualTo(temp + 5)
    }
    @Test
    fun `report high temperature for rear left motor`() {
        val maxMediumTemp = MotorRearLeftTemperatureComponent::class.memberProperties
            .firstOrNull { it.name =="maxMediumTemp" }
        maxMediumTemp?.isAccessible = true
        val temp = maxMediumTemp?.getter?.call(motorRearLeftTempComponent) as Int

        `when`(motorRearLeftTempComponent.value).thenReturn(temp + 5)
        `when`(motorRearLeftTempComponent.warning).thenReturn(WARNING_TYPE_MEDIUM)

        task?.hardwareItems = arrayOf(motorRearLeftTempComponent)

        task?.checkTemps()
        assertThat(task?.reportedTempWarnings?.get(0)).isEqualTo(WARNING_TYPE_MEDIUM)
        assertThat(task?.primaryTempValues?.get(0)).isEqualTo(temp + 5)
    }

    @Test
    fun `report normal temperature for rear right motor`() {
        val minMediumTemp = MotorRearRightTemperatureComponent::class.memberProperties
            .firstOrNull { it.name =="minMediumTemp" }
        minMediumTemp?.isAccessible = true
        val temp = minMediumTemp?.getter?.call(motorRearRightTempComponent) as Int

        `when`(motorRearRightTempComponent.value).thenReturn(temp-5)
        `when`(motorRearRightTempComponent.warning).thenReturn(WARNING_TYPE_NORMAL)

        task?.hardwareItems = arrayOf(motorRearRightTempComponent)

        task?.checkTemps()
        assertThat(task?.reportedTempWarnings?.get(0)).isEqualTo(WARNING_TYPE_NORMAL)
        assertThat(task?.primaryTempValues?.get(0)).isEqualTo(temp-5)
    }
    @Test
    fun `report medium temperature for rear right motor`() {
        val maxMediumTemp = MotorRearRightTemperatureComponent::class.memberProperties
            .firstOrNull { it.name =="maxMediumTemp" }
        maxMediumTemp?.isAccessible = true
        val temp = maxMediumTemp?.getter?.call(motorRearRightTempComponent) as Int

        `when`(motorRearRightTempComponent.value).thenReturn(temp + 5)
        `when`(motorRearRightTempComponent.warning).thenReturn(WARNING_TYPE_MEDIUM)

        task?.hardwareItems = arrayOf(motorRearRightTempComponent)

        task?.checkTemps()
        assertThat(task?.reportedTempWarnings?.get(0)).isEqualTo(WARNING_TYPE_MEDIUM)
        assertThat(task?.primaryTempValues?.get(0)).isEqualTo(temp + 5)
    }
    @Test
    fun `report high temperature for rear right motor`() {
        val maxMediumTemp = MotorRearRightTemperatureComponent::class.memberProperties
            .firstOrNull { it.name =="maxMediumTemp" }
        maxMediumTemp?.isAccessible = true
        val temp = maxMediumTemp?.getter?.call(motorRearRightTempComponent) as Int

        `when`(motorRearRightTempComponent.value).thenReturn(temp + 5)
        `when`(motorRearRightTempComponent.warning).thenReturn(WARNING_TYPE_MEDIUM)

        task?.hardwareItems = arrayOf(motorRearRightTempComponent)

        task?.checkTemps()
        assertThat(task?.reportedTempWarnings?.get(0)).isEqualTo(WARNING_TYPE_MEDIUM)
        assertThat(task?.primaryTempValues?.get(0)).isEqualTo(temp + 5)
    }
    @Test
    fun `report normal temperature for front left motor`() {
        val minMediumTemp = MotorFrontLeftTemperatureComponent::class.memberProperties
            .firstOrNull { it.name =="minMediumTemp" }
        minMediumTemp?.isAccessible = true
        val temp = minMediumTemp?.getter?.call(motorFrontLeftTempComponent) as Int

        `when`(motorFrontLeftTempComponent.value).thenReturn(temp-5)
        `when`(motorFrontLeftTempComponent.warning).thenReturn(WARNING_TYPE_NORMAL)

        task?.hardwareItems = arrayOf(motorFrontLeftTempComponent)

        task?.checkTemps()
        assertThat(task?.reportedTempWarnings?.get(0)).isEqualTo(WARNING_TYPE_NORMAL)
        assertThat(task?.primaryTempValues?.get(0)).isEqualTo(temp-5)
    }
    @Test
    fun `report medium temperature for front left motor`() {
        val minMediumTemp = MotorFrontLeftTemperatureComponent::class.memberProperties
            .firstOrNull { it.name =="minMediumTemp" }
        minMediumTemp?.isAccessible = true
        val temp = minMediumTemp?.getter?.call(motorFrontLeftTempComponent) as Int

        `when`(motorFrontLeftTempComponent.value).thenReturn(temp + 5)
        `when`(motorFrontLeftTempComponent.warning).thenReturn(WARNING_TYPE_MEDIUM)

        task?.hardwareItems = arrayOf(motorFrontLeftTempComponent)

        task?.checkTemps()
        assertThat(task?.reportedTempWarnings?.get(0)).isEqualTo(WARNING_TYPE_MEDIUM)
        assertThat(task?.primaryTempValues?.get(0)).isEqualTo(temp + 5)
    }
    @Test
    fun `report high temperature for front left motor`() {
        val maxMediumTemp = MotorFrontLeftTemperatureComponent::class.memberProperties
            .firstOrNull { it.name =="maxMediumTemp" }
        maxMediumTemp?.isAccessible = true
        val temp = maxMediumTemp?.getter?.call(motorFrontLeftTempComponent) as Int

        `when`(motorFrontLeftTempComponent.value).thenReturn(temp + 5)
        `when`(motorFrontLeftTempComponent.warning).thenReturn(WARNING_TYPE_MEDIUM)

        task?.hardwareItems = arrayOf(motorFrontLeftTempComponent)

        task?.checkTemps()
        assertThat(task?.reportedTempWarnings?.get(0)).isEqualTo(WARNING_TYPE_MEDIUM)
        assertThat(task?.primaryTempValues?.get(0)).isEqualTo(temp + 5)
    }

    @Test
    fun `report normal temperature for front right motor`() {
        val minMediumTemp = MotorFrontRightTemperatureComponent::class.memberProperties
            .firstOrNull { it.name =="minMediumTemp" }
        minMediumTemp?.isAccessible = true
        val temp = minMediumTemp?.getter?.call(motorFrontRightTempComponent) as Int

        `when`(motorFrontRightTempComponent.value).thenReturn(temp-5)
        `when`(motorFrontRightTempComponent.warning).thenReturn(WARNING_TYPE_NORMAL)

        task?.hardwareItems = arrayOf(motorFrontRightTempComponent)

        task?.checkTemps()
        assertThat(task?.reportedTempWarnings?.get(0)).isEqualTo(WARNING_TYPE_NORMAL)
        assertThat(task?.primaryTempValues?.get(0)).isEqualTo(temp-5)
    }
    @Test
    fun `report medium temperature for front right motor`() {
        val minMediumTemp = MotorFrontRightTemperatureComponent::class.memberProperties
            .firstOrNull { it.name =="minMediumTemp" }
        minMediumTemp?.isAccessible = true
        val temp = minMediumTemp?.getter?.call(motorFrontRightTempComponent) as Int

        `when`(motorFrontRightTempComponent.value).thenReturn(temp + 5)
        `when`(motorFrontRightTempComponent.warning).thenReturn(WARNING_TYPE_MEDIUM)

        task?.hardwareItems = arrayOf(motorFrontRightTempComponent)

        task?.checkTemps()
        assertThat(task?.reportedTempWarnings?.get(0)).isEqualTo(WARNING_TYPE_MEDIUM)
        assertThat(task?.primaryTempValues?.get(0)).isEqualTo(temp + 5)
    }
    @Test
    fun `report high temperature for front right motor`() {
        val maxMediumTemp = MotorFrontRightTemperatureComponent::class.memberProperties
            .firstOrNull { it.name =="maxMediumTemp" }
        maxMediumTemp?.isAccessible = true
        val temp = maxMediumTemp?.getter?.call(motorFrontRightTempComponent) as Int

        `when`(motorFrontRightTempComponent.value).thenReturn(temp + 5)
        `when`(motorFrontRightTempComponent.warning).thenReturn(WARNING_TYPE_MEDIUM)

        task?.hardwareItems = arrayOf(motorFrontRightTempComponent)

        task?.checkTemps()
        assertThat(task?.reportedTempWarnings?.get(0)).isEqualTo(WARNING_TYPE_MEDIUM)
        assertThat(task?.primaryTempValues?.get(0)).isEqualTo(temp + 5)
    }

    @Test
    fun `report normal temperature for rear h bridge`() {
        val minMediumTemp = HBridgeRearTemperatureComponent::class.memberProperties
            .firstOrNull { it.name =="minMediumTemp" }
        minMediumTemp?.isAccessible = true
        val temp = minMediumTemp?.getter?.call(hBridgeRearTempComponent) as Int

        `when`(hBridgeRearTempComponent.value).thenReturn(temp-5)
        `when`(hBridgeRearTempComponent.warning).thenReturn(WARNING_TYPE_NORMAL)

        task?.hardwareItems = arrayOf(hBridgeRearTempComponent)

        task?.checkTemps()
        assertThat(task?.reportedTempWarnings?.get(0)).isEqualTo(WARNING_TYPE_NORMAL)
        assertThat(task?.primaryTempValues?.get(0)).isEqualTo(temp-5)
    }
    @Test
    fun `report medium temperature for rear h bridge`() {
        val minMediumTemp = HBridgeRearTemperatureComponent::class.memberProperties
            .firstOrNull { it.name =="minMediumTemp" }
        minMediumTemp?.isAccessible = true
        val temp = minMediumTemp?.getter?.call(hBridgeRearTempComponent) as Int

        `when`(hBridgeRearTempComponent.value).thenReturn(temp + 5)
        `when`(hBridgeRearTempComponent.warning).thenReturn(WARNING_TYPE_MEDIUM)

        task?.hardwareItems = arrayOf(hBridgeRearTempComponent)

        task?.checkTemps()
        assertThat(task?.reportedTempWarnings?.get(0)).isEqualTo(WARNING_TYPE_MEDIUM)
        assertThat(task?.primaryTempValues?.get(0)).isEqualTo(temp + 5)
    }
    @Test
    fun `report high temperature for rear h bridge`() {
        val maxMediumTemp = HBridgeRearTemperatureComponent::class.memberProperties
            .firstOrNull { it.name =="maxMediumTemp" }
        maxMediumTemp?.isAccessible = true
        val temp = maxMediumTemp?.getter?.call(hBridgeRearTempComponent) as Int

        `when`(hBridgeRearTempComponent.value).thenReturn(temp + 5)
        `when`(hBridgeRearTempComponent.warning).thenReturn(WARNING_TYPE_MEDIUM)

        task?.hardwareItems = arrayOf(hBridgeRearTempComponent)

        task?.checkTemps()
        assertThat(task?.reportedTempWarnings?.get(0)).isEqualTo(WARNING_TYPE_MEDIUM)
        assertThat(task?.primaryTempValues?.get(0)).isEqualTo(temp + 5)
    }

    @Test
    fun `report normal temperature for front h bridge`() {
        val minMediumTemp = HBridgeFrontTemperatureComponent::class.memberProperties
            .firstOrNull { it.name =="minMediumTemp" }
        minMediumTemp?.isAccessible = true
        val temp = minMediumTemp?.getter?.call(hBridgeFrontTempComponent) as Int

        `when`(hBridgeFrontTempComponent.value).thenReturn(temp-5)
        `when`(hBridgeFrontTempComponent.warning).thenReturn(WARNING_TYPE_NORMAL)

        task?.hardwareItems = arrayOf(hBridgeFrontTempComponent)

        task?.checkTemps()
        assertThat(task?.reportedTempWarnings?.get(0)).isEqualTo(WARNING_TYPE_NORMAL)
        assertThat(task?.primaryTempValues?.get(0)).isEqualTo(temp-5)
    }
    @Test
    fun `report medium temperature for front h bridge`() {
        val minMediumTemp = HBridgeFrontTemperatureComponent::class.memberProperties
            .firstOrNull { it.name =="minMediumTemp" }
        minMediumTemp?.isAccessible = true
        val temp = minMediumTemp?.getter?.call(hBridgeFrontTempComponent) as Int

        `when`(hBridgeFrontTempComponent.value).thenReturn(temp + 5)
        `when`(hBridgeFrontTempComponent.warning).thenReturn(WARNING_TYPE_MEDIUM)

        task?.hardwareItems = arrayOf(hBridgeFrontTempComponent)

        task?.checkTemps()
        assertThat(task?.reportedTempWarnings?.get(0)).isEqualTo(WARNING_TYPE_MEDIUM)
        assertThat(task?.primaryTempValues?.get(0)).isEqualTo(temp + 5)
    }
    @Test
    fun `report high temperature for front h bridge`() {
        val maxMediumTemp = HBridgeFrontTemperatureComponent::class.memberProperties
            .firstOrNull { it.name =="maxMediumTemp" }
        maxMediumTemp?.isAccessible = true
        val temp = maxMediumTemp?.getter?.call(hBridgeFrontTempComponent) as Int

        `when`(hBridgeFrontTempComponent.value).thenReturn(temp + 5)
        `when`(hBridgeFrontTempComponent.warning).thenReturn(WARNING_TYPE_MEDIUM)

        task?.hardwareItems = arrayOf(hBridgeFrontTempComponent)

        task?.checkTemps()
        assertThat(task?.reportedTempWarnings?.get(0)).isEqualTo(WARNING_TYPE_MEDIUM)
        assertThat(task?.primaryTempValues?.get(0)).isEqualTo(temp + 5)
    }

    @Test
    fun `report normal temperature for raspberry pi`() {
        val minMediumTemp = RaspberryPiTemperatureComponent::class.memberProperties
            .firstOrNull { it.name =="minMediumTemp" }
        minMediumTemp?.isAccessible = true
        val temp = minMediumTemp?.getter?.call(raspberryPiTempComponent) as Int

        `when`(raspberryPiTempComponent.value).thenReturn(temp-5)
        `when`(raspberryPiTempComponent.warning).thenReturn(WARNING_TYPE_NORMAL)

        task?.hardwareItems = arrayOf(raspberryPiTempComponent)

        task?.checkTemps()
        assertThat(task?.reportedTempWarnings?.get(0)).isEqualTo(WARNING_TYPE_NORMAL)
        assertThat(task?.primaryTempValues?.get(0)).isEqualTo(temp-5)
    }
    @Test
    fun `report medium temperature for raspberry pi`() {
        val minMediumTemp = RaspberryPiTemperatureComponent::class.memberProperties
            .firstOrNull { it.name =="minMediumTemp" }
        minMediumTemp?.isAccessible = true
        val temp = minMediumTemp?.getter?.call(raspberryPiTempComponent) as Int

        `when`(raspberryPiTempComponent.value).thenReturn(temp + 5)
        `when`(raspberryPiTempComponent.warning).thenReturn(WARNING_TYPE_MEDIUM)

        task?.hardwareItems = arrayOf(raspberryPiTempComponent)

        task?.checkTemps()
        assertThat(task?.reportedTempWarnings?.get(0)).isEqualTo(WARNING_TYPE_MEDIUM)
        assertThat(task?.primaryTempValues?.get(0)).isEqualTo(temp + 5)
    }
    @Test
    fun `report high temperature for raspberry pi`() {
        val maxMediumTemp = RaspberryPiTemperatureComponent::class.memberProperties
            .firstOrNull { it.name =="maxMediumTemp" }
        maxMediumTemp?.isAccessible = true
        val temp = maxMediumTemp?.getter?.call(raspberryPiTempComponent) as Int

        `when`(raspberryPiTempComponent.value).thenReturn(temp + 5)
        `when`(raspberryPiTempComponent.warning).thenReturn(WARNING_TYPE_MEDIUM)

        task?.hardwareItems = arrayOf(raspberryPiTempComponent)

        task?.checkTemps()
        assertThat(task?.reportedTempWarnings?.get(0)).isEqualTo(WARNING_TYPE_MEDIUM)
        assertThat(task?.primaryTempValues?.get(0)).isEqualTo(temp + 5)
    }

    @Test
    fun `report normal temperature for batteries`() {
        val minMediumTemp = BatteriesTemperatureComponent::class.memberProperties
            .firstOrNull { it.name =="minMediumTemp" }
        minMediumTemp?.isAccessible = true
        val temp = minMediumTemp?.getter?.call(batteriesTempComponent) as Int

        `when`(batteriesTempComponent.value).thenReturn(temp-5)
        `when`(batteriesTempComponent.warning).thenReturn(WARNING_TYPE_NORMAL)

        task?.hardwareItems = arrayOf(batteriesTempComponent)

        task?.checkTemps()
        assertThat(task?.reportedTempWarnings?.get(0)).isEqualTo(WARNING_TYPE_NORMAL)
        assertThat(task?.primaryTempValues?.get(0)).isEqualTo(temp-5)
    }
    @Test
    fun `report medium temperature for batteries`() {
        val minMediumTemp = BatteriesTemperatureComponent::class.memberProperties
            .firstOrNull { it.name =="minMediumTemp" }
        minMediumTemp?.isAccessible = true
        val temp = minMediumTemp?.getter?.call(batteriesTempComponent) as Int

        `when`(batteriesTempComponent.value).thenReturn(temp + 5)
        `when`(batteriesTempComponent.warning).thenReturn(WARNING_TYPE_MEDIUM)

        task?.hardwareItems = arrayOf(batteriesTempComponent)

        task?.checkTemps()
        assertThat(task?.reportedTempWarnings?.get(0)).isEqualTo(WARNING_TYPE_MEDIUM)
        assertThat(task?.primaryTempValues?.get(0)).isEqualTo(temp + 5)
    }
    @Test
    fun `report high temperature for batteries`() {
        val maxMediumTemp = BatteriesTemperatureComponent::class.memberProperties
            .firstOrNull { it.name =="maxMediumTemp" }
        maxMediumTemp?.isAccessible = true
        val temp = maxMediumTemp?.getter?.call(batteriesTempComponent) as Int

        `when`(batteriesTempComponent.value).thenReturn(temp + 5)
        `when`(batteriesTempComponent.warning).thenReturn(WARNING_TYPE_MEDIUM)

        task?.hardwareItems = arrayOf(batteriesTempComponent)

        task?.checkTemps()
        assertThat(task?.reportedTempWarnings?.get(0)).isEqualTo(WARNING_TYPE_MEDIUM)
        assertThat(task?.primaryTempValues?.get(0)).isEqualTo(temp + 5)
    }

    @Test
    fun `report normal temperature for shift registers`() {
        val minMediumTemp = ShiftRegistersTemperatureComponent::class.memberProperties
            .firstOrNull { it.name =="minMediumTemp" }
        minMediumTemp?.isAccessible = true
        val temp = minMediumTemp?.getter?.call(shiftRegistersTempComponent) as Int

        `when`(shiftRegistersTempComponent.value).thenReturn(temp-5)
        `when`(shiftRegistersTempComponent.warning).thenReturn(WARNING_TYPE_NORMAL)

        task?.hardwareItems = arrayOf(shiftRegistersTempComponent)

        task?.checkTemps()
        assertThat(task?.reportedTempWarnings?.get(0)).isEqualTo(WARNING_TYPE_NORMAL)
        assertThat(task?.primaryTempValues?.get(0)).isEqualTo(temp-5)
    }
    @Test
    fun `report medium temperature for shift registers`() {
        val minMediumTemp = ShiftRegistersTemperatureComponent::class.memberProperties
            .firstOrNull { it.name =="minMediumTemp" }
        minMediumTemp?.isAccessible = true
        val temp = minMediumTemp?.getter?.call(shiftRegistersTempComponent) as Int

        `when`(shiftRegistersTempComponent.value).thenReturn(temp + 5)
        `when`(shiftRegistersTempComponent.warning).thenReturn(WARNING_TYPE_MEDIUM)

        task?.hardwareItems = arrayOf(shiftRegistersTempComponent)

        task?.checkTemps()
        assertThat(task?.reportedTempWarnings?.get(0)).isEqualTo(WARNING_TYPE_MEDIUM)
        assertThat(task?.primaryTempValues?.get(0)).isEqualTo(temp + 5)
    }
    @Test
    fun `report high temperature for shift registers`() {
        val maxMediumTemp = ShiftRegistersTemperatureComponent::class.memberProperties
            .firstOrNull { it.name =="maxMediumTemp" }
        maxMediumTemp?.isAccessible = true
        val temp = maxMediumTemp?.getter?.call(shiftRegistersTempComponent) as Int

        `when`(shiftRegistersTempComponent.value).thenReturn(temp + 5)
        `when`(shiftRegistersTempComponent.warning).thenReturn(WARNING_TYPE_MEDIUM)

        task?.hardwareItems = arrayOf(shiftRegistersTempComponent)

        task?.checkTemps()
        assertThat(task?.reportedTempWarnings?.get(0)).isEqualTo(WARNING_TYPE_MEDIUM)
        assertThat(task?.primaryTempValues?.get(0)).isEqualTo(temp + 5)
    }
}