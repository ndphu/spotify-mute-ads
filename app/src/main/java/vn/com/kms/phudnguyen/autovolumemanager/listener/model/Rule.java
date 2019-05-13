package vn.com.kms.phudnguyen.autovolumemanager.listener.model;

import vn.com.kms.phudnguyen.autovolumemanager.listener.database.Column;

import java.util.Date;

public class Rule {

    public static final String TABLE_NAME = "rules";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_RULE_ID = "rule_id";
    public static final String COLUMN_PACKAGE_NAME = "package_name";
    public static final String COLUMN_TEXT = "text";
    public static final String COLUMN_SUB_TEXT = "sub_text";
    public static final String COLUMN_ACTIVE = "active";
    public static final String COLUMN_TIMESTAMP = "timestamp";

    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_RULE_ID + " TEXT,"
            + COLUMN_PACKAGE_NAME + " TEXT,"
            + COLUMN_TEXT + " TEXT,"
            + COLUMN_SUB_TEXT + " TEXT,"
            + COLUMN_ACTIVE + " BIT,"
            + COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP"
            + ")";


    @Column(name = COLUMN_ID)
    private Long id;

    @Column(name = COLUMN_RULE_ID)
    private String ruleId;

    @Column(name = COLUMN_PACKAGE_NAME)
    private String packageName;

    @Column(name = COLUMN_TEXT)
    private String text;

    @Column(name = COLUMN_SUB_TEXT)
    private String subText;

    @Column(name = COLUMN_TIMESTAMP)
    private Date timestamp;

    public Rule() {

    }

    public Rule(String ruleId, String packageName, String text, String subText, Date timestamp) {
        this.ruleId = ruleId;
        this.packageName = packageName;
        this.text = text;
        this.subText = subText;
        this.timestamp = timestamp;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSubText() {
        return subText;
    }

    public void setSubText(String subText) {
        this.subText = subText;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getRuleId() {
        return ruleId;
    }

    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }
}
