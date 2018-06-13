package io.stanwood.uitesting.log;

import android.support.test.uiautomator.UiDevice;

import io.stanwood.uitesting.model.TestSuite;

/**
 * Created by borut2 on 16/01/2018.
 */

public interface Tracker {

    String generateReport(UiDevice device, TestSuite testSuite, Report report) throws Exception;
}
