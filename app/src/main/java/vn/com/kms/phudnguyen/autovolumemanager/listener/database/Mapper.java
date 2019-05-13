package vn.com.kms.phudnguyen.autovolumemanager.listener.database;

import android.database.Cursor;
import android.util.Log;
import vn.com.kms.phudnguyen.autovolumemanager.listener.model.Constants;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.database.Cursor.*;

public class Mapper {

    private static final String TAG = Mapper.class.getName();

    public static <T> List<T> mapToList(Cursor cursor, Class<T> clazz) throws IllegalAccessException, InstantiationException, InvocationTargetException, ParseException {
        List<T> result = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                T rs = clazz.newInstance();
                for (Field f : clazz.getDeclaredFields()) {
                    Column mapping = f.getAnnotation(Column.class);
                    if (mapping == null) {
                        continue;
                    }
                    int columnIndex = cursor.getColumnIndex(mapping.name());
                    f.setAccessible(true);

                    switch (cursor.getType(columnIndex)) {
                        case FIELD_TYPE_NULL: {
                            f.set(rs, null);
                            break;
                        }
                        case FIELD_TYPE_FLOAT: {
                            f.setFloat(rs, cursor.getFloat(columnIndex));
                            break;
                        }
                        case FIELD_TYPE_STRING: {
                            String dbValue = cursor.getString(columnIndex);
                            Log.i(TAG, dbValue);
                            if (f.getType() == Date.class) {
                                Date dbTime = Constants.DATE_FORMAT_UTC.parse(dbValue);
                                f.set(rs, dbTime);
                            } else {
                                f.set(rs, dbValue);
                            }
                            break;
                        }
                        case FIELD_TYPE_INTEGER: {
                            int dbValue = cursor.getInt(columnIndex);
                            if (f.getType() == Long.class) {
                                f.set(rs, Long.valueOf(dbValue));
                            } else if (f.getType() == Integer.class) {
                                f.set(rs, Integer.valueOf(dbValue));
                            } else if (f.getType() == Boolean.class) {
                                f.setBoolean(rs, Boolean.valueOf(dbValue > 0));
                            } else if (f.getType() == Date.class) {
                                f.set(rs, new Date(dbValue));
                            } else {
                                throw new RuntimeException("Invalid field type " + f.getType() + " for db type FIELD_TYPE_INTEGER");
                            }
                            break;
                        }
                    }
                }
                result.add(rs);
            } while (cursor.moveToNext());
        }
        return result;
    }
}
