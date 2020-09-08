package com.morayl.footprintktx

import android.util.Log

/**
 * When you choose [LogPriority.ERROR] or [LogPriority.ASSERT] logcat-log could become red.
 */
enum class LogPriority(val value: Int) {
    VERBOSE(Log.VERBOSE),
    DEBUG(Log.DEBUG),
    INFO(Log.INFO),
    WARN(Log.WARN),
    ERROR(Log.ERROR),
    ASSERT(Log.ASSERT)
}