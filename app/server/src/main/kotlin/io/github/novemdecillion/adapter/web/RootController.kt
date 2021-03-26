package io.github.novemdecillion.adapter.web;

import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties
import org.springframework.security.oauth2.client.registration.ClientRegistration
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

@Controller
class RootController(
  clientRegistrationRepository: InMemoryClientRegistrationRepository,
  oauth2ClientProperties: OAuth2ClientProperties
) {
  val clientRegistrations: List<ClientRegistration> = clientRegistrationRepository
    .map {
      it
    }

  @GetMapping("/login")
  fun showLoginPage(model: Model): String {
    model.addAttribute("oauth2", clientRegistrations)
    return "/app/login"
  }

//  @GetMapping
//  fun showTopPage(): String {
//    return "/admin/top"
//  }
}
