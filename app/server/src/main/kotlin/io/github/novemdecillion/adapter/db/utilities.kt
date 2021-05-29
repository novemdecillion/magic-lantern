package io.github.novemdecillion.adapter.db

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.jooq.JSONB

fun <T> ObjectMapper.writeValueAsJsonb(value: T): JSONB? {
  return value?.let { this.writeValueAsString(it) }?.let { JSONB.valueOf(it) }
}

inline fun <reified T> ObjectMapper.readValue(value: JSONB?): T? {
  return value?.data()?.let { this.readValue(it) }
}
