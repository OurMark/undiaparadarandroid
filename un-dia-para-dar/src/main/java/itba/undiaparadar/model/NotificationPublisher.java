package itba.undiaparadar.model;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import itba.undiaparadar.activities.PositiveActionDetail;

public class NotificationPublisher extends BroadcastReceiver {

    public static String NOTIFICATION_ID = "notification-id";
    public static String NOTIFICATION = "notification";
    private static final String POSITIVE_ACTION_ID = "POSITIVE_ACTION_ID";
    private static final String TOPIC_IMG_RES = "TOPIC_IMG_RES";
    private static final String OBJECT_ID = "OBJECT_ID";

    public void onReceive(Context context, Intent intent) {

        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        Notification notification = intent.getParcelableExtra(NOTIFICATION);
        int topicImgRes = -1;
        long positiveActionId = 0;
        String objectId = "";
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            Bundle bundle = notification.extras;
            if (bundle != null) {
                topicImgRes = bundle.getInt(TOPIC_IMG_RES);
                positiveActionId = bundle.getLong(POSITIVE_ACTION_ID);
                objectId = bundle.getString(OBJECT_ID);
            }
        }
        int id = intent.getIntExtra(NOTIFICATION_ID, 0);
        PendingIntent pendingNotificationIntent = PendingIntent.getActivity(
                context, 0, PositiveActionDetail.getIntent(context, topicImgRes, positiveActionId, id, objectId),
                PendingIntent.FLAG_UPDATE_CURRENT);
        notification.contentIntent =  pendingNotificationIntent;
        notificationManager.notify(id, notification);

    }
}