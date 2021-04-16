package io.github.novemdecillion.adapter.web;

import io.github.novemdecillion.adapter.db.AccountRepository
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties
import org.springframework.security.oauth2.client.registration.ClientRegistration
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository
import org.springframework.stereotype.Controller
import org.springframework.transaction.annotation.Transactional
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

@Controller
class RootController(
  val accountRepository: AccountRepository,
  clientRegistrationRepository: InMemoryClientRegistrationRepository,
  oauth2ClientProperties: OAuth2ClientProperties
) {
  val clientRegistrations: List<ClientRegistration> = clientRegistrationRepository.map { it }

  @GetMapping("/login")
  @Transactional(readOnly = true, rollbackFor = [Exception::class])
  fun showLoginPage(model: Model): String {
    val enableRealms = accountRepository.selectRealm().filter { it.enabled != false }.map { it.realmId }.toSet()
    val realms = clientRegistrations.filter { enableRealms.contains(it.registrationId) }
    model.addAttribute("oauth2", realms)
    return "/app/login"
  }

//  @GetMapping
//  fun showTopPage(): String {
//    return "/admin/top"
//  }
}
