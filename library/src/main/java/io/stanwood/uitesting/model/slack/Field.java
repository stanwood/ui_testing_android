package io.stanwood.uitesting.model.slack;

import com.google.gson.annotations.SerializedName;

public class Field {
    public String title;
    public String value;
    @SerializedName("short")
    public boolean isShort;
}
