package vn.com.kms.phudnguyen.autovolumemanager.listener;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.util.Date;

public class NotificationListenerImpl extends NotificationListenerService {
    private String TAG = this.getClass().getSimpleName();
    private Gson gson;
    private int currentVolume = 0;
    private static String NOTIFICATION_CHANNEL_ID = "1123123";

    @Override
    public void onCreate() {
        super.onCreate();
        gson = new GsonBuilder().create();
    }

    @Override
    public void onListenerConnected() {
        super.onListenerConnected();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "Default", NotificationManager.IMPORTANCE_NONE);
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (manager == null) {
                return;
            }
            manager.createNotificationChannel(chan);
            startForeground(1990, buildNotification());
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return super.onStartCommand(intent, flags, startId);
    }

    private Notification buildNotification() {
        NotificationCompat.Builder b = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);

        b.setOngoing(true)
                .setContentTitle("Auto Volume Manager")
                .setContentText("Started at " + new Date())
                .setTicker("Auto Volume Manager");

        return (b.build());
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        AudioManager am = (AudioManager) getSystemService(AUDIO_SERVICE);
        if (am == null) {
            Log.w(TAG, "Fail to get audio manager");
            return;
        }

        Log.i(TAG, "**********  onNotificationPosted");
        Notification notification = sbn.getNotification();
        Log.i(TAG, "ID :" + sbn.getId() + "\t" + notification.tickerText + "\t" + sbn.getPackageName());
        if ("com.spotify.music".equals(sbn.getPackageName())) {
            JsonObject json = gson.fromJson(gson.toJson(notification), JsonObject.class);
            JsonObject mMap = json.getAsJsonObject("extras").getAsJsonObject("mMap");
            String title = mMap.getAsJsonObject("android.title").get("mText").getAsString();
            String subTitle = mMap.getAsJsonObject("android.text").get("mText").getAsString();
            if ("Advertisement".contentEquals(title)) {
                int volume = am.getStreamVolume(AudioManager.STREAM_MUSIC);
                if (volume <= 0) {
                    return;
                }
                currentVolume = volume;
                // Mute
                am.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
            } else {
                Log.i(TAG, "Current volume is " + am.getStreamVolume(AudioManager.STREAM_MUSIC));
                Log.i(TAG, "title: " + title + "; subTitle: " + subTitle);

                if (currentVolume <= 0) {
                    Log.i(TAG, "Current volume is " + currentVolume + ".We don't touch volume here.");
                    return;
                }
                am.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume, 0);
            }
        }
    }



    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        super.onNotificationRemoved(sbn);
    }
}
