/*
 * Auto-generated file. Created by MyBatis Generator
 * Generation date: 2021-03-25T16:08:49.526875+09:00
 */
package io.github.novemdecillion.adapter.mybatis

import java.time.LocalDateTime

data class FlywaySchemaHistoryRecord(
    var installedRank: Int? = null,
    var version: String? = null,
    var description: String? = null,
    var type: String? = null,
    var script: String? = null,
    var checksum: Int? = null,
    var installedBy: String? = null,
    var installedOn: LocalDateTime? = null,
    var executionTime: Int? = null,
    var success: Boolean? = null
)