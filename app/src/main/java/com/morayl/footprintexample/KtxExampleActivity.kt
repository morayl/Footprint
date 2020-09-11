package com.morayl.footprintexample

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.morayl.footprintktx.*

/**
 * You can see examples.
 * Launch this activity and see Logcat. (Default LogTag is "Footprint")
 */
class KtxExampleActivity : AppCompatActivity(R.layout.activity_ktx_exsample), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // You write put this.
        footprint() // [ExampleActivity#onCreate:25]

        // a param
        footprint("You can leave message.")
        // [ExampleActivity#onCreate:29] You can leave message.

        // params
        footprint("You can leave multiple params like", this::class.java, 5, false, "test")
        // [ExampleActivity#onCreate:33] You can leave multiple params like  class com.morayl.footprintexample.ExampleActivity 5 false test

        // with setting LogPriority
        footprint("You can leave message with set LogPriority", priority = LogPriority.ERROR)

        // show address when you put object(To be exact, output the toString())
        footprint(this)

        // stand out log in many footprints log
        accentFootprint()
        accentFootprint("stand out", "log", 500)

        // Just show log. It's faster than leave because it's not use stackTrace.
        simpleFootprint("simple")
        // simple

        // params
        simpleFootprint("simple", "multiple", "params", 1, true)
        // simple multiple params 1 true

        // log receiver with receive
        val floatValue = 3.5f
        floatValue.withFootprint()
        "sample text".withFootprint()

        findViewById<View>(R.id.button_stacktrace).setOnClickListener(this)
        findViewById<View>(R.id.button_json).setOnClickListener(this)
        findViewById<View>(R.id.button_key_values).setOnClickListener(this)
        findViewById<View>(R.id.button_java_sample).setOnClickListener {
            startActivity(Intent(this, ExampleActivity::class.java))
        }
    }

    override fun onClick(v: View) {
        footprint() // [ExampleActivity#onClick:83]

        when (v.id) {
            R.id.button_stacktrace -> {
                try {
                    val str: String? = null
                    str!!.length
                } catch (e: NullPointerException) {
                    // You can log stacktrace
                    e.stacktraceFootprint()
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
                stacktraceFootprint()
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
            }
            R.id.button_json -> {
                val dataClass = SampleDataClass("hoge", "fuga", 5, true)

                // log json
                jsonFootprint(dataClass)
                /*
                 [ExampleActivity#onClick:107]
                  {
                    "value3": 5,
                    "value4": true,
                    "value1": "test",
                    "value2": "test2"
                  }
                 */

                // log json with receive
                val dataClass2 = SampleDataClass("hoge2", "fuga2", 5, false).withJsonFootprint()
                footprint(dataClass2.value1, dataClass2.value2, dataClass2.value3, dataClass2.value4)
            }
            R.id.button_key_values -> {
                val isCorrect = true
                val wasCorrect = false
                // log pair.
                pairFootprint("isCorrect" to isCorrect, "wasCorrect" to wasCorrect)
                pairFootprint("first" to 1, "second" to "secondValue", "third" to 3.toString())
                /*
                 [ExampleActivity#onClick:133]
                  ・isCorrect : true
                  ・wasCorrect : false
                 */
            }
        }
    }

    private data class SampleDataClass(val value1: String,
                                       val value2: String,
                                       val value3: Int,
                                       val value4: Boolean)
}