package com.kelompokberdua.iotipcdev.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Preferences {
    static final String KEY_LOGIN_STATUS = "KEY_LOGIN_STATUS";

    private static SharedPreferences getSharedPreference(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static void setIsLoggedUser(Context context, boolean value) {
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.putBoolean(KEY_LOGIN_STATUS, value);
        editor.apply();
    }

    public static boolean getKeyLoginStatus(Context context) {
        return getSharedPreference(context).getBoolean(KEY_LOGIN_STATUS, false);
    }
}
