package io.stanwood.uitesting.model.slack;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Payload {
    public String username;
    @SerializedName("icon_emoji")
    public String emoji;
    public String channel;
    public List<Attachment> attachments;
}
