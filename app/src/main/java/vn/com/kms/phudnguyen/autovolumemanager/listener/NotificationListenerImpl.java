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
import vn.com.kms.phudnguyen.autovolumemanager.listener.database.DatabaseHelper;
import vn.com.kms.phudnguyen.autovolumemanager.listener.model.Event;
import vn.com.kms.phudnguyen.autovolumemanager.listener.model.EventAction;
import vn.com.kms.phudnguyen.autovolumemanager.listener.model.Rule;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.*;

public class NotificationListenerImpl extends NotificationListenerService {
    private String TAG = this.getClass().getSimpleName();
    private Gson gson;
    private int beforeMuted = 0;
    private static String NOTIFICATION_CHANNEL_ID = "1123123";

    @Override
    public void onCreate() {
        super.onCreate();
        gson = new GsonBuilder().create();
        DatabaseHelper.initialize(getApplicationContext());
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

        Event event = new Event();
        event.setEventId(UUID.randomUUID().toString());
        event.setAction(EventAction.SERVICE_STARTED.name());
        event.setTimestamp(new Date());
        DatabaseHelper.getInstance().insertEvent(event, null);
    }

    @Override
    public void onListenerDisconnected() {
        Event event = new Event();
        event.setEventId(UUID.randomUUID().toString());
        event.setAction(EventAction.SERVICE_STOPPED.name());
        DatabaseHelper.getInstance().insertEvent(event, null);
        super.onListenerDisconnected();
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
        final AudioManager am = (AudioManager) getSystemService(AUDIO_SERVICE);
        if (am == null) {
            Log.w(TAG, "Fail to get audio manager");
            return;
        }

        Notification notification = sbn.getNotification();
        Log.i(TAG, "ID :" + sbn.getId() + "\t" + notification.tickerText + "\t" + sbn.getPackageName());

        DatabaseHelper instance = DatabaseHelper.getInstance();

        List<Rule> allRules = null;
        try {
            allRules = instance.getAllRulesByPackageName(sbn.getPackageName());
        } catch (IllegalAccessException | InvocationTargetException | InstantiationException | ParseException e) {
            Log.e(TAG, "Fail to query rules", e);
            return;
        }
        for (Rule rule : allRules) {
            String title;
            String subTitle;

            try {
                String details = gson.toJson(notification);
                JsonObject json = gson.fromJson(details, JsonObject.class);
                JsonObject mMap = json.getAsJsonObject("extras").getAsJsonObject("mMap");
                title = mMap.getAsJsonObject("android.title").get("mText").getAsString();
                subTitle = mMap.getAsJsonObject("android.text").get("mText").getAsString();
            } catch (Exception e) {
                title = notification.extras.get("android.title") + "";
                subTitle = notification.extras.get("android.text") + "";
            }

            Log.i(TAG, "Title: " + title + "; Subtitle: " + subTitle);

            if (rule.getText().contentEquals(title) && rule.getSubText().equals(subTitle)) {
                int volume = am.getStreamVolume(AudioManager.STREAM_MUSIC);
                if (volume <= 0) {
                    return;
                }
                beforeMuted = volume;
                // Mute
                am.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);

                Event event = new Event();
                event.setEventId(UUID.randomUUID().toString());
                event.setAction(EventAction.MUTED.name());
                event.setRuleId(rule.getRuleId());
                event.setTimestamp(new Date());
                instance.insertEvent(event, null);

                break;
            } else {
                int currentVolume = am.getStreamVolume(AudioManager.STREAM_MUSIC);
                Log.i(TAG, "Current volume is " + currentVolume);
                if (currentVolume > 0) {
                    Log.i(TAG, "We don't change volume if not muted.");
                    beforeMuted = currentVolume;
                    Log.i(TAG, "Cached volume = " + beforeMuted);
                    return;
                }
                Log.i(TAG, "Restoring volume to " + beforeMuted);
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    //ignore
                }
                am.setStreamVolume(AudioManager.STREAM_MUSIC, beforeMuted, 0);

                Event event = new Event();
                event.setEventId(UUID.randomUUID().toString());
                event.setAction(EventAction.RESTORED.name());
                event.setRuleId(rule.getRuleId());
                event.setTimestamp(new Date());
                instance.insertEvent(event, null);
            }
        }
    }


    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        super.onNotificationRemoved(sbn);
    }
}
