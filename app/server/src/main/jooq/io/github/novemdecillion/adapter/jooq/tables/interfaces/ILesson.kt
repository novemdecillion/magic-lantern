/*
 * This file is generated by jOOQ.
 */
package io.github.novemdecillion.adapter.jooq.tables.interfaces


import java.io.Serializable
import java.util.UUID


/**
 * This class is generated by jOOQ.
 */
@Suppress("UNCHECKED_CAST")
interface ILesson : Serializable {
    var lessonId: UUID?
    var groupOriginId: UUID?
    var slideId: String?

    // -------------------------------------------------------------------------
    // FROM and INTO
    // -------------------------------------------------------------------------

    /**
     * Load data from another generated Record/POJO implementing the common interface ILesson
     */
    fun from(from: ILesson)

    /**
     * Copy data into another generated Record/POJO implementing the common interface ILesson
     */
    fun <E : ILesson> into(into: E): E
}
