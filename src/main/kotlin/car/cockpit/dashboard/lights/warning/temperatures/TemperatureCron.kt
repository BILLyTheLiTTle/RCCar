package car.cockpit.dashboard.lights.warning.temperatures

import car.TYPE_CRITICAL
import car.cockpit.engine.*
import car.doNonBlockingRequest
import car.showMessage
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import kotlin.reflect.KClass

@Configuration
@EnableAsync
@EnableScheduling
class TemperaturesCronJob {

    @Autowired
    private lateinit var engineComponent: Engine


    private val tempUri = "/temp"
    private val paramKeyItem = "item"
    private val paramKeyWarning = "warning"
    private val paramKeyValue = "value"

    var primaryTempValues = intArrayOf(
        EMPTY_INT, EMPTY_INT,
        EMPTY_INT, EMPTY_INT,
        EMPTY_INT, EMPTY_INT,
        EMPTY_INT,
        EMPTY_INT,
        EMPTY_INT)

    var hardwareItems = arrayOf(MotorRearLeftTemperature,MotorRearRightTemperature,
        MotorFrontLeftTemperature, MotorFrontRightTemperature,
        HBridgeRearTemperature, HBridgeFrontTemperature,
        RaspberryPiTemperature,
        BatteriesTemperature,
        ShiftRegistersTemperature)

    var reportedTempWarnings = arrayOf(Temperature.WARNING_TYPE_NOTHING, Temperature.WARNING_TYPE_NOTHING,
        Temperature.WARNING_TYPE_NOTHING, Temperature.WARNING_TYPE_NOTHING,
        Temperature.WARNING_TYPE_NOTHING, Temperature.WARNING_TYPE_NOTHING,
        Temperature.WARNING_TYPE_NOTHING,
        Temperature.WARNING_TYPE_NOTHING,
        Temperature.WARNING_TYPE_NOTHING)

    @Scheduled(initialDelay = 2000, fixedDelay = 420)
    fun checkTemps(){

        if (engineComponent.engineState) {
            for (i in 0 until hardwareItems.size) {
                primaryTempValues[i] = hardwareItems[i].value
                if (reportedTempWarnings[i] != hardwareItems[i].warning) {
                    reportedTempWarnings[i] = hardwareItems[i].warning

                    informClient(hardwareItems[i].id, reportedTempWarnings[i], primaryTempValues[i])

                    //if (reportedTempWarnings[i] == Temperature.WARNING_TYPE_HIGH)
                    //    printHighTempInfo(hardwareItems[i]::class, primaryTemp)
                }
            }
        }

        // rear left motor
        /* primaryTemp = MotorRearLeftTemperature.value
         The following commented code block will be enabled if I want to notify the client
            whenever the value changes, also.

         */
        /*if ((reportedMrltTempValue > primaryTemp + temperatureDiff)
            || reportedMrltTempValue < primaryTemp + temperatureDiff) {
            reportedMrltTempValue = primaryTemp
            doNonBlockingRequest("http://" +
                    "$nanohttpClientIp:" +
                    "$nanohttpClientPort" +
                    "/temp" +
                    "?item=${MotorRearLeftTemp.ID}" +
                    "&warning=$reportedMrltTempWarning" +
                    "&value=$reportedMrltTempValue")

        }
        if (reportedMrltTempWarning != MotorRearLeftTemp.warning) {
            reportedMrltTempWarning = MotorRearLeftTemp.warning
            doNonBlockingRequest("http://" +
                    "$nanohttpClientIp:" +
                    "$nanohttpClientPort" +
                    "/temp" +
                    "?item=${MotorRearLeftTemp.ID}" +
                    "&warning=$reportedMrltTempWarning" +
                    "&value=$reportedMrltTempValue")
        }*/
    }

    private fun informClient(hardwareID: String, warning: String, value: Int){
        doNonBlockingRequest(
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
        showMessage(msgType = TYPE_CRITICAL,
            title = "TEMPERATURE CRON JOB",
            body = "{ ${hardware?.simpleName ?: "No Name Class"} } Temp Value: $value")
    }
}