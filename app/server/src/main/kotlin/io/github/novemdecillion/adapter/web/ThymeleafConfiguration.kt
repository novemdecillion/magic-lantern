package io.github.novemdecillion.adapter.web

import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafProperties
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver
import org.thymeleaf.templatemode.TemplateMode
import java.nio.charset.StandardCharsets

@Configuration
class ThymeleafConfiguration {
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
}