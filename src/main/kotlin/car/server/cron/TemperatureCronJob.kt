package car.server.cron

import car.controllers.temperatures.*
import car.server.EngineSystem
import car.server.doNonBlockingRequest
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import kotlin.reflect.KClass

@Configuration
@EnableAsync
@EnableScheduling
class TemperaturesCronJob {

    private val temperatureDiff = 9

    private val TEMP_URI = "/temp"
    private val PARAM_KEY_ITEM = "item"
    private val PARAM_KEY_WARNING = "warning"
    private val PARAM_KEY_VALUE = "value"

    private var primaryTemp = EngineSystem.EMPTY_INT

    var hardwareItems = arrayOf(MotorRearLeftTemperatureImpl,MotorRearRightTemperatureImpl,
        MotorFrontLeftTemperatureImpl, MotorFrontRightTemperatureImpl,
        HBridgeRearTemperatureImpl, HBridgeFrontTemperatureImpl,
        RaspberryPiTemperatureImpl)

    var reportedTempWarnings = arrayOf(WARNING_TYPE_NOTHING, WARNING_TYPE_NOTHING,
        WARNING_TYPE_NOTHING, WARNING_TYPE_NOTHING,
        WARNING_TYPE_NOTHING, WARNING_TYPE_NOTHING,
        WARNING_TYPE_NOTHING)

    @Scheduled(initialDelay = 2000, fixedDelay = 420)
    fun checkPrimaryTemps(){

        for(i in 0 until hardwareItems.size) {
            primaryTemp = hardwareItems[i].value
            if(reportedTempWarnings[i] != hardwareItems[i].warning) {
                reportedTempWarnings[i] = hardwareItems[i].warning

                informClient(hardwareItems[i].ID, reportedTempWarnings[i], primaryTemp)

                if(reportedTempWarnings[i] == WARNING_TYPE_HIGH)
                    printTempInfo(hardwareItems[i]::class, primaryTemp)
            }
        }

        // rear left motor
        /* primaryTemp = MotorRearLeftTemperatureImpl.value
         The following commented code block will be enabled if I want to notify the client
            whenever the value changes, also.

         */
        /*if ((reportedMrltTempValue > primaryTemp + temperatureDiff)
            || reportedMrltTempValue < primaryTemp + temperatureDiff) {
            reportedMrltTempValue = primaryTemp
            doNonBlockingRequest("http://" +
                    "${EngineSystem.nanohttpClientIp}:" +
                    "${EngineSystem.nanohttpClientPort}" +
                    "/temp" +
                    "?item=${MotorRearLeftTemp.ID}" +
                    "&warning=$reportedMrltTempWarning" +
                    "&value=$reportedMrltTempValue")

        }
        if (reportedMrltTempWarning != MotorRearLeftTemp.warning) {
            reportedMrltTempWarning = MotorRearLeftTemp.warning
            doNonBlockingRequest("http://" +
                    "${EngineSystem.nanohttpClientIp}:" +
                    "${EngineSystem.nanohttpClientPort}" +
                    "/temp" +
                    "?item=${MotorRearLeftTemp.ID}" +
                    "&warning=$reportedMrltTempWarning" +
                    "&value=$reportedMrltTempValue")
        }*/
    }

    private fun informClient(hardwareID: String, warning: String, value: Int){
        doNonBlockingRequest(
            "http://" +
                    "${EngineSystem.nanohttpClientIp}:" +
                    "${EngineSystem.nanohttpClientPort}" +
                    TEMP_URI +
                    "?$PARAM_KEY_ITEM=$hardwareID" +
                    "&$PARAM_KEY_WARNING=$warning" +
                    "&$PARAM_KEY_VALUE=$value"
        )
    }

    private fun printTempInfo(hardware: KClass<out Temperature>?, value: Int) {
        println("{${hardware?.simpleName ?: "No Name Class"}} Temp Value: $value\n")
    }
}