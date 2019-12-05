package orzu.org;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class NotificationsMessagingService extends FirebaseMessagingService {
    Context context = this;
    DBHelper dbHelper;
    SharedPreferences prefs;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onMessageReceived(@NotNull RemoteMessage remoteMessage) {
        prefs = getSharedPreferences(" ", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("New_task", remoteMessage.getData() + "");
        editor.apply();
        if (remoteMessage.getData().containsKey("ID")) {
            showNotification(remoteMessage);
        }
    }

    private void showNotification(@NotNull RemoteMessage remoteMessage) {
        String[] ar = remoteMessage.getData().get("ID").split(".");
        Intent intent = new Intent(this, TaskViewMain.class);
        ;
        SharedPreferences.Editor editor = prefs.edit();
        String id;
        if (ar.length >= 2) {
            editor.putString("mytask", "my");
            id = ar[1];
        } else {
            editor.putString("mytask", "not");
            id = ar[0];
        }
        editor.putString("idd", id);
        editor.putString("New_task", remoteMessage.getData() + "");
        editor.putString("opt", "view");
        editor.putBoolean("notif", true);
        editor.apply();
        PendingIntent activity = PendingIntent.getActivity(this, 0, intent, 0);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "orzu.org.test";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "Notification",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription("Hello");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.BLUE);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.logo_in_desktop)
                .setContentTitle(remoteMessage.getNotification().getTitle())
                .setContentText(remoteMessage.getNotification().getBody())
                .setContentInfo("Info")
                .setContentIntent(activity);
        notificationManager.notify(new Random().nextInt(), notificationBuilder.build());
    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
    }
}
