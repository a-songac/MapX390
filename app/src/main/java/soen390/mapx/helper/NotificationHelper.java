package soen390.mapx.helper;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import soen390.mapx.BitmapUtils;
import soen390.mapx.R;
import soen390.mapx.activity.MainActivity;
import soen390.mapx.application.MapXApplication;
import soen390.mapx.model.Node;

/**
 * Class to implement notification helper
 */
public class NotificationHelper {

    private static final int NOTIFICATION_LARGE_ICON_HEIGHT = 64;
    private static final int NOTIFICATION_LARGE_ICON_WIDTH = 64;

    private static NotificationHelper instance;

    static {
        instance = new NotificationHelper();
    }

    private NotificationHelper(){}

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

    /**
     * Reached a point of interest
     * @param poi
     */
    public void showPOIReachedNotification(Node poi){

        Context context = MapXApplication.getGlobalContext();

        CharSequence tickerText = context.getString(R.string.notification_poi_reached_title);
        CharSequence contentTitle = context.getString(R.string.notification_poi_reached_title);
        long[] vibratePattern = { 0, 200, 200, 300 };


        Intent notificationIntent = new Intent(context, MainActivity.class);
        notificationIntent.putExtra(ConstantsHelper.INTENT_POI_REACHED_EXTRA_KEY, true);
        notificationIntent.addFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent notificationPendingIntent = PendingIntent.getActivity(
                context,
                ConstantsHelper.PENDING_INTENT_NOTIFICATION_POI_REACHED,
                notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Bitmap b = null;
        if (!MapXApplication.isVirtualDevice()) { // To avoid OutOfMemoryError on virtual devices
            b = BitmapUtils.decodeSampledBitmapFromResource(
                    context.getResources(),
                    R.drawable.moeb_logo,
                    BitmapUtils.dpToPx(NOTIFICATION_LARGE_ICON_WIDTH),
                    BitmapUtils.dpToPx(NOTIFICATION_LARGE_ICON_HEIGHT));
        }

        showNotification(
                context,
                tickerText,
                contentTitle,
                poi.getTitle(),
                b,
                R.drawable.ic_place_black_24dp,
                notificationPendingIntent,
                1,
                null,
                vibratePattern,
                context.getString(R.string.notification_poi_reached_floor, poi.getFloorId()));



    }

    /**
     * Show notification
     * Show a notification
     * @param context
     * @param tickerText
     * @param title
     * @param message
     * @param largeIcon
     * @param smallIcon
     * @param pendingIntent
     * @param id
     * @param sound
     * @param vibrationPattern
     */
    private void showNotification(Context context, CharSequence tickerText, CharSequence title,
                                  CharSequence message,@Nullable Bitmap largeIcon, int smallIcon,
                                  PendingIntent pendingIntent, int id,
                                  Uri sound, long[] vibrationPattern, String... otherLines) {

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(
                context)
                .setTicker(tickerText)
                .setSmallIcon(smallIcon)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setContentText(message)
                .setContentIntent(pendingIntent)
                .setVibrate(vibrationPattern)
                .setPriority(Notification.PRIORITY_MAX);

        if (null != largeIcon){
            notificationBuilder.setLargeIcon(largeIcon);
        }

        if (otherLines.length > 0) {

            NotificationCompat.InboxStyle inboxStyle =
                    new NotificationCompat.InboxStyle();
            inboxStyle.setBigContentTitle(title);

            inboxStyle.addLine(message);
            for (String line : otherLines) {
                inboxStyle.addLine(line);
            }
            notificationBuilder.setStyle(inboxStyle);

        }

        if (null != sound){
            notificationBuilder.setSound(sound);
        }


        getNotificationManager().notify(
                id,
                notificationBuilder.build());
    }

}
