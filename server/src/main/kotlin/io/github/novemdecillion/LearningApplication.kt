package io.github.novemdecillion

import org.neo4j.driver.Driver
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.data.neo4j.Neo4jDataAutoConfiguration
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.data.neo4j.core.transaction.ReactiveNeo4jTransactionManager

import org.springframework.data.neo4j.core.ReactiveDatabaseSelectionProvider

import org.springframework.transaction.ReactiveTransactionManager

import org.springframework.data.neo4j.repository.config.ReactiveNeo4jRepositoryConfigurationExtension
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.router

import graphql.Scalars
import graphql.schema.*
import io.github.novemdecillion.slide.web.AppSlideProperties
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafProperties
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.context.ApplicationContext
import org.springframework.core.Ordered
import org.springframework.core.io.Resource
import org.springframework.stereotype.Controller
import org.springframework.web.reactive.config.EnableWebFlux
import org.springframework.web.reactive.function.server.EntityResponse
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver
import org.thymeleaf.templatemode.TemplateMode
import java.nio.charset.StandardCharsets
import java.util.function.UnaryOperator
//import org.springframework.cloud.gateway.webflux.ProxyExchange

import org.springframework.http.ResponseEntity
import org.springframework.security.config.Customizer.withDefaults

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import reactor.core.publisher.Mono
import java.lang.Exception
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity.AuthorizeExchangeSpec
import org.springframework.security.config.web.server.ServerHttpSecurity.OAuth2ResourceServerSpec
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter

import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.header.XFrameOptionsServerHttpHeadersWriter


@Controller
@SpringBootApplication
@ConfigurationPropertiesScan
class LearningApplication {
  // see: https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-2.4.0-M2-Release-Notes#neo4j-1
  @Bean(ReactiveNeo4jRepositoryConfigurationExtension.DEFAULT_TRANSACTION_MANAGER_BEAN_NAME)
  fun reactiveTransactionManager(
    driver: Driver,
    databaseNameProvider: ReactiveDatabaseSelectionProvider
  ): ReactiveTransactionManager? {
    return ReactiveNeo4jTransactionManager(driver, databaseNameProvider)
  }

  @Bean
  fun testRouter(prop: AppSlideProperties): RouterFunction<ServerResponse> = router {
    GET("hello") {
      EntityResponse.fromObject("I'm fine!").build()
    }
  }

  @Bean
  fun systemTemplateResolver(applicationContext: ApplicationContext): SpringResourceTemplateResolver {
    val resolver = SpringResourceTemplateResolver()
    resolver.setApplicationContext(applicationContext)
    resolver.prefix = ThymeleafProperties.DEFAULT_PREFIX
    resolver.suffix = ThymeleafProperties.DEFAULT_SUFFIX
    resolver.templateMode = TemplateMode.HTML
    resolver.characterEncoding = StandardCharsets.UTF_8.displayName()
    resolver.isCacheable = true
    resolver.order = Ordered.LOWEST_PRECEDENCE
    resolver.checkExistence = true
    return resolver
  }

//  @RequestMapping("/**")
//  fun proxy(proxy: ProxyExchange<ByteArray?>): Mono<ResponseEntity<ByteArray?>>? {
//    println(proxy.path())
//    return proxy.uri("http://localhost:4200${proxy.path()}").forward()
//  }

  @Bean
  fun springSecurityFilterChain(http: ServerHttpSecurity, jwtDecoder: ReactiveJwtDecoder): SecurityWebFilterChain {
    http
      .authorizeExchange().anyExchange().authenticated()
      .and()
      .formLogin().disable()
      .oauth2Login()
      .and()
      .oauth2Client()
      .and()
      .csrf().disable()
      .headers()
      // デフォルトのDENYだとiframeに教材を埋め込み表示できないので変更
      .frameOptions().mode(XFrameOptionsServerHttpHeadersWriter.Mode.SAMEORIGIN)

    http.oauth2ResourceServer { server: OAuth2ResourceServerSpec ->
      server.jwt { jwt: OAuth2ResourceServerSpec.JwtSpec ->
        jwt.jwtDecoder(jwtDecoder)
      }
    }

    return http.build()
  }

}

fun main(args: Array<String>) {
  runApplication<LearningApplication>(*args)
}
