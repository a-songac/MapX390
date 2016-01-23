package soen390.mapx;

import android.content.Context;
import android.widget.Toast;

import soen390.mapx.application.MapXApplication;

/**
 * Class to implement ui utils
 */
public class UiUtils {

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
