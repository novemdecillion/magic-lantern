/*
 * This file is generated by jOOQ.
 */
package io.github.novemdecillion.adapter.jooq.tables.interfaces


import io.github.novemdecillion.domain.StudyStatus

import java.io.Serializable
import java.time.OffsetDateTime
import java.util.UUID

import org.jooq.JSONB


/**
 * This class is generated by jOOQ.
 */
@Suppress("UNCHECKED_CAST")
interface IStudy : Serializable {
    var studyId: UUID?
    var accountId: UUID?
    var slideId: String?
    var status: StudyStatus?
    var progress: JSONB?
    var progressRate: Int?
    var answer: JSONB?
    var score: JSONB?
    var startAt: OffsetDateTime?
    var endAt: OffsetDateTime?

    // -------------------------------------------------------------------------
    // FROM and INTO
    // -------------------------------------------------------------------------

    /**
     * Load data from another generated Record/POJO implementing the common interface IStudy
     */
    fun from(from: IStudy)

    /**
     * Copy data into another generated Record/POJO implementing the common interface IStudy
     */
    fun <E : IStudy> into(into: E): E
}