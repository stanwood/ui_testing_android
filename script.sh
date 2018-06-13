
./gradlew app:installDebug app:installDebugAndroidTest

adb shell pm grant io.stanwood.uitesting.demo android.permission.WRITE_EXTERNAL_STORAGE
adb shell pm grant io.stanwood.uitesting.demo android.permission.READ_EXTERNAL_STORAGE
adb shell pm grant io.stanwood.uitesting.demo.test android.permission.WRITE_EXTERNAL_STORAGE
adb shell pm grant io.stanwood.uitesting.demo.test android.permission.READ_EXTERNAL_STORAGE

adb shell am instrument -w io.stanwood.uitesting.demo.test/android.support.test.runner.AndroidJUnitRunner
