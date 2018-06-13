package io.stanwood.uitesting.log;

/**
 * Created by borut2 on 16/01/2018.
 */

public class Report {

    public int getFailedTests() {
        return failedTests;
    }

    public void setFailedTests(int failedTests) {
        this.failedTests = failedTests;
    }

    public int getSucceededTests() {
        return succeededTests;
    }

    public void setSucceededTests(int succeededTests) {
        this.succeededTests = succeededTests;
    }

    public boolean succeeded() {
        return failedTests == 0;
    }

    private int failedTests;
    private int succeededTests;


}
