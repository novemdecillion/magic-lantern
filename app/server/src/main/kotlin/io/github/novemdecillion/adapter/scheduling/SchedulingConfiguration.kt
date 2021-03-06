package io.github.novemdecillion.adapter.scheduling

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling

const val SPRING_SCHEDULING_ENABLED_KEY = "spring.scheduling.enabled"

@Configuration
@EnableScheduling
@ConditionalOnProperty(value = [SPRING_SCHEDULING_ENABLED_KEY], matchIfMissing = true, havingValue = "true")
class SchedulingConfiguration
