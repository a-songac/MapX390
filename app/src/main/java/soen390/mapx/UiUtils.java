package soen390.mapx;

import android.content.Context;
import android.widget.Toast;

import soen390.mapx.application.MapXApplication;

/**
 * Class to implement ui utils
 */
public class UiUtils {

    private static int rootViewHeight = 0;
    private static int rootViewWidth = 0;

    public static int getRootViewHeight() {
        return rootViewHeight;
    }

    public static void setRootViewHeight(int rootViewHeight) {
        UiUtils.rootViewHeight = rootViewHeight;
    }

    public static int getRootViewWidth() {
        return rootViewWidth;
    }

    public static void setRootViewWidth(int rootViewWidth) {
        UiUtils.rootViewWidth = rootViewWidth;
    }

    /**
     * Display toast message
     * @param msg : message to display
     */
    public static void displayToast(String msg) {

        Context context = MapXApplication.getGlobalContext();
        Toast.makeText(context, msg,Toast.LENGTH_SHORT).show();
    }

    /**
     * Display long toast message
     * @param msg : message to display
     */
    public static void displayToastLong(String msg) {

        Context context = MapXApplication.getGlobalContext();
        Toast.makeText(context, msg,Toast.LENGTH_LONG).show();
    }
}
