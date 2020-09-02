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
        // [ExampleActivity#onCreate:29] You can leave message.

        // params
        Footprint.leave("You can leave multiple params like", this.getClass(), 5, false, "test");
        // [ExampleActivity#onCreate:33] You can leave multiple params like  class com.morayl.footprintexample.ExampleActivity 5 false test

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
        Footprint.stackTrace();
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

        DataClass dataClass = new DataClass("test", "test2", 5, true);

        // leave json
        Footprint.json(dataClass);
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
        Footprint.json(dataClass, 8);
        /*
         [ExampleActivity#onClick:119]
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
         [ExampleActivity#onClick:133]
          ・isCorrect : true
          ・wasCorrect : false
         */

        // fields
        Footprint.fields(this);
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
