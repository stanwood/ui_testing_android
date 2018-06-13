package io.stanwood.uitesting.log;

import android.support.annotation.Nullable;
import android.support.test.uiautomator.UiDevice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.stanwood.uitesting.model.TestSuite;

public class ReportDispatcher {

    List<Tracker> items = new ArrayList<>();

    public ReportDispatcher(@Nullable Tracker... items) {
        if (items != null) Collections.addAll(this.items, items);
    }

    public void process(UiDevice device, TestSuite testSuite, Report report) {
        for(Tracker tracker: items) {
            try {
                tracker.generateReport(device, testSuite, report);
            } catch (Exception e) {
                Logger.log(e);
            }
        }
    }
}
