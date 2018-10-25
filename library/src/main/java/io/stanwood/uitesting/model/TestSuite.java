package io.stanwood.uitesting.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TestSuite {

    @SerializedName("test_cases")
    private List<TestCase> testCases;

    @SerializedName("package_name")
    private String packageName;

    @SerializedName("initial_sleep_time")
    private long initialSleep;

    @SerializedName("command_sleep_time")
    private long commandsSleep;

    @SerializedName("launch_timeout")
    private long launchTimeout;

    @SerializedName("view_timeout")
    private long viewTimeout;

    @SerializedName("auto_snapshot")
    private boolean autoSnapshot;

    public boolean isAutoSnapshot() {
        return autoSnapshot;
    }

    public long getLaunchTimeout() {
        return launchTimeout;
    }

    public long getViewTimeout() {
        return viewTimeout;
    }

    public long getInitialSleep() {
        return initialSleep;
    }

    public long getCommandsSleep() {
        return commandsSleep;
    }

    public List<TestCase> getTestCases() {
        return testCases;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public boolean isPassed() {
        for (TestCase testCase : testCases) {
            if (!testCase.isPassed()) return false;
        }

        return true;
    }
}
