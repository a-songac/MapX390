package com.arnaud.android.core.application;

import android.app.Application;
import android.content.Context;

import com.arnaud.android.core.utils.LogUtils;

/**
 * Base Application
 */
public class BaseApplication extends Application {

    /**
     * Application global context
     */
    private static Context GLOBAL_CONTEXT;

    /**
     * Whether main activity is visible
     */
    private static boolean mainActivityVisible;

    @Override
    public void onCreate() {
        super.onCreate();
        GLOBAL_CONTEXT = getApplicationContext();
        LogUtils.debug(this.getClass(), "onCreate", "Global context: " + GLOBAL_CONTEXT);

    }

    public static Context getGlobalContext(){
        return GLOBAL_CONTEXT;
    }

    public static void setGlobalContext(final Context activityContext) {
        GLOBAL_CONTEXT = activityContext;
    }

    public static void mainActivityPaused() { mainActivityVisible = false;}
    public static void mainActivityResumed() {mainActivityVisible = true;}
    public static boolean isMainActivityVisible() {return  mainActivityVisible;}
}
