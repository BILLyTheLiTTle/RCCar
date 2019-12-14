package car

import org.springframework.boot.autoconfigure.AutoConfigureAfter
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletAutoConfiguration
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer


@Configuration
@AutoConfigureAfter(DispatcherServletAutoConfiguration::class)
class StaticResourceConfiguration : WebMvcConfigurer {
    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        val myExternalFilePath = "file:///home/little/Documents/ltl.txt"
        registry.addResourceHandler("/m/**").addResourceLocations(myExternalFilePath)
    }
}