package com.arnaud.android.core.utils;

import android.util.Log;

/**
 * Class to implement logging utilities
 */
public class LogUtils {

    private static String LOG_TAG = "ARNO_LOG_TAG";

    /**
     * Log a verbose message
     * @param classType : type of class
     * @param methodName : name of method
     * @param logMessage : log message
     */
    public static void verbose(final Class<?> classType, final String methodName, final String logMessage) {

        Log.v(getTag(classType, methodName), logMessage);
    }

    /**
     * Log an information message
     * @param classType : type of class
     * @param methodName : name of method
     * @param logMessage : log message
     */
    public static void info(final Class<?> classType, final String methodName, final String logMessage) {

        Log.i(getTag(classType, methodName), logMessage);
    }

    /**
     * Log a debug message
     * @param classType : type of class
     * @param methodName : name of method
     * @param logMessage : log message
     */
    public static void debug(final Class<?> classType, final String methodName, final String logMessage) {

        Log.d(getTag(classType, methodName), logMessage);
    }

    /**
     * Log a warning message
     * @param classType : type of class
     * @param methodName : name of method
     * @param logMessage : log message
     */
    public static void warn(final Class<?> classType, final String methodName, final String logMessage) {

        Log.w(getTag(classType, methodName), logMessage);
    }

    /**
     * Log an error message
     * @param classType : type of class
     * @param methodName : name of method
     * @param logMessage : log message
     */
    public static void error(final Class<?> classType, final String methodName, final String logMessage) {

        Log.e(getTag(classType, methodName), logMessage);
    }

    /**
     * Log a failure message
     * @param classType : type of class
     * @param methodName : name of method
     * @param logMessage : log message
     */
    public static void failure(final Class<?> classType, final String methodName, final String logMessage) {

        Log.wtf(getTag(classType, methodName), logMessage);
    }

    /**
     * Get the log tag
     * @param classType : type of class
     * @param methodName : name of method
     * @return String : the log tag
     */
    private static String getTag(final Class<?> classType, final String methodName) {

        StringBuilder builder = new StringBuilder();
        builder.append(LOG_TAG);
        String typeName = classType.getSimpleName();
        builder.append("<").append(typeName);
        builder.append("::").append(methodName).append(">");
        return builder.toString();
    }

    /**
     * Get the Log Tag
     * @return String : the log tag value
     */
    public static String getLogTag() {

        return LogUtils.LOG_TAG;
    }

    /**
     * Set the Log Tag
     * @param logTag : new value of Log Tag
     */
    public static void setLogTag(final String logTag) {

        LogUtils.LOG_TAG = logTag;
    }

}
