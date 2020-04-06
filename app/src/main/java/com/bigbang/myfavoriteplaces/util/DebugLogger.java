package com.bigbang.myfavoriteplaces.util;

import android.util.Log;

import static com.bigbang.myfavoriteplaces.util.Constants.*;

public class DebugLogger {

    public static void logError (Throwable throwable){
        Log.d(TAG, ERROR_PREFIX + throwable.getLocalizedMessage());
    }

    public static void logDebug(String message){
        Log.d(TAG, message);
    }

}
