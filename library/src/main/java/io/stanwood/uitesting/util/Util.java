package io.stanwood.uitesting.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Environment;
import android.os.RemoteException;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.test.InstrumentationRegistry;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;
import android.support.test.uiautomator.Until;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

public class Util {

    private static final String SCREENSHOTS_FOLDER = "screengrab";

    /**
     * Uses package manager to find the package name of the device launcher. Usually this package
     * is "com.android.launcher" but can be different at times. This is a generic solution which
     * works on all platforms.`
     */
    public static String getLauncherPackageName() {
        // Create launcher Intent
        final Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);

        // Use PackageManager to get the launcher package name
        PackageManager pm = InstrumentationRegistry.getContext().getPackageManager();
        ResolveInfo resolveInfo = pm.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return resolveInfo.activityInfo.packageName;
    }

    /**
     * Start the app
     * @param packageName
     * @param launchTimeout
     */
    public static void startApp(String packageName, long launchTimeout) {
        UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        device.pressHome();
        // Wait for launcher
        final String launcherPackage = Util.getLauncherPackageName();
        assertThat(launcherPackage, notNullValue());
        device.wait(Until.hasObject(By.pkg(launcherPackage).depth(0)), launchTimeout);
        Context context = InstrumentationRegistry.getContext();
        final Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);    // Clear out any previous instances
        context.startActivity(intent);
        // Wait for the app to appear
        device.wait(Until.hasObject(By.pkg(packageName).depth(0)), launchTimeout);
    }

    /**
     * Sleep the app for a certain amount of time
     */
    public static void sleep(long milisecs) {
        SystemClock.sleep(milisecs);
    }

    /**
     * Kill all the running apps
     */
    public static void killApps() {
        UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        try {
            device.pressHome();
            device.pressRecentApps();

            // Clear all isn't always visible unless you scroll all apps down
            int height = device.getDisplayHeight();
            int width = device.getDisplayWidth();
            device.swipe(width / 2, height / 2, width / 2, height, 50);

            UiObject clear = device.findObject(new UiSelector()
                    .resourceId("com.android.systemui:id/button")
                    .text("CLEAR ALL")
            );
            if (clear.exists()) {
                clear.click();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Switch the language
     *
     * @param language
     */
    public static void changeLanguage(UiDevice device, String language) {
        Locale locale = new Locale(language);
        Configuration config = new Configuration();
        Locale.setDefault(locale);
        config.locale = locale;
        Resources resources = InstrumentationRegistry.getTargetContext().getApplicationContext().getResources();
        resources.updateConfiguration(config, resources.getDisplayMetrics());
    }

    /**
     * Take a screenshot
     *
     * @param device
     * @param screenshotName
     * @throws Exception
     */
    public static void screenshot(UiDevice device, @NonNull String screenshotName) throws Exception {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext().getApplicationContext();
        File e = getFilesDirectory(context, Locale.getDefault());
        String screenshotFileName = String.format("%s%d%s", screenshotName.replace(" ", "_"), System.currentTimeMillis(), ".png");
        File screenshotFile = new File(e, screenshotFileName);
        device.takeScreenshot(screenshotFile);
        //UiDevice.getInstance(InstrumentationRegistry.getInstrumentation()).takeScreenshot(screenshotFile);
    }

    private static File getFilesDirectory(Context context, Locale locale) throws IOException {
        File directory = null;
        File internalDir;
        if (Build.VERSION.SDK_INT >= 21) {
            internalDir = new File(Environment.getExternalStorageDirectory(), getDirectoryName(context, locale));
            directory = initializeDirectory(internalDir);
        }
        if (directory == null && Build.VERSION.SDK_INT < 24) {
            internalDir = new File(context.getDir(SCREENSHOTS_FOLDER, Context.MODE_WORLD_READABLE), localeToDirName(locale));
            directory = initializeDirectory(internalDir);
        }
        if (directory == null) {
            throw new IOException("Unable to get a screenshot storage directory");
        } else {
            Log.d("Screengrab", "Using screenshot storage directory: " + directory.getAbsolutePath());
            return directory;
        }
    }

    private static File initializeDirectory(File dir) {
        try {
            createPathTo(dir);
            if (dir.isDirectory() && dir.canWrite()) {
                return dir;
            }
        } catch (IOException var2) {
            Log.e("Screengrab", "Failed to initialize directory: " + dir.getAbsolutePath(), var2);
        }

        return null;
    }

    private static String getDirectoryName(Context context, Locale locale) {
        return context.getPackageName() + "/" + SCREENSHOTS_FOLDER + "/" + localeToDirName(locale);
    }

    private static String localeToDirName(Locale locale) {
        return locale.getLanguage() + "-" + locale.getCountry();
    }

    private static void createPathTo(File dir) throws IOException {
        File parent = dir.getParentFile();
        if (!parent.exists()) {
            createPathTo(parent);
        }

        if (!dir.exists() && !dir.mkdirs()) {
            throw new IOException("Unable to create output dir: " + dir.getAbsolutePath());
        } else {
            Chmod.chmodPlusRWX(dir);
        }
    }
}
