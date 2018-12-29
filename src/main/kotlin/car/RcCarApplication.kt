package car

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class RcCarApplication

fun main(args: Array<String>) {
    runApplication<RcCarApplication>(*args)
}