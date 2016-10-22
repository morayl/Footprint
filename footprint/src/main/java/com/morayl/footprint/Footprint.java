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
 * ★ひとことメソッド紹介★
 * いちいちLogTagを入れるの面倒ですよね。{@link Footprint}
 * Log.dってキーボードで打ちにくいですよね。{@link #leave()}
 * いちいち変数と文字列を"" + ""って書くの面倒ですよね。{@link #leave(Object...)}
 * データクラスの全値を見やすく出したいですよね。{@link #json(Object)}
 * トーストのLength指定が毎度めんどい、.show()を忘れるとかありますよね。{@link #toast(Context, String)}
 * トーストすぐ消えちゃってデバッグにならないことありますよね。{@link #toast(Context, String, int)}
 *
 * メソッドの頭文字はなるべくホームポジションから動かなくていいものにしています
 * (s,f,t,j,k,l)
 *
 * Created by morayl on 2015/02/21.
 */
public class Footprint {
    /** デフォルトのタグ */
    private static final String DEFAULT_TAG = "Footprint";
    /** デフォルトのインデント */
    private static final int DEFAULT_INDENT = 4;

    /** タグ */
    private static String LOG_TAG = DEFAULT_TAG;

    /**
     * タグを変更する
     * デフォルトはFootprint
     *
     * @param logTag タグ名
     */
    public static void setLogTag(final String logTag) {
        LOG_TAG = getStringValue(logTag);
    }

    /**
     * クラス名、メソッド名、行数、メッセージをログに出力
     * ※leaveメソッド郡は、StackTraceを参照するため、回数の多いfor文等で利用すると処理落ちする可能性があります
     *
     * @param message メッセージ
     */
    public static void leave(final Object message) {
        simple(getMetaInfo(), getStringValue(message));
    }

    /**
     * クラス名、メソッド名、行数をログに出力
     *
     * @see #leave(Object)
     */
    public static void leave() {
        leave("");
    }

    /**
     * クラス名、メソッド名、行数、メッセージをログに出力
     * メッセージは順番に連結されます
     * 文字列と変数をログ出力したい場合に、"" + valueと+を入力するのが面倒な人へ
     *
     * @param messages メッセージ
     * @see #leave(Object)
     */
    public static void leave(final Object... messages) {
        leave(unionObjectsString(messages));
    }

    /**
     * Log.dを行う
     *
     * @param message メッセージ
     */
    public static void simple(final Object message) {
        Log.d(LOG_TAG, getStringValue(message));
    }

    /**
     * Log.dを行う
     *
     * @param messages メッセージ
     */
    public static void simple(final Object... messages) {
        simple(unionObjectsString(messages));
    }

    /**
     * クラス名、メソッド名、行数、メッセージをログに出力
     * 引数は、キー,バリュー,キー,バリューという形で交互に入れることで、
     * ・キー：バリュー
     * ・キー：バリュー
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
     * 与えられた変数をjsonに整形して出力
     * データ・モデルクラスでない場合で循環参照などがある場合には、
     * {@link StackOverflowError}、{@link JSONException}が発生する可能性があります(内部でcatchされます)
     *
     * @param data データ
     */
    public static void json(Object data) {
        json(data, DEFAULT_INDENT);
    }

    /**
     * 与えられた変数のフィールドを全てログに出力する(インデント数指定)
     *
     * @param data データ
     * @param indent インデントの幅
     * @see #json(Object)
     */
    public static void json(Object data, final int indent) {
        leave("\n", getFormattedJSON(data, indent));
    }

    /**
     * 全てのフィールドとその値を出力する
     *
     * @param object 対象
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
     * {@link Throwable#printStackTrace()}をLogcatで再現する
     *
     * @param t 対象
     */
    public static void stackTrace(final Throwable t) {
        leave(Log.getStackTraceString(t));
    }

    /**
     * この時点でのスタックトレースを出力
     * メソッドの呼び出し階層を確認したいときなどに
     */
    public static void stackTrace() {
        stackTrace(new Throwable());
    }

    /**
     * トーストを出力
     *
     * @param context {@link Context}
     * @param message メッセージ
     */
    public static void toast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * 指定秒数間トーストを表示し続ける
     *
     * @param context {@link Context}
     * @param message メッセージ
     * @param durationMillis 表示時間[ms](大体です)
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
     * Objectを文字列に変換
     *
     * @param object 変換対象
     * @return 文字列
     */
    private static String getStringValue(Object object) {
        return String.valueOf(object);
    }

    /**
     * ログ呼び出し元のメタ情報を取得する
     * なければ"none"を返す
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
     * @param object 対象
     * @param indent インデント
     * @return json文字列
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
     * 文字列をスペースで連結して返す
     *
     * @param objects {@link Object}
     * @return スペースで連結された文字列
     */
    private static String unionObjectsString(final Object... objects) {
        if (objects == null) {
            return "null";
        }
        StringBuilder sb = new StringBuilder();
        for (Object object : objects) {
            sb.append(getStringValue(object)).append(" ");

        }
        return sb.deleteCharAt(sb.length() - 1).toString();
    }

    /**
     * nullならnullログ出力してtrueを返す
     *
     * @param object 対象
     * @return nullならtrue
     */
    private static boolean isNullLeaveNull(final Object object) {
        if (object == null) {
            leave("null");
            return true;
        }
        return false;
    }
}
