package io.stanwood.uitesting.model;

public class ActionView {

    private String resourceId;
    private int index;
    private boolean parent;

    public ActionView(String resourceId, int index, boolean parent) {
        this.resourceId = resourceId;
        this.index = index;
        this.parent = parent;
    }

    public boolean isParent() {
        return parent;
    }

    public void setParent(boolean parent) {
        this.parent = parent;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
