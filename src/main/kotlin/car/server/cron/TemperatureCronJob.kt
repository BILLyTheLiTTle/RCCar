package car.server.cron

import car.TYPE_CRITICAL
import car.controllers.basic.EngineImpl
import car.controllers.temperatures.*
import car.server.EngineSystem
import car.server.doNonBlockingRequest
import car.showMessage
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import kotlin.reflect.KClass

@Configuration
@EnableAsync
@EnableScheduling
class TemperaturesCronJob {

    private val TEMP_URI = "/temp"
    private val PARAM_KEY_ITEM = "item"
    private val PARAM_KEY_WARNING = "warning"
    private val PARAM_KEY_VALUE = "value"

    private var primaryTemp = EngineSystem.EMPTY_INT

    var hardwareItems = arrayOf(MotorRearLeftTemperatureImpl,MotorRearRightTemperatureImpl,
        MotorFrontLeftTemperatureImpl, MotorFrontRightTemperatureImpl,
        HBridgeRearTemperatureImpl, HBridgeFrontTemperatureImpl,
        RaspberryPiTemperatureImpl,
        BatteriesTemperatureImpl,
        ShiftRegistersTemperatureImpl)

    var reportedTempWarnings = arrayOf(Temperature.WARNING_TYPE_NOTHING, Temperature.WARNING_TYPE_NOTHING,
        Temperature.WARNING_TYPE_NOTHING, Temperature.WARNING_TYPE_NOTHING,
        Temperature.WARNING_TYPE_NOTHING, Temperature.WARNING_TYPE_NOTHING,
        Temperature.WARNING_TYPE_NOTHING,
        Temperature.WARNING_TYPE_NOTHING,
        Temperature.WARNING_TYPE_NOTHING)

    @Scheduled(initialDelay = 2000, fixedDelay = 420)
    fun checkTemps(){

        if (EngineImpl.engineState) {
            for (i in 0 until hardwareItems.size) {
                primaryTemp = hardwareItems[i].value
                if (reportedTempWarnings[i] != hardwareItems[i].warning) {
                    reportedTempWarnings[i] = hardwareItems[i].warning

                    informClient(hardwareItems[i].ID, reportedTempWarnings[i], primaryTemp)

                    if (reportedTempWarnings[i] == Temperature.WARNING_TYPE_HIGH)
                        printHighTempInfo(hardwareItems[i]::class, primaryTemp)
                }
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

    private fun printHighTempInfo(hardware: KClass<out Temperature>?, value: Int) {
        showMessage(msgType = TYPE_CRITICAL,
            title = "TEMPERATURE CRON JOB",
            body = "{ ${hardware?.simpleName ?: "No Name Class"} } Temp Value: $value")
    }
}