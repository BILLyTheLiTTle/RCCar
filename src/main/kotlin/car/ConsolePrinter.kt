package car

import car.LoggerTypes.*
import org.slf4j.Logger

/* Use enum class for logger types to specify the available values*/
enum class LoggerTypes {
    INFO,
    WARNING,
    CRITICAL
}

private const val ENABLE_INFO_MESSAGES = true
private const val ENABLE_WARNING_MESSAGES = true
private const val ENABLE_CRITICAL_MESSAGES = true

fun showMessage(msgType: LoggerTypes = INFO, logger: Logger, body: String) {

    if(msgType == INFO && ENABLE_INFO_MESSAGES)
        logger.info(body)
    else if(msgType == WARNING && ENABLE_WARNING_MESSAGES)
        logger.warn(body)
    else if(msgType == CRITICAL && ENABLE_CRITICAL_MESSAGES)
        logger.error(body)
}