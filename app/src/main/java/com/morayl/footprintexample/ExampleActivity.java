package com.morayl.footprintexample;

import android.os.Bundle;
import android.view.View;

import com.morayl.footprint.Footprint;

import androidx.appcompat.app.AppCompatActivity;

/**
 * You can see examples. (Default LogTag is "Footprint")
 */
public class ExampleActivity extends AppCompatActivity implements View.OnClickListener {

    private String field1 = "field1";
    public boolean field2 = true;
    public DataClass field3 = new DataClass("fuga", "hoge", 100, false);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);

        // You just put this.
        Footprint.leave();// [ExampleActivity#onCreate:25]

        // a param
        Footprint.leave("You can leave message.");
        // [ExampleActivity#onCreate:28] You can leave message.

        // params
        Footprint.leave("You can leave multiple params like", this.getClass(), 5, false, "test");
        // [ExampleActivity#onCreate:32] You can leave multiple params like  class com.morayl.footprintexample.ExampleActivity 5 false test

        // Just show log. It's faster than leave because it's not use stackTrace.
        Footprint.simple("simple");
        // simple

        // params
        Footprint.simple("simple", "multiple", "params", 1, true);
        // simple multiple params 1 true

        View button = findViewById(R.id.button);
        if (button != null) {
            button.setOnClickListener(this);
        }

        findViewById(R.id.toast).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // display toast
                Footprint.toast(ExampleActivity.this, "toast");
            }
        });

        findViewById(R.id.toast_duration).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // You can set duration
                Footprint.toast(ExampleActivity.this, "about 10 sec", 10000);
            }
        });

        findViewById(R.id.dialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // You can show long message or object(convert to json in method) in dialog
                Footprint.dialog(ExampleActivity.this, getString(R.string.long_message));
            }
        });

        findViewById(R.id.notification).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // You can show notification and can show long message in BigPictureStyle
                Footprint.notify(ExampleActivity.this, getString(R.string.long_message));
            }
        });
    }

    @Override
    public void onClick(View v) {
        Footprint.leave();// [ExampleActivity#onClick:83]

        // StackTrace
        try {
            String str = null;
            str.length();
        } catch (NullPointerException e) {
            Footprint.stackTrace(e);
            /*
            [ExampleActivity#onClick:90] java.lang.NullPointerException: Attempt to invoke virtual method 'int java.lang.String.length()' on a null object reference
            at com.morayl.footprintexample.ExampleActivity.onClick(ExampleActivity.java:88)
            at android.view.View.performClick(View.java:7143)
            at android.view.View.performClickInternal(View.java:7120)
            at android.view.View.access$3500(View.java:804)
            at android.view.View$PerformClick.run(View.java:27433)
            at android.os.Handler.handleCallback(Handler.java:883)
            at android.os.Handler.dispatchMessage(Handler.java:100)
            at android.os.Looper.loop(Looper.java:231)
            at android.app.ActivityThread.main(ActivityThread.java:7769)
            at java.lang.reflect.Method.invoke(Native Method)
            at com.android.internal.os.RuntimeInit$MethodAndArgsCaller.run(RuntimeInit.java:492)
            at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:953)
             */
        }

        // You can also use with no params. You can see callers.
        Footprint.stackTrace();
        /*
        [ExampleActivity#onClick:109] java.lang.Throwable
        at com.morayl.footprint.Footprint.stackTrace(Footprint.java:237)
        at com.morayl.footprintexample.ExampleActivity.onClick(ExampleActivity.java:109)
        at android.view.View.performClick(View.java:7143)
        at android.view.View.performClickInternal(View.java:7120)
        at android.view.View.access$3500(View.java:804)
        at android.view.View$PerformClick.run(View.java:27433)
        at android.os.Handler.handleCallback(Handler.java:883)
        at android.os.Handler.dispatchMessage(Handler.java:100)
        at android.os.Looper.loop(Looper.java:231)
        at android.app.ActivityThread.main(ActivityThread.java:7769)
        at java.lang.reflect.Method.invoke(Native Method)
        at com.android.internal.os.RuntimeInit$MethodAndArgsCaller.run(RuntimeInit.java:492)
        at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:953)
         */

        DataClass dataClass = new DataClass("test", "test2", 5, true);

        // leave json
        Footprint.json(dataClass);
        /*
         [ExampleActivity#onClick:130]
          {
            "value3": 5,
            "value4": true,
            "value1": "test",
            "value2": "test2"
          }
         */

        // leave json with set indent
        Footprint.json(dataClass, 8);
        /*
         [ExampleActivity#onClick:142]
          {
                "value3": 5,
                "value4": true,
                "value1": "test",
                "value2": "test2"
          }
         */

        boolean isCorrect = true;
        boolean wasCorrect = false;
        // key and value
        Footprint.keyAndValues("isCorrect", isCorrect, "wasCorrect", wasCorrect);
        /*
         [ExampleActivity#onClick:156]
          ・isCorrect : true
          ・wasCorrect : false
         */

        // fields
        Footprint.fields(this);
        /*
         [ExampleActivity#onClick:164]
          {
            "nameValuePairs": {
            "field1": "field1",
            "field3": "com.morayl.footprintexample.ExampleActivity$DataClass@418187e0",
            "field2": "true"
          }
         */
    }

    private static class DataClass {
        public String value1;
        public String value2;
        public int value3;
        public boolean value4;

        public DataClass(String value1, String value2, int value3, boolean value4) {
            this.value1 = value1;
            this.value2 = value2;
            this.value3 = value3;
            this.value4 = value4;
        }
    }
}
