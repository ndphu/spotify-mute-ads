package vn.com.phudnguyen.tools.autovolumemanager.listener.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import vn.com.phudnguyen.tools.autovolumemanager.listener.model.Event;
import vn.com.phudnguyen.tools.autovolumemanager.listener.model.EventAction;
import vn.com.phudnguyen.tools.autovolumemanager.listener.model.Rule;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.*;

public class DatabaseHelper extends SQLiteOpenHelper {


    private static final String TAG = DatabaseHelper.class.getName();

    private static final String DB_NAME = "auto-volume-manager";
    private static final int DB_VERSION = 6;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Rule.CREATE_TABLE);
        db.execSQL(Event.CREATE_TABLE);

        insertRule(Rule.builder()
                .ruleId(UUID.randomUUID().toString())
                .packageName("com.spotify.music")
                .text("Advertisement")
                .subText(".*")
                .build(), db);
        insertRule(Rule.builder()
                .ruleId(UUID.randomUUID().toString())
                .packageName("com.spotify.music")
                .text(".*")
                .subText("Spotify")
                .build(), db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + Rule.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Event.TABLE_NAME);

        // Create tables again
        onCreate(db);
    }

    public void insertRule(Rule rule, SQLiteDatabase db) {
        Log.i(TAG, "Inserting rule " + rule.getText() + " - " + rule.getSubText());
        if (db == null) {
            db = getWritableDatabase();
        }

        db.beginTransaction();

        try {
            ContentValues values = new ContentValues();
            values.put(Rule.COLUMN_RULE_ID, rule.getRuleId());
            values.put(Rule.COLUMN_PACKAGE_NAME, rule.getPackageName());
            values.put(Rule.COLUMN_TEXT, rule.getText());
            values.put(Rule.COLUMN_SUB_TEXT, rule.getSubText());
            db.insertOrThrow(Rule.TABLE_NAME, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, "Fail to insert rule by error", e);
            throw e;
        } finally {
            db.endTransaction();
        }
    }

    public void updateRule(Rule rule, SQLiteDatabase db) {
        Log.i(TAG, "Updating rule " + rule.getId() + "-" + rule.getText() + " - " + rule.getSubText());
        if (db == null) {
            db = getWritableDatabase();
        }

        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(Rule.COLUMN_RULE_ID, rule.getRuleId());
            values.put(Rule.COLUMN_PACKAGE_NAME, rule.getPackageName());
            values.put(Rule.COLUMN_TEXT, rule.getText());
            values.put(Rule.COLUMN_SUB_TEXT, rule.getSubText());
            db.update(Rule.TABLE_NAME, values, "id = ?", new String[]{ rule.getId().toString() });
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, "Fail to update rule by error", e);
            throw e;
        } finally {
            db.endTransaction();
        }
    }

    public void insertEvent(Event event, SQLiteDatabase db) {
        if (db == null) {
            db = getWritableDatabase();
        }

        db.beginTransaction();

        try {
            ContentValues values = new ContentValues();
            values.put(Event.COLUMN_RULE_ID, event.getRuleId());
            values.put(Event.COLUMN_EVENT_ID, event.getEventId());
            values.put(Event.COLUMN_ACTION, event.getAction());
            values.put(Event.COLUMN_RULE_ID, event.getRuleId());
            db.insertOrThrow(Event.TABLE_NAME, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, "Fail to insert rule by error", e);
            throw e;
        } finally {
            db.endTransaction();
        }
    }

    public List<Event> getAllEvents() throws IllegalAccessException, InvocationTargetException, InstantiationException, ParseException {
        SQLiteDatabase db = getReadableDatabase();
        try (Cursor cursor = db.query(Event.TABLE_NAME, new String[]{
                        Event.COLUMN_ID,
                        Event.COLUMN_EVENT_ID,
                        Event.COLUMN_ACTION,
                        Event.COLUMN_RULE_ID,
                        Event.COLUMN_TIMESTAMP},
                null,
                null,
                null,
                null,
                Event.COLUMN_ID + " desc", "25")) {
            return EntityMapper.mapToList(cursor, Event.class);
        } catch (Exception e) {
            Log.e(TAG, "Fail to query rules from DB", e);
            throw e;
        }
    }

    public List<Event> getAllEventsByTypes(EventAction[] actions) throws IllegalAccessException, InvocationTargetException, InstantiationException, ParseException {
        if (actions == null || actions.length == 0) {
            return Collections.emptyList();
        }
        SQLiteDatabase db = getReadableDatabase();
        try (Cursor cursor = db.query(Event.TABLE_NAME, new String[]{
                        Event.COLUMN_ID,
                        Event.COLUMN_EVENT_ID,
                        Event.COLUMN_ACTION,
                        Event.COLUMN_RULE_ID,
                        Event.COLUMN_TIMESTAMP},
                Event.COLUMN_ACTION + " IN " + buildActionsArray(actions),
                null,
                null,
                null,
                Event.COLUMN_ID + " desc", "25")) {
            return EntityMapper.mapToList(cursor, Event.class);
        } catch (Exception e) {
            Log.e(TAG, "Fail to query rules from DB", e);
            throw e;
        }
    }

    public static String buildActionsArray(EventAction[] actions) {
        if (actions == null) {
            return "()";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        for (int i = 0; i < actions.length; i++) {
            sb.append("'").append(actions[i].name()).append("'");
            if (i < actions.length - 1) {
                sb.append(",");
            }
        }
        sb.append(")");
        return sb.toString();
    }

    public List<Rule> getAllRules() throws IllegalAccessException, InvocationTargetException, InstantiationException, ParseException {
        SQLiteDatabase db = getReadableDatabase();
        try (Cursor cursor = db.query(Rule.TABLE_NAME, new String[]{
                Rule.COLUMN_ID,
                Rule.COLUMN_RULE_ID,
                Rule.COLUMN_PACKAGE_NAME,
                Rule.COLUMN_TEXT,
                Rule.COLUMN_SUB_TEXT,
                Rule.COLUMN_TIMESTAMP}, null, null, null, null, null)) {
            return EntityMapper.mapToList(cursor, Rule.class);
        } catch (Exception e) {
            Log.e(TAG, "Fail to query rules from DB", e);
            throw e;
        }
    }

    public List<Rule> getAllRulesByPackageName(String packageName) throws IllegalAccessException, InvocationTargetException, InstantiationException, ParseException {
        SQLiteDatabase db = getReadableDatabase();
        try (Cursor cursor = db.query(Rule.TABLE_NAME, new String[]{
                        Rule.COLUMN_ID,
                        Rule.COLUMN_RULE_ID,
                        Rule.COLUMN_PACKAGE_NAME,
                        Rule.COLUMN_TEXT,
                        Rule.COLUMN_SUB_TEXT,
                        Rule.COLUMN_TIMESTAMP},
                Rule.COLUMN_PACKAGE_NAME + "=?",
                new String[]{packageName}, null, null, null)) {
            return EntityMapper.mapToList(cursor, Rule.class);
        } catch (Exception e) {
            Log.e(TAG, "Fail to query rules from DB", e);
            throw e;
        }
    }

    private static DatabaseHelper INSTANCE;

    // Singleton Stuffs

    public static synchronized void initialize(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new DatabaseHelper(context);
        }
    }

    public static synchronized DatabaseHelper getInstance() {
        return INSTANCE;
    }

}
