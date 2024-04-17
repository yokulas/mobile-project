package com.example.navbotdialog;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

public class NightFragment extends Fragment {

    private int currentThemeMode;

    private static final String PREF_THEME_MODE = "theme_mode";
    private static final int THEME_LIGHT = AppCompatDelegate.MODE_NIGHT_NO;
    private static final int THEME_DARK = AppCompatDelegate.MODE_NIGHT_YES;
    private static final int THEME_SYSTEM_DEFAULT = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM;

    public static void setThemeMode(Context context, int themeMode) {
        SharedPreferences preferences = context.getSharedPreferences("app_theme", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(PREF_THEME_MODE, themeMode);
        editor.apply();
    }

    public static int getThemeMode(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("app_theme", Context.MODE_PRIVATE);
        return preferences.getInt(PREF_THEME_MODE, THEME_SYSTEM_DEFAULT);
    }



    // Method to toggle the theme mode
    public static void toggleThemeMode(Context context) {
        // Retrieve the current theme mode from SharedPreferences
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        int currentThemeMode = preferences.getInt("theme_mode", THEME_LIGHT);

        // Toggle the theme mode
        int newThemeMode = currentThemeMode == THEME_LIGHT ? THEME_DARK : THEME_LIGHT;

        // Save the new theme mode
        preferences.edit().putInt("theme_mode", newThemeMode).apply();

        // Apply the new theme
        applyTheme(context);
    }

    // Method to apply the theme based on the current mode
    public static void applyTheme(Context context) {
        // Retrieve the current theme mode from SharedPreferences
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        int currentThemeMode = preferences.getInt("theme_mode", THEME_LIGHT);

        // Apply the theme based on the current mode
        if (currentThemeMode == THEME_LIGHT) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
    }

}