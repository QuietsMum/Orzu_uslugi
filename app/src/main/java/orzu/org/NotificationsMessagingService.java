package orzu.org;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;

import android.content.Context;

import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Build;
import android.util.Log;


import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;


import com.google.firebase.messaging.RemoteMessage;
import com.pusher.pushnotifications.fcm.MessagingService;


import org.jetbrains.annotations.NotNull;

public class NotificationsMessagingService extends MessagingService {
    Context context = this;
    DBHelper dbHelper;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onMessageReceived(@NotNull RemoteMessage remoteMessage) {

        Log.e("BEAMS", String.valueOf(remoteMessage));
/*
        NotificationManager mNotificationManager;
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context.getApplicationContext(), "notify_001");
        Intent ii = new Intent(context, Main2Activity.class);
        final SharedPreferences prefs = getSharedPreferences(" ", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("openNotification", true);
        editor.apply();
        ii.putExtra("openNotification", "asdasdsd");
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, ii, 0);
        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
        bigText.bigText(remoteMessage.getData().get("user"));
        bigText.setBigContentTitle(remoteMessage.getData().get("message"));
        bigText.setSummaryText("date");
        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setSmallIcon(R.mipmap.ic_launcher);
        mBuilder.setContentTitle(remoteMessage.getData().get("user"));
        mBuilder.setContentText(remoteMessage.getData().get("message"));
        mBuilder.setPriority(Notification.PRIORITY_MAX);
        mBuilder.setStyle(bigText);
        mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);


        String channelId = "Your_channel_id";
        NotificationChannel channel1 = new NotificationChannel(
                channelId,
                "Channel human readable title",
                NotificationManager.IMPORTANCE_DEFAULT);
        mNotificationManager.createNotificationChannel(channel1);
        mBuilder.setChannelId(channelId);
        mNotificationManager.notify(0, mBuilder.build());*/
    }
}
