package io.stanwood.uitesting.schema;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HttpSchemaProvider extends SchemaProvider {

    private final OkHttpClient client = new OkHttpClient();

    public HttpSchemaProvider(String fileName, String packageName) {
        super(fileName, packageName);
    }

    @Override
    public String provideJson() throws IOException {
        Request request = new Request.Builder()
                .url(fileName)
                .build();
        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException(response.toString());
        return response.body().string();
    }

}
