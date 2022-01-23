package com.example.finalprojectexpensemanager;

import android.content.Context;
import android.content.SharedPreferences;

public class MSPV3 {

    private final String SP_FILE = "SP_FILE";

    private static MSPV3 me;
    private SharedPreferences sharedPreferences;

    public static MSPV3 getMe() {
        return me;
    }

    private MSPV3(Context context) {
        sharedPreferences = context.getApplicationContext().getSharedPreferences(SP_FILE, Context.MODE_PRIVATE);
    }

    public static MSPV3 initHelper(Context context) {
        if (me == null) {
            me = new MSPV3(context);
        }
        return me;
    }

    public void putDouble(String KEY, double defValue) {
        putString(KEY, String.valueOf(defValue));
    }

    public double getDouble(String KEY, double defValue) {
        return Double.parseDouble(getString(KEY, String.valueOf(defValue)));
    }

    public int getInt(String KEY, int defValue) {
        return sharedPreferences.getInt(KEY, defValue);
    }

    public void putInt(String KEY, int value) {
        sharedPreferences.edit().putInt(KEY, value).apply();
    }

    public String getString(String KEY, String defValue) {
        return sharedPreferences.getString(KEY, defValue);
    }

    public void putString(String KEY, String value) {
        sharedPreferences.edit().putString(KEY, value).apply();
    }
}
