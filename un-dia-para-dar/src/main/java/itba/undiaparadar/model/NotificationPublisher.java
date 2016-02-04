package itba.undiaparadar.model;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import itba.undiaparadar.activities.MainActivity;

public class NotificationPublisher extends BroadcastReceiver {

    public static String NOTIFICATION_ID = "notification-id";
    public static String NOTIFICATION = "notification";

    public void onReceive(Context context, Intent intent) {

        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        Notification notification = intent.getParcelableExtra(NOTIFICATION);
        int id = intent.getIntExtra(NOTIFICATION_ID, 0);
        PendingIntent pendingNotificationIntent = PendingIntent.getActivity(
                context, 0, MainActivity.getIntent(context, id),
                PendingIntent.FLAG_UPDATE_CURRENT);
        notification.contentIntent =  pendingNotificationIntent;
        notificationManager.notify(id, notification);

    }
}