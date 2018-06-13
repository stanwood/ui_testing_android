package io.stanwood.uitesting.model.slack;

import java.util.Date;
import java.util.List;

public class Attachment {
    public String fallback;
    public String color;
    public String pretext;
    public String title;
    public String text;
    public String footer;
    public String mrkdwn_in;
    public Date ts;
    public List<Field> fields;
}
