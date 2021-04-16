package io.github.novemdecillion.adapter.api

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
class PerRequestTransactionInstrumentation(private val transactionManager: PlatformTransactionManager) : SimpleInstrumentation() {
  companion object {
    val log = logger()
  }
  override fun beginExecuteOperation(parameters: InstrumentationExecuteOperationParameters): InstrumentationContext<ExecutionResult> {
    log.info("transaction start")
    val tx = TransactionTemplate(transactionManager)
    if (OperationDefinition.Operation.QUERY == parameters.executionContext.operationDefinition.operation) {
      tx.isReadOnly = true
    }
    val status = transactionManager.getTransaction(tx)
    return SimpleInstrumentationContext.whenCompleted { _, e ->
      log.info("transaction end")
      if (e != null) {
        transactionManager.rollback(status)
      } else {
        transactionManager.commit(status)
      }
    }
  }
}