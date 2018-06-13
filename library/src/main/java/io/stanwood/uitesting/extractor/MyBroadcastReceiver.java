package io.stanwood.uitesting.extractor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.test.uiautomator.UiDevice;
import android.util.Log;

import java.io.ByteArrayOutputStream;

public class MyBroadcastReceiver extends BroadcastReceiver {

    UiDevice device;

    public MyBroadcastReceiver(UiDevice device) {
        this.device = device;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            device.dumpWindowHierarchy(os);
            Log.w("DumpWindowHierarchy", "Window hierarchy:");
            for (String line : os.toString("UTF-8").split("\n")) {
                Log.w("DumpWindowHierarchy", line);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}