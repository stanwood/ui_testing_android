package io.stanwood.uitesting.schema;

import android.support.annotation.NonNull;

public class FirebaseSchemaProvider extends HttpSchemaProvider {

    private static final String BASE_URL = "https://stanwood-ui-testing.firebaseio.com/android/%s/%s.json";

    public FirebaseSchemaProvider(@NonNull String packageName, @NonNull String version) {
        super("", packageName);
        fileName = String.format(BASE_URL, packageName.replace(".", "-"), version.replace(".", "-"));
    }
}
