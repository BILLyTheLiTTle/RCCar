package car.server.cron

import car.controllers.basic.EngineImpl
import car.controllers.temperatures.*
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.SpyBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible



@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class TemperatureCronJobTest {

    @SpyBean
    private val task: TemperaturesCronJob? = null

    @Test
    fun testMocksCreation() {
        assertNotNull(motorRearLeftTemp)
    }

    // checkTemps
    @Test
    fun `report normal temperature for rear left motor`() {
        val minMediumTemp = MotorRearLeftTemperatureImpl::class.memberProperties
            .firstOrNull { it.name =="minMediumTemp" }
        minMediumTemp?.isAccessible = true
        val temp = minMediumTemp?.getter?.call(MotorRearLeftTemperatureImpl) as Int

        `when`(motorRearLeftTemp?.value).thenReturn(temp-5)
        `when`(motorRearLeftTemp?.warning).thenReturn(Temperature.WARNING_TYPE_NORMAL)

        task?.hardwareItems = arrayOf(motorRearLeftTemp!!)

        task?.checkTemps()
        assertThat(task?.reportedTempWarnings?.get(0)).isEqualTo(Temperature.WARNING_TYPE_NORMAL)
        assertThat(task?.primaryTempValues?.get(0)).isEqualTo(temp-5)
    }
    @Test
    fun `report medium temperature for rear left motor`() {
        val minMediumTemp = MotorRearLeftTemperatureImpl::class.memberProperties
            .firstOrNull { it.name =="minMediumTemp" }
        minMediumTemp?.isAccessible = true
        val temp = minMediumTemp?.getter?.call(MotorRearLeftTemperatureImpl) as Int

        `when`(motorRearLeftTemp?.value).thenReturn(temp + 5)
        `when`(motorRearLeftTemp?.warning).thenReturn(Temperature.WARNING_TYPE_MEDIUM)

        task?.hardwareItems = arrayOf(motorRearLeftTemp!!)

        task?.checkTemps()
        assertThat(task?.reportedTempWarnings?.get(0)).isEqualTo(Temperature.WARNING_TYPE_MEDIUM)
        assertThat(task?.primaryTempValues?.get(0)).isEqualTo(temp + 5)
    }
    @Test
    fun `report high temperature for rear left motor`() {
        val maxMediumTemp = MotorRearLeftTemperatureImpl::class.memberProperties
            .firstOrNull { it.name =="maxMediumTemp" }
        maxMediumTemp?.isAccessible = true
        val temp = maxMediumTemp?.getter?.call(MotorRearLeftTemperatureImpl) as Int

        `when`(motorRearLeftTemp?.value).thenReturn(temp + 5)
        `when`(motorRearLeftTemp?.warning).thenReturn(Temperature.WARNING_TYPE_MEDIUM)

        task?.hardwareItems = arrayOf(motorRearLeftTemp!!)

        task?.checkTemps()
        assertThat(task?.reportedTempWarnings?.get(0)).isEqualTo(Temperature.WARNING_TYPE_MEDIUM)
        assertThat(task?.primaryTempValues?.get(0)).isEqualTo(temp + 5)
    }

    @Test
    fun `report normal temperature for rear right motor`() {
        val minMediumTemp = MotorRearRightTemperatureImpl::class.memberProperties
            .firstOrNull { it.name =="minMediumTemp" }
        minMediumTemp?.isAccessible = true
        val temp = minMediumTemp?.getter?.call(MotorRearRightTemperatureImpl) as Int

        `when`(motorRearRightTemp?.value).thenReturn(temp-5)
        `when`(motorRearRightTemp?.warning).thenReturn(Temperature.WARNING_TYPE_NORMAL)

        task?.hardwareItems = arrayOf(motorRearRightTemp!!)

        task?.checkTemps()
        assertThat(task?.reportedTempWarnings?.get(0)).isEqualTo(Temperature.WARNING_TYPE_NORMAL)
        assertThat(task?.primaryTempValues?.get(0)).isEqualTo(temp-5)
    }
    @Test
    fun `report medium temperature for rear right motor`() {
        val maxMediumTemp = MotorRearRightTemperatureImpl::class.memberProperties
            .firstOrNull { it.name =="maxMediumTemp" }
        maxMediumTemp?.isAccessible = true
        val temp = maxMediumTemp?.getter?.call(MotorRearRightTemperatureImpl) as Int

        `when`(motorRearRightTemp?.value).thenReturn(temp + 5)
        `when`(motorRearRightTemp?.warning).thenReturn(Temperature.WARNING_TYPE_MEDIUM)

        task?.hardwareItems = arrayOf(motorRearRightTemp!!)

        task?.checkTemps()
        assertThat(task?.reportedTempWarnings?.get(0)).isEqualTo(Temperature.WARNING_TYPE_MEDIUM)
        assertThat(task?.primaryTempValues?.get(0)).isEqualTo(temp + 5)
    }
    @Test
    fun `report high temperature for rear right motor`() {
        val maxMediumTemp = MotorRearRightTemperatureImpl::class.memberProperties
            .firstOrNull { it.name =="maxMediumTemp" }
        maxMediumTemp?.isAccessible = true
        val temp = maxMediumTemp?.getter?.call(MotorRearRightTemperatureImpl) as Int

        `when`(motorRearRightTemp?.value).thenReturn(temp + 5)
        `when`(motorRearRightTemp?.warning).thenReturn(Temperature.WARNING_TYPE_MEDIUM)

        task?.hardwareItems = arrayOf(motorRearRightTemp!!)

        task?.checkTemps()
        assertThat(task?.reportedTempWarnings?.get(0)).isEqualTo(Temperature.WARNING_TYPE_MEDIUM)
        assertThat(task?.primaryTempValues?.get(0)).isEqualTo(temp + 5)
    }
    @Test
    fun `report normal temperature for front left motor`() {
        val minMediumTemp = MotorFrontLeftTemperatureImpl::class.memberProperties
            .firstOrNull { it.name =="minMediumTemp" }
        minMediumTemp?.isAccessible = true
        val temp = minMediumTemp?.getter?.call(MotorFrontLeftTemperatureImpl) as Int

        `when`(motorFrontLeftTemp?.value).thenReturn(temp-5)
        `when`(motorFrontLeftTemp?.warning).thenReturn(Temperature.WARNING_TYPE_NORMAL)

        task?.hardwareItems = arrayOf(motorFrontLeftTemp!!)

        task?.checkTemps()
        assertThat(task?.reportedTempWarnings?.get(0)).isEqualTo(Temperature.WARNING_TYPE_NORMAL)
        assertThat(task?.primaryTempValues?.get(0)).isEqualTo(temp-5)
    }
    @Test
    fun `report medium temperature for front left motor`() {
        val minMediumTemp = MotorFrontLeftTemperatureImpl::class.memberProperties
            .firstOrNull { it.name =="minMediumTemp" }
        minMediumTemp?.isAccessible = true
        val temp = minMediumTemp?.getter?.call(MotorFrontLeftTemperatureImpl) as Int

        `when`(motorFrontLeftTemp?.value).thenReturn(temp + 5)
        `when`(motorFrontLeftTemp?.warning).thenReturn(Temperature.WARNING_TYPE_MEDIUM)

        task?.hardwareItems = arrayOf(motorFrontLeftTemp!!)

        task?.checkTemps()
        assertThat(task?.reportedTempWarnings?.get(0)).isEqualTo(Temperature.WARNING_TYPE_MEDIUM)
        assertThat(task?.primaryTempValues?.get(0)).isEqualTo(temp + 5)
    }
    @Test
    fun `report high temperature for front left motor`() {
        val maxMediumTemp = MotorFrontLeftTemperatureImpl::class.memberProperties
            .firstOrNull { it.name =="maxMediumTemp" }
        maxMediumTemp?.isAccessible = true
        val temp = maxMediumTemp?.getter?.call(MotorFrontLeftTemperatureImpl) as Int

        `when`(motorFrontLeftTemp?.value).thenReturn(temp + 5)
        `when`(motorFrontLeftTemp?.warning).thenReturn(Temperature.WARNING_TYPE_MEDIUM)

        task?.hardwareItems = arrayOf(motorFrontLeftTemp!!)

        task?.checkTemps()
        assertThat(task?.reportedTempWarnings?.get(0)).isEqualTo(Temperature.WARNING_TYPE_MEDIUM)
        assertThat(task?.primaryTempValues?.get(0)).isEqualTo(temp + 5)
    }

    @Test
    fun `report normal temperature for front right motor`() {
        val minMediumTemp = MotorFrontRightTemperatureImpl::class.memberProperties
            .firstOrNull { it.name =="minMediumTemp" }
        minMediumTemp?.isAccessible = true
        val temp = minMediumTemp?.getter?.call(MotorFrontRightTemperatureImpl) as Int

        `when`(motorFrontRightTemp?.value).thenReturn(temp-5)
        `when`(motorFrontRightTemp?.warning).thenReturn(Temperature.WARNING_TYPE_NORMAL)

        task?.hardwareItems = arrayOf(motorFrontRightTemp!!)

        task?.checkTemps()
        assertThat(task?.reportedTempWarnings?.get(0)).isEqualTo(Temperature.WARNING_TYPE_NORMAL)
        assertThat(task?.primaryTempValues?.get(0)).isEqualTo(temp-5)
    }
    @Test
    fun `report medium temperature for front right motor`() {
        val minMediumTemp = MotorFrontRightTemperatureImpl::class.memberProperties
            .firstOrNull { it.name =="minMediumTemp" }
        minMediumTemp?.isAccessible = true
        val temp = minMediumTemp?.getter?.call(MotorFrontRightTemperatureImpl) as Int

        `when`(motorFrontRightTemp?.value).thenReturn(temp + 5)
        `when`(motorFrontRightTemp?.warning).thenReturn(Temperature.WARNING_TYPE_MEDIUM)

        task?.hardwareItems = arrayOf(motorFrontRightTemp!!)

        task?.checkTemps()
        assertThat(task?.reportedTempWarnings?.get(0)).isEqualTo(Temperature.WARNING_TYPE_MEDIUM)
        assertThat(task?.primaryTempValues?.get(0)).isEqualTo(temp + 5)
    }
    @Test
    fun `report high temperature for front right motor`() {
        val maxMediumTemp = MotorFrontRightTemperatureImpl::class.memberProperties
            .firstOrNull { it.name =="maxMediumTemp" }
        maxMediumTemp?.isAccessible = true
        val temp = maxMediumTemp?.getter?.call(MotorFrontRightTemperatureImpl) as Int

        `when`(motorFrontRightTemp?.value).thenReturn(temp + 5)
        `when`(motorFrontRightTemp?.warning).thenReturn(Temperature.WARNING_TYPE_MEDIUM)

        task?.hardwareItems = arrayOf(motorFrontRightTemp!!)

        task?.checkTemps()
        assertThat(task?.reportedTempWarnings?.get(0)).isEqualTo(Temperature.WARNING_TYPE_MEDIUM)
        assertThat(task?.primaryTempValues?.get(0)).isEqualTo(temp + 5)
    }

    @Test
    fun `report normal temperature for rear h bridge`() {
        val minMediumTemp = HBridgeRearTemperatureImpl::class.memberProperties
            .firstOrNull { it.name =="minMediumTemp" }
        minMediumTemp?.isAccessible = true
        val temp = minMediumTemp?.getter?.call(HBridgeRearTemperatureImpl) as Int

        `when`(hBridgeRearTemp?.value).thenReturn(temp-5)
        `when`(hBridgeRearTemp?.warning).thenReturn(Temperature.WARNING_TYPE_NORMAL)

        task?.hardwareItems = arrayOf(hBridgeRearTemp!!)

        task?.checkTemps()
        assertThat(task?.reportedTempWarnings?.get(0)).isEqualTo(Temperature.WARNING_TYPE_NORMAL)
        assertThat(task?.primaryTempValues?.get(0)).isEqualTo(temp-5)
    }
    @Test
    fun `report medium temperature for rear h bridge`() {
        val minMediumTemp = HBridgeRearTemperatureImpl::class.memberProperties
            .firstOrNull { it.name =="minMediumTemp" }
        minMediumTemp?.isAccessible = true
        val temp = minMediumTemp?.getter?.call(HBridgeRearTemperatureImpl) as Int

        `when`(hBridgeRearTemp?.value).thenReturn(temp + 5)
        `when`(hBridgeRearTemp?.warning).thenReturn(Temperature.WARNING_TYPE_MEDIUM)

        task?.hardwareItems = arrayOf(hBridgeRearTemp!!)

        task?.checkTemps()
        assertThat(task?.reportedTempWarnings?.get(0)).isEqualTo(Temperature.WARNING_TYPE_MEDIUM)
        assertThat(task?.primaryTempValues?.get(0)).isEqualTo(temp + 5)
    }
    @Test
    fun `report high temperature for rear h bridge`() {
        val maxMediumTemp = HBridgeRearTemperatureImpl::class.memberProperties
            .firstOrNull { it.name =="maxMediumTemp" }
        maxMediumTemp?.isAccessible = true
        val temp = maxMediumTemp?.getter?.call(HBridgeRearTemperatureImpl) as Int

        `when`(hBridgeRearTemp?.value).thenReturn(temp + 5)
        `when`(hBridgeRearTemp?.warning).thenReturn(Temperature.WARNING_TYPE_MEDIUM)

        task?.hardwareItems = arrayOf(hBridgeRearTemp!!)

        task?.checkTemps()
        assertThat(task?.reportedTempWarnings?.get(0)).isEqualTo(Temperature.WARNING_TYPE_MEDIUM)
        assertThat(task?.primaryTempValues?.get(0)).isEqualTo(temp + 5)
    }

    @Test
    fun `report normal temperature for front h bridge`() {
        val minMediumTemp = HBridgeFrontTemperatureImpl::class.memberProperties
            .firstOrNull { it.name =="minMediumTemp" }
        minMediumTemp?.isAccessible = true
        val temp = minMediumTemp?.getter?.call(HBridgeFrontTemperatureImpl) as Int

        `when`(hBridgeFrontTemp?.value).thenReturn(temp-5)
        `when`(hBridgeFrontTemp?.warning).thenReturn(Temperature.WARNING_TYPE_NORMAL)

        task?.hardwareItems = arrayOf(hBridgeFrontTemp!!)

        task?.checkTemps()
        assertThat(task?.reportedTempWarnings?.get(0)).isEqualTo(Temperature.WARNING_TYPE_NORMAL)
        assertThat(task?.primaryTempValues?.get(0)).isEqualTo(temp-5)
    }
    @Test
    fun `report medium temperature for front h bridge`() {
        val minMediumTemp = HBridgeFrontTemperatureImpl::class.memberProperties
            .firstOrNull { it.name =="minMediumTemp" }
        minMediumTemp?.isAccessible = true
        val temp = minMediumTemp?.getter?.call(HBridgeFrontTemperatureImpl) as Int

        `when`(hBridgeFrontTemp?.value).thenReturn(temp + 5)
        `when`(hBridgeFrontTemp?.warning).thenReturn(Temperature.WARNING_TYPE_MEDIUM)

        task?.hardwareItems = arrayOf(hBridgeFrontTemp!!)

        task?.checkTemps()
        assertThat(task?.reportedTempWarnings?.get(0)).isEqualTo(Temperature.WARNING_TYPE_MEDIUM)
        assertThat(task?.primaryTempValues?.get(0)).isEqualTo(temp + 5)
    }
    @Test
    fun `report high temperature for front h bridge`() {
        val maxMediumTemp = HBridgeFrontTemperatureImpl::class.memberProperties
            .firstOrNull { it.name =="maxMediumTemp" }
        maxMediumTemp?.isAccessible = true
        val temp = maxMediumTemp?.getter?.call(HBridgeFrontTemperatureImpl) as Int

        `when`(hBridgeFrontTemp?.value).thenReturn(temp + 5)
        `when`(hBridgeFrontTemp?.warning).thenReturn(Temperature.WARNING_TYPE_MEDIUM)

        task?.hardwareItems = arrayOf(hBridgeFrontTemp!!)

        task?.checkTemps()
        assertThat(task?.reportedTempWarnings?.get(0)).isEqualTo(Temperature.WARNING_TYPE_MEDIUM)
        assertThat(task?.primaryTempValues?.get(0)).isEqualTo(temp + 5)
    }

    @Test
    fun `report normal temperature for raspberry pi`() {
        val minMediumTemp = RaspberryPiTemperatureImpl::class.memberProperties
            .firstOrNull { it.name =="minMediumTemp" }
        minMediumTemp?.isAccessible = true
        val temp = minMediumTemp?.getter?.call(RaspberryPiTemperatureImpl) as Int

        `when`(raspberryPiTemp?.value).thenReturn(temp-5)
        `when`(raspberryPiTemp?.warning).thenReturn(Temperature.WARNING_TYPE_NORMAL)

        task?.hardwareItems = arrayOf(raspberryPiTemp!!)

        task?.checkTemps()
        assertThat(task?.reportedTempWarnings?.get(0)).isEqualTo(Temperature.WARNING_TYPE_NORMAL)
        assertThat(task?.primaryTempValues?.get(0)).isEqualTo(temp-5)
    }
    @Test
    fun `report medium temperature for raspberry pi`() {
        val minMediumTemp = RaspberryPiTemperatureImpl::class.memberProperties
            .firstOrNull { it.name =="minMediumTemp" }
        minMediumTemp?.isAccessible = true
        val temp = minMediumTemp?.getter?.call(RaspberryPiTemperatureImpl) as Int

        `when`(raspberryPiTemp?.value).thenReturn(temp + 5)
        `when`(raspberryPiTemp?.warning).thenReturn(Temperature.WARNING_TYPE_MEDIUM)

        task?.hardwareItems = arrayOf(raspberryPiTemp!!)

        task?.checkTemps()
        assertThat(task?.reportedTempWarnings?.get(0)).isEqualTo(Temperature.WARNING_TYPE_MEDIUM)
        assertThat(task?.primaryTempValues?.get(0)).isEqualTo(temp + 5)
    }
    @Test
    fun `report high temperature for raspberry pi`() {
        val maxMediumTemp = RaspberryPiTemperatureImpl::class.memberProperties
            .firstOrNull { it.name =="maxMediumTemp" }
        maxMediumTemp?.isAccessible = true
        val temp = maxMediumTemp?.getter?.call(RaspberryPiTemperatureImpl) as Int

        `when`(raspberryPiTemp?.value).thenReturn(temp + 5)
        `when`(raspberryPiTemp?.warning).thenReturn(Temperature.WARNING_TYPE_MEDIUM)

        task?.hardwareItems = arrayOf(raspberryPiTemp!!)

        task?.checkTemps()
        assertThat(task?.reportedTempWarnings?.get(0)).isEqualTo(Temperature.WARNING_TYPE_MEDIUM)
        assertThat(task?.primaryTempValues?.get(0)).isEqualTo(temp + 5)
    }

    @Test
    fun `report normal temperature for batteries`() {
        val minMediumTemp = BatteriesTemperatureImpl::class.memberProperties
            .firstOrNull { it.name =="minMediumTemp" }
        minMediumTemp?.isAccessible = true
        val temp = minMediumTemp?.getter?.call(BatteriesTemperatureImpl) as Int

        `when`(batteriesTemp?.value).thenReturn(temp-5)
        `when`(batteriesTemp?.warning).thenReturn(Temperature.WARNING_TYPE_NORMAL)

        task?.hardwareItems = arrayOf(batteriesTemp!!)

        task?.checkTemps()
        assertThat(task?.reportedTempWarnings?.get(0)).isEqualTo(Temperature.WARNING_TYPE_NORMAL)
        assertThat(task?.primaryTempValues?.get(0)).isEqualTo(temp-5)
    }
    @Test
    fun `report medium temperature for batteries`() {
        val minMediumTemp = BatteriesTemperatureImpl::class.memberProperties
            .firstOrNull { it.name =="minMediumTemp" }
        minMediumTemp?.isAccessible = true
        val temp = minMediumTemp?.getter?.call(BatteriesTemperatureImpl) as Int

        `when`(batteriesTemp?.value).thenReturn(temp + 5)
        `when`(batteriesTemp?.warning).thenReturn(Temperature.WARNING_TYPE_MEDIUM)

        task?.hardwareItems = arrayOf(batteriesTemp!!)

        task?.checkTemps()
        assertThat(task?.reportedTempWarnings?.get(0)).isEqualTo(Temperature.WARNING_TYPE_MEDIUM)
        assertThat(task?.primaryTempValues?.get(0)).isEqualTo(temp + 5)
    }
    @Test
    fun `report high temperature for batteries`() {
        val maxMediumTemp = BatteriesTemperatureImpl::class.memberProperties
            .firstOrNull { it.name =="maxMediumTemp" }
        maxMediumTemp?.isAccessible = true
        val temp = maxMediumTemp?.getter?.call(BatteriesTemperatureImpl) as Int

        `when`(batteriesTemp?.value).thenReturn(temp + 5)
        `when`(batteriesTemp?.warning).thenReturn(Temperature.WARNING_TYPE_MEDIUM)

        task?.hardwareItems = arrayOf(batteriesTemp!!)

        task?.checkTemps()
        assertThat(task?.reportedTempWarnings?.get(0)).isEqualTo(Temperature.WARNING_TYPE_MEDIUM)
        assertThat(task?.primaryTempValues?.get(0)).isEqualTo(temp + 5)
    }

    @Test
    fun `report normal temperature for shift registers`() {
        val minMediumTemp = ShiftRegistersTemperatureImpl::class.memberProperties
            .firstOrNull { it.name =="minMediumTemp" }
        minMediumTemp?.isAccessible = true
        val temp = minMediumTemp?.getter?.call(ShiftRegistersTemperatureImpl) as Int

        `when`(shiftRegistersTemp?.value).thenReturn(temp-5)
        `when`(shiftRegistersTemp?.warning).thenReturn(Temperature.WARNING_TYPE_NORMAL)

        task?.hardwareItems = arrayOf(shiftRegistersTemp!!)

        task?.checkTemps()
        assertThat(task?.reportedTempWarnings?.get(0)).isEqualTo(Temperature.WARNING_TYPE_NORMAL)
        assertThat(task?.primaryTempValues?.get(0)).isEqualTo(temp-5)
    }
    @Test
    fun `report medium temperature for shift registers`() {
        val minMediumTemp = ShiftRegistersTemperatureImpl::class.memberProperties
            .firstOrNull { it.name =="minMediumTemp" }
        minMediumTemp?.isAccessible = true
        val temp = minMediumTemp?.getter?.call(ShiftRegistersTemperatureImpl) as Int

        `when`(shiftRegistersTemp?.value).thenReturn(temp + 5)
        `when`(shiftRegistersTemp?.warning).thenReturn(Temperature.WARNING_TYPE_MEDIUM)

        task?.hardwareItems = arrayOf(shiftRegistersTemp!!)

        task?.checkTemps()
        assertThat(task?.reportedTempWarnings?.get(0)).isEqualTo(Temperature.WARNING_TYPE_MEDIUM)
        assertThat(task?.primaryTempValues?.get(0)).isEqualTo(temp + 5)
    }
    @Test
    fun `report high temperature for shift registers`() {
        val maxMediumTemp = ShiftRegistersTemperatureImpl::class.memberProperties
            .firstOrNull { it.name =="maxMediumTemp" }
        maxMediumTemp?.isAccessible = true
        val temp = maxMediumTemp?.getter?.call(ShiftRegistersTemperatureImpl) as Int

        `when`(shiftRegistersTemp?.value).thenReturn(temp + 5)
        `when`(shiftRegistersTemp?.warning).thenReturn(Temperature.WARNING_TYPE_MEDIUM)

        task?.hardwareItems = arrayOf(shiftRegistersTemp!!)

        task?.checkTemps()
        assertThat(task?.reportedTempWarnings?.get(0)).isEqualTo(Temperature.WARNING_TYPE_MEDIUM)
        assertThat(task?.primaryTempValues?.get(0)).isEqualTo(temp + 5)
    }

    companion object {

        @Mock
        private var motorRearLeftTemp: MotorRearLeftTemperatureImpl? = null
        @Mock
        private var motorRearRightTemp: MotorRearRightTemperatureImpl? = null
        @Mock
        private var motorFrontLeftTemp: MotorFrontLeftTemperatureImpl? = null
        @Mock
        private var motorFrontRightTemp: MotorFrontRightTemperatureImpl? = null
        @Mock
        private var batteriesTemp: BatteriesTemperatureImpl? = null
        @Mock
        private var hBridgeRearTemp: HBridgeRearTemperatureImpl? = null
        @Mock
        private var hBridgeFrontTemp: HBridgeFrontTemperatureImpl? = null
        @Mock
        private var raspberryPiTemp: RaspberryPiTemperatureImpl? = null
        @Mock
        private var shiftRegistersTemp: ShiftRegistersTemperatureImpl? = null

        @BeforeAll
        @JvmStatic
        internal fun beforeAll() {
            EngineImpl.engineState = true
        }
    }
}