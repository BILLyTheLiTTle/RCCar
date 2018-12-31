package car

import org.slf4j.LoggerFactory
import kotlin.reflect.KClass

const val TYPE_INFO = "info"
const val TYPE_WARNING = "warning"
const val TYPE_CRITICAL = "critical"

private const val ENABLE_INFO_MESSAGES = true
private const val ENABLE_WARNING_MESSAGES = true
private const val ENABLE_CRITICAL_MESSAGES = true

fun showMessage(msgType: String = TYPE_INFO, klass: KClass<*>, body: String) {

    val logger = LoggerFactory.getLogger(klass.java)

    if(msgType == TYPE_INFO && ENABLE_INFO_MESSAGES)
        logger.info(body)
    else if(msgType == TYPE_WARNING && ENABLE_WARNING_MESSAGES)
        logger.warn(body)
    else if(msgType == TYPE_CRITICAL && ENABLE_CRITICAL_MESSAGES)
        logger.error(body)
}