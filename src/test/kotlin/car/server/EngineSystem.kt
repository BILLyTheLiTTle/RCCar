package car.server

/* For more info about KotlinTest see
    here (https://github.com/kotlintest/kotlintest/blob/master/doc/reference.md)
    and here (https://github.com/kotlintest/kotlintest/blob/master/kotlintest-samples/kotlintest-samples-spring/src/test/kotlin/io/kotlintest/spring/MyBean.kt)
 */

import io.kotlintest.shouldBe
import io.kotlintest.shouldNotBe
import io.kotlintest.specs.WordSpec
import io.kotlintest.spring.SpringListener
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.test.context.ContextConfiguration

class MyBean

@Configuration
class TestConfiguration {
    @Bean
    fun myBean() = EngineSystem()
}

@ContextConfiguration(classes = [(TestConfiguration::class)])
class SpringTest : WordSpec() {

    override fun listeners() = listOf(SpringListener)

    @Autowired
    var bean: EngineSystem? = null

    init {
        "Spring Extension" should {
            "have wired up the bean" {
                bean shouldNotBe null
            }
        }
        "Stop Engine" should {
            "be successful" {
                bean?.stopEngine() shouldBe EngineSystem.SUCCESS
            }
        }
    }
}