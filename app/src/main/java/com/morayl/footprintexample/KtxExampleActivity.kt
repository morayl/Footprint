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
        footprint() // [KtxExampleActivity#onCreate:19]

        // A param.
        footprint("You can leave message.")
        // [KtxExampleActivity#onCreate:22] You can leave message.

        // Params.
        footprint("You can leave multiple params like", this::class.java, 5, false, "test")
        // [KtxExampleActivity#onCreate:26] You can leave multiple params like class com.morayl.footprintexample.KtxExampleActivity 5 false test

        // With setting LogPriority.
        footprint("You can leave message with set LogPriority", priority = LogPriority.ERROR)
        // [KtxExampleActivity#onCreate:30] You can leave message with set LogPriority (With red colored log.)

        // Show address when you put object(To be exact, output the toString())
        footprint(this)
        // [KtxExampleActivity#onCreate:34] com.morayl.footprintexample.KtxExampleActivity@f7ae936

        // Stand out log in many footprints log
        accentFootprint()
        // [KtxExampleActivity#onCreate:38] (With red colored log.)
        accentFootprint("stand out", "log", 500)
        // [KtxExampleActivity#onCreate:40] stand out log 500 (With red colored log.)

        // Just show log. It's faster than leave because it's not use stacktrace.
        simpleFootprint("simple")
        // simple

        // Params
        simpleFootprint("simple", "multiple", "params", 1, true)
        // simple multiple params 1 true

        // Log receiver with receiving
        val floatValue: Float = 3.5f.withFootprint()
        // [KtxExampleActivity#onCreate:52] 3.5
        val textAndNumber: String = "sample text and float:$floatValue".withFootprint()
        // [KtxExampleActivity#onCreate:54] sample text and float:3.5
        footprint(textAndNumber)
        // [KtxExampleActivity#onCreate:56] sample text and float:3.5
        val text: String = "sample text".withFootprint { it.length }
        // [KtxExampleActivity#onCreate:58] 11
        val intValue: Int = 100.withSimpleFootprint()
        // 100
        val intValue2: Int = 100.withSimpleFootprint { "Number is $it" }
        // Number is 100

        initButtons()
    }

    override fun onClick(v: View) {
        footprint() // [ExampleActivity#onClick:69]

        when (v.id) {
            R.id.button_stacktrace -> {
                try {
                    val str: String? = null
                    str!!.length
                } catch (e: NullPointerException) {
                    // You can log stacktrace.
                    e.stacktraceFootprint()
                    /*
                     [KtxExampleActivity#onClick:78] java.lang.NullPointerException
                     at com.morayl.footprintexample.KtxExampleActivity.onClick(KtxExampleActivity.kt:75)
                     at android.view.View.performClick(View.java:7155)
                     at android.view.View.performClickInternal(View.java:7132)
                     at android.view.View.access$3500(View.java:816)
                     at android.view.View$PerformClick.run(View.java:27461)
                     at android.os.Handler.handleCallback(Handler.java:883)
                     at android.os.Handler.dispatchMessage(Handler.java:100)
                     at android.os.Looper.loop(Looper.java:214)
                     at android.app.ActivityThread.main(ActivityThread.java:7890)
                     at java.lang.reflect.Method.invoke(Native Method)
                     at com.android.internal.os.RuntimeInit$MethodAndArgsCaller.run(RuntimeInit.java:492)
                     at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:960)
                     */
                }

                // You can also use with no params. You can see callers.
                stacktraceFootprint()
                /*
                 [KtxExampleActivity#onClick:97] java.lang.Throwable
                 at com.morayl.footprintktx.FootprintKt.stacktraceFootprint(Footprint.kt:238)
                 at com.morayl.footprintktx.FootprintKt.stacktraceFootprint$default(Footprint.kt:236)
                 at com.morayl.footprintexample.KtxExampleActivity.onClick(KtxExampleActivity.kt:91)
                 at android.view.View.performClick(View.java:7155)
                 at android.view.View.performClickInternal(View.java:7132)
                 at android.view.View.access$3500(View.java:816)
                 at android.view.View$PerformClick.run(View.java:27461)
                 at android.os.Handler.handleCallback(Handler.java:883)
                 at android.os.Handler.dispatchMessage(Handler.java:100)
                 at android.os.Looper.loop(Looper.java:214)
                 at android.app.ActivityThread.main(ActivityThread.java:7890)
                 at java.lang.reflect.Method.invoke(Native Method)
                 at com.android.internal.os.RuntimeInit$MethodAndArgsCaller.run(RuntimeInit.java:492)
                 at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:960)
                 */
            }
            R.id.button_json -> {
                val innerDataClass = SampleData2("name", 3, 5.5f)
                val dataClass = SampleData("hoge", "fuga", 5, true, innerDataClass)

                // Log json.
                jsonFootprint(dataClass)
                /*
                 [KtxExampleActivity#onClick:121]
                 {
                    "value1": "hoge",
                    "value2": "fuga",
                    "value3": 5,
                    "value4": true,
                    "value5": {
                        "count": 3,
                        "float": 5.5,
                        "name": "name"
                    }
                 }
                 */

                // Log json with receiving
                val dataClass2 = SampleData("hoge2", "fuga2", 5, false, innerDataClass).withJsonFootprint()
                /*
                [KtxExampleActivity#onClick:138]
                 {
                    "value1": "hoge2",
                    "value2": "fuga2",
                    "value3": 5,
                    "value4": false,
                    "value5": {
                        "count": 3,
                        "float": 5.5,
                        "name": "name"
                    }
                 }
                 */
                footprint(dataClass2.value1, dataClass2.value2, dataClass2.value3, dataClass2.value4, dataClass2.value5.name)
                // [KtxExampleActivity#onClick:153] hoge2 fuga2 5 false name
                val dataClass3 = SampleData("hoge2", "fuga2", 5, false, innerDataClass).withJsonFootprint { it.value5 }
                /*
                [KtxExampleActivity#onClick:155]
                 {
                    "count": 3,
                    "float": 5.5,
                    "name": "name"
                }
                 */
            }
            R.id.button_key_values -> {
                val isCorrect = true
                val wasCorrect = false
                // Log pair.
                pairFootprint("isCorrect" to isCorrect, "wasCorrect" to wasCorrect)
                /*
                [KtxExampleActivity#onClick:169]
                isCorrect : true
                wasCorrect : false
                 */
                pairFootprint("first" to 1, "second" to "secondValue", "third" to 3.toString())
                /*
                [KtxExampleActivity#onClick:175]
                first : 1
                second : secondValue
                third : 3
                 */
            }
            R.id.button_configure -> {
                // You can configure Footprint. All params have default, You can set only you want to change.
                // More information of params, see method declaration.
                configFootprint(
                        enable = true,
                        logTag = "FootprintConfigured",
                        logPriority = LogPriority.ERROR,
                        showInternalJsonException = true,
                        forceSimple = true,
                        stacktraceLogLogPriority = LogPriority.DEBUG,
                        jsonIndentCount = 2
                )
                recreate()
            }
            R.id.button_configure_default -> {
                configFootprint(
                        enable = true,
                        logTag = "Footprint",
                        logPriority = LogPriority.DEBUG,
                        showInternalJsonException = false,
                        forceSimple = false,
                        stacktraceLogLogPriority = LogPriority.ERROR,
                        jsonIndentCount = 4
                )
                recreate()
            }
        }
    }

    private fun initButtons() {
        findViewById<View>(R.id.button_stacktrace).setOnClickListener(this)
        findViewById<View>(R.id.button_json).setOnClickListener(this)
        findViewById<View>(R.id.button_key_values).setOnClickListener(this)
        findViewById<View>(R.id.button_configure).setOnClickListener(this)
        findViewById<View>(R.id.button_configure_default).setOnClickListener(this)
        findViewById<View>(R.id.button_java_sample).setOnClickListener {
            startActivity(Intent(this, ExampleActivity::class.java))
        }
    }

    private data class SampleData(val value1: String,
                                  val value2: String,
                                  val value3: Int,
                                  val value4: Boolean,
                                  val value5: SampleData2)

    private data class SampleData2(val name: String,
                                   val count: Int,
                                   val float: Float)
}