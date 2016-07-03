package com.morayl.footprint;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;

/**
 * 足跡を、残す。
 * ログを吐くUtilクラス
 * Footprint.leave();と書くだけで、
 * それを書いた場所のクラス名・メソッド名・行数が出力されます。
 * [クラス#メソッド:行数]
 *
 * Created by morayl on 2015/02/21.
 */
public class Footprint {
    /** デフォルトのタグ */
    private static final String DEFAULT_TAG = "Footprint";
    /** タグ */
    private static String LOG_TAG = DEFAULT_TAG;
    /** デフォルトのインデント */
    private static final int DEFAULT_INDENT = 4;

    /**
     * Objectを文字列に変換
     *
     * @param object
     * @return
     */
    private static String getStringValue(Object object) {
        return String.valueOf(object);
    }

    /**
     * ログ呼び出し元のメタ情報を取得する
     *
     * @return [className#methodName:line]
     */
    private static String getMetaInfo() {
        // スタックトレースから情報を取得 // 0: VM, 1: Thread, 2: このメソッド, 3: このメソッドの呼び出し元...
        final StackTraceElement[] elements = Thread.currentThread().getStackTrace();
        for (int i = 2; i < elements.length; i++) {
            if (!elements[i].getFileName().startsWith("Footprint")) {
                return getMetaInfo(elements[i]);
            }
        }
        return "none";
    }

    /**
     * スタックトレースからクラス名、メソッド名、行数を取得する
     *
     * @return [className#methodName:line]
     */
    private static String getMetaInfo(StackTraceElement element) {
        // クラス名、メソッド名、行数を取得
        final String fullClassName = element.getClassName();
        final String simpleClassName = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
        final String methodName = element.getMethodName();
        final int lineNumber = element.getLineNumber();

        return "[" + simpleClassName + "#" + methodName + ":" + lineNumber + "]";
    }

    /**
     * 整形されたJSON文字列を返す
     *
     * @param object
     * @param indent
     * @return
     */
    private static String getFormattedJSON(Object object, final int indent) {
        try {
            final String json = new Gson().toJson(object);
            try {
                JSONObject jsonObject = new JSONObject(json);
                return jsonObject.toString(indent);
            } catch (JSONException e) {
                JSONArray jsonArray = new JSONArray(json);
                return jsonArray.toString(indent);
            }
        } catch (Throwable t) {
            stackTrace(t);
        }
        return "error";
    }

    /**
     * 文字列を連結して返す
     *
     * @param objects
     * @return
     */
    private static String unionObjectsString(final Object... objects) {
        if (objects == null) {
            return "null";
        }
        StringBuilder sb = new StringBuilder();
        for (Object object : objects) {
            sb.append(getStringValue(object)).append(" ");

        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    /**
     * nullならnullログ出力してtrueを返す
     *
     * @param object
     */
    private static boolean isNullLeaveNull(final Object object) {
        if (object == null) {
            leave("null");
            return true;
        }
        return false;
    }

    /**
     * クラス名、メソッド名、行数をログに出力
     */
    public static void leave() {
        leave("");
    }

    /**
     * クラス名、メソッド名、行数、メッセージをログに出力
     */
    public static void leave(final Object message) {
        simple(getMetaInfo(), getStringValue(message));
    }

    /**
     * クラス名、メソッド名、行数、メッセージをログに出力
     * メッセージは順番に連結されます
     * 文字列と変数をログ出力したい場合に、"" + valueと+を入力するのが面倒な人へ
     *
     * @param messages
     */
    public static void leave(final Object... messages) {
        leave(unionObjectsString(messages));
    }

    /**
     * Log.dを行う
     *
     * @param message
     */
    public static void simple(final Object message) {
        Log.d(LOG_TAG, getStringValue(message));
    }

    /**
     * Log.dを行う
     *
     * @param messages
     */
    public static void simple(final Object... messages) {
        simple(unionObjectsString(messages));
    }

    /**
     * クラス名、メソッド名、行数、メッセージをログに出力
     * 引数は、キー,バリュー,キー,バリューという形で交互に入れることで、
     * ■キー：バリュー
     * ■キー：バリュー
     * とログ出力されます
     *
     * @param keyValueKeyValue String,Object,String,Object...
     */
    public static void keyAndValues(final Object... keyValueKeyValue) {
        if (isNullLeaveNull(keyValueKeyValue)) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < keyValueKeyValue.length; i++) {
            final String message = getStringValue(keyValueKeyValue[i]);
            if (i % 2 == 0) {
                sb.append("\n ・");
                sb.append(message);
                sb.append(" : ");
            } else {
                sb.append(getStringValue(message));
            }
        }
        leave(sb.toString());
    }

    /**
     * 与えられた変数のフィールドを全てログに出力する
     *
     * @param object
     */
    public static void json(Object object) {
        json(object, DEFAULT_INDENT);
    }

    /**
     * 与えられた変数のフィールドを全てログに出力する
     *
     * @param object
     * @param indent インデントの幅
     * @return
     */
    public static void json(Object object, final int indent) {
        leave("\n", getFormattedJSON(object, indent));
    }

    /**
     * 全てのフィールドとその値を出力する
     *
     * @param object
     */
    public static void fields(Object object) {
        if (isNullLeaveNull(object)) {
            return;
        }
        try {
            final JSONObject jsonObject = new JSONObject();
            for (Field field : object.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                jsonObject.put(field.getName(), getStringValue(field.get(object)));
            }
            json(jsonObject);
        } catch (Throwable t) {
            stackTrace(t);
        }
    }

    /**
     * 渡されたThrowableのスタックトレースを出力
     *
     * @param t
     */
    public static void stackTrace(final Throwable t) {
        leave(Log.getStackTraceString(t));
    }

    /**
     * この時点でのスタックトレースを出力
     */
    public static void stackTrace() {
        stackTrace(new Throwable());
    }

    /**
     * トーストを出力する
     *
     * @param context
     * @param message
     */
    public static void toast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * 指定秒数間トーストを表示し続ける
     *
     * @param context
     * @param message
     * @param durationMillis
     */
    public static void toast(final Context context, final String message, final int durationMillis) {
        toast(context, message);
        if (durationMillis > 1500) {
            // 残り時間
            final int leftDurationMillis = durationMillis - 1500;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    toast(context, message, leftDurationMillis);
                }
            }, leftDurationMillis > 1500 ? 1500 : leftDurationMillis);
        }
    }

    /**
     * タグを変更する
     *
     * @param logTag
     */
    public static void changeLogTag(final String logTag) {
        LOG_TAG = getStringValue(logTag);
    }
}
