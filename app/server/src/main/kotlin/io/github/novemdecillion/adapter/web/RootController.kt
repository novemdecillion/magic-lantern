package io.github.novemdecillion.adapter.web;

import io.github.novemdecillion.adapter.db.RealmRepository
import org.springframework.security.oauth2.client.registration.ClientRegistration
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository
import org.springframework.stereotype.Controller
import org.springframework.transaction.annotation.Transactional
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
class RootController(
  val realmRepository: RealmRepository,
  clientRegistrationRepository: InMemoryClientRegistrationRepository
) {
  val clientRegistrations: List<ClientRegistration> = clientRegistrationRepository.map { it }

  @GetMapping("/login")
  @Transactional(readOnly = true, rollbackFor = [Exception::class])
  fun showLoginPage(@RequestParam(required = false) error: String?, model: Model): String {
    val enableRealms = realmRepository.selectAll().filter { it.enabled != false }.map { it.realmId }.toSet()
    val realms = clientRegistrations.filter { enableRealms.contains(it.registrationId) }
    if (null != error) {
      model.addAttribute("error", "アカウントまたはパスワードが誤りがありました。")
    }
    model.addAttribute("oauth2", realms)
    return "app/login"
  }
}
