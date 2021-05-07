package io.github.novemdecillion.adapter.api

import graphql.kickstart.servlet.apollo.ApolloScalars
import graphql.scalars.ExtendedScalars
import org.dataloader.DataLoaderRegistry
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

}