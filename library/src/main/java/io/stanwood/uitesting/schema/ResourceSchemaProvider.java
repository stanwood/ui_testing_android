package io.stanwood.uitesting.schema;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ResourceSchemaProvider extends SchemaProvider {

    Object mObject;

    public ResourceSchemaProvider(Object object, String fileName, String packageName) {
        super(fileName, packageName);
        mObject = object;
    }

    public String provideJson() throws IOException {
        InputStream inputStream = mObject.getClass().getClassLoader().getResourceAsStream(fileName);

        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) != -1) {
            result.write(buffer, 0, length);
        }

        return result.toString();
    }
}
