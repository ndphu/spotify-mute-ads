package vn.com.kms.phudnguyen.autovolumemanager.listener.model;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

public class Constants {
    public static final SimpleDateFormat DATE_FORMAT_UTC = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
    public static final SimpleDateFormat DATE_FORMAT_WITH_LOCAL_TIMEZONE = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
    public static final SimpleDateFormat DATE_FORMAT_TIME_ONLY_WITH_LOCAL_TIMEZONE = new SimpleDateFormat("HH:mm:ss", Locale.US);

    static {
        DATE_FORMAT_UTC.setTimeZone(TimeZone.getTimeZone("UTC"));

        TimeZone defaultTimezone = TimeZone.getDefault();
        DATE_FORMAT_WITH_LOCAL_TIMEZONE.setTimeZone(defaultTimezone);
        DATE_FORMAT_TIME_ONLY_WITH_LOCAL_TIMEZONE.setTimeZone(defaultTimezone);
    }
}
