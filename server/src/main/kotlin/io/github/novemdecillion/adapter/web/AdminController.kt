package io.github.novemdecillion.adapter.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("/admin")
class AdminController {
  @GetMapping("/login")
  fun showLoginPage(): String {
    return "/admin/login"
  }
}
