package io.github.novemdecillion.adapter.api

import graphql.kickstart.servlet.apollo.ApolloScalars
import graphql.kickstart.tools.SchemaParserDictionary
import graphql.scalars.ExtendedScalars
import graphql.schema.GraphQLScalarType
import io.github.novemdecillion.domain.*
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class GraphQLConfig {
  @Bean
  fun dateTimeScaler(): GraphQLScalarType = ExtendedScalars.DateTime
  @Bean
  fun dateScaler(): GraphQLScalarType = ExtendedScalars.Date
  @Bean
  fun timeScaler(): GraphQLScalarType = ExtendedScalars.Time
  @Bean
  fun longScaler(): GraphQLScalarType = ExtendedScalars.GraphQLLong
  @Bean
  fun uploadScaler(): GraphQLScalarType = ApolloScalars.Upload

  @Bean
  fun schemaParserDictionary(): SchemaParserDictionary? {
    return SchemaParserDictionary()
      .add(ExamChapter::class.java)
      .add(ExplainChapter::class.java)
      .add(SurveyChapter::class.java)
      .add(NotStartStudy::class.java)
      .add(Study::class.java)
  }
}