package com.ssw.linkedinmanager.common;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceManager {
    private static PreferenceManager preferenceManager;
    private Context context;

    public static PreferenceManager getInstance(Context context) {
        if (preferenceManager == null) {
            preferenceManager = new PreferenceManager();
        }
        preferenceManager.context = context;
        return preferenceManager;
    }

    public boolean putString(String preferenceName, String key, String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        return editor.commit();
    }

    public boolean putInt(String preferenceName, String key, int value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        return editor.commit();
    }

    public boolean putFloat(String preferenceName, String key, float value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat(key, value);
        return editor.commit();
    }

    public String getString(String preferenceName, String key, String defaultValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, defaultValue);
    }

    public int getInt(String preferenceName, String key, int defaultValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(key, defaultValue);
    }

    public float getFloat(String preferenceName, String key, float defaultValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE);
        return sharedPreferences.getFloat(key, defaultValue);
    }

    public boolean removePreferenceKey(String preferenceName, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE);
        return sharedPreferences.edit().remove(key).commit();
    }
}
