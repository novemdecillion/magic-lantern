package io.github.novemdecillion

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.stereotype.Controller
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Configuration

import org.springframework.scheduling.annotation.EnableScheduling

@Controller
@SpringBootApplication
@ConfigurationPropertiesScan
class ServerApplication

fun main(args: Array<String>) {
  runApplication<ServerApplication>(*args)
}
