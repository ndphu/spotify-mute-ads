package vn.com.phudnguyen.tools.autovolumemanager.listener.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.com.phudnguyen.tools.autovolumemanager.listener.database.Column;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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


}
