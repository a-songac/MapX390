package soen390.mapx.helper;

import android.content.Context;
import android.support.v4.app.NotificationManagerCompat;

import soen390.mapx.application.MapXApplication;

/**
 * Class to implement notification helper
 */
public class NotificationHelper {

    private static NotificationHelper instance;

    static {
        instance = new NotificationHelper();
    }

    /**
     * Get singleton instance
     * @return NotificationHelper : singleton instance
     */
    public static NotificationHelper getInstance() {return instance;}


    /**
     * Get notification manager
     * @return NotificationManagerCompat : notification manager
     */
    private NotificationManagerCompat getNotificationManager() {

        Context context = MapXApplication.getGlobalContext();

        return NotificationManagerCompat.from(context);
    }

}
