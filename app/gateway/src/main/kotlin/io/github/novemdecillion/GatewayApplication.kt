package io.github.novemdecillion

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.web.ServerProperties
import org.springframework.boot.autoconfigure.web.WebProperties
import org.springframework.boot.autoconfigure.web.WebProperties.Resources
import org.springframework.boot.autoconfigure.web.reactive.error.DefaultErrorWebExceptionHandler
import org.springframework.boot.runApplication
import org.springframework.boot.web.reactive.error.ErrorAttributes
import org.springframework.context.ApplicationContext
import org.springframework.core.annotation.Order
import org.springframework.http.MediaType
import org.springframework.http.codec.ServerCodecConfigurer
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.*
import org.springframework.web.reactive.function.server.RequestPredicates.all
import org.springframework.web.reactive.function.server.RouterFunctions.route


@SpringBootApplication
class GatewayApplication


@Order(-2)
@Component
class GlobalErrorWebExceptionHandler(
  errorAttributes: ErrorAttributes, resources: Resources,
  serverProperties: ServerProperties,
  webProperties: WebProperties,
  resourceProperties: WebProperties.Resources,
  serverCodecConfigurer: ServerCodecConfigurer,
  applicationContext: ApplicationContext
) : DefaultErrorWebExceptionHandler(errorAttributes, resources, serverProperties.error, applicationContext) {
  val welcomePage = if(resourceProperties.hasBeenCustomized()) {
    resourceProperties.staticLocations
  } else {
    webProperties.resources.staticLocations
  }
    .map { location -> applicationContext.getResource("${location}index.html") }
    .firstOrNull { it.isReadable }

  init {
    setMessageWriters(serverCodecConfigurer.writers)
    setMessageReaders(serverCodecConfigurer.readers)
  }

  override fun getRoutingFunction(errorAttributes: ErrorAttributes): RouterFunction<ServerResponse> {
    return route(acceptsTextHtml()) { request ->
      val error = getErrorAttributes(request, getErrorAttributeOptions(request, MediaType.TEXT_HTML))
      val errorStatus = getHttpStatus(error)
      if ((errorStatus == 404) && (welcomePage != null)) {
        ServerResponse.ok().contentType(MediaType.TEXT_HTML).bodyValue(welcomePage!!)
      } else {
        renderErrorView(request)
      }
    }.andRoute(all(), this::renderErrorResponse);
  }
}


//class ResourceNotErrorWebFilter : WebFilter {
////  @Autowired
////  var objectMapper: ObjectMapper? = null
//
////  @Autowired
////  var defaultHandler: DefaultHandler? = null
//
//  override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
//    if (exchange.response.statusCode == HttpStatus.NOT_FOUND) {
//      return chain.filter(
//        exchange.mutate().request(exchange.request.mutate().path("/index.html").build()).build()
//      )
//    }
//    return chain.filter(exchange)
//  }
//}


fun main(args: Array<String>) {
  runApplication<GatewayApplication>(*args)
}
