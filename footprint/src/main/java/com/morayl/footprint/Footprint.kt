package com.morayl.footprint

import android.util.Log
import com.google.gson.Gson
import com.morayl.footprint.MetaInfo.Companion.prefixString
import com.morayl.footprint.MetaInfo.Companion.suffixString
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

/**
 * Leave footprint.
 * Footprint-ktx is a library for debugging Android-Kotlin.
 *
 * You just use [footprint], logcat shows "[ClassName#MethodName:LineNumber] + (links to the line of code)".
 * Actually displayed as "[ClassName#MethodName:LineNumber] + (FileName:LineNumber)".
 * The parentheses at the end are links, and you can jump to the line of code when you press it.
 * You can log multiple params, object as json, pair values, stacktrace.
 *  → [jsonFootprint], [withJsonFootprint], [pairFootprint], [stacktraceFootprint].
 * You can configure enable, log level, etc. Use [configFootprint].
 *
 * [footprint] use [StackTraceElement] to log "[ClassName#MethodName:LineNumber] + (links to the line of code)",
 * it is a bit heavy and may slow down the UI thread
 * if you call a lot at short intervals like a long "for" statement.
 * You can use [simpleFootprint], which is faster than [footprint]
 * because it doesn't use [StackTraceElement]. (It doesn't log "[ClassName#MethodName:LineNumber]+ (links to the line of code)".)
 *
 * (In Java, You "can" use to write FootprintKt.~ but it's not useful.)
 */

private var enableInternal = true
private var defaultLogTag = "Footprint"
private var defaultLogPriority = LogPriority.DEBUG
private var showJsonExceptionInternal = false
private var forceSimpleInternal = false
private var defaultStackTraceLogLevel = LogPriority.ERROR
private var defaultJsonIndentCount = 4

/**
 * Configure default of Footprint.
 * Params not specify, the current settings will be inherited.
 * This settings available in memory.
 *
 * @param enable If it false, all Footprint log are not shown. (Default true)
 * @param logTag Set default LogTag. (Default "Footprint")
 * @param logPriority Set default [LogPriority] (like Verbose/Debug/Error). (Default [LogPriority.DEBUG])
 * @param showInternalJsonException If it true, Footprint show "internal [JSONException]"
 *                                  when exception occurred while you use [withJsonFootprint]. (Default false)
 * @param forceSimple If it true, all Footprint log are not use [getMetaInfo] and are not showed [Class#Method:Linenumber] (FileName:LineNumber).
 *                    It is used for improve performance. (Default false)
 * @param stacktraceLogLogPriority Set default LogPriority when Footprint printing stacktrace. (Default [LogPriority.ERROR])
 * @param jsonIndentCount Set default count of Json Indention space.
 *
 * @see [LogPriority]
 */
fun configFootprint(
        enable: Boolean = enableInternal,
        logTag: String = defaultLogTag,
        logPriority: LogPriority = defaultLogPriority,
        showInternalJsonException: Boolean = showJsonExceptionInternal,
        forceSimple: Boolean = forceSimpleInternal,
        stacktraceLogLogPriority: LogPriority = defaultStackTraceLogLevel,
        jsonIndentCount: Int = defaultJsonIndentCount
) {
    enableInternal = enable
    defaultLogTag = logTag
    defaultLogPriority = logPriority
    showJsonExceptionInternal = showInternalJsonException
    forceSimpleInternal = forceSimple
    defaultStackTraceLogLevel = stacktraceLogLogPriority
    defaultJsonIndentCount = jsonIndentCount
}

/**
 * Log [ClassName#MethodName:LineNumber] + (links to the line of code).
 *
 * @param priority (Optional) Log priority of this log. Select from [LogPriority].
 * @param logTag (Optional) Logcat-log's tag of this log.
 *
 * @see [LogPriority]
 */
fun footprint(priority: LogPriority = defaultLogPriority, logTag: String = defaultLogTag) {
    if (enableInternal) {
        val metaInfo = getMetaInfo()
        val metaInfoText = if (metaInfo == null) {
            ""
        } else {
            "${metaInfo.prefixString()} ${metaInfo.suffixString()}"
        }
        simpleFootprint(metaInfoText, priority = priority, logTag = logTag)
    }
}

/**
 * Log [ClassName#MethodName:LineNumber] and messages + (links to the line of code).
 *
 * @param messages Messages you want to log.
 *                 This param is vararg, you can put multiple messages with using comma.
 *                 Messages are concat at space.
 * @param priority (Optional) Log priority of this log. Select from [LogPriority].
 * @param logTag (Optional) Logcat-log's tag of this log.
 *
 * @see [LogPriority]
 */
fun footprint(vararg messages: Any?, priority: LogPriority = defaultLogPriority, logTag: String = defaultLogTag) {
    if (enableInternal) {
        val metaInfo = getMetaInfo()
        simpleFootprint(
            metaInfo.prefixString(),
            messages.joinToString(separator = " "),
            metaInfo.suffixString(),
            priority = priority,
            logTag = logTag
        )
    }
}

/**
 * Log [ClassName#MethodName:LineNumber] and messages + (links to the line of code).
 * Log priority of this log is force [LogPriority.ERROR].
 * The log could become red, so you can find log easier in many debug logs.
 *
 * @param messages (Optional) Messages you want to log.
 *                 This param is vararg, you can put multiple messages with using comma.
 *                 Messages are concat at space.
 * @param logTag (Optional) Logcat-log's tag of this log.
 */
fun accentFootprint(vararg messages: Any? = emptyArray(), logTag: String = defaultLogTag) {
    footprint(*messages, priority = LogPriority.ERROR, logTag = logTag)
}

/**
 * Just log messages without stacktrace information.
 * It is faster than [footprint].
 * Recommended when using many times at short intervals.
 *
 * @param message Messages you want to log.
 * @param priority (Optional) Log priority of this log. Select from [LogPriority].
 * @param logTag (Optional) Logcat-log's tag of this log.
 */
fun simpleFootprint(message: Any?, priority: LogPriority = defaultLogPriority, logTag: String = defaultLogTag) {
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
 * @param priority (Optional) Log priority of this log. Select from [LogPriority].
 * @param logTag (Optional) Logcat-log's tag of this log.
 */
fun simpleFootprint(vararg messages: Any?, priority: LogPriority = defaultLogPriority, logTag: String = defaultLogTag) {
    if (enableInternal) {
        simpleFootprint(messages.joinToString(separator = " "), priority = priority, logTag = logTag)
    }
}

/**
 * Log [ClassName#MethodName:LineNumber] and target as Json + (links to the line of code).
 *
 * @param target "Any object" you want to log as Json.
 *               If target Json conversion failed, it show toString().
 *               Want to know failure reason, use [#configFootprint(showJsonExceptionInternal = true)]
 * @param indent (Optional) Count of Json Indention space.
 * @param priority (Optional) Log priority of this log. Select from [LogPriority].
 * @param logTag (Optional) Logcat-log's tag of this log.
 */
fun jsonFootprint(
    target: Any?,
    indent: Int = defaultJsonIndentCount,
    priority: LogPriority = defaultLogPriority,
    logTag: String = defaultLogTag
) {
    target.withJsonFootprint(indent, priority, logTag)
}

/**
 * Log [ClassName#MethodName:LineNumber] and receiver as Json + (links to the line of code). And return receiver.
 * Want to log primitive value, use [withFootprint].
 * If receiver Json conversion failed, it show toString().
 * Want to know failure reason, use [configFootprint] and "showJsonExceptionInternal = true".
 *
 * @receiver Object you want to log as json.
 * @param indent (Optional) Count of Json Indention space.
 * @param priority (Optional) Log priority of this log. Select from [LogPriority].
 * @param logTag (Optional) Logcat-log's tag of this log.
 * @param block (Optional) Variable which you want to log. Default is "this".
 * @return Receiver (this).
 */
fun <T> T.withJsonFootprint(
    indent: Int = defaultJsonIndentCount, priority: LogPriority = defaultLogPriority,
    logTag: String = defaultLogTag, block: (T) -> Any? = { this }
): T {
    if (enableInternal) {
        linkFirstFootprint("\n", block(this).toFormattedJSON(indent), priority = priority, logTag = logTag)
    }
    return this
}

/**
 * Log [ClassName#MethodName:LineNumber] and receiver as toString() + (links to the line of code). And return receiver.
 * Want to log object json, use [withJsonFootprint].
 *
 * @receiver Object you want to log as toString().
 * @param priority (Optional) Log priority of this log. Select from [LogPriority].
 * @param logTag (Optional) Logcat-log's tag of this log.
 * @param block (Optional) Variable which you want to log as toString(). Default is "this".
 * @return Receiver (this).
 */
fun <T> T.withFootprint(priority: LogPriority = defaultLogPriority, logTag: String = defaultLogTag, block: (T) -> Any? = { this }): T {
    if (enableInternal) {
        footprint(block(this), priority = priority, logTag = logTag)
    }
    return this
}

/**
 * Just log receiver as toString() without stacktrace information. And return receiver.
 * It is faster than [withFootprint].
 * Recommended when outputting many times at short intervals.
 *
 * @receiver Object you want to log as toString().
 * @param priority (Optional) Log priority of this log. Select from [LogPriority].
 * @param logTag (Optional) Logcat-log's tag of this log.
 * @param block (Optional) Variable which you want to log as toString(). Default is "this".
 * @return Receiver (this).
 */
fun <T> T.withSimpleFootprint(
    priority: LogPriority = defaultLogPriority,
    logTag: String = defaultLogTag,
    block: (T) -> Any? = { this }
): T {
    if (enableInternal) {
        simpleFootprint(block(this), priority = priority, logTag = logTag)
    }
    return this
}

/**
 * Log [ClassName#MethodName:LineNumber] and pair values + (links to the line of code).
 * Usage: pairFootprint("first" to 1, "second" to "secondValue", "third" to 3.toString())
 * Output: [ClassName#MethodName:LineNumber]
 *         first : 1
 *         second : secondValue
 *         third : 3
 *
 * @param pairs Value pairs you want to log.
 *              This param is vararg, you can put multiple messages with using comma.
 * @param priority (Optional) Log priority of this log. Select from [LogPriority].
 * @param logTag (Optional) Logcat-log's tag of this log.
 */
fun pairFootprint(vararg pairs: Pair<String, Any?>, priority: LogPriority = defaultLogPriority, logTag: String = defaultLogTag) {
    val message = pairs.joinToString(separator = "\n", prefix = "\n") {
        "${it.first} : ${it.second}"
    }
    linkFirstFootprint(message, priority = priority, logTag = logTag)
}

/**
 * Log [ClassName#MethodName:LineNumber] and stacktrace of exception + (links to the line of code).
 *
 * @receiver [Throwable] you want to log as stacktrace string.
 * @param priority (Optional) Log priority of this log. Select from [LogPriority].
 * @param logTag (Optional) Logcat-log's tag of this log.
 */
fun Throwable.stacktraceFootprint(priority: LogPriority = defaultStackTraceLogLevel, logTag: String = defaultLogTag) {
    if (enableInternal) {
        linkFirstFootprint(Log.getStackTraceString(this), priority = priority, logTag = logTag)
    }
}

/**
 * Log [ClassName#MethodName:LineNumber] and current stacktrace + (links to the line of code).
 * Useful for confirming calling hierarchy.
 *
 * @param priority (Optional) Log priority of this log. Select from [LogPriority].
 * @param logTag (Optional) Logcat-log's tag of this log.
 */
fun stacktraceFootprint(priority: LogPriority = defaultStackTraceLogLevel, logTag: String = defaultLogTag) {
    if (enableInternal) {
        footprint(Log.getStackTraceString(Throwable()), priority = priority, logTag = logTag)
    }
}

/**
 * Log [ClassName#MethodName:LineNumber] (links to the line of code) + messages.
 * Logcat can only link the line of code on the first line, so use this in [jsonFootprint] and [stacktraceFootprint].
 *
 * @param messages (Optional) Messages you want to log.
 *                 This param is vararg, you can put multiple messages with using comma.
 *                 Messages are concat at space.
 * @param priority (Optional) Log priority of this log. Select from [LogPriority].
 * @param logTag (Optional) Logcat-log's tag of this log.
 */
private fun linkFirstFootprint(vararg messages: Any?, priority: LogPriority = defaultLogPriority, logTag: String = defaultLogTag) {
    if (enableInternal) {
        val metaInfo = getMetaInfo()
        simpleFootprint(
            metaInfo.prefixString(),
            metaInfo.suffixString(),
            messages.joinToString(separator = " "),
            priority = priority,
            logTag = logTag
        )
    }
}

/**
 * Convert receiver to formatted Json.
 *
 * @param indent Count of Json Indention space.
 * @return formatted json.
 *         If Json conversion failed, return toString().
 */
private fun Any?.toFormattedJSON(indent: Int): String? {
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
 * Get stacktrace information from current [StackTraceElement].
 *
 * @return [MetaInfo]
 */
private fun getMetaInfo(): MetaInfo? {
    if (forceSimpleInternal) {
        return null
    }
    // Get stackTraceElement array. // 0: VM, 1: Thread, 2: This method, 3: Caller of this method...
    val elements = Thread.currentThread().stackTrace
    for (i in 2 until elements.size) {
        if (elements[i].fileName?.startsWith("Footprint") == false) {
            return getMetaInfo(elements[i])
        }
    }
    return null
}

/**
 * Get stacktrace information from [StackTraceElement].
 *
 * @param element a [StackTraceElement]
 * @return [MetaInfo]
 */
private fun getMetaInfo(element: StackTraceElement): MetaInfo {
    val fullClassName = element.className
    val fileName = element.fileName
    val simpleClassName = fullClassName?.substring(fullClassName.lastIndexOf(".") + 1)
    val methodName = element.methodName
    val lineNumber = element.lineNumber
    return MetaInfo(fileName, simpleClassName, methodName, lineNumber)
}

private data class MetaInfo(
    private val fileName: String?,
    private val simpleClassName: String?,
    private val methodName: String?,
    private val lineNumber: Int,
) {
    companion object {
        fun MetaInfo?.prefixString(): String {
            this ?: return ""
            return "[$simpleClassName#$methodName:$lineNumber]"
        }

        fun MetaInfo?.suffixString(): String {
            this ?: return ""
            return "($fileName:$lineNumber)"
        }
    }
}

