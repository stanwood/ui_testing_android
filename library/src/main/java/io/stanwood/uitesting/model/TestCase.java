package io.stanwood.uitesting.model;

import java.util.List;

public class TestCase {

    private String id;
    private String title;
    private String description;
    private List<String> navigation;
    private boolean isPassed;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getNavigation() {
        return navigation;
    }

    public void setNavigation(List<String> navigation) {
        this.navigation = navigation;
    }

    public boolean isPassed() {
        return isPassed;
    }

    public void setPassed(boolean passed) {
        isPassed = passed;
    }
}
