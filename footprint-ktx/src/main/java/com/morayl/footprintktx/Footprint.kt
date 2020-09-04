package com.morayl.footprintktx

import android.util.Log
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

private var enableInternal = true
private var showJsonExceptionInternal = false
private var forceSimpleInternal = false
private var logLevelInternal = LogPriority.DEBUG
private var stackTraceLogLevelInternal = LogPriority.ERROR
private var logTagInternal = "Footprint"

fun configFootprint(
        enable: Boolean = enableInternal,
        showJsonException: Boolean = showJsonExceptionInternal,
        forceSimple: Boolean = forceSimpleInternal,
        defaultLogLogPriority: LogPriority = logLevelInternal,
        defaultStackTraceLogLogPriority: LogPriority = stackTraceLogLevelInternal,
        logTag: String = logTagInternal
) {
    enableInternal = enable
    showJsonExceptionInternal = showJsonException
    forceSimpleInternal = forceSimple
    logLevelInternal = defaultLogLogPriority
    stackTraceLogLevelInternal = defaultStackTraceLogLogPriority
    logTagInternal = logTag
}

fun footprint(priority: LogPriority = logLevelInternal, logTag: String = logTagInternal) {
    if (enableInternal) {
        footprintSimple(getMetaInfo(), priority = priority, logTag = logTag)
    }
}

fun footprint(vararg messages: Any?, priority: LogPriority = logLevelInternal, logTag: String = logTagInternal) {
    if (enableInternal) {
        footprintSimple(getMetaInfo(), messages.joinToString(separator = " "), priority = priority, logTag = logTag)
    }
}

fun footprintSimple(vararg messages: Any?, priority: LogPriority = logLevelInternal, logTag: String = logTagInternal) {
    if (enableInternal) {
        footprintSimple(messages.joinToString(separator = " "), priority = priority, logTag = logTag)
    }
}

fun footprintSimple(message: Any?, priority: LogPriority = logLevelInternal, logTag: String = logTagInternal) {
    if (enableInternal) {
        Log.println(priority.value, logTag, message.toString())
    }
}

fun <T> T.withFootprintJson(priority: LogPriority = logLevelInternal, logTag: String = logTagInternal): T {
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

fun Throwable.footprintStackTrace(priority: LogPriority = stackTraceLogLevelInternal, logTag: String = logTagInternal) {
    if (enableInternal) {
        footprint(Log.getStackTraceString(this), priority = priority, logTag = logTag)
    }
}

fun footprintStackTrace(priority: LogPriority = stackTraceLogLevelInternal, logTag: String = logTagInternal) {
    if (enableInternal) {
        Throwable().footprintStackTrace(priority = priority, logTag = logTag)
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
