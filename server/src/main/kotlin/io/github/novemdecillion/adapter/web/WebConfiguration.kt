package io.github.novemdecillion.adapter.web

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.cloud.gateway.handler.RoutePredicateHandlerMapping
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component
import org.springframework.web.reactive.HandlerMapping
import org.springframework.web.reactive.config.ResourceHandlerRegistry
import org.springframework.web.reactive.config.WebFluxConfigurationSupport
import org.springframework.web.reactive.config.WebFluxConfigurer
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping
import javax.annotation.PostConstruct

@Configuration
class WebConfiguration: WebFluxConfigurer {

  override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
    registry.setOrder(10)
  }
}