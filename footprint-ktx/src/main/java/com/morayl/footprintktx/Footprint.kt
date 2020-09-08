package com.morayl.footprintktx

import android.util.Log
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

/**
 * Footprint-ktx
 * Leave footprint. Footprint-ktx is a library for debugging Android-Kotlin.
 *
 * You just write [footprint], logcat show [ClassName#MethodName:LineNumber]
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
 * @param defaultLogLogPriority set default LogPriority(like Verbose/Debug/Error). (default [LogPriority.DEBUG])
 * @param defaultStackTraceLogLogPriority Set default LogPriority when Footprint printing stacktrace. (default [LogPriority.ERROR])
 * @param defaultLogTag Set default LogTag. (default "Footprint")
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
 * log [ClassName#MethodName:LineNumber].
 *
 * @param priority (Optional) log priority of this log.
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
 * log [ClassName#MethodName:LineNumber] and messages.
 *
 * @param messages Messages you want to log. You can put multiple messages with using comma.
 *                 Messages are concat at space.
 * @param priority (Optional) log priority of this log.
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
 * log [ClassName#MethodName:LineNumber] and messages.
 * log priority of this log is force [LogPriority.ERROR].
 * The log could become red, so you can find log easier in many debug logs.
 *
 * @param messages (Optional) Messages you want to log. You can put multiple messages with using comma.
 *                 Messages are concat at space.
 * @param logTag (Optional) Logcat-log's tag of this log.
 */
fun accentFootprint(vararg messages: Any? = emptyArray(), logTag: String = logTagInternal) {
    footprint(*messages, priority = LogPriority.ERROR, logTag = logTag)
}

/**
 * Just log messages without stacktrace information.
 * It is faster than [footprint].
 * Recommended when outputting many times at short intervals.
 *
 * @param messages (Optional) Messages you want to log. You can put multiple messages with using comma.
 *                 Messages are concat at space.
 * @param logTag (Optional) Logcat-log's tag of this log.
 */
fun simpleFootprint(vararg messages: Any?, priority: LogPriority = logLevelInternal, logTag: String = logTagInternal) {
    if (enableInternal) {
        simpleFootprint(messages.joinToString(separator = " "), priority = priority, logTag = logTag)
    }
}

fun simpleFootprint(message: Any?, priority: LogPriority = logLevelInternal, logTag: String = logTagInternal) {
    if (enableInternal) {
        Log.println(priority.value, logTag, message.toString())
    }
}

fun <T> T.withJsonFootprint(priority: LogPriority = logLevelInternal, logTag: String = logTagInternal): T {
    if (enableInternal) {
        footprint("\n", toFormattedJSON(), priority = priority, logTag = logTag)
    }
    return this
}

fun <T> T.withFootprint(priority: LogPriority = logLevelInternal, logTag: String = logTagInternal): T {
    if (enableInternal) {
        footprint(this, priority = priority, logTag = logTag)
    }
    return this
}

fun jsonFootprint(target: Any?, priority: LogPriority = logLevelInternal, logTag: String = logTagInternal) {
    target.withJsonFootprint(priority, logTag)
}

fun pairFootprint(vararg pairs: Pair<String, Any?>) {
    val message = pairs.joinToString(separator = "\n", prefix = "\n") {
        "${it.first} : ${it.second}"
    }
    footprint(message)
}

fun Throwable.stacktraceFootprint(priority: LogPriority = stackTraceLogLevelInternal, logTag: String = logTagInternal) {
    if (enableInternal) {
        footprint(Log.getStackTraceString(this), priority = priority, logTag = logTag)
    }
}

fun stacktraceFootprint(priority: LogPriority = stackTraceLogLevelInternal, logTag: String = logTagInternal) {
    if (enableInternal) {
        footprint(Log.getStackTraceString(Throwable()), priority = priority, logTag = logTag)
    }
}

/**
 * 整形されたJSON文字列を返す
 *
 * @param indent インデント
 * @return json文字列
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
 * ログ呼び出し元のメタ情報を取得する
 * なければ"none"を返す
 *
 * @return [className#methodName:line]
 */
private fun getMetaInfo(): String? {
    if (forceSimpleInternal) {
        return ""
    }
    // スタックトレースから情報を取得 // 0: VM, 1: Thread, 2: このメソッド, 3: このメソッドの呼び出し元...
    val elements = Thread.currentThread().stackTrace
    for (i in 2 until elements.size) {
        if (!elements[i].fileName.startsWith("Footprint")) {
            return getMetaInfo(elements[i])
        }
    }
    return "No meta info"
}

/**
 * スタックトレースからクラス名、メソッド名、行数を取得する
 *
 * @return [className#methodName:line]
 */
private fun getMetaInfo(element: StackTraceElement): String? {
    // クラス名、メソッド名、行数を取得
    val fullClassName = element.className
    val simpleClassName = fullClassName.substring(fullClassName.lastIndexOf(".") + 1)
    val methodName = element.methodName
    val lineNumber = element.lineNumber
    return "[$simpleClassName#$methodName:$lineNumber]"
}
