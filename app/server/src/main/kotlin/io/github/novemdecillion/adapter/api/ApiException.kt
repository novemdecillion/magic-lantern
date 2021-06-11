package io.github.novemdecillion.adapter.api

import graphql.ErrorType
import graphql.GraphQLError
import graphql.language.SourceLocation

class ApiException(@JvmField override var message: String, code: String = VALUE_UNKNOWN_ERROR) : RuntimeException(message), GraphQLError {
  companion object {
    const val KEY_CODE = "code"
    const val VALUE_UNKNOWN_ERROR = "UnknownError"
  }
  @JvmField val extensions : MutableMap<String, Any> = mutableMapOf(KEY_CODE to code)

  fun setCode(code: String) {
    extensions[KEY_CODE] = code
  }

  override fun getMessage(): String = message

  override fun getLocations(): List<SourceLocation> = emptyList()

  override fun getErrorType(): ErrorType = ErrorType.DataFetchingException

  override fun getExtensions(): MutableMap<String, Any> = extensions
}