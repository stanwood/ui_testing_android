package io.stanwood.uitesting.model;

public enum Action {
    TAP("tap"),                 //tap/click within selected view
    SWIPELEFT("swipeLeft"),     //swipe left within selected view
    SWIPERIGHT("swipeRight"),   //swipe right within selected view
    SCROLLDOWN("scrollDown"),   //scroll down within selected view
    SCROLLUP("scrollUp"),       //scroll up within selected view
    VISIBLE("visible"),         //checks if view is visible
    SETTEXT("setText"),         //sets the text on EditText
    ENTER("enter"),             //press ENTER
    BACK("back"),               //back pressed
    SLEEP("sleep"),             //sleeps for certain amount of time
    SNAPSHOT("snapshot"),       //takes a snapshot
    KILL("kill"),               //kills the app
    LANGUAGE("language");       //changes the language

    private final String stringValue;
    private String text;

    Action(final String s) {
        stringValue = s;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String toString() {
        return stringValue;
    }
}

