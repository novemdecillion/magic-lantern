package io.github.novemdecillion

import org.junit.jupiter.api.Test
import reactor.cache.CacheMono
import reactor.core.publisher.Mono

class ReactorTest {
  @Test
  fun cacheMono() {
    val mono = Mono.just("aaaa").doOnNext { println("next") }.cache()

    mono.subscribe { println(it) }
    mono.subscribe { println(it) }
    mono.subscribe { println(it) }
    mono.subscribe { println(it) }
    mono.subscribe { println(it) }
  }
}