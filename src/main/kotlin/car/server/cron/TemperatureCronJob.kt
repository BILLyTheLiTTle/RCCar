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

    // rear left motor temp data
    //var reportedMrltTempValue = EngineSystem.EMPTY_INT
    var reportedMrltTempWarning = WARNING_TYPE_NOTHING
    var reportedMrrtTempWarning = WARNING_TYPE_NOTHING
    var reportedMfltTempWarning = WARNING_TYPE_NOTHING
    var reportedMfrtTempWarning = WARNING_TYPE_NOTHING
    var reportedHbrTempWarning = WARNING_TYPE_NOTHING
    var reportedHbfTempWarning = WARNING_TYPE_NOTHING

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

            informClient(MotorRearLeftTemperatureImpl.ID, reportedMrltTempWarning, primaryTemp)

            if(reportedMrltTempWarning == WARNING_TYPE_HIGH)
                printTempInfo(MotorRearLeftTemperatureImpl::class, primaryTemp)
        }

        primaryTemp = MotorRearRightTemperatureImpl.value
        if (reportedMrrtTempWarning != MotorRearRightTemperatureImpl.warning) {
            reportedMrrtTempWarning = MotorRearRightTemperatureImpl.warning

            informClient(MotorRearRightTemperatureImpl.ID, reportedMrrtTempWarning, primaryTemp)

            if(reportedMrrtTempWarning == WARNING_TYPE_HIGH)
                printTempInfo(MotorRearRightTemperatureImpl::class, primaryTemp)
        }

        primaryTemp = MotorFrontLeftTemperatureImpl.value
        if (reportedMfltTempWarning != MotorFrontLeftTemperatureImpl.warning) {
            reportedMfltTempWarning = MotorFrontLeftTemperatureImpl.warning

            informClient(MotorFrontLeftTemperatureImpl.ID, reportedMfltTempWarning, primaryTemp)

            if(reportedMfltTempWarning == WARNING_TYPE_HIGH)
                printTempInfo(MotorFrontLeftTemperatureImpl::class, primaryTemp)
        }

        primaryTemp = MotorFrontRightTemperatureImpl.value
        if (reportedMfrtTempWarning != MotorFrontRightTemperatureImpl.warning) {
            reportedMfrtTempWarning = MotorFrontRightTemperatureImpl.warning

            informClient(MotorFrontRightTemperatureImpl.ID, reportedMfrtTempWarning, primaryTemp)

            if(reportedMfrtTempWarning == WARNING_TYPE_HIGH)
                printTempInfo(MotorFrontRightTemperatureImpl::class, primaryTemp)
        }

        primaryTemp = HBridgeRearTemperatureImpl.value
        if (reportedHbrTempWarning != HBridgeRearTemperatureImpl.warning) {
            reportedHbrTempWarning = HBridgeRearTemperatureImpl.warning

            informClient(HBridgeRearTemperatureImpl.ID, reportedHbrTempWarning, primaryTemp)

            if(reportedHbrTempWarning == WARNING_TYPE_HIGH)
                printTempInfo(HBridgeRearTemperatureImpl::class, primaryTemp)
        }

        primaryTemp = HBridgeFrontTemperatureImpl.value
        if (reportedHbfTempWarning != HBridgeFrontTemperatureImpl.warning) {
            reportedHbfTempWarning = HBridgeFrontTemperatureImpl.warning

            informClient(HBridgeFrontTemperatureImpl.ID, reportedHbfTempWarning, primaryTemp)

            if(reportedHbfTempWarning == WARNING_TYPE_HIGH)
                printTempInfo(HBridgeFrontTemperatureImpl::class, primaryTemp)
        }
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