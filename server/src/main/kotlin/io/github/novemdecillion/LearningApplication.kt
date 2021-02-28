package io.github.novemdecillion

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.router

import io.github.novemdecillion.adapter.web.AppSlideProperties
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.stereotype.Controller
import org.springframework.web.reactive.function.server.EntityResponse
//import org.springframework.cloud.gateway.webflux.ProxyExchange


@Controller
@SpringBootApplication
@ConfigurationPropertiesScan
class LearningApplication {
  @Bean
  fun testRouter(prop: AppSlideProperties): RouterFunction<ServerResponse> = router {
    GET("hello") {
      EntityResponse.fromObject("I'm fine!").build()
    }
  }
}

fun main(args: Array<String>) {
  runApplication<LearningApplication>(*args)
}
