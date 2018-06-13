#!/bin/bash

usage()
{
    echo "usage: run_ui_tests -a app.id.to.test -c full.reference.to.a.test.class -b BuildName -e avdName [-h]"
}

TEST_CLASS=
APP_ID=
BUILD_NAME=
AVD_NAME=

while [ "$1" != "" ]; do
    case $1 in
        -a | --app-id )         shift
                                APP_ID=$1
                                ;;
        -c | --test-class )     shift
                                TEST_CLASS=$1
                                ;;
        -b | --build )          shift
                                BUILD_NAME=$1
                                ;;
        -e | --avd-name )       shift
                                AVD_NAME=$1
                                ;;
        -h | --help )           usage
                                exit
                                ;;
        * )                     usage
                                exit 1
    esac
    shift
done

if [ ! -z "$AVD_NAME" ]; then
    #Start the emulator
    emulator -avd $AVD_NAME -wipe-data &
    EMULATOR_PID=$!

    # Wait for Android to finish booting
    WAIT_CMD="adb wait-for-device shell getprop init.svc.bootanim"
    until $WAIT_CMD | grep -m 1 stopped; do
      echo "Waiting..."
      sleep 1
    done

    # Unlock the Lock Screen
    adb shell input keyevent 82

    # Clear and capture logcat
    adb logcat -c
    adb logcat > build/logcat.log &
    LOGCAT_PID=$!
fi

#install the app and testing app
./gradlew app:install$BUILD_NAME app:install"$BUILD_NAME"AndroidTest

#create the folder for snapshots
adb shell rm -r /storage/emulated/0/$APP_ID/screengrab

#add privileges to the app
adb shell pm grant $APP_ID android.permission.WRITE_EXTERNAL_STORAGE
adb shell pm grant $APP_ID android.permission.READ_EXTERNAL_STORAGE
adb shell pm grant $APP_ID.test android.permission.WRITE_EXTERNAL_STORAGE
adb shell pm grant $APP_ID.test android.permission.READ_EXTERNAL_STORAGE

#enter demo mode
adb shell settings put global sysui_demo_allowed 1
adb shell am broadcast -a com.android.systemui.demo -e command clock -e hhmm 1200
adb shell am broadcast -a com.android.systemui.demo -e command network -e mobile show -e level 4 -e datatype false
adb shell am broadcast -a com.android.systemui.demo -e command network -e wifi show -e level 4 -e fully true
adb shell am broadcast -a com.android.systemui.demo -e command notifications -e visible false
adb shell am broadcast -a com.android.systemui.demo -e command battery -e plugged false -e level 100

#run the test
adb shell am instrument -w -r -e class $TEST_CLASS $APP_ID.test/android.support.test.runner.AndroidJUnitRunner

#exit demo mode
adb shell am broadcast -a com.android.systemui.demo -e command exit

#grab the images
mkdir -p "screengrab"
adb pull /storage/emulated/0/$APP_ID/screengrab ./screengrab/$APP_ID

if [ ! -z "$AVD_NAME" ]; then
    # Stop the background processes
    kill $LOGCAT_PID
    kill $EMULATOR_PID
fi