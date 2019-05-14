package vn.com.phudnguyen.tools.autovolumemanager.listener.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.com.phudnguyen.tools.autovolumemanager.listener.database.Column;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Event {

    public static final String TABLE_NAME = "events";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_EVENT_ID = "event_id";
    public static final String COLUMN_ACTION = "action";
    public static final String COLUMN_RULE_ID = "rule_id";
    public static final String COLUMN_TIMESTAMP = "timestamp";

    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_RULE_ID + " TEXT,"
            + COLUMN_EVENT_ID + " TEXT,"
            + COLUMN_ACTION + " TEXT,"
            + COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP"
            + ")";


    @Column(name = COLUMN_ID)
    private Long id;

    @Column(name = COLUMN_EVENT_ID)
    private String eventId;

    @Column(name = COLUMN_ACTION)
    private String action;

    @Column(name = COLUMN_EVENT_ID)
    private String ruleId;

    @Column(name = COLUMN_TIMESTAMP)
    private Date timestamp;

}
