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
import org.apache.commons.lang3.StringUtils;
import vn.com.kms.phudnguyen.autovolumemanager.listener.database.DatabaseHelper;
import vn.com.kms.phudnguyen.autovolumemanager.listener.model.Event;
import vn.com.kms.phudnguyen.autovolumemanager.listener.model.EventAction;
import vn.com.kms.phudnguyen.autovolumemanager.listener.model.Rule;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

public class ListenerService extends NotificationListenerService {
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

        DatabaseHelper.getInstance().insertEvent(Event.builder()
                .eventId(UUID.randomUUID().toString())
                .action(EventAction.SERVICE_STARTED.name())
                .build(), null);
    }

    @Override
    public void onListenerDisconnected() {
        DatabaseHelper.getInstance().insertEvent(Event.builder()
                .eventId(UUID.randomUUID().toString())
                .action(EventAction.SERVICE_STOPPED.name())
                .build(), null);
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



        List<Rule> allRules = null;
        try {
            allRules = instance.getAllRulesByPackageName(sbn.getPackageName());
        } catch (IllegalAccessException | InvocationTargetException | InstantiationException | ParseException e) {
            Log.e(TAG, "Fail to query rules", e);
            return;
        }

        for (Rule rule : allRules) {

            if (isRuleMatched(rule, title, subTitle)) {
                int volume = am.getStreamVolume(AudioManager.STREAM_MUSIC);
                if (volume <= 0) {
                    return;
                }
                beforeMuted = volume;
                // Mute
                am.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);

                instance.insertEvent(Event.builder()
                        .eventId(UUID.randomUUID().toString())
                        .action(EventAction.MUTED.name())
                        .ruleId(rule.getRuleId())
                        .build(), null);

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

                instance.insertEvent(Event.builder()
                        .eventId(UUID.randomUUID().toString())
                        .action(EventAction.RESTORED.name())
                        .ruleId(rule.getRuleId())
                        .build(), null);
            }
        }
    }

    public static boolean isRuleMatched(Rule rule, String text, String subText) {
        return Pattern.matches(StringUtils.isBlank(rule.getText()) ? ".*" : rule.getText(), text)
                && Pattern.matches(StringUtils.isBlank(rule.getSubText()) ? ".*" : rule.getSubText(), subText);
    }


    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        super.onNotificationRemoved(sbn);
    }
}
