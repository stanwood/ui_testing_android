package io.stanwood.uitesting.demo.test;

import android.support.test.filters.SdkSuppress;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.stanwood.uitesting.BaseTest;
import io.stanwood.uitesting.demo.BuildConfig;
import io.stanwood.uitesting.log.SlackTracker;
import io.stanwood.uitesting.schema.ResourceSchemaProvider;

@RunWith(AndroidJUnit4.class)
@SdkSuppress(minSdkVersion = 18)
public class SUITest extends BaseTest {

    private static final String JSON_SCHEMA_FILE = "res/raw/test_schema.json";

    @Before
    public void start() {
        SlackTracker slackTracker = new SlackTracker(
                "<enter #channel_name here>",
                "<enter team id here>",
                "<enter token here>");
        //FirebaseSchemaProvider schemaProvider = new FirebaseSchemaProvider("io.stanwood.uitesting.demo", "1.0.0");
        super.initTests(new ResourceSchemaProvider(this, JSON_SCHEMA_FILE, BuildConfig.APPLICATION_ID), slackTracker);
    }

    @Test
    public void runTests() {
        super.runTests();
    }

}