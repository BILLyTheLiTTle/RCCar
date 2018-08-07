package car.server.cron

import car.controllers.basic.EngineImpl
import car.server.EngineSystem
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.*
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.boot.test.mock.mockito.SpyBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import kotlin.reflect.jvm.javaField


@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class ConnectionCronJobTest {

    @SpyBean
    private val task: ConnectionCronJob? = null

    // checkClientStatus
    @Test
    fun `function called from scheduler`() {
        // TODO how to implement this kind of test?
    }
    @Test
    fun `engine state on with wrong ip format`() {
        EngineImpl.engineState = true
        val ip = EngineSystem.Companion::nanohttpClientIp.javaField
        ip?.isAccessible = true
        ip?.set(String::class, "oops")
        val ret = task?.checkClientStatus()
        assertThat(ret).isEqualTo(0) // IP_ERROR
    }
    @Test
    fun `engine state on with client still online`() {
        EngineImpl.engineState = true
        val ip = EngineSystem.Companion::nanohttpClientIp.javaField
        ip?.isAccessible = true
        ip?.set(String::class, "localhost")
        val isClientOnline = ConnectionCronJob::class.java.getDeclaredField("isClientOnline")
        isClientOnline?.isAccessible = true
        isClientOnline?.setBoolean(task, true)
        val wasClientOnline = ConnectionCronJob::class.java.getDeclaredField("wasClientOnline")
        wasClientOnline?.isAccessible = true
        wasClientOnline?.setBoolean(task, true)
        val ret = task?.checkClientStatus()
        assertThat(ret).isEqualTo(2) // CLIENT_STILL_ONLINE
    }
    @Test
    fun `engine state on with client still online with message`() {
        EngineImpl.engineState = true
        val ip = EngineSystem.Companion::nanohttpClientIp.javaField
        ip?.isAccessible = true
        ip?.set(String::class, "localhost")
        val rstCounter = ConnectionCronJob::class.java.getDeclaredField("maxResetCounter")
        rstCounter?.isAccessible = true
        var i = 0
        var ret: Int? = -100
        while (i <= rstCounter!!.getInt(task!!)) {
            ret = task.checkClientStatus()
            i++
        }
        assertThat(ret).isEqualTo(2) // CLIENT_STILL_ONLINE
    }
    @Test
    fun `engine state on with client came online`() {
        EngineImpl.engineState = true
        val ip = EngineSystem.Companion::nanohttpClientIp.javaField
        ip?.isAccessible = true
        ip?.set(String::class, "localhost")
        val isClientOnline = ConnectionCronJob::class.java.getDeclaredField("isClientOnline")
        isClientOnline?.isAccessible = true
        isClientOnline?.setBoolean(task, true)
        val wasClientOnline = ConnectionCronJob::class.java.getDeclaredField("wasClientOnline")
        wasClientOnline?.isAccessible = true
        wasClientOnline?.setBoolean(task, false)
        val ret = task?.checkClientStatus()
        assertThat(ret).isEqualTo(3) // CLIENT_CAME_ONLINE

    }
    @Test
    fun `engine state on with client not found`() {
        EngineImpl.engineState = true
        val ip = EngineSystem.Companion::nanohttpClientIp.javaField
        ip?.isAccessible = true
        ip?.set(String::class, "127.0.0.0")
        val isClientOnline = ConnectionCronJob::class.java.getDeclaredField("isClientOnline")
        isClientOnline?.isAccessible = true
        isClientOnline?.setBoolean(task, false)
        val wasClientOnline = ConnectionCronJob::class.java.getDeclaredField("wasClientOnline")
        wasClientOnline?.isAccessible = true
        wasClientOnline?.setBoolean(task, true)
        val ret = task?.checkClientStatus()
        assertThat(ret).isEqualTo(4) // CLIENT_NOT_FOUND

    }
    @Test
    fun `engine state on with client still not found`() {
        EngineImpl.engineState = true
        val ip = EngineSystem.Companion::nanohttpClientIp.javaField
        ip?.isAccessible = true
        ip?.set(String::class, "127.0.0.0")
        val isClientOnline = ConnectionCronJob::class.java.getDeclaredField("isClientOnline")
        isClientOnline?.isAccessible = true
        isClientOnline?.setBoolean(task, false)
        val wasClientOnline = ConnectionCronJob::class.java.getDeclaredField("wasClientOnline")
        wasClientOnline?.isAccessible = true
        wasClientOnline?.setBoolean(task, false)
        val ret = task?.checkClientStatus()
        assertThat(ret).isEqualTo(5) // CLIENT_STILL_NOT_FOUND

    }
    @Test
    fun `engine state on with client still not found with message`() {
        EngineImpl.engineState = true
        val ip = EngineSystem.Companion::nanohttpClientIp.javaField
        ip?.isAccessible = true
        ip?.set(String::class, "127.0.0.0")
        val isClientOnline = ConnectionCronJob::class.java.getDeclaredField("isClientOnline")
        isClientOnline?.isAccessible = true
        isClientOnline?.setBoolean(task, false)
        val wasClientOnline = ConnectionCronJob::class.java.getDeclaredField("wasClientOnline")
        wasClientOnline?.isAccessible = true
        wasClientOnline?.setBoolean(task, false)
        val rstCounter = ConnectionCronJob::class.java.getDeclaredField("maxResetCounter")
        rstCounter?.isAccessible = true
        var i = 0
        var ret: Int? = -100
        while (i <= rstCounter!!.getInt(task!!)) {
            ret = task.checkClientStatus()
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