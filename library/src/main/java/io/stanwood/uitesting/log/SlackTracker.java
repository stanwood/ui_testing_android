package io.stanwood.uitesting.log;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.test.uiautomator.UiDevice;
import android.text.TextUtils;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import io.stanwood.uitesting.model.TestSuite;
import io.stanwood.uitesting.model.slack.Attachment;
import io.stanwood.uitesting.model.slack.Field;
import io.stanwood.uitesting.model.slack.Payload;

public class SlackTracker implements Tracker {

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static String baseURL = "https://hooks.slack.com/services/%s/%s";
    private static String failureMessage = "Your tests have *failed!*";
    private static String successMessage = "Your tests have *passed*, well done!";
    //private TestSuite testSuite;
    //private UiDevice device;
    private String teamId;
    private String token;
    private String channelName;

    public SlackTracker(@Nullable String channelName, @NonNull String teamId, @NonNull String token) {
        this.channelName = channelName;
        this.teamId = teamId;
        this.token = token;
    }


    public String generateReport(UiDevice device, TestSuite testSuite, Report report) throws Exception {
        Payload payload = new Payload();
        payload.username = "Bobby Testing";
        payload.emoji = true ? ":fast-parrot:" : ":cop:";
        if (!TextUtils.isEmpty(channelName)) payload.channel = channelName;
        payload.attachments = buildAttachments(device, testSuite, report);

        String json = new Gson().toJson(payload);
        RequestBody requestBody = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(String.format(baseURL, teamId, token))
                .post(requestBody)
                .build();
        OkHttpClient client = new OkHttpClient();
        Response response = client.newCall(request).execute();
        String s = response.body().string();
        if (!response.isSuccessful()) throw new IOException(response.toString());
        return s;
    }

    private List<Field> buildFields(UiDevice device, TestSuite testSuite, Report report) {
        List<Field> fields = new ArrayList<>();
        Field field = new Field();
        field.title = "Priority";
        field.value = report.succeeded() ? "Low" : "High";
        field.isShort = true;
        fields.add(field);

        field = new Field();
        field.title = "Bundle ID";
        field.value = testSuite.getPackageName();
        field.isShort = true;
        fields.add(field);

        field = new Field();
        field.title = "Device";
        field.value = device.getProductName();
        field.isShort = true;
        fields.add(field);

        field = new Field();
        field.title = "OS";
        field.value = "Android";
        field.isShort = true;
        fields.add(field);

        return fields;
    }

    private List<Attachment> buildAttachments(UiDevice device, TestSuite testSuite, Report report) {
        Attachment attachment = new Attachment();
        attachment.fallback = "UI Testing report";
        attachment.color = report.succeeded() ? "good" : "danger";
        attachment.pretext = report.succeeded() ? successMessage : failureMessage;
        attachment.title = "Report";
        attachment.footer = "Stanwood UI Testing API";
        attachment.mrkdwn_in = "['text', 'pretext']";
        attachment.text = "testing text";
        attachment.fields = buildFields(device, testSuite, report);

        List<Attachment> attachments = new ArrayList<>();
        attachments.add(attachment);
        return attachments;
    }

}