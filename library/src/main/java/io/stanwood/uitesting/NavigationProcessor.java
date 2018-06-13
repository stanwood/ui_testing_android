package io.stanwood.uitesting;

import android.support.test.uiautomator.By;
import android.support.test.uiautomator.Direction;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject2;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.Until;
import android.text.TextUtils;

import java.util.List;

import io.stanwood.uitesting.log.Logger;
import io.stanwood.uitesting.log.Report;
import io.stanwood.uitesting.log.ReportDispatcher;
import io.stanwood.uitesting.log.Tracker;
import io.stanwood.uitesting.model.ActionView;
import io.stanwood.uitesting.model.Command;
import io.stanwood.uitesting.model.TestCase;
import io.stanwood.uitesting.model.TestSuite;
import io.stanwood.uitesting.util.Util;

public class NavigationProcessor {

    UiDevice device;
    TestSuite testSuite;
    NavigationParser navigationParser;
    ReportDispatcher reportDispatcher;

    public NavigationProcessor(UiDevice device, TestSuite testSuite, Tracker... trackers) {
        this.device = device;
        this.testSuite = testSuite;
        this.navigationParser = new NavigationParser(testSuite);
        this.reportDispatcher = new ReportDispatcher(trackers);
    }

    /**
     * Find and return a view by it's resource id
     *
     * @param view
     * @param actionView
     * @return
     */
    private UiObject2 findView(UiObject2 view, ActionView actionView) {
        if (!actionView.isParent()) {
            if (view != null) {
                view = view.findObject(By.res(actionView.getResourceId()));
            } else {
                view = device.wait(Until.findObject(By.res(actionView.getResourceId())), testSuite.getViewTimeout());
            }
        } else {
            if (view != null) {
                view = view.findObject(By.res(actionView.getResourceId())).getParent();
                //view = view.findObjects(By.res(actionView.getResourceId())).get(actionView.getIndex());
            } else {
                view = device.wait(Until.findObject(By.res(actionView.getResourceId())), testSuite.getViewTimeout()).getParent();
                //view = device.wait(Until.findObjects(By.res(actionView.getResourceId())), VIEW_TIMEOUT).get(actionView.getIndex());
            }
        }
        return view;
    }

    /**
     * Process the command
     *
     * @param command
     * @throws UiObjectNotFoundException
     */
    public void processCommand(Command command, String title) throws UiObjectNotFoundException {
        List<ActionView> actionViews = command.getActionViews();
        UiObject2 view = null;

        for (int i = 0, size = actionViews.size(); i < size; i++) {

            ActionView actionView = actionViews.get(i);

            //get view by resource
            view = findView(view, actionView);

            //if index is set, get child id by index
            if (actionView.getIndex() > -1) {
                if (view.getChildCount() > actionView.getIndex()) {
                    view = view.getChildren().get(actionView.getIndex());
                } else {
                    throw new IllegalArgumentException(String.format("resourceId %s, child index out of bounds: %d", actionView.getResourceId(), actionView.getIndex()));
                }
            }
        }

        switch (command.getAction()) {
            case TAP:
                view.click();
                break;
            case SWIPELEFT:
                view.swipe(Direction.LEFT, 1.0f);
                break;
            case SWIPERIGHT:
                view.swipe(Direction.RIGHT, 1.0f);
                break;
            case SCROLLDOWN:
                view.scroll(Direction.DOWN, 3.0f);
                break;
            case SCROLLUP:
                view.scroll(Direction.UP, 1.0f);
                break;
            case VISIBLE:
                //already handled by device.wait(Until.findObject
                break;
            case SETTEXT:
                if (!TextUtils.isEmpty(command.getAction().getText())) {
                    view.setText(command.getAction().getText());
                }
                break;
            case ENTER:
                device.pressEnter();
                break;
            case BACK:
                device.pressBack();
                break;
            case SLEEP:
                Util.sleep(1000);
                break;
            case KILL:
                Util.killApps();
                Util.startApp(testSuite.getPackageName(), testSuite.getLaunchTimeout());
                break;
            case SNAPSHOT:
                try {
                    Util.screenshot(device, title);
                } catch (Exception e) {
                    Logger.log(e);
                }
                break;
            case LANGUAGE:
                Util.changeLanguage(device, command.getAction().getText());
                break;
            default:
                throw new IllegalArgumentException("Unknown Action command");
        }

        Util.sleep(1000);
    }

    public void process() {
        List<TestCase> testCases = testSuite.getTestCases();
        Report report = new Report();

        Logger.logDelimiter();
        Logger.log("Package name: " + testSuite.getPackageName());

        Util.startApp(testSuite.getPackageName(), testSuite.getLaunchTimeout());

        int counterSuccess = 0, counterFailure = 0;
        for (int i = 0, size = testCases.size(); i < size; i++) {

            TestCase testCase = testCases.get(i);

            Logger.logDelimiter();
            Logger.log("Starting testcase with title: " + testCase.getTitle());
            Logger.log("Test description: " + testCase.getDescription());
            Logger.logDelimiter();

            for (int j = 0; j < testCase.getNavigation().size(); j++) {

                String navStr = testCase.getNavigation().get(j);

                Logger.log("       command: " + navStr);

                Command command = null;
                try {
                    command = navigationParser.parseCommand(navStr);
                    processCommand(command, testCase.getTitle());
                    if (testSuite.isAutoSnapshot()) {
                        Util.screenshot(device, testCase.getTitle());
                    }
                    command.setPassed(true);
                } catch (Exception e) {
                    addError(command, e);
                    Logger.log(e);
                }

                if (command != null && command.isPassed()) {
                    counterSuccess++;
                } else {
                    counterFailure++;
                }
            }
        }

        report.setFailedTests(counterFailure);
        report.setSucceededTests(counterSuccess);

        Logger.logDelimiter();
        Logger.log(String.format("%d tests succeeded, %d tests failed", counterSuccess, counterFailure));

        reportDispatcher.process(device, testSuite, report);
    }

    private void addError(Command command, Exception e) {
        if (command != null) command.setError(e);
    }

}