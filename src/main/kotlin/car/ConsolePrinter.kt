package car

import org.slf4j.LoggerFactory
import kotlin.reflect.KClass
import car.LoggerTypes.*

/* Use enum class for logger types to specify the available values*/
enum class LoggerTypes {
    INFO,
    WARNING,
    CRITICAL
}

private const val ENABLE_INFO_MESSAGES = true
private const val ENABLE_WARNING_MESSAGES = true
private const val ENABLE_CRITICAL_MESSAGES = true

fun showMessage(msgType: LoggerTypes = INFO, klass: KClass<*>, body: String) {

    val logger = LoggerFactory.getLogger(klass.java)

    if(msgType == INFO && ENABLE_INFO_MESSAGES)
        logger.info(body)
    else if(msgType == WARNING && ENABLE_WARNING_MESSAGES)
        logger.warn(body)
    else if(msgType == CRITICAL && ENABLE_CRITICAL_MESSAGES)
        logger.error(body)
}