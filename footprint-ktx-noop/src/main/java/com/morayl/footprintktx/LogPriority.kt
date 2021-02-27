package com.morayl.footprintktx

/**
 * NoOp.
 */
enum class LogPriority(val value: Int) {
    VERBOSE(2),
    DEBUG(3),
    INFO(4),
    WARN(5),
    ERROR(6),
    ASSERT(7)
}