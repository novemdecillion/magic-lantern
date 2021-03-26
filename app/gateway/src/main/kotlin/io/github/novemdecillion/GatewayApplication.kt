package io.github.novemdecillion

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
class GatewayApplication

fun main(args: Array<String>) {
  runApplication<GatewayApplication>(*args)
}
