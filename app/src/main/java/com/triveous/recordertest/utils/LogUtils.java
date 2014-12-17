package com.triveous.recordertest.utils;

import android.util.Log;

public class LogUtils {

    private static final String TAG = "inappbilling";

    public static void printLogD(String message) {
        Log.d(TAG, message);
    }

    public static void printLogE(String message) {
        Log.e(TAG, message);
    }

    public static void printLogW(String message) {
        Log.w(TAG, message);
    }

    public static void printLogI(String message) {
        Log.i(TAG, message);
    }

    public static void printLogV(String message) {
        Log.v(TAG, message);
    }

}
