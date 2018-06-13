package io.stanwood.uitesting.model;

import java.util.List;

public class Command {

    private List<ActionView> actionViews;
    private Action action;
    private boolean passed;
    private Exception error;

    public Command(List<ActionView> actionViews, Action action) {
        this.actionViews = actionViews;
        this.action = action;
        passed = false;
    }

    public List<ActionView> getActionViews() {
        return actionViews;
    }

    public void setActionViews(List<ActionView> actionViews) {
        this.actionViews = actionViews;
    }

    public Exception getError() {
        return error;
    }

    public void setError(Exception error) {
        this.error = error;
    }

    public boolean isPassed() {
        return passed;
    }

    public void setPassed(boolean passed) {
        this.passed = passed;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

}
