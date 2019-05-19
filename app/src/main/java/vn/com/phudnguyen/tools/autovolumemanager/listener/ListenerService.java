package vn.com.phudnguyen.tools.autovolumemanager.listener;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import com.google.gson.JsonObject;
import org.apache.commons.lang3.StringUtils;
import vn.com.phudnguyen.tools.autovolumemanager.R;
import vn.com.phudnguyen.tools.autovolumemanager.listener.database.DatabaseHelper;
import vn.com.phudnguyen.tools.autovolumemanager.listener.model.*;
import vn.com.phudnguyen.tools.autovolumemanager.listener.utils.GsonUtils;
import vn.com.phudnguyen.tools.autovolumemanager.listener.utils.PrefUtils;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

public class ListenerService extends NotificationListenerService {
    private String TAG = this.getClass().getSimpleName();
    private ServiceState serviceState = new ServiceState();
    private static String NOTIFICATION_CHANNEL_ID = "1123123";

    @Override
    public void onCreate() {
        super.onCreate();
        DatabaseHelper.initialize(getApplicationContext());
    }

    @Override
    public void onListenerConnected() {
        super.onListenerConnected();

        Notification notification = buildNotification();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "Default", NotificationManager.IMPORTANCE_NONE);
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (manager == null) {
                return;
            }
            manager.createNotificationChannel(chan);
        }

        startForeground(1990, notification);

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
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP  | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        b.setOngoing(true)
                .setContentTitle("Auto Volume Manager is running")
                .setContentText("Tap to configure")
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_service_notification)
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

        String title = notification.extras.get("android.title") + "";
        String content = notification.extras.get("android.text") + "";

        Log.i(TAG, "Title: " + title + "; Content: " + content);

        if (PrefUtils.isNotificationLogEnabled(this, sbn.getPackageName())) {
            NotificationLog notificationLog = NotificationLog.builder()
                    .title(title)
                    .content(content)
                    .packageName(sbn.getPackageName())
                    .build();
            instance.insertNotificationLog(notificationLog, null);
        } else {
            Log.w(TAG, "notification logging is disabled");
        }

        List<Rule> allRules;
        try {
            allRules = instance.getAllRulesByPackageName(sbn.getPackageName());
        } catch (IllegalAccessException | InvocationTargetException | InstantiationException | ParseException e) {
            Log.e(TAG, "Fail to query rules", e);
            return;
        }

        Rule matchedRule = null;
        for (Rule rule : allRules) {
            if (isRuleMatched(rule, title, content)) {
                matchedRule = rule;
                break;
            }
        }

        if (matchedRule != null) {
            muteAndSaveState(am, instance, matchedRule);
        } else {
            if (isSamePackageOfMutedRule(sbn)) {
                restoreVolumeAndSaveState(am, instance);
            }
        }
    }

    private boolean isSamePackageOfMutedRule(StatusBarNotification sbn) {
        return serviceState.getAppliedRule() != null
                && StringUtils.equals(serviceState.getAppliedRule().getPackageName(), sbn.getPackageName());
    }

    private void restoreVolumeAndSaveState(AudioManager am, DatabaseHelper instance) {
        int currentVolume = am.getStreamVolume(AudioManager.STREAM_MUSIC);
        Log.i(TAG, "Current volume is " + currentVolume);
        if (currentVolume > 0) {
            Log.i(TAG, "We don't change volume if not muted.");
            serviceState.setBeforeMuted(currentVolume);
            Log.i(TAG, "Cached volume = " + serviceState.getBeforeMuted());
        } else {
            Log.i(TAG, "Restoring volume to " + serviceState.getBeforeMuted());

            int sleepInterval = PrefUtils.getSleepInterval(getApplicationContext());
            if (sleepInterval > 0) {
                try {
                    Thread.sleep(sleepInterval);
                } catch (InterruptedException e) {
                    //ignore
                }
            }
            am.setStreamVolume(AudioManager.STREAM_MUSIC, serviceState.getBeforeMuted(), 0);

            instance.insertEvent(Event.builder()
                    .eventId(UUID.randomUUID().toString())
                    .action(EventAction.RESTORED.name())
                    .ruleId(serviceState.getAppliedRule().getRuleId())
                    .build(), null);

            serviceState.setAppliedRule(null);
        }
    }

    private void muteAndSaveState(AudioManager am, DatabaseHelper instance, Rule matchedRule) {
        int volume = am.getStreamVolume(AudioManager.STREAM_MUSIC);
        if (volume > 0) {
            // Mute
            am.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);

            instance.insertEvent(Event.builder()
                    .eventId(UUID.randomUUID().toString())
                    .action(EventAction.MUTED.name())
                    .ruleId(matchedRule.getRuleId())
                    .build(), null);

            serviceState.setBeforeMuted(volume);
            serviceState.setAppliedRule(matchedRule);
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
