package com.morayl.footprintktx

import android.util.Log
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

/**
 * Leave footprint.
 * Footprint-ktx is a library for debugging Android-Kotlin.
 *
 * You just write [footprint], logcat show "[ClassName#MethodName:LineNumber]"
 * You can log multiple params, object as json, pair, stacktrace.
 *  → [jsonFootprint], [withJsonFootprint], [pairFootprint], [stacktraceFootprint].
 * You can configure enable, log level, etc. Use [configFootprint].
 *
 * [footprint] use [StackTraceElement] to log "[ClassName#MethodName:LineNumber]",
 * it is a bit heavy and may slow down the UI thread
 * if you call a lot at short intervals like a long for statement.
 * You can use [simpleFootprint] which faster than [footprint]
 * because it doesn't use [StackTraceElement]. (It doesn't log "[ClassName#MethodName:LineNumber]".)
 *
 * (In Java, You "can" use to write FootprintKt.~ but it's not useful.)
 */

private var enableInternal = true
private var showJsonExceptionInternal = false
private var forceSimpleInternal = false
private var logLevelInternal = LogPriority.DEBUG
private var stackTraceLogLevelInternal = LogPriority.ERROR
private var logTagInternal = "Footprint"

/**
 * Configure Footprint.
 * Params not specify, the current settings will be inherited.
 *
 * @param enable If it false, all Footprint log are not shown. (Default true)
 * @param showJsonException If it true, Footprint show "internal [JSONException]" when exception occurred while you use [withJsonFootprint]. (Default false)
 * @param forceSimple If it true, all Footprint log are not use [getMetaInfo] and are not showed [Class#Method:Linenumber]. It is used for improve performance.
 * @param defaultLogLogPriority set default [LogPriority] (like Verbose/Debug/Error). (default [LogPriority.DEBUG])
 * @param defaultStackTraceLogLogPriority Set default LogPriority when Footprint printing stacktrace. (default [LogPriority.ERROR])
 * @param defaultLogTag Set default LogTag. (default "Footprint")
 *
 * @see [LogPriority]
 */
fun configFootprint(
        enable: Boolean = enableInternal,
        showJsonException: Boolean = showJsonExceptionInternal,
        forceSimple: Boolean = forceSimpleInternal,
        defaultLogLogPriority: LogPriority = logLevelInternal,
        defaultStackTraceLogLogPriority: LogPriority = stackTraceLogLevelInternal,
        defaultLogTag: String = logTagInternal
) {
    enableInternal = enable
    showJsonExceptionInternal = showJsonException
    forceSimpleInternal = forceSimple
    logLevelInternal = defaultLogLogPriority
    stackTraceLogLevelInternal = defaultStackTraceLogLogPriority
    logTagInternal = defaultLogTag
}

/**
 * Log [ClassName#MethodName:LineNumber].
 *
 * @param priority (Optional) log priority of this log. select from [LogPriority].
 * @param logTag (Optional) Logcat-log's tag of this log.
 *
 * @see [LogPriority]
 */
fun footprint(priority: LogPriority = logLevelInternal, logTag: String = logTagInternal) {
    if (enableInternal) {
        simpleFootprint(getMetaInfo(), priority = priority, logTag = logTag)
    }
}

/**
 * Log [ClassName#MethodName:LineNumber] and messages.
 *
 * @param messages Messages you want to log.
 *                 This param is vararg, you can put multiple messages with using comma.
 *                 Messages are concat at space.
 * @param priority (Optional) log priority of this log. select from [LogPriority].
 * @param logTag (Optional) Logcat-log's tag of this log.
 *
 * @see [LogPriority]
 */
fun footprint(vararg messages: Any?, priority: LogPriority = logLevelInternal, logTag: String = logTagInternal) {
    if (enableInternal) {
        simpleFootprint(getMetaInfo(), messages.joinToString(separator = " "), priority = priority, logTag = logTag)
    }
}

/**
 * Log [ClassName#MethodName:LineNumber] and messages.
 * Log priority of this log is force [LogPriority.ERROR].
 * The log could become red, so you can find log easier in many debug logs.
 *
 * @param messages (Optional) Messages you want to log.
 *                 This param is vararg, you can put multiple messages with using comma.
 *                 Messages are concat at space.
 * @param logTag (Optional) Logcat-log's tag of this log.
 *
 * @see [LogPriority]
 */
fun accentFootprint(vararg messages: Any? = emptyArray(), logTag: String = logTagInternal) {
    footprint(*messages, priority = LogPriority.ERROR, logTag = logTag)
}

/**
 * Just log messages without stacktrace information.
 * It is faster than [footprint].
 * Recommended when outputting many times at short intervals.
 *
 * @param message Messages you want to log.
 * @param priority (Optional) log priority of this log. select from [LogPriority].
 * @param logTag (Optional) Logcat-log's tag of this log.
 */
fun simpleFootprint(message: Any?, priority: LogPriority = logLevelInternal, logTag: String = logTagInternal) {
    if (enableInternal) {
        Log.println(priority.value, logTag, message.toString())
    }
}

/**
 * Just log messages without stacktrace information.
 * It is faster than [footprint].
 * Recommended when outputting many times at short intervals.
 *
 * @param messages (Optional) Messages you want to log.
 *                 This param is vararg, you can put multiple messages with using comma.
 *                 Messages are concat at space.
 * @param priority (Optional) log priority of this log. select from [LogPriority].
 * @param logTag (Optional) Logcat-log's tag of this log.
 */
fun simpleFootprint(vararg messages: Any?, priority: LogPriority = logLevelInternal, logTag: String = logTagInternal) {
    if (enableInternal) {
        simpleFootprint(messages.joinToString(separator = " "), priority = priority, logTag = logTag)
    }
}

/**
 * Log [ClassName#MethodName:LineNumber] and target as Json.
 *
 * @param target "Any object" you want to log as Json.
 *               If target Json conversion failed, it show toString().
 *               Want to know failure reason, use [#configFootprint(showJsonExceptionInternal = true)]
 * @param priority (Optional) log priority of this log. select from [LogPriority].
 * @param logTag (Optional) Logcat-log's tag of this log.
 */
fun jsonFootprint(target: Any?, priority: LogPriority = logLevelInternal, logTag: String = logTagInternal) {
    target.withJsonFootprint(priority, logTag)
}

/**
 * Log [ClassName#MethodName:LineNumber] and receiver as Json.
 * Want to log primitive value, use [withFootprint].
 * If receiver Json conversion failed, it show toString().
 * Want to know failure reason, use [#configFootprint(showJsonExceptionInternal = true)]
 *
 * @receiver Object you want to log as json.
 * @param priority (Optional) log priority of this log. select from [LogPriority].
 * @param logTag (Optional) Logcat-log's tag of this log.
 * @return Receiver (this).
 */
fun <T> T.withJsonFootprint(priority: LogPriority = logLevelInternal, logTag: String = logTagInternal): T {
    if (enableInternal) {
        footprint("\n", toFormattedJSON(), priority = priority, logTag = logTag)
    }
    return this
}

/**
 * Log [ClassName#MethodName:LineNumber] and receiver as toString().
 * Want to log object json, use [withJsonFootprint].
 *
 * @receiver Object you want to log as toString().
 * @param priority (Optional) log priority of this log. select from [LogPriority].
 * @param logTag (Optional) Logcat-log's tag of this log.
 * @return Receiver (this).
 */
fun <T> T.withFootprint(priority: LogPriority = logLevelInternal, logTag: String = logTagInternal): T {
    if (enableInternal) {
        footprint(this, priority = priority, logTag = logTag)
    }
    return this
}

/**
 * Log [ClassName#MethodName:LineNumber] and pair values.
 * usage: pairFootprint("first" to 1, "second" to "secondValue", "third" to 3.toString())
 * output: [ClassName#MethodName:LineNumber]
 *         first : 1
 *         second : secondValue
 *         third : 3
 *
 * @param pairs value pairs you want to log.
 *              This param is vararg, you can put multiple messages with using comma.
 * @param priority (Optional) log priority of this log. select from [LogPriority].
 * @param logTag (Optional) Logcat-log's tag of this log.
 */
fun pairFootprint(vararg pairs: Pair<String, Any?>, priority: LogPriority = logLevelInternal, logTag: String = logTagInternal) {
    val message = pairs.joinToString(separator = "\n", prefix = "\n") {
        "${it.first} : ${it.second}"
    }
    footprint(message, priority = priority, logTag = logTag)
}

/**
 * Log [ClassName#MethodName:LineNumber] and stacktrace of exception.
 *
 * @receiver [Throwable] you want to log as stacktrace string.
 * @param priority (Optional) log priority of this log. select from [LogPriority].
 * @param logTag (Optional) Logcat-log's tag of this log.
 */
fun Throwable.stacktraceFootprint(priority: LogPriority = stackTraceLogLevelInternal, logTag: String = logTagInternal) {
    if (enableInternal) {
        footprint(Log.getStackTraceString(this), priority = priority, logTag = logTag)
    }
}

/**
 * Log [ClassName#MethodName:LineNumber] and current stacktrace.
 * Useful for confirming calling hierarchy.
 *
 * @param priority (Optional) log priority of this log. select from [LogPriority].
 * @param logTag (Optional) Logcat-log's tag of this log.
 */
fun stacktraceFootprint(priority: LogPriority = stackTraceLogLevelInternal, logTag: String = logTagInternal) {
    if (enableInternal) {
        footprint(Log.getStackTraceString(Throwable()), priority = priority, logTag = logTag)
    }
}

/**
 * Convert receiver to formatted Json.
 *
 * @param indent count of indent's space.
 * @return formatted json.
 *         If Json conversion failed, return toString().
 */
private fun Any?.toFormattedJSON(indent: Int = 2): String? {
    return runCatching {
        val json = Gson().toJson(this)
        try {
            val jsonObject = JSONObject(json)
            jsonObject.toString(indent)
        } catch (e: JSONException) {
            val jsonArray = JSONArray(json)
            jsonArray.toString(indent)
        }
    }.onFailure {
        if (showJsonExceptionInternal) {
            it.stacktraceFootprint()
        }
    }.getOrElse {
        toString()
    }
}


/**
 * Get stacktrace information string from current [StackTraceElement].
 *
 * @return [className#methodName:line]
 */
private fun getMetaInfo(): String? {
    if (forceSimpleInternal) {
        return ""
    }
    // Get stackTraceElement array. // 0: VM, 1: Thread, 2: This method, 3: Caller of this method...
    val elements = Thread.currentThread().stackTrace
    for (i in 2 until elements.size) {
        if (!elements[i].fileName.startsWith("Footprint")) {
            return getMetaInfo(elements[i])
        }
    }
    return "No meta info"
}

/**
 * Get stacktrace information string from [StackTraceElement].
 *
 * @param element a [StackTraceElement]
 * @return [className#methodName:line]
 */
private fun getMetaInfo(element: StackTraceElement): String? {
    val fullClassName = element.className
    val simpleClassName = fullClassName.substring(fullClassName.lastIndexOf(".") + 1)
    val methodName = element.methodName
    val lineNumber = element.lineNumber
    return "[$simpleClassName#$methodName:$lineNumber]"
}
