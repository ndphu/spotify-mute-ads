package vn.com.phudnguyen.tools.autovolumemanager.listener.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonUtils {

    private static final Gson GSON = new GsonBuilder().create();

    public static String serialize(Object input) {
        return GSON.toJson(input);
    }

    public static <T> T deserizalize(String json, Class<T> clazz) {
        return GSON.fromJson(json, clazz);
    }

}
