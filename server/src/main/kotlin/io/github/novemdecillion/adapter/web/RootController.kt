package io.github.novemdecillion.adapter.web;

import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties
import org.springframework.security.oauth2.client.registration.ClientRegistration
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository
import org.springframework.security.oauth2.client.registration.InMemoryReactiveClientRegistrationRepository
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.security.Principal


@Controller
class RootController(val clientRegistrationRepository: ReactiveClientRegistrationRepository,
                     val oauth2ClientProperties: OAuth2ClientProperties) {
//  val clientRegistrations: Mono<MutableList<ClientRegistration>>
////  = oauth2ClientProperties.registration
////      .map {
////        clientRegistrationRepository.findByRegistrationId(it.key)
////      }
//    =
//    .subscribe {  }


  @GetMapping("/login")
  fun showLoginPage(model: Model): Mono<String> {
    return Flux.fromIterable(oauth2ClientProperties.registration.keys)
      .flatMap {
        clientRegistrationRepository.findByRegistrationId(it)
      }
      .collectList()
      .cache()
      .map {
        model.addAttribute("oauth2", it)
        return@map "/app/login"
      }
  }

//  @GetMapping
//  fun showTopPage(): String {
//    return "/admin/top"
//  }
}
