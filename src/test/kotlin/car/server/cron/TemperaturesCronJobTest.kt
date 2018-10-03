package car.server.cron

import car.controllers.basic.EngineImpl
import car.server.EngineSystem
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
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
internal class TemperaturesCronJobTest {
    @SpyBean
    private val task: TemperaturesCronJob? = null

    @BeforeAll
    internal fun setUp() {

    }

    // checkTemps

    fun `report normal temperature for rear left motor`() {

    }
    fun `report medium temperature for rear left motor`() {

    }
    fun `report high temperature for rear left motor`() {

    }

    fun `report normal temperature for rear right motor`() {

    }
    fun `report medium temperature for rear right motor`() {

    }
    fun `report high temperature for rear right motor`() {

    }

    fun `report normal temperature for rear h bridge`() {

    }
    fun `report medium temperature for rear h bridge`() {

    }
    fun `report high temperature for rear h bridge`() {

    }

    fun `report normal temperature for front h bridge`() {

    }
    fun `report medium temperature for front h bridge`() {

    }
    fun `report high temperature for frotn h bridge`() {

    }

    fun `report normal temperature for raspberry pi`() {

    }
    fun `report medium temperature for raspberry pi`() {
    }
    fun `report high temperature for raspberry pi`() {

    }

    fun `report normal temperature for batteries`() {

    }
    fun `report medium temperature for batteries`() {

    }
    fun `report high temperature for batteries`() {

    }

    fun `report normal temperature for shift registers`() {

    }
    fun `report medium temperature for shift registers`() {

    }
    fun `report high temperature for shift registers`() {

    }
}