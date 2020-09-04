package com.morayl.footprintktx

import android.util.Log
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

internal const val DEFAULT_TAG = "Footprint"

private var enableInternal = true
private var showJsonExceptionInternal = false
private var forceSimpleInternal = false
private var defaultLogLevelInternal = LogPriority.DEBUG
private var defaultStackTraceLogLevelInternal = LogPriority.ERROR

fun footprintConfig(
        enable: Boolean = enableInternal,
        showJsonException: Boolean = showJsonExceptionInternal,
        forceSimple: Boolean = forceSimpleInternal,
        defaultLogLogPriority: LogPriority = defaultLogLevelInternal,
        defaultStackTraceLogLogPriority: LogPriority = defaultStackTraceLogLevelInternal
) {
    enableInternal = enable
    showJsonExceptionInternal = showJsonException
    forceSimpleInternal = forceSimple
    defaultLogLevelInternal = defaultLogLogPriority
    defaultStackTraceLogLevelInternal = defaultStackTraceLogLogPriority
}

fun footprint(priority: LogPriority = defaultLogLevelInternal) {
    footprintSimple(getMetaInfo(), priority = priority)
}

fun footprint(vararg messages: Any?, priority: LogPriority = defaultLogLevelInternal) {
    footprintSimple(getMetaInfo(), messages.joinToString(separator = " "), priority = priority)
}

fun footprintSimple(vararg messages: Any?, priority: LogPriority = defaultLogLevelInternal) {
    footprintSimple(messages.joinToString(separator = " "), priority = priority)
}

private fun footprintSimple(message: Any?, priority: LogPriority = defaultLogLevelInternal) {
    Log.println(priority.value, DEFAULT_TAG, message.toString())
}

fun <T> T.withFootprintJson(priority: LogPriority = defaultLogLevelInternal): T {
    footprint("\n", toFormattedJSON(), priority = priority)
    return this
}

fun <T> T.withFootprint(priority: LogPriority = defaultLogLevelInternal): T {
    footprint(this, priority = priority)
    return this
}

fun Throwable.footprintStackTrace(priority: LogPriority = defaultStackTraceLogLevelInternal) {
    footprint(Log.getStackTraceString(this), priority = priority)
}

fun footprintStackTrace(priority: LogPriority = defaultStackTraceLogLevelInternal) {
    Throwable().footprintStackTrace(priority = priority)
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
            it.footprintStackTrace()
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
    // スタックトレースから情報を取得 // 0: VM, 1: Thread, 2: このメソッド, 3: このメソッドの呼び出し元...
    val elements = Thread.currentThread().stackTrace
    for (i in 2 until elements.size) {
        if (!elements[i].fileName.startsWith("Footprint")) {
            return getMetaInfo(elements[i])
        }
    }
    return "none"
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
