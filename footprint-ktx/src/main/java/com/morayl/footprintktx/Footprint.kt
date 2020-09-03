package com.morayl.footprintktx

import android.util.Log
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

internal const val DEFAULT_TAG = "Footprint"

fun leave() {
    leaveSimple(getMetaInfo())
}

fun leave(vararg messages: Any?) {
    leaveSimple(getMetaInfo(), messages.joinToString(separator = " "))
}

fun leaveSimple(vararg messages: Any?) {
    leaveSimple(messages.joinToString(separator = " "))
}

fun leaveSimple(message: Any?) {
    Log.d(DEFAULT_TAG, message.toString())
}

fun <T> T.leaveJson(): T {
    leave("\n", toFormattedJSON())
    return this
}

/**
 * 整形されたJSON文字列を返す
 *
 * @param indent インデント
 * @return json文字列
 */
private fun Any?.toFormattedJSON(indent: Int = 2): String? {
    try {
        val json = Gson().toJson(this)
        return try {
            val jsonObject = JSONObject(json)
            jsonObject.toString(indent)
        } catch (e: JSONException) {
            val jsonArray = JSONArray(json)
            jsonArray.toString(indent)
        }
    } catch (t: Throwable) {
        t.leaveStackTrace()
    }
    return toString()
}


fun Throwable.leaveStackTrace() {
    leave(Log.getStackTraceString(this))
}

fun leaveStacktrace() {
    Throwable().leaveStackTrace()
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
