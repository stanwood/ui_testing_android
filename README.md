[![Release](https://jitpack.io/v/stanwood/ui_testing_android.svg?style=flat-square)](https://jitpack.io/#stanwood/ui_testing_android)

# Stanwood UI testing framework

This library contains helper classes for UI testing, making screenshots etc.

# Table of contents

- [Import](#import)
- [Add test class](#add-test-class)
- [Resource ids](#resource-ids)
- [Supported actions](#supported-actions)
- [Run tests](#run-tests)
- [Roadmap](#roadmap)


## Import

The Stanwood UI testing framework is hosted on JitPack. Therefore you can simply import it by adding the following text to your apps's `build.gradle`.

```groovy
allprojects {
    repositories {
        ...
        maven { url "https://jitpack.io" }
    }
}
```

Then add this to you app's `build.gradle`:

```groovy
dependencies {
    androidTestImplementation "com.github.stanwood:ui_testing_android:<add latest version here>"
    androidTestImplementation 'com.android.support.test.espresso:espresso-core:3.0.2'
    androidTestImplementation 'com.android.support.test:runner:1.0.2'
    androidTestImplementation 'com.android.support.test:rules:1.0.2'
}
```

## Add test class

A simple test class SUITest needs to be added to the following folder
![TestClassFolder](/images/img_test_path.png)

```java
@RunWith(AndroidJUnit4.class)
@SdkSuppress(minSdkVersion = 18)
public class SUITest extends BaseTest {

    private static final String JSON_SCHEMA_FILE = "res/raw/test_schema.json";

    @Before
    public void start() {
        SlackTracker slackTracker = new SlackTracker(
                "<add slack channel name here>",
                "<add slack team id here>",
                "<add slack token here>");
        //FirebaseSchemaProvider schemaProvider = new FirebaseSchemaProvider("io.stanwood.uitesting.demo", "1.0.0");
        ResourceSchemaProvider schemaProvider = new ResourceSchemaProvider(this, JSON_SCHEMA_FILE, BuildConfig.APPLICATION_ID);
        super.initTests(schemaProvider, slackTracker);
    }

    // DO NOT leave this method out: it just does the super call, but adds the @Test annotation!
    @Test
    public void runTests() {
        super.runTests();
    }

}
```

Same class in Kotlin:

```
const val JSON_SCHEMA_FILE = "res/raw/test_schema.json"

@RunWith(AndroidJUnit4::class)
@SdkSuppress(minSdkVersion = 18)
class SUITest : BaseTest() {

    @Before
    fun start() {
        ResourceSchemaProvider(this, JSON_SCHEMA_FILE, BuildConfig.APPLICATION_ID).let {
            super.initTests(it)
        }
    }

    @Test
    public override fun runTests() {
        super.runTests()
    }
}
```

## Resource ids

1. Start the app within emulator or on a device(needs to be connected to the computer)
2. Start uiautomatorviewer (usually at /Users/username/Library/Android/sdk/tools/bin)
./uiautomatorviewer
3. Pick the desired device (click at an upper 'phone' icon)
![UiAutomatorViewer](/images/img_uiautomatorviewer_1.png)
4. UIAutomatorViewer has made a screenshot of current screen. Now you can explore all the visible views and get their resource ids, which are needed in order to be able to look for them within tests
![UiAutomatorViewer](/images/img_uiautomatorviewer_2.png)
On the left side you can see a screenshot of the current view.
On the right side you see all the views used within current activity.
In this particular case the resource id of the selected view is: countryTitle
This id is being used within test if we want to do some action on that view, or maybe just test if the view is being displayed
There might be the case that a view does not have resource id defined yet, especially when/if databinding is being used. In that case the developer either needs to define a resource id for that view in the XML layout or you search for the View by the text on it.

## Supported actions

[Actions](/library/src/main/java/io/stanwood/uitesting/model/Action.java)

#### Json example
```json
  {
    "initial_sleep_time": 2000,
    "command_sleep_time": 1000,
    "launch_timeout": 5000,
    "view_timeout": 10000,
    "auto_snapshot": false,
    "test_cases": [
      {
        "id": "1",
        "title": "Simple button click test",
        "description": "Simple button click test",
        "navigation": [
          "view['@button'].click", // click on button defined by resource ID
          "sleep",
          "snapshot",
          "view['text'].setText['12345']", // set text on text field defined by its text
          "sleep",
          "snapshot"
        ]
      }
    ]
  }
```

For quick tests the json file can be stored within an app (under /res/raw/test_schema.json).
But in most cases the test schema file should be stored as a resource which can be grabbed via http request (https://console.firebase.google.com/u/0/project/stanwood-ui-testing/database/stanwood-ui-testing/data)

## Run tests

There are two ways how to run tests

#### Within the Android Studio IDE

Select SUITest class in the left Navigator view, right click it, and select "Run/Debug SUITest"
#### With a helper script
[Script](run_ui_tests.sh)

```./run_ui_tests -a app.id.to.test -c full.reference.to.a.test.class -b BuildName [-e avd_name]```

**-a**, **--app-id** - app id<br/>
**-c**, **--test-class** - full reference to the class that runs tests<br/>
**-b**, **--build** - build/flavour name (Release, Debug, FlavorDebug, Qa, etc.)<br/>
**-e**, **--avd-name** - *optional* - avd image name that will be started where test will be executed<br/>


For more information check out the sample app.

## Roadmap
