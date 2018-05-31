package car.server.cron

import car.controllers.temperatures.MotorRearLeftTemperatureImpl
import car.controllers.temperatures.WARNING_TYPE_NOTHING
import car.server.EngineSystem
import car.server.doNonBlockingRequest
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled

@Configuration
@EnableAsync
@EnableScheduling
class TemperaturesCronJob {

    private val temperatureDiff = 9

    private val SUB_URL = "/temp"
    private val PARAM_KEY_ITEM = "item"
    private val PARAM_KEY_WARNING = "warning"
    private val PARAM_KEY_VALUE = "value"

    private var primaryTemp = EngineSystem.EMPTY_INT

    // rear left motor temp data
    //var reportedMrltTempValue = EngineSystem.EMPTY_INT
    var reportedMrltTempWarning = WARNING_TYPE_NOTHING

    @Scheduled(initialDelay = 2000, fixedDelay = 420)
    fun checkPrimaryTemps(){
        // rear left motor
        primaryTemp = MotorRearLeftTemperatureImpl.value
        /* The following commented code block will be enabled if I want to notify the client
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
        if (reportedMrltTempWarning != MotorRearLeftTemperatureImpl.warning) {
            reportedMrltTempWarning = MotorRearLeftTemperatureImpl.warning
            doNonBlockingRequest(
                "http://" +
                        "${EngineSystem.nanohttpClientIp}:" +
                        "${EngineSystem.nanohttpClientPort}" +
                        SUB_URL +
                        "?$PARAM_KEY_ITEM=${MotorRearLeftTemperatureImpl.ID}" +
                        "&$PARAM_KEY_WARNING=$reportedMrltTempWarning" +
                        "&$PARAM_KEY_VALUE=$primaryTemp"
            )
        }
    }
}