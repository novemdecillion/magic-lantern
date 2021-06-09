package io.github.novemdecillion.adapter.api

import graphql.ExceptionWhileDataFetching
import graphql.ExecutionResult
import graphql.execution.instrumentation.InstrumentationContext
import graphql.execution.instrumentation.SimpleInstrumentation
import graphql.execution.instrumentation.SimpleInstrumentationContext
import graphql.execution.instrumentation.parameters.InstrumentationExecuteOperationParameters
import graphql.language.OperationDefinition
import io.github.novemdecillion.utils.lang.logger
import org.springframework.stereotype.Component
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.support.TransactionTemplate

@Component
class PerRequestTransactionInstrumentation(private val transactionManager: PlatformTransactionManager) :
  SimpleInstrumentation() {
  companion object {
    val log = logger()
  }

  override fun beginExecuteOperation(parameters: InstrumentationExecuteOperationParameters): InstrumentationContext<ExecutionResult> {
    log.debug("transaction start")
    val tx = TransactionTemplate(transactionManager)
    if (OperationDefinition.Operation.QUERY == parameters.executionContext.operationDefinition.operation) {
      tx.isReadOnly = true
    }
    val status = transactionManager.getTransaction(tx)
    return SimpleInstrumentationContext.whenCompleted { result, e ->
      log.debug("transaction end")

      when {
        (e != null)
          || (result.errors.isNotEmpty())
        -> transactionManager.rollback(status)
        else -> transactionManager.commit(status)
      }
    }
  }
}