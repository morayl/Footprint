package com.morayl.footprint

/**
 * NoOp.
 * footprint-ktx has been deprecated.
 * Please see below for the migration.
 * https://github.com/morayl/Footprint/blob/master/CHANGELOG.md
 */
@Deprecated(message = "footprint-ktx has been deprecated. Please see github for the migration. https://github.com/morayl/Footprint/blob/master/CHANGELOG.md ")
enum class LogPriority(val value: Int) {
    VERBOSE(2),
    DEBUG(3),
    INFO(4),
    WARN(5),
    ERROR(6),
    ASSERT(7)
}