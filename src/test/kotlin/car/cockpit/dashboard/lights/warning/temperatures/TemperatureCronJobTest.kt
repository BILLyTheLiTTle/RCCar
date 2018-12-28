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
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.SpyBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible



@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class TemperatureCronJobTest(@Autowired private val engineComponent: Engine) {

    @SpyBean
    private val task: TemperaturesCronJob? = null

    @Mock
    private var motorRearLeftTemp: MotorRearLeftTemperature? = null
    @Mock
    private var motorRearRightTemp: MotorRearRightTemperature? = null
    @Mock
    private var motorFrontLeftTemp: MotorFrontLeftTemperature? = null
    @Mock
    private var motorFrontRightTemp: MotorFrontRightTemperature? = null
    @Mock
    private var batteriesTemp: BatteriesTemperature? = null
    @Mock
    private var hBridgeRearTemp: HBridgeRearTemperature? = null
    @Mock
    private var hBridgeFrontTemp: HBridgeFrontTemperature? = null
    @Mock
    private var raspberryPiTemp: RaspberryPiTemperature? = null
    @Mock
    private var shiftRegistersTemp: ShiftRegistersTemperature? = null

    @BeforeEach
    internal fun setUp() {
        engineComponent.engineState = true
    }

    @Test
    fun testMocksCreation() {
        assertNotNull(motorRearLeftTemp)
    }

    // checkTemps
    @Test
    fun `report normal temperature for rear left motor`() {
        val minMediumTemp = MotorRearLeftTemperature::class.memberProperties
            .firstOrNull { it.name =="minMediumTemp" }
        minMediumTemp?.isAccessible = true
        val temp = minMediumTemp?.getter?.call(MotorRearLeftTemperature) as Int

        `when`(motorRearLeftTemp?.value).thenReturn(temp-5)
        `when`(motorRearLeftTemp?.warning).thenReturn(Temperature.WARNING_TYPE_NORMAL)

        task?.hardwareItems = arrayOf(motorRearLeftTemp!!)

        task?.checkTemps()
        assertThat(task?.reportedTempWarnings?.get(0)).isEqualTo(Temperature.WARNING_TYPE_NORMAL)
        assertThat(task?.primaryTempValues?.get(0)).isEqualTo(temp-5)
    }
    @Test
    fun `report medium temperature for rear left motor`() {
        val minMediumTemp = MotorRearLeftTemperature::class.memberProperties
            .firstOrNull { it.name =="minMediumTemp" }
        minMediumTemp?.isAccessible = true
        val temp = minMediumTemp?.getter?.call(MotorRearLeftTemperature) as Int

        `when`(motorRearLeftTemp?.value).thenReturn(temp + 5)
        `when`(motorRearLeftTemp?.warning).thenReturn(Temperature.WARNING_TYPE_MEDIUM)

        task?.hardwareItems = arrayOf(motorRearLeftTemp!!)

        task?.checkTemps()
        assertThat(task?.reportedTempWarnings?.get(0)).isEqualTo(Temperature.WARNING_TYPE_MEDIUM)
        assertThat(task?.primaryTempValues?.get(0)).isEqualTo(temp + 5)
    }
    @Test
    fun `report high temperature for rear left motor`() {
        val maxMediumTemp = MotorRearLeftTemperature::class.memberProperties
            .firstOrNull { it.name =="maxMediumTemp" }
        maxMediumTemp?.isAccessible = true
        val temp = maxMediumTemp?.getter?.call(MotorRearLeftTemperature) as Int

        `when`(motorRearLeftTemp?.value).thenReturn(temp + 5)
        `when`(motorRearLeftTemp?.warning).thenReturn(Temperature.WARNING_TYPE_MEDIUM)

        task?.hardwareItems = arrayOf(motorRearLeftTemp!!)

        task?.checkTemps()
        assertThat(task?.reportedTempWarnings?.get(0)).isEqualTo(Temperature.WARNING_TYPE_MEDIUM)
        assertThat(task?.primaryTempValues?.get(0)).isEqualTo(temp + 5)
    }

    @Test
    fun `report normal temperature for rear right motor`() {
        val minMediumTemp = MotorRearRightTemperature::class.memberProperties
            .firstOrNull { it.name =="minMediumTemp" }
        minMediumTemp?.isAccessible = true
        val temp = minMediumTemp?.getter?.call(MotorRearRightTemperature) as Int

        `when`(motorRearRightTemp?.value).thenReturn(temp-5)
        `when`(motorRearRightTemp?.warning).thenReturn(Temperature.WARNING_TYPE_NORMAL)

        task?.hardwareItems = arrayOf(motorRearRightTemp!!)

        task?.checkTemps()
        assertThat(task?.reportedTempWarnings?.get(0)).isEqualTo(Temperature.WARNING_TYPE_NORMAL)
        assertThat(task?.primaryTempValues?.get(0)).isEqualTo(temp-5)
    }
    @Test
    fun `report medium temperature for rear right motor`() {
        val maxMediumTemp = MotorRearRightTemperature::class.memberProperties
            .firstOrNull { it.name =="maxMediumTemp" }
        maxMediumTemp?.isAccessible = true
        val temp = maxMediumTemp?.getter?.call(MotorRearRightTemperature) as Int

        `when`(motorRearRightTemp?.value).thenReturn(temp + 5)
        `when`(motorRearRightTemp?.warning).thenReturn(Temperature.WARNING_TYPE_MEDIUM)

        task?.hardwareItems = arrayOf(motorRearRightTemp!!)

        task?.checkTemps()
        assertThat(task?.reportedTempWarnings?.get(0)).isEqualTo(Temperature.WARNING_TYPE_MEDIUM)
        assertThat(task?.primaryTempValues?.get(0)).isEqualTo(temp + 5)
    }
    @Test
    fun `report high temperature for rear right motor`() {
        val maxMediumTemp = MotorRearRightTemperature::class.memberProperties
            .firstOrNull { it.name =="maxMediumTemp" }
        maxMediumTemp?.isAccessible = true
        val temp = maxMediumTemp?.getter?.call(MotorRearRightTemperature) as Int

        `when`(motorRearRightTemp?.value).thenReturn(temp + 5)
        `when`(motorRearRightTemp?.warning).thenReturn(Temperature.WARNING_TYPE_MEDIUM)

        task?.hardwareItems = arrayOf(motorRearRightTemp!!)

        task?.checkTemps()
        assertThat(task?.reportedTempWarnings?.get(0)).isEqualTo(Temperature.WARNING_TYPE_MEDIUM)
        assertThat(task?.primaryTempValues?.get(0)).isEqualTo(temp + 5)
    }
    @Test
    fun `report normal temperature for front left motor`() {
        val minMediumTemp = MotorFrontLeftTemperature::class.memberProperties
            .firstOrNull { it.name =="minMediumTemp" }
        minMediumTemp?.isAccessible = true
        val temp = minMediumTemp?.getter?.call(MotorFrontLeftTemperature) as Int

        `when`(motorFrontLeftTemp?.value).thenReturn(temp-5)
        `when`(motorFrontLeftTemp?.warning).thenReturn(Temperature.WARNING_TYPE_NORMAL)

        task?.hardwareItems = arrayOf(motorFrontLeftTemp!!)

        task?.checkTemps()
        assertThat(task?.reportedTempWarnings?.get(0)).isEqualTo(Temperature.WARNING_TYPE_NORMAL)
        assertThat(task?.primaryTempValues?.get(0)).isEqualTo(temp-5)
    }
    @Test
    fun `report medium temperature for front left motor`() {
        val minMediumTemp = MotorFrontLeftTemperature::class.memberProperties
            .firstOrNull { it.name =="minMediumTemp" }
        minMediumTemp?.isAccessible = true
        val temp = minMediumTemp?.getter?.call(MotorFrontLeftTemperature) as Int

        `when`(motorFrontLeftTemp?.value).thenReturn(temp + 5)
        `when`(motorFrontLeftTemp?.warning).thenReturn(Temperature.WARNING_TYPE_MEDIUM)

        task?.hardwareItems = arrayOf(motorFrontLeftTemp!!)

        task?.checkTemps()
        assertThat(task?.reportedTempWarnings?.get(0)).isEqualTo(Temperature.WARNING_TYPE_MEDIUM)
        assertThat(task?.primaryTempValues?.get(0)).isEqualTo(temp + 5)
    }
    @Test
    fun `report high temperature for front left motor`() {
        val maxMediumTemp = MotorFrontLeftTemperature::class.memberProperties
            .firstOrNull { it.name =="maxMediumTemp" }
        maxMediumTemp?.isAccessible = true
        val temp = maxMediumTemp?.getter?.call(MotorFrontLeftTemperature) as Int

        `when`(motorFrontLeftTemp?.value).thenReturn(temp + 5)
        `when`(motorFrontLeftTemp?.warning).thenReturn(Temperature.WARNING_TYPE_MEDIUM)

        task?.hardwareItems = arrayOf(motorFrontLeftTemp!!)

        task?.checkTemps()
        assertThat(task?.reportedTempWarnings?.get(0)).isEqualTo(Temperature.WARNING_TYPE_MEDIUM)
        assertThat(task?.primaryTempValues?.get(0)).isEqualTo(temp + 5)
    }

    @Test
    fun `report normal temperature for front right motor`() {
        val minMediumTemp = MotorFrontRightTemperature::class.memberProperties
            .firstOrNull { it.name =="minMediumTemp" }
        minMediumTemp?.isAccessible = true
        val temp = minMediumTemp?.getter?.call(MotorFrontRightTemperature) as Int

        `when`(motorFrontRightTemp?.value).thenReturn(temp-5)
        `when`(motorFrontRightTemp?.warning).thenReturn(Temperature.WARNING_TYPE_NORMAL)

        task?.hardwareItems = arrayOf(motorFrontRightTemp!!)

        task?.checkTemps()
        assertThat(task?.reportedTempWarnings?.get(0)).isEqualTo(Temperature.WARNING_TYPE_NORMAL)
        assertThat(task?.primaryTempValues?.get(0)).isEqualTo(temp-5)
    }
    @Test
    fun `report medium temperature for front right motor`() {
        val minMediumTemp = MotorFrontRightTemperature::class.memberProperties
            .firstOrNull { it.name =="minMediumTemp" }
        minMediumTemp?.isAccessible = true
        val temp = minMediumTemp?.getter?.call(MotorFrontRightTemperature) as Int

        `when`(motorFrontRightTemp?.value).thenReturn(temp + 5)
        `when`(motorFrontRightTemp?.warning).thenReturn(Temperature.WARNING_TYPE_MEDIUM)

        task?.hardwareItems = arrayOf(motorFrontRightTemp!!)

        task?.checkTemps()
        assertThat(task?.reportedTempWarnings?.get(0)).isEqualTo(Temperature.WARNING_TYPE_MEDIUM)
        assertThat(task?.primaryTempValues?.get(0)).isEqualTo(temp + 5)
    }
    @Test
    fun `report high temperature for front right motor`() {
        val maxMediumTemp = MotorFrontRightTemperature::class.memberProperties
            .firstOrNull { it.name =="maxMediumTemp" }
        maxMediumTemp?.isAccessible = true
        val temp = maxMediumTemp?.getter?.call(MotorFrontRightTemperature) as Int

        `when`(motorFrontRightTemp?.value).thenReturn(temp + 5)
        `when`(motorFrontRightTemp?.warning).thenReturn(Temperature.WARNING_TYPE_MEDIUM)

        task?.hardwareItems = arrayOf(motorFrontRightTemp!!)

        task?.checkTemps()
        assertThat(task?.reportedTempWarnings?.get(0)).isEqualTo(Temperature.WARNING_TYPE_MEDIUM)
        assertThat(task?.primaryTempValues?.get(0)).isEqualTo(temp + 5)
    }

    @Test
    fun `report normal temperature for rear h bridge`() {
        val minMediumTemp = HBridgeRearTemperature::class.memberProperties
            .firstOrNull { it.name =="minMediumTemp" }
        minMediumTemp?.isAccessible = true
        val temp = minMediumTemp?.getter?.call(HBridgeRearTemperature) as Int

        `when`(hBridgeRearTemp?.value).thenReturn(temp-5)
        `when`(hBridgeRearTemp?.warning).thenReturn(Temperature.WARNING_TYPE_NORMAL)

        task?.hardwareItems = arrayOf(hBridgeRearTemp!!)

        task?.checkTemps()
        assertThat(task?.reportedTempWarnings?.get(0)).isEqualTo(Temperature.WARNING_TYPE_NORMAL)
        assertThat(task?.primaryTempValues?.get(0)).isEqualTo(temp-5)
    }
    @Test
    fun `report medium temperature for rear h bridge`() {
        val minMediumTemp = HBridgeRearTemperature::class.memberProperties
            .firstOrNull { it.name =="minMediumTemp" }
        minMediumTemp?.isAccessible = true
        val temp = minMediumTemp?.getter?.call(HBridgeRearTemperature) as Int

        `when`(hBridgeRearTemp?.value).thenReturn(temp + 5)
        `when`(hBridgeRearTemp?.warning).thenReturn(Temperature.WARNING_TYPE_MEDIUM)

        task?.hardwareItems = arrayOf(hBridgeRearTemp!!)

        task?.checkTemps()
        assertThat(task?.reportedTempWarnings?.get(0)).isEqualTo(Temperature.WARNING_TYPE_MEDIUM)
        assertThat(task?.primaryTempValues?.get(0)).isEqualTo(temp + 5)
    }
    @Test
    fun `report high temperature for rear h bridge`() {
        val maxMediumTemp = HBridgeRearTemperature::class.memberProperties
            .firstOrNull { it.name =="maxMediumTemp" }
        maxMediumTemp?.isAccessible = true
        val temp = maxMediumTemp?.getter?.call(HBridgeRearTemperature) as Int

        `when`(hBridgeRearTemp?.value).thenReturn(temp + 5)
        `when`(hBridgeRearTemp?.warning).thenReturn(Temperature.WARNING_TYPE_MEDIUM)

        task?.hardwareItems = arrayOf(hBridgeRearTemp!!)

        task?.checkTemps()
        assertThat(task?.reportedTempWarnings?.get(0)).isEqualTo(Temperature.WARNING_TYPE_MEDIUM)
        assertThat(task?.primaryTempValues?.get(0)).isEqualTo(temp + 5)
    }

    @Test
    fun `report normal temperature for front h bridge`() {
        val minMediumTemp = HBridgeFrontTemperature::class.memberProperties
            .firstOrNull { it.name =="minMediumTemp" }
        minMediumTemp?.isAccessible = true
        val temp = minMediumTemp?.getter?.call(HBridgeFrontTemperature) as Int

        `when`(hBridgeFrontTemp?.value).thenReturn(temp-5)
        `when`(hBridgeFrontTemp?.warning).thenReturn(Temperature.WARNING_TYPE_NORMAL)

        task?.hardwareItems = arrayOf(hBridgeFrontTemp!!)

        task?.checkTemps()
        assertThat(task?.reportedTempWarnings?.get(0)).isEqualTo(Temperature.WARNING_TYPE_NORMAL)
        assertThat(task?.primaryTempValues?.get(0)).isEqualTo(temp-5)
    }
    @Test
    fun `report medium temperature for front h bridge`() {
        val minMediumTemp = HBridgeFrontTemperature::class.memberProperties
            .firstOrNull { it.name =="minMediumTemp" }
        minMediumTemp?.isAccessible = true
        val temp = minMediumTemp?.getter?.call(HBridgeFrontTemperature) as Int

        `when`(hBridgeFrontTemp?.value).thenReturn(temp + 5)
        `when`(hBridgeFrontTemp?.warning).thenReturn(Temperature.WARNING_TYPE_MEDIUM)

        task?.hardwareItems = arrayOf(hBridgeFrontTemp!!)

        task?.checkTemps()
        assertThat(task?.reportedTempWarnings?.get(0)).isEqualTo(Temperature.WARNING_TYPE_MEDIUM)
        assertThat(task?.primaryTempValues?.get(0)).isEqualTo(temp + 5)
    }
    @Test
    fun `report high temperature for front h bridge`() {
        val maxMediumTemp = HBridgeFrontTemperature::class.memberProperties
            .firstOrNull { it.name =="maxMediumTemp" }
        maxMediumTemp?.isAccessible = true
        val temp = maxMediumTemp?.getter?.call(HBridgeFrontTemperature) as Int

        `when`(hBridgeFrontTemp?.value).thenReturn(temp + 5)
        `when`(hBridgeFrontTemp?.warning).thenReturn(Temperature.WARNING_TYPE_MEDIUM)

        task?.hardwareItems = arrayOf(hBridgeFrontTemp!!)

        task?.checkTemps()
        assertThat(task?.reportedTempWarnings?.get(0)).isEqualTo(Temperature.WARNING_TYPE_MEDIUM)
        assertThat(task?.primaryTempValues?.get(0)).isEqualTo(temp + 5)
    }

    @Test
    fun `report normal temperature for raspberry pi`() {
        val minMediumTemp = RaspberryPiTemperature::class.memberProperties
            .firstOrNull { it.name =="minMediumTemp" }
        minMediumTemp?.isAccessible = true
        val temp = minMediumTemp?.getter?.call(RaspberryPiTemperature) as Int

        `when`(raspberryPiTemp?.value).thenReturn(temp-5)
        `when`(raspberryPiTemp?.warning).thenReturn(Temperature.WARNING_TYPE_NORMAL)

        task?.hardwareItems = arrayOf(raspberryPiTemp!!)

        task?.checkTemps()
        assertThat(task?.reportedTempWarnings?.get(0)).isEqualTo(Temperature.WARNING_TYPE_NORMAL)
        assertThat(task?.primaryTempValues?.get(0)).isEqualTo(temp-5)
    }
    @Test
    fun `report medium temperature for raspberry pi`() {
        val minMediumTemp = RaspberryPiTemperature::class.memberProperties
            .firstOrNull { it.name =="minMediumTemp" }
        minMediumTemp?.isAccessible = true
        val temp = minMediumTemp?.getter?.call(RaspberryPiTemperature) as Int

        `when`(raspberryPiTemp?.value).thenReturn(temp + 5)
        `when`(raspberryPiTemp?.warning).thenReturn(Temperature.WARNING_TYPE_MEDIUM)

        task?.hardwareItems = arrayOf(raspberryPiTemp!!)

        task?.checkTemps()
        assertThat(task?.reportedTempWarnings?.get(0)).isEqualTo(Temperature.WARNING_TYPE_MEDIUM)
        assertThat(task?.primaryTempValues?.get(0)).isEqualTo(temp + 5)
    }
    @Test
    fun `report high temperature for raspberry pi`() {
        val maxMediumTemp = RaspberryPiTemperature::class.memberProperties
            .firstOrNull { it.name =="maxMediumTemp" }
        maxMediumTemp?.isAccessible = true
        val temp = maxMediumTemp?.getter?.call(RaspberryPiTemperature) as Int

        `when`(raspberryPiTemp?.value).thenReturn(temp + 5)
        `when`(raspberryPiTemp?.warning).thenReturn(Temperature.WARNING_TYPE_MEDIUM)

        task?.hardwareItems = arrayOf(raspberryPiTemp!!)

        task?.checkTemps()
        assertThat(task?.reportedTempWarnings?.get(0)).isEqualTo(Temperature.WARNING_TYPE_MEDIUM)
        assertThat(task?.primaryTempValues?.get(0)).isEqualTo(temp + 5)
    }

    @Test
    fun `report normal temperature for batteries`() {
        val minMediumTemp = BatteriesTemperature::class.memberProperties
            .firstOrNull { it.name =="minMediumTemp" }
        minMediumTemp?.isAccessible = true
        val temp = minMediumTemp?.getter?.call(BatteriesTemperature) as Int

        `when`(batteriesTemp?.value).thenReturn(temp-5)
        `when`(batteriesTemp?.warning).thenReturn(Temperature.WARNING_TYPE_NORMAL)

        task?.hardwareItems = arrayOf(batteriesTemp!!)

        task?.checkTemps()
        assertThat(task?.reportedTempWarnings?.get(0)).isEqualTo(Temperature.WARNING_TYPE_NORMAL)
        assertThat(task?.primaryTempValues?.get(0)).isEqualTo(temp-5)
    }
    @Test
    fun `report medium temperature for batteries`() {
        val minMediumTemp = BatteriesTemperature::class.memberProperties
            .firstOrNull { it.name =="minMediumTemp" }
        minMediumTemp?.isAccessible = true
        val temp = minMediumTemp?.getter?.call(BatteriesTemperature) as Int

        `when`(batteriesTemp?.value).thenReturn(temp + 5)
        `when`(batteriesTemp?.warning).thenReturn(Temperature.WARNING_TYPE_MEDIUM)

        task?.hardwareItems = arrayOf(batteriesTemp!!)

        task?.checkTemps()
        assertThat(task?.reportedTempWarnings?.get(0)).isEqualTo(Temperature.WARNING_TYPE_MEDIUM)
        assertThat(task?.primaryTempValues?.get(0)).isEqualTo(temp + 5)
    }
    @Test
    fun `report high temperature for batteries`() {
        val maxMediumTemp = BatteriesTemperature::class.memberProperties
            .firstOrNull { it.name =="maxMediumTemp" }
        maxMediumTemp?.isAccessible = true
        val temp = maxMediumTemp?.getter?.call(BatteriesTemperature) as Int

        `when`(batteriesTemp?.value).thenReturn(temp + 5)
        `when`(batteriesTemp?.warning).thenReturn(Temperature.WARNING_TYPE_MEDIUM)

        task?.hardwareItems = arrayOf(batteriesTemp!!)

        task?.checkTemps()
        assertThat(task?.reportedTempWarnings?.get(0)).isEqualTo(Temperature.WARNING_TYPE_MEDIUM)
        assertThat(task?.primaryTempValues?.get(0)).isEqualTo(temp + 5)
    }

    @Test
    fun `report normal temperature for shift registers`() {
        val minMediumTemp = ShiftRegistersTemperature::class.memberProperties
            .firstOrNull { it.name =="minMediumTemp" }
        minMediumTemp?.isAccessible = true
        val temp = minMediumTemp?.getter?.call(ShiftRegistersTemperature) as Int

        `when`(shiftRegistersTemp?.value).thenReturn(temp-5)
        `when`(shiftRegistersTemp?.warning).thenReturn(Temperature.WARNING_TYPE_NORMAL)

        task?.hardwareItems = arrayOf(shiftRegistersTemp!!)

        task?.checkTemps()
        assertThat(task?.reportedTempWarnings?.get(0)).isEqualTo(Temperature.WARNING_TYPE_NORMAL)
        assertThat(task?.primaryTempValues?.get(0)).isEqualTo(temp-5)
    }
    @Test
    fun `report medium temperature for shift registers`() {
        val minMediumTemp = ShiftRegistersTemperature::class.memberProperties
            .firstOrNull { it.name =="minMediumTemp" }
        minMediumTemp?.isAccessible = true
        val temp = minMediumTemp?.getter?.call(ShiftRegistersTemperature) as Int

        `when`(shiftRegistersTemp?.value).thenReturn(temp + 5)
        `when`(shiftRegistersTemp?.warning).thenReturn(Temperature.WARNING_TYPE_MEDIUM)

        task?.hardwareItems = arrayOf(shiftRegistersTemp!!)

        task?.checkTemps()
        assertThat(task?.reportedTempWarnings?.get(0)).isEqualTo(Temperature.WARNING_TYPE_MEDIUM)
        assertThat(task?.primaryTempValues?.get(0)).isEqualTo(temp + 5)
    }
    @Test
    fun `report high temperature for shift registers`() {
        val maxMediumTemp = ShiftRegistersTemperature::class.memberProperties
            .firstOrNull { it.name =="maxMediumTemp" }
        maxMediumTemp?.isAccessible = true
        val temp = maxMediumTemp?.getter?.call(ShiftRegistersTemperature) as Int

        `when`(shiftRegistersTemp?.value).thenReturn(temp + 5)
        `when`(shiftRegistersTemp?.warning).thenReturn(Temperature.WARNING_TYPE_MEDIUM)

        task?.hardwareItems = arrayOf(shiftRegistersTemp!!)

        task?.checkTemps()
        assertThat(task?.reportedTempWarnings?.get(0)).isEqualTo(Temperature.WARNING_TYPE_MEDIUM)
        assertThat(task?.primaryTempValues?.get(0)).isEqualTo(temp + 5)
    }
}