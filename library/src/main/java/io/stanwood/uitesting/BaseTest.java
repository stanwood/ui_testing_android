package io.stanwood.uitesting;

import android.content.IntentFilter;
import android.support.test.InstrumentationRegistry;
import android.support.test.uiautomator.UiDevice;

import io.stanwood.uitesting.extractor.MyBroadcastReceiver;
import io.stanwood.uitesting.log.Tracker;
import io.stanwood.uitesting.model.TestSuite;
import io.stanwood.uitesting.schema.SchemaProvider;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

public class BaseTest {

    protected TestSuite testSuite;
    protected UiDevice device;
    NavigationProcessor navigationProcessor;
    MyBroadcastReceiver receiver;

    protected void initTests(SchemaProvider schemaProvider, Tracker... trackers) {
        testSuite = schemaProvider.readSchema();

        assertThat(testSuite, notNullValue());
        assertThat(testSuite.getPackageName(), notNullValue());
        assertThat(testSuite.getTestCases(), notNullValue());

        // Initialize UiDevice instance
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        receiver = new MyBroadcastReceiver(device);
        InstrumentationRegistry.getTargetContext().registerReceiver(receiver, new IntentFilter("io.stanwood.uitesting.DUMP_WINDOW_HIERARCHY_INTENT"));

        navigationProcessor = new NavigationProcessor(device, testSuite, trackers);

    }

    protected void runTests() {
        assertThat(device, notNullValue());
        assertThat(testSuite, notNullValue());
        navigationProcessor.process();
        if (receiver != null) {
            InstrumentationRegistry.getTargetContext().unregisterReceiver(receiver);
        }
    }

}
