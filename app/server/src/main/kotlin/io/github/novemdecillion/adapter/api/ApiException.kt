package io.github.novemdecillion.adapter.api

import graphql.ErrorType
import graphql.GraphQLError
import graphql.language.SourceLocation

class ApiException(@JvmField override val message: String,
                   @JvmField val extensions : Map<String, Any> = emptyMap()) : RuntimeException(message), GraphQLError {

  override fun getMessage(): String = message

  override fun getLocations(): List<SourceLocation> = emptyList()

  override fun getErrorType(): ErrorType = ErrorType.DataFetchingException

  override fun getExtensions(): Map<String, Any> = extensions
}