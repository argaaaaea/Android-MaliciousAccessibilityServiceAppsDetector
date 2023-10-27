package com.theowni.remotecontroldetector;

import android.os.Bundle;
import android.util.Log;
import android.widget.Switch;

import com.theowni.remotecontroldetector.utils.RemoteDetector;

import java.io.InputStream;
import java.util.Set;
import com.google.common.collect.Sets;

public class MainActivity extends BaseSecureActivity {
    String logTag = "MainActivity";

    Boolean debugMode = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        runChecks();
    }

    void runChecks() {
        InputStream configFileStream = getResources().openRawResource(R.raw.appconfigs);

        RemoteDetector remoteDetector = null;
        remoteDetector = new RemoteDetector(configFileStream, getApplicationContext());

        Log.d(logTag, "Lists applications found in blocked lists - configurable");
        Set<String> suspiciousAppsInstalled = remoteDetector.getSuspiciousApplicationsInstalled();
        Log.d(logTag, "Lists suspicious AccessibilityServices installed - configurable");
        Set<String> appsWithSuspiciousASvcsInstalled = remoteDetector.getSuspiciousAccessibilityServicesInstalled();
        Log.d(logTag, "Lists suspicious AccessibilityServices enabled - configurable");
        Set<String> appsWithSuspiciousASvcsEnabled = remoteDetector.getSuspiciousAccessibilityServicesEnabled();
        Log.d(logTag, "Lists applications that currently use suspicious ports - configurable");
        Set<String> appsWithSuspiciousPortsInUse = remoteDetector.getAppsWithSuspiciousPortsInUse();
        Log.d(logTag, "Lists applications that use suspicious permissions - based on hardcoded checks");
        Set<String> appsWithSuspiciousASvcsSettings = remoteDetector.getAccessibilityServicesWithSuspiciousSettingsInstalled();
        Log.d(logTag, "Lists applications that were installed in 15 minutes offset to suspicious app installation time - based on hardcoded checks");
        Set<String> appsWithCorrelatedInstallTimesWithSuspiciousApps = remoteDetector.getAppsWithCorrelatedInstallTimesWithSuspiciousApps();
        Log.d(logTag, "Lists applications that were installed in last 15 minutes - based on hardcoded checks");
        Set<String> appsInstalledInLastQuarter = remoteDetector.getAvailabilityServicesInstalledInLastQuarter();
        Log.d(logTag, "Lists AccessibilityServices permitted to overlay");
        Set<String> accessibilityServicesPermittedToOverlay = remoteDetector.getAccessibilityServicesPermittedToOverlay();

        Log.d(logTag, appsWithSuspiciousPortsInUse.toString());
        if (Sets.intersection(
                appsWithSuspiciousASvcsEnabled,
                appsWithSuspiciousPortsInUse)
                .size() > 0) {
            Log.d(logTag, "Suspicious AccessibilityService enabled and network ports in use");
            ((Switch) findViewById(R.id.switch2)).setChecked(true);
        }

        if (Sets.intersection(
                Sets.intersection(appsWithSuspiciousASvcsEnabled, appsInstalledInLastQuarter),
                appsWithCorrelatedInstallTimesWithSuspiciousApps).size() > 0) {
            Log.d(logTag, "Recently installed and enabled suspicious AccessibilityService");
            ((Switch) findViewById(R.id.switch3)).setChecked(true);
        }

        if (Sets.intersection(
                Sets.intersection(remoteDetector.getAccessibilityServiceIDsEnabled(), appsWithSuspiciousASvcsSettings),
                accessibilityServicesPermittedToOverlay)
                .size() > 0) {
            Log.d(logTag, "Suspicious AccessibilityService enabled and can draw over apps");
            ((Switch) findViewById(R.id.switch31)).setChecked(true);
        }

        if (appsWithSuspiciousASvcsEnabled.size() > 0) {
            Log.d(logTag, "Suspicious AccessibilityService enabled");
            ((Switch) findViewById(R.id.switch4)).setChecked(true);
            if (!debugMode) {
                throw new RuntimeException("This application crashed");
            }
        }

        if (Sets.intersection(
                remoteDetector.getAccessibilityServiceIDsEnabled(),
                appsWithSuspiciousASvcsSettings)
                .size() > 0) {
            Log.d(logTag, "AccessibilityService with suspicious capabilities enabled");
            ((Switch) findViewById(R.id.switch5)).setChecked(true);
            if (!debugMode) {
                throw new RuntimeException("This application crashed");
            }
        }

        if (Sets.intersection(
                appsWithSuspiciousASvcsInstalled,
                appsWithSuspiciousASvcsSettings)
                .size() != 0) {
            Log.d(logTag, "AccessibilityService with suspicious capabilities installed");
            ((Switch) findViewById(R.id.switch6)).setChecked(true);
            if (!debugMode) {
                throw new RuntimeException("This application crashed");
            }
        }
    }
}