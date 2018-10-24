package io.stanwood.uitesting;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.stanwood.uitesting.model.Action;
import io.stanwood.uitesting.model.ActionView;
import io.stanwood.uitesting.model.Command;
import io.stanwood.uitesting.model.TestSuite;

public class NavigationParser {

    TestSuite mTestSuite;

    public NavigationParser(TestSuite testSuite) {
        mTestSuite = testSuite;
    }

    /**
     * Parse navigation command
     *
     * @param s
     * @return
     */
    public Command parseCommand(String s) {
        String[] splited = TextUtils.split(s, "(?<!\\\\\\\\)\\.");

        if (splited.length < 1) throw new IllegalArgumentException();

        for (int i = 0; i < splited.length; i++) {
            String partialCommand = splited[i];
            splited[i] = partialCommand.replaceAll("(?<=\\\\\\\\)\\.", "");
        }

        //get action, always the last item
        Action action = parseAction(splited);

        //parse resource ids
        List<ActionView> actionViews = parseResourceIds(splited);

        return new Command(actionViews, action);
    }

    /**
     * Parse navigation action (should be the last word in a sentence)
     *
     * @param splited
     * @return
     */
    public Action parseAction(String[] splited) {
        String actionCommand = splited[splited.length - 1];
        String actionValue = null;

        Pattern pattern = Pattern.compile("\\['(.*?)'\\]");
        Matcher matcher = pattern.matcher(actionCommand);
        if (matcher.find()) {
            actionCommand = actionCommand.substring(0, matcher.start());
            actionValue = matcher.group(1);
        }

        Pattern pattern1 = Pattern.compile("\\('(.*?)'\\)");
        Matcher matcher1 = pattern1.matcher(actionCommand);
        if (matcher1.find()) {
            actionCommand = actionCommand.substring(0, matcher1.start());
            actionValue = matcher1.group(1);
        }

        Action action = Action.valueOf(actionCommand.toUpperCase());
        action.setText(actionValue);
        return action;
    }

    /**
     * Parse view resource ids nested views are supported, for example: view['container'].view['recyclerView'][3].click
     * If there is no package name present, a default package name is added programatically to the resource id.
     * example:  view['container']   -> view['de.rtv:id/container']
     *
     * @param words
     * @return
     */
    public List<ActionView> parseResourceIds(String[] words) {
        List<ActionView> actionViews = new ArrayList<>();
        for (int i = 0; i < words.length - 1; i++) {

            String viewId = null;
            String viewText = null;
            int viewIndex = -1;
            boolean shouldSeekParent = false;

            //find view['tab_now'] or view['recycler_view'][12] or view['android:id/text1'][parent_0]
            //Pattern pattern = Pattern.compile("\\[(.*?)\\]");
            Pattern pattern = Pattern.compile("view\\['@(.*?)'\\](\\[(\\w+)\\])?");
            Matcher matcher = pattern.matcher(words[i]);
            if (matcher.find()) {

                //get view resource id
                viewId = matcher.group(1);

                //add app package id, if not added yet
                String[] p = TextUtils.split(viewId, "/");
                if (p.length == 1) {
                    viewId = String.format("%s:id/%s", mTestSuite.getPackageName(), viewId);
                }

                //get index, if any
                String s = matcher.group(3);
                if (s != null) {
                    String[] splited = TextUtils.split(s, "\\_");
                    if (splited.length > 1) {
                        shouldSeekParent = true;
                        viewIndex = Integer.parseInt(splited[1]);
                    } else {
                        viewIndex = Integer.parseInt(s);
                    }
                }

            } else {
                //the view wasn't described using the ID, let's see whether it is defined by text

                pattern = Pattern.compile("view\\['(.*?)'\\](\\[(\\w+)\\])?");
                matcher = pattern.matcher(words[i]);

                if (matcher.find()) {
                    //get text
                    viewText = matcher.group(1);

                    //get index, if any
                    String s = matcher.group(3);
                    if (s != null) {
                        String[] splited = TextUtils.split(s, "\\_");
                        if (splited.length > 1) {
                            shouldSeekParent = true;
                            viewIndex = Integer.parseInt(splited[1]);
                        } else {
                            viewIndex = Integer.parseInt(s);
                        }
                    }
                }
            }

            actionViews.add(new ActionView(viewText, viewId, viewIndex, shouldSeekParent));
        }
        return actionViews;
    }
}
