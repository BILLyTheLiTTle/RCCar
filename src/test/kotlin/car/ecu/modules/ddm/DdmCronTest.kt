package car.ecu.modules.ddm

import car.cockpit.engine.EngineImpl
import car.cockpit.engine.EngineSystem
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.SpyBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KProperty
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible


@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class DdmCronTest {

    @SpyBean
    private val task: DdmCron? = null

    private var nanohttpClientIpMemProp: KMutableProperty<*>? = null
    private var wasClientOnlineMemProp: KMutableProperty<*>? = null
    private var rstCounterMemProp: KProperty<*>? = null

    @BeforeEach
    internal fun setUp() {
        nanohttpClientIpMemProp = EngineSystem.Companion::class.memberProperties
            .filterIsInstance<KMutableProperty<*>>()
            .firstOrNull { it.name =="nanohttpClientIp" }
        nanohttpClientIpMemProp?.isAccessible = true
        wasClientOnlineMemProp = DdmCron::class.memberProperties
            .filterIsInstance<KMutableProperty<*>>()
            .firstOrNull { it.name =="wasClientOnline" }
        wasClientOnlineMemProp?.isAccessible = true
        rstCounterMemProp = DdmCron::class.memberProperties
            .firstOrNull { it.name =="maxResetCounter" }
        rstCounterMemProp?.isAccessible = true
    }

    // checkClientStatus
    @Test
    fun `function called from scheduler`() {
        // TODO how to implement this kind of test?
    }

    /*@Test
    fun `engine state on with wrong ip format`() {
        EngineImpl.engineState = true
        nanohttpClientIpMemProp?.setter?.call(EngineSystem.Companion, "oops")
        val ret = task?.checkClientStatus()
        assertThat(ret).isEqualTo(0) // IP_ERROR
    }*/

    @Test
    fun `engine state on with client still online`() {
        EngineImpl.engineState = true
        nanohttpClientIpMemProp?.setter?.call(EngineSystem.Companion, "localhost")
        wasClientOnlineMemProp?.setter?.call(task, true)
        val ret = task?.checkClientStatus()
        assertThat(ret).isEqualTo(2) // CLIENT_STILL_ONLINE
    }

    @Test
    fun `engine state on with client still online with message`() {
        EngineImpl.engineState = true
        nanohttpClientIpMemProp?.setter?.call(EngineSystem.Companion, "localhost")
        var i = 0
        var ret: Int? = -100
        while (i <= rstCounterMemProp?.getter?.call(task!!) as Int) {
            ret = task!!.checkClientStatus()
            i++
        }
        assertThat(ret).isEqualTo(2) // CLIENT_STILL_ONLINE
    }

    @Test
    fun `engine state on with client came online`() {
        EngineImpl.engineState = true
        nanohttpClientIpMemProp?.setter?.call(EngineSystem.Companion, "localhost")
        wasClientOnlineMemProp?.setter?.call(task, false)
        val ret = task?.checkClientStatus()
        assertThat(ret).isEqualTo(3) // CLIENT_CAME_ONLINE

    }

    @Test
    fun `engine state on with client not found`() {
        EngineImpl.engineState = true
        nanohttpClientIpMemProp?.setter?.call(EngineSystem.Companion, "127.0.0.0")
        wasClientOnlineMemProp?.setter?.call(task, true)
        val ret = task?.checkClientStatus()
        assertThat(ret).isEqualTo(4) // CLIENT_NOT_FOUND

    }

    @Test
    fun `engine state on with client still not found`() {
        EngineImpl.engineState = true
        nanohttpClientIpMemProp?.setter?.call(EngineSystem.Companion, "127.0.0.0")
        wasClientOnlineMemProp?.setter?.call(task, false)
        val ret = task?.checkClientStatus()
        assertThat(ret).isEqualTo(5) // CLIENT_STILL_NOT_FOUND

    }

    @Test
    fun `engine state on with client still not found with message`() {
        EngineImpl.engineState = true
        nanohttpClientIpMemProp?.setter?.call(EngineSystem.Companion, "127.0.0.0")
        wasClientOnlineMemProp?.setter?.call(task, false)
        var i = 0
        var ret: Int? = -100
        while (i <= rstCounterMemProp?.getter?.call(task!!) as Int) {
            ret = task!!.checkClientStatus()
            i++
        }
        assertThat(ret).isEqualTo(5) // CLIENT_STILL_NOT_FOUND

    }

    @Test
    fun `engine state off`() {
        EngineImpl.engineState = false
        val ret = task?.checkClientStatus()
        assertThat(ret).isEqualTo(1) // ENGINE_OFF_STATE
    }
}