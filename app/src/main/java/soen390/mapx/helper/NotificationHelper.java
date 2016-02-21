package soen390.mapx.helper;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.app.NotificationManagerCompat;

import soen390.mapx.R;
import soen390.mapx.activity.MainActivity;
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

    public void showPOIReachedNotification(){

        CharSequence tickerText = "Point of Interest";
        CharSequence contentTitle = "Point of Interest";
        CharSequence contentText = "You have reached the point of Interest POI_1!";
        long[] vibratePattern = { 0, 200, 200, 300 };


        Context context = MapXApplication.getGlobalContext();
        Intent notificationIntent = new Intent(context, MainActivity.class);
        notificationIntent.addFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent notificationPendingIntent = PendingIntent.getActivity(
                context,
                1,
                notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Bitmap b = BitmapFactory.decodeResource(context.getResources(), R.drawable.moeb_logo_blue);

        showNotification(
                context,
                tickerText,
                contentTitle,
                contentText,
                b,
                R.drawable.ic_place_black_24dp,
                notificationPendingIntent,
                1,
                null,
                vibratePattern);



    }

    /**
     *
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
                                  CharSequence message,Bitmap largeIcon, int smallIcon, PendingIntent pendingIntent, int id,
                                  Uri sound, long[] vibrationPattern) {

        Notification.Builder notificationBuilder = new Notification.Builder(
                context)
                .setTicker(tickerText)
                .setSmallIcon(smallIcon)
                .setLargeIcon(largeIcon)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setContentText(message)
                .setContentIntent(pendingIntent)
                .setVibrate(vibrationPattern);

        if (null != sound){
            notificationBuilder.setSound(sound);
        }


        getNotificationManager().notify(
                id,
                notificationBuilder.build());
    }

}
