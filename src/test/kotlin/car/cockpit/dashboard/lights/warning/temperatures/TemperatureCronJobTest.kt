package car.cockpit.dashboard.lights.warning.temperatures

import car.cockpit.engine.Engine
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.*
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
    @Autowired
    @Qualifier("Raspberry Pi Temperature Component")
    val raspberryPiTempComponent: Temperature,
    @Autowired
    @Qualifier("Shift Registers Temperature Component")
    val shiftRegistersTempComponent: Temperature,
    @Autowired
    @Qualifier("Motor Rear Right Temperature Component")
    val motorRearRightTempComponent: Temperature,
    @Autowired
    @Qualifier("Motor Rear Left Temperature Component")
    val motorRearLeftTempComponent: Temperature,
    @Autowired
    @Qualifier("Motor Front Right Temperature Component")
    val motorFrontRightTempComponent: Temperature,
    @Autowired
    @Qualifier("Motor Front Left Temperature Component")
    val motorFrontLeftTempComponent: Temperature,
    @Autowired
    @Qualifier("H-Bridge Rear Temperature Component")
    val hBridgeRearTempComponent: Temperature,
    @Autowired
    @Qualifier("H-Bridge Front Temperature Component")
    val hBridgeFrontTempComponent: Temperature,
    @Autowired
    @Qualifier("Batteries Temperature Component")
    val batteriesTempComponent: Temperature
) {

    @SpyBean
    private val task: TemperaturesCron? = null

    @Mock
    private var motorRearLeftTemp: MotorRearLeftTemperatureComponent? = null
    @Mock
    private var motorRearRightTemp: MotorRearRightTemperatureComponent? = null
    @Mock
    private var motorFrontLeftTemp: MotorFrontLeftTemperatureComponent? = null
    @Mock
    private var motorFrontRightTemp: MotorFrontRightTemperatureComponent? = null
    @Mock
    private var batteriesTemp: BatteriesTemperatureComponent? = null
    @Mock
    private var hBridgeRearTemp: HBridgeRearTemperatureComponent? = null
    @Mock
    private var hBridgeFrontTemp: HBridgeFrontTemperatureComponent? = null
    @Mock
    private var raspberryPiTemp: RaspberryPiTemperatureComponent? = null
    @Mock
    private var shiftRegistersTemp: ShiftRegistersTemperatureComponent? = null

    @BeforeEach
    internal fun setUp() {
        engineComponent.engineState = true
    }

    @Test
    fun testMocksCreation() {
        assertNotNull(motorRearLeftTemp)
        assertNotNull(motorRearRightTemp)
        assertNotNull(motorFrontLeftTemp)
        assertNotNull(motorFrontRightTemp)
        assertNotNull(batteriesTemp)
        assertNotNull(hBridgeRearTemp)
        assertNotNull(hBridgeFrontTemp)
        assertNotNull(raspberryPiTemp)
        assertNotNull(shiftRegistersTemp)
    }

    // checkTemps
    @Test
    fun `report normal temperature for rear left motor`() {
        val minMediumTemp = MotorRearLeftTemperatureComponent::class.memberProperties
            .firstOrNull { it.name =="minMediumTemp" }
        minMediumTemp?.isAccessible = true
        val temp = minMediumTemp?.getter?.call(motorRearLeftTempComponent) as Int

        `when`(motorRearLeftTemp?.value).thenReturn(temp-5)
        `when`(motorRearLeftTemp?.warning).thenReturn(WARNING_TYPE_NORMAL)

        task?.hardwareItems = arrayOf(motorRearLeftTemp!!)

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

        `when`(motorRearLeftTemp?.value).thenReturn(temp + 5)
        `when`(motorRearLeftTemp?.warning).thenReturn(WARNING_TYPE_MEDIUM)

        task?.hardwareItems = arrayOf(motorRearLeftTemp!!)

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

        `when`(motorRearLeftTemp?.value).thenReturn(temp + 5)
        `when`(motorRearLeftTemp?.warning).thenReturn(WARNING_TYPE_MEDIUM)

        task?.hardwareItems = arrayOf(motorRearLeftTemp!!)

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

        `when`(motorRearRightTemp?.value).thenReturn(temp-5)
        `when`(motorRearRightTemp?.warning).thenReturn(WARNING_TYPE_NORMAL)

        task?.hardwareItems = arrayOf(motorRearRightTemp!!)

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

        `when`(motorRearRightTemp?.value).thenReturn(temp + 5)
        `when`(motorRearRightTemp?.warning).thenReturn(WARNING_TYPE_MEDIUM)

        task?.hardwareItems = arrayOf(motorRearRightTemp!!)

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

        `when`(motorRearRightTemp?.value).thenReturn(temp + 5)
        `when`(motorRearRightTemp?.warning).thenReturn(WARNING_TYPE_MEDIUM)

        task?.hardwareItems = arrayOf(motorRearRightTemp!!)

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

        `when`(motorFrontLeftTemp?.value).thenReturn(temp-5)
        `when`(motorFrontLeftTemp?.warning).thenReturn(WARNING_TYPE_NORMAL)

        task?.hardwareItems = arrayOf(motorFrontLeftTemp!!)

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

        `when`(motorFrontLeftTemp?.value).thenReturn(temp + 5)
        `when`(motorFrontLeftTemp?.warning).thenReturn(WARNING_TYPE_MEDIUM)

        task?.hardwareItems = arrayOf(motorFrontLeftTemp!!)

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

        `when`(motorFrontLeftTemp?.value).thenReturn(temp + 5)
        `when`(motorFrontLeftTemp?.warning).thenReturn(WARNING_TYPE_MEDIUM)

        task?.hardwareItems = arrayOf(motorFrontLeftTemp!!)

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

        `when`(motorFrontRightTemp?.value).thenReturn(temp-5)
        `when`(motorFrontRightTemp?.warning).thenReturn(WARNING_TYPE_NORMAL)

        task?.hardwareItems = arrayOf(motorFrontRightTemp!!)

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

        `when`(motorFrontRightTemp?.value).thenReturn(temp + 5)
        `when`(motorFrontRightTemp?.warning).thenReturn(WARNING_TYPE_MEDIUM)

        task?.hardwareItems = arrayOf(motorFrontRightTemp!!)

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

        `when`(motorFrontRightTemp?.value).thenReturn(temp + 5)
        `when`(motorFrontRightTemp?.warning).thenReturn(WARNING_TYPE_MEDIUM)

        task?.hardwareItems = arrayOf(motorFrontRightTemp!!)

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

        `when`(hBridgeRearTemp?.value).thenReturn(temp-5)
        `when`(hBridgeRearTemp?.warning).thenReturn(WARNING_TYPE_NORMAL)

        task?.hardwareItems = arrayOf(hBridgeRearTemp!!)

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

        `when`(hBridgeRearTemp?.value).thenReturn(temp + 5)
        `when`(hBridgeRearTemp?.warning).thenReturn(WARNING_TYPE_MEDIUM)

        task?.hardwareItems = arrayOf(hBridgeRearTemp!!)

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

        `when`(hBridgeRearTemp?.value).thenReturn(temp + 5)
        `when`(hBridgeRearTemp?.warning).thenReturn(WARNING_TYPE_MEDIUM)

        task?.hardwareItems = arrayOf(hBridgeRearTemp!!)

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

        `when`(hBridgeFrontTemp?.value).thenReturn(temp-5)
        `when`(hBridgeFrontTemp?.warning).thenReturn(WARNING_TYPE_NORMAL)

        task?.hardwareItems = arrayOf(hBridgeFrontTemp!!)

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

        `when`(hBridgeFrontTemp?.value).thenReturn(temp + 5)
        `when`(hBridgeFrontTemp?.warning).thenReturn(WARNING_TYPE_MEDIUM)

        task?.hardwareItems = arrayOf(hBridgeFrontTemp!!)

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

        `when`(hBridgeFrontTemp?.value).thenReturn(temp + 5)
        `when`(hBridgeFrontTemp?.warning).thenReturn(WARNING_TYPE_MEDIUM)

        task?.hardwareItems = arrayOf(hBridgeFrontTemp!!)

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

        `when`(raspberryPiTemp?.value).thenReturn(temp-5)
        `when`(raspberryPiTemp?.warning).thenReturn(WARNING_TYPE_NORMAL)

        task?.hardwareItems = arrayOf(raspberryPiTemp!!)

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

        `when`(raspberryPiTemp?.value).thenReturn(temp + 5)
        `when`(raspberryPiTemp?.warning).thenReturn(WARNING_TYPE_MEDIUM)

        task?.hardwareItems = arrayOf(raspberryPiTemp!!)

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

        `when`(raspberryPiTemp?.value).thenReturn(temp + 5)
        `when`(raspberryPiTemp?.warning).thenReturn(WARNING_TYPE_MEDIUM)

        task?.hardwareItems = arrayOf(raspberryPiTemp!!)

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

        `when`(batteriesTemp?.value).thenReturn(temp-5)
        `when`(batteriesTemp?.warning).thenReturn(WARNING_TYPE_NORMAL)

        task?.hardwareItems = arrayOf(batteriesTemp!!)

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

        `when`(batteriesTemp?.value).thenReturn(temp + 5)
        `when`(batteriesTemp?.warning).thenReturn(WARNING_TYPE_MEDIUM)

        task?.hardwareItems = arrayOf(batteriesTemp!!)

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

        `when`(batteriesTemp?.value).thenReturn(temp + 5)
        `when`(batteriesTemp?.warning).thenReturn(WARNING_TYPE_MEDIUM)

        task?.hardwareItems = arrayOf(batteriesTemp!!)

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

        `when`(shiftRegistersTemp?.value).thenReturn(temp-5)
        `when`(shiftRegistersTemp?.warning).thenReturn(WARNING_TYPE_NORMAL)

        task?.hardwareItems = arrayOf(shiftRegistersTemp!!)

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

        `when`(shiftRegistersTemp?.value).thenReturn(temp + 5)
        `when`(shiftRegistersTemp?.warning).thenReturn(WARNING_TYPE_MEDIUM)

        task?.hardwareItems = arrayOf(shiftRegistersTemp!!)

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

        `when`(shiftRegistersTemp?.value).thenReturn(temp + 5)
        `when`(shiftRegistersTemp?.warning).thenReturn(WARNING_TYPE_MEDIUM)

        task?.hardwareItems = arrayOf(shiftRegistersTemp!!)

        task?.checkTemps()
        assertThat(task?.reportedTempWarnings?.get(0)).isEqualTo(WARNING_TYPE_MEDIUM)
        assertThat(task?.primaryTempValues?.get(0)).isEqualTo(temp + 5)
    }
}