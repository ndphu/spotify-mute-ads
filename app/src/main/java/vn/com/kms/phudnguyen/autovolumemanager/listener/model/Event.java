package vn.com.kms.phudnguyen.autovolumemanager.listener.model;

import vn.com.kms.phudnguyen.autovolumemanager.listener.database.Column;

import java.util.Date;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getRuleId() {
        return ruleId;
    }

    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
