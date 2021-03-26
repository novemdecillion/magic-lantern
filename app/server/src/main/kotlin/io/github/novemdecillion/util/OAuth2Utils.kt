package io.github.novemdecillion.util

class OAuth2Utils {
  companion object {
    fun issuerUriToBaseUri(issuerUri: String): String {
      return issuerUri.split("/realms/")[0]
    }
    fun issuerUriToRealm(issuerUri: String): String {
      return issuerUri.split("/realms/")[1]
    }
  }
}