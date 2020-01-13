package com.myproject.aplication;

import android.content.Context;


import androidx.annotation.NonNull;

import net.grandcentrix.tray.TrayPreferences;
import net.grandcentrix.tray.core.SharedPreferencesImport;

public class PreferencesHelper extends TrayPreferences {
    private static final String KEY_FIRST_LAUNCH = "KEY_FIRST_LAUNCH";
    private static final String KEY_FIRST_LAUNCH_TRAY = "KEY_FIRST_LAUNCH_TRAY";

    public PreferencesHelper(@NonNull Context context) {
        super(context, "preferencesHelper", 1);
    }

    protected void onCreate(int initialVersion) {
        super.onCreate(initialVersion);
        SharedPreferencesImport importPref = new SharedPreferencesImport(getContext(), getContext().getPackageName() + "_preferences", KEY_FIRST_LAUNCH, KEY_FIRST_LAUNCH_TRAY);
        migrate(importPref);
    }
}
