#!/bin/sh

DEFAULT_TR='android.test.InstrumentationTestRunner'
CUSTOM_TR='com.zutubi.android.junitreport.JUnitReportTestRunner'

DEFAULT_AM='AndroidManifest.xml'
CUSTOM_AM='TestAndroidManifest.xml'

sed "s/$DEFAULT_TR/$CUSTOM_TR/" $DEFAULT_AM > $CUSTOM_AM
