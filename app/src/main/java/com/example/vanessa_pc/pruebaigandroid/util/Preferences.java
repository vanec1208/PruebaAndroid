package com.example.vanessa_pc.pruebaigandroid.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

public class Preferences {
    public static final String PREFERENCES_FILE_NAME = "MyPreferences";
    public static final String PREF_EMAIL = "email";
    public static final String PREF_PASSWORD = "password";
    public static final String PREF_TOKEN = "token";

    public static void setPreferenceValue(Context context, String preferenceKey, String preferenceValue) {
        SharedPreferences prefs = context.getSharedPreferences(PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(preferenceKey, encrypt(preferenceValue));
        editor.commit();
    }

    public static String getPreferenceValue(Context context, String preferenceKey) {
        SharedPreferences prefs = context.getSharedPreferences(PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
        String strPrefs = prefs.getString((preferenceKey), null);
        return strPrefs == null || strPrefs.equals("") ? null : decrypt(strPrefs);
    }

    public static String encrypt(String string) {
        return Base64.encodeToString(string.getBytes(), Base64.DEFAULT);
    }

    public static String decrypt(String string) {
        return new String(Base64.decode(string, Base64.DEFAULT));
    }
}
