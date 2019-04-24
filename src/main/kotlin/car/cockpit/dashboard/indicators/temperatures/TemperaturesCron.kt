package car.cockpit.dashboard.indicators.temperatures

import car.LoggerTypes.*
import car.cockpit.engine.*
import car.launchRequest
import car.showMessage
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import javax.annotation.PostConstruct
import kotlin.reflect.KClass

@Configuration
@EnableAsync
@EnableScheduling
class TemperaturesCron {

    @Autowired
    private lateinit var engineComponent: Engine

    @Autowired
    @Qualifier("Raspberry Pi Temperature Component")
    private lateinit var raspberryPiTempComponent: Temperature

    @Autowired
    @Qualifier("Shift Registers Temperature Component")
    private lateinit var shiftRegistersTempComponent: Temperature

    @Autowired
    @Qualifier("Motor Rear Right Temperature Component")
    private lateinit var motorRearRightTempComponent: Temperature

    @Autowired
    @Qualifier("Motor Rear Left Temperature Component")
    private lateinit var motorRearLeftTempComponent: Temperature

    @Autowired
    @Qualifier("Motor Front Right Temperature Component")
    private lateinit var motorFrontRightTempComponent: Temperature

    @Autowired
    @Qualifier("Motor Front Left Temperature Component")
    private lateinit var motorFrontLeftTempComponent: Temperature

    @Autowired
    @Qualifier("H-Bridge Rear Temperature Component")
    private lateinit var hBridgeRearTempComponent: Temperature

    @Autowired
    @Qualifier("H-Bridge Front Temperature Component")
    private lateinit var hBridgeFrontTempComponent: Temperature

    @Autowired
    @Qualifier("Batteries Temperature Component")
    private lateinit var batteriesTempComponent: Temperature

    private val logger = LoggerFactory.getLogger(TemperaturesCron::class.java)

    private val tempUri = "/temp"
    private val paramKeyItem = "item"
    private val paramKeyWarning = "warning"
    private val paramKeyValue = "value"

    var primaryTempValues = intArrayOf(EMPTY_INT, EMPTY_INT,
        EMPTY_INT, EMPTY_INT,
        EMPTY_INT, EMPTY_INT,
        EMPTY_INT,
        EMPTY_INT,
        EMPTY_INT)

    var reportedTempWarnings = arrayOf(
        TemperatureWarningType.NOTHING.name, TemperatureWarningType.NOTHING.name,
        TemperatureWarningType.NOTHING.name, TemperatureWarningType.NOTHING.name,
        TemperatureWarningType.NOTHING.name, TemperatureWarningType.NOTHING.name,
        TemperatureWarningType.NOTHING.name,
        TemperatureWarningType.NOTHING.name,
        TemperatureWarningType.NOTHING.name)

    lateinit var hardwareItems: Array<Temperature>
    @PostConstruct
    private fun getHardwareItems(){
        hardwareItems = arrayOf(motorRearLeftTempComponent,motorRearRightTempComponent,
            motorFrontLeftTempComponent, motorFrontRightTempComponent,
            hBridgeRearTempComponent, hBridgeFrontTempComponent,
            raspberryPiTempComponent,
            batteriesTempComponent,
            shiftRegistersTempComponent)
    }

    @Scheduled(initialDelay = 2000, fixedDelay = 420)
    fun checkTemps(){

        if (engineComponent.engineState) {
            for (i in 0 until hardwareItems.size) {
                primaryTempValues[i] = hardwareItems[i].value
                if (reportedTempWarnings[i] != hardwareItems[i].warning) {
                    reportedTempWarnings[i] = hardwareItems[i].warning

                    informClient(hardwareItems[i].id.name, reportedTempWarnings[i], primaryTempValues[i])

                    //if (reportedTempWarnings[i] == Temperature.WARNING_TYPE_HIGH)
                    //    printHighTempInfo(hardwareItems[i]::class, primaryTemp)
                }
            }
        }

        // rear left motor
        /* primaryTemp = MotorRearLeftTemperatureComponent.value
         The following commented code block will be enabled if I want to notify the client
            whenever the value changes, also.

         */
        /*if ((reportedMrltTempValue > primaryTemp + temperatureDiff)
            || reportedMrltTempValue < primaryTemp + temperatureDiff) {
            reportedMrltTempValue = primaryTemp
            launchRequest("http://" +
                    "$nanohttpClientIp:" +
                    "$nanohttpClientPort" +
                    "/temp" +
                    "?item=${MotorRearLeftTemp.ID}" +
                    "&warning=$reportedMrltTempWarning" +
                    "&value=$reportedMrltTempValue")

        }
        if (reportedMrltTempWarning != MotorRearLeftTemp.warning) {
            reportedMrltTempWarning = MotorRearLeftTemp.warning
            launchRequest("http://" +
                    "$nanohttpClientIp:" +
                    "$nanohttpClientPort" +
                    "/temp" +
                    "?item=${MotorRearLeftTemp.ID}" +
                    "&warning=$reportedMrltTempWarning" +
                    "&value=$reportedMrltTempValue")
        }*/
    }

    private fun informClient(hardwareID: String, warning: String, value: Int){
        launchRequest(
            "http://" +
                    "$nanohttpClientIp:" +
                    "$nanohttpClientPort" +
                    tempUri +
                    "?$paramKeyItem=$hardwareID" +
                    "&$paramKeyWarning=$warning" +
                    "&$paramKeyValue=$value"
        )
    }

    private fun printHighTempInfo(hardware: KClass<out Temperature>?, value: Int) {
        showMessage(msgType = CRITICAL,
            logger = logger,
            body = "{ ${hardware?.simpleName ?: "No Name Class"} } Temp Value: $value")
    }
}