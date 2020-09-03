package com.morayl.footprintexample

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.morayl.footprint.Footprint
import com.morayl.footprintktx.*

/**
 * You can see examples. (Default LogTag is "Footprint")
 */
class KtxExampleActivity : AppCompatActivity(), View.OnClickListener {
    private val field1 = "field1"
    var field2 = true
    var field3 = DataClass("fuga", "hoge", 100, false)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sample)

        // You write put this.
        leave() // [ExampleActivity#onCreate:25]

        // a param
        leave("You can leave message.")
        // [ExampleActivity#onCreate:29] You can leave message.

        // params
        leave("You can leave multiple params like", this.javaClass, 5, false, "test")
        // [ExampleActivity#onCreate:33] You can leave multiple params like  class com.morayl.footprintexample.ExampleActivity 5 false test

        // Just show log. It's faster than leave because it's not use stackTrace.
        leaveSimple("simple")
        // simple

        // params
        leaveSimple("simple", "multiple", "params", 1, true)
        // simple multiple params 1 true
        val button = findViewById<View>(R.id.button)
        button?.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        leave() // [ExampleActivity#onClick:83]

        // StackTrace
        try {
            val str: String? = null
            str!!.length
        } catch (e: NullPointerException) {
            e.printStackTrace()
            e.leaveStackTrace()
            /*
             [ExampleActivity#onClick:75] java.lang.NullPointerException
             at com.morayl.footprintexample.ExampleActivity.onClick(ExampleActivity.java:73)
             at android.view.View.performClick(View.java:4307)
             at android.view.View$PerformClick.run(View.java:17507)
             at android.os.Handler.handleCallback(Handler.java:725)
             at android.os.Handler.dispatchMessage(Handler.java:92)
             at android.os.Looper.loop(Looper.java:137)
             at android.app.ActivityThread.main(ActivityThread.java:5159)
             at java.lang.reflect.Method.invokeNative(Native Method)
             */
        }

        // You can also use with no params. You can see callers.
        leaveStacktrace()
        /*
         [ExampleActivity#onClick:90] java.lang.Throwable
         at com.morayl.footprint.Footprint.stackTrace(Footprint.java:253)
         at com.morayl.footprintexample.ExampleActivity.onClick(ExampleActivity.java:90)
         at android.view.View.performClick(View.java:4307)
         at android.view.View$PerformClick.run(View.java:17507)
         at android.os.Handler.handleCallback(Handler.java:725)
         at android.os.Handler.dispatchMessage(Handler.java:92)
         at android.os.Looper.loop(Looper.java:137)
         at android.app.ActivityThread.main(ActivityThread.java:5159)
         at java.lang.reflect.Method.invokeNative(Native Method)
         */
        val dataClass = DataClass("test", "test2", 5, true)

        // leave json
        dataClass.leaveJson()
        /*
         [ExampleActivity#onClick:107]
          {
            "value3": 5,
            "value4": true,
            "value1": "test",
            "value2": "test2"
          }
         */

        // leave json with set indent
//        Footprint.json(dataClass, 8)
        /*
         [ExampleActivity#onClick:119]
          {
                "value3": 5,
                "value4": true,
                "value1": "test",
                "value2": "test2"
          }
         */
        val isCorrect = true
        val wasCorrect = false
        // key and value
        Footprint.keyAndValues("isCorrect", isCorrect, "wasCorrect", wasCorrect)
        /*
         [ExampleActivity#onClick:133]
          ・isCorrect : true
          ・wasCorrect : false
         */

        // fields
        Footprint.fields(this)
        /*
         [ExampleActivity#onClick:141]
          {
            "nameValuePairs": {
            "field1": "field1",
            "field3": "com.morayl.footprintexample.ExampleActivity$DataClass@418187e0",
            "field2": "true"
          }
         */
    }

    class DataClass(var value1: String, var value2: String, var value3: Int, var value4: Boolean)
}