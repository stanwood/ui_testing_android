package io.stanwood.uitesting.log;

import android.util.Log;

public class Logger {

    public static void logDelimiter() {
        log("-------------------------------------------------------------------");
    }

    public static void log(String s) {
        Log.d("StanwoodTest", s);
    }

    public static void log(Exception e) {
        if (e.getMessage() != null) {
            Log.e("StanwoodTest", e.getMessage());
        } else {
            Log.e("StanwoodTest", e.getMessage());
        }
        e.printStackTrace();
    }
}
