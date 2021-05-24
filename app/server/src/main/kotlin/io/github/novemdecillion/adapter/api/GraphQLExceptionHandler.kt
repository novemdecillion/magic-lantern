package io.github.novemdecillion.adapter.api

import graphql.kickstart.execution.error.GenericGraphQLError
import org.springframework.dao.DuplicateKeyException
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.ExceptionHandler

//@Component
//class GraphQLExceptionHandler {
//  @ExceptionHandler(DuplicateKeyException::class)
//  fun handleDuplicateKeyException(e: DuplicateKeyException): GenericGraphQLError {
//    return GenericGraphQLError()
//  }
//}