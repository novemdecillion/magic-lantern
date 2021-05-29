package io.github.novemdecillion.adapter.api

import graphql.kickstart.servlet.apollo.ApolloScalars
import graphql.kickstart.tools.SchemaParserDictionary
import graphql.scalars.ExtendedScalars
import io.github.novemdecillion.domain.ExamChapter
import io.github.novemdecillion.domain.ExplainChapter
import io.github.novemdecillion.domain.SurveyChapter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class GraphQLConfig {
  @Bean
  fun dateTimeScaler() = ExtendedScalars.DateTime
  @Bean
  fun dateScaler() = ExtendedScalars.Date
  @Bean
  fun timeScaler() = ExtendedScalars.Time
  @Bean
  fun longScaler() = ExtendedScalars.GraphQLLong
  @Bean
  fun uploadScaler() = ApolloScalars.Upload

  @Bean
  fun schemaParserDictionary(): SchemaParserDictionary? {
    return SchemaParserDictionary()
      .add(ExamChapter::class.java)
      .add(ExplainChapter::class.java)
      .add(SurveyChapter::class.java)
  }
}