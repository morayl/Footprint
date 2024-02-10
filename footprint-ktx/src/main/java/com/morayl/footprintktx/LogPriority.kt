package com.morayl.footprintktx

import android.util.Log

/**
 * LogPriority of Logcat.
 * When you choose [LogPriority.ERROR] or [LogPriority.ASSERT] logcat-log could become red.
 */
@Deprecated(message = "footprint-ktx has been deprecated. Please see github for the migration. https://github.com/morayl/Footprint/blob/master/CHANGELOG.md ")
enum class LogPriority(val value: Int) {
    VERBOSE(Log.VERBOSE),
    DEBUG(Log.DEBUG),
    INFO(Log.INFO),
    WARN(Log.WARN),
    ERROR(Log.ERROR),
    ASSERT(Log.ASSERT)
}