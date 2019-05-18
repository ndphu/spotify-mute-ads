package vn.com.phudnguyen.tools.autovolumemanager.listener.model;

import lombok.Builder;
import lombok.Data;
import vn.com.phudnguyen.tools.autovolumemanager.listener.database.Column;

import java.util.Date;

@Data
@Builder
public class NotificationLog {
    public static final String TABLE_NAME = "notification_log";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_CONTENT = "content";
    public static final String COLUMN_PACKAGE_NAME = "package_name";
    public static final String COLUMN_TIMESTAMP = "timestamp";

    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_TITLE + " TEXT,"
            + COLUMN_CONTENT + " TEXT,"
            + COLUMN_PACKAGE_NAME + " TEXT,"
            + COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP"
            + ")";


    @Column(name = COLUMN_ID)
    private Long id;

    @Column(name = COLUMN_TITLE)
    private String title;

    @Column(name = COLUMN_CONTENT)
    private String content;

    @Column(name = COLUMN_PACKAGE_NAME)
    private String packageName;

    @Column(name = COLUMN_TIMESTAMP)
    private Date timestamp;

}
