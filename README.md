# Apache UIMA Fit v3 with OSGi support
============

Proper OSGi version of the Apache UIMA Fit.

## Build

We are using Gradle as a build system.
To invoke the full build:

    ./gradlew clean build -x test

## Run Tests in Debugger

    ./gradlew clean test --debug-jvm --info --stacktrace

## Run Specific Test in Debugger

    ./gradlew -Dtest.single=Logging clean test --debug-jvm --info --stacktrace
    gradle -Dtest.single=Logging* test

## Run OSGi Tests

    ./gradlew resolve
    ./gradlew testOSGi

## Code convention

1.  Bundle projects should be located in 2-nd directory level (see settings.gradle);
2.  Bundle projects should have `bnd.bnd` (can be empty) file to indicate that it is a bundle (see settings.gradle);
3.  Bundle projects export packages that named like project itself, e.g. project `ru.agentlab.maia` export all packages which have name starting with `ru.agentlab.maia`;
4.  File `bnd.bnd` can be used to customize bundle headers, all instructions takes priority over the defaults;
5.  Test projects should be a Fragment bundles of the bundle under testing;
6.  Bundle projects should locate java sources in the `src` folder;
7.  Tests projects are deliverables too, so located in the `src` folder;
