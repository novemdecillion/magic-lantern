package io.github.novemdecillion

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.stereotype.Controller
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Configuration

import org.springframework.scheduling.annotation.EnableScheduling




//import org.springframework.cloud.gateway.webflux.ProxyExchange

@Configuration
@EnableScheduling
@ConditionalOnProperty(value = ["spring.scheduling.enabled"], matchIfMissing = true, havingValue = "true")
class SchedulingConfiguration

@Controller
@SpringBootApplication
@ConfigurationPropertiesScan
class ServerApplication {
//  @Bean
//  fun testRouter(prop: AppSlideProperties): RouterFunction<ServerResponse> = router {
//    GET("hello") {
//      EntityResponse.fromObject("I'm fine!").build()
//    }
//  }
}

fun main(args: Array<String>) {
  runApplication<ServerApplication>(*args)
}
