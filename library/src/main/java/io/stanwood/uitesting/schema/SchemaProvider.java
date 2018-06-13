package io.stanwood.uitesting.schema;

import com.google.gson.Gson;

import java.io.IOException;

import io.stanwood.uitesting.log.Logger;
import io.stanwood.uitesting.model.TestSuite;

public abstract class SchemaProvider {

    String fileName;
    String packageName;

    public SchemaProvider(String fileName, String packageName) {
        this.fileName = fileName;
        this.packageName = packageName;
    }

    public TestSuite readSchema() {
        TestSuite testSuite = null;
        try {
            Logger.logDelimiter();
            Logger.log("Reading JSON schema:" + fileName);

            String json = provideJson();

            Gson gson = new Gson();
            testSuite = gson.fromJson(json, TestSuite.class);
            testSuite.setPackageName(packageName);
            Logger.log(String.format("JSON schema has been read, %s test cases", testSuite.getTestCases().size()));
        } catch (Exception e) {
            Logger.log(e);
        }
        return testSuite;
    }

    protected abstract String provideJson() throws IOException;
}
