package car

const val TYPE_INFO = "info"
const val TYPE_WARNING = "warning"
const val TYPE_CRITICAL = "critical"

private const val ENABLE_INFO_MESSAGES = true
private const val ENABLE_WARNING_MESSAGES = true
private const val ENABLE_CRITICAL_MESSAGES = true

fun showMessage(msgType: String = TYPE_INFO, title: String, body: String) {
    if(msgType == TYPE_INFO && ENABLE_INFO_MESSAGES)
        println("=== $title ===\n$body\n======\n")
    else if(msgType == TYPE_WARNING && ENABLE_WARNING_MESSAGES)
        println("=== $title ===\n$body\n======\n")
    else if(msgType == TYPE_CRITICAL && ENABLE_CRITICAL_MESSAGES)
        println("=== $title ===\n$body\n======\n")
}