package com.jason.core.utils;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.util.Map;

/**
 * json解析相关处理封装，方便之后gson,fastjson之类底层库的切换
 */
public class JsonUtils {

    private static final String TAG = "JsonUtils";

    public static <T> T fromJsonUnsafe(Gson gson, String json, Class<T> clazz) throws JsonSyntaxException {
        if (gson == null || TextUtils.isEmpty(json)) {
            return null;
        }
        return gson.fromJson(json, clazz);
    }

    public static <T> T fromJsonUnsafe(Gson gson, String json, TypeToken<T> typeToken) throws JsonSyntaxException {
        if (gson == null || TextUtils.isEmpty(json) || typeToken == null) {
            return null;
        }
        return gson.fromJson(json, typeToken.getType());
    }

    public static <T> T fromJson(Gson gson, String json, Class<T> clazz) {
        try {
            return fromJsonUnsafe(gson, json, clazz);
        } catch (Exception e) {
            Log.w(TAG, e);
        }
        return null;
    }

    public static <T> T fromJson(String json, Class<T> clazz) {
        return fromJson(new Gson(), json, clazz);
    }

    public static <T> T fromJson(Gson gson, String json, TypeToken<T> typeToken) {
        try {
            return fromJsonUnsafe(gson, json, typeToken);
        } catch (Exception e) {
            Log.w(TAG, e);
        }
        return null;
    }

    public static <T> T fromJson(String json, TypeToken<T> typeToken) {
        return fromJson(new Gson(), json, typeToken);
    }

    public static <T> String toJson(Object data, TypeToken<T> typeToken) {
        return toJson(new Gson(), data, typeToken);
    }

    public static <T> String toJson(Gson gson, Object data, TypeToken<T> typeToken) {
        if (gson == null || data == null || typeToken == null) {
            return null;
        }
        return gson.toJson(data, typeToken.getType());
    }

    public static String toJson(Gson gson, Object data) {
        if (gson == null || data == null) {
            return null;
        }
        return gson.toJson(data);
    }

    public static String toJson(Object data) {
        return toJson(new Gson(), data);
    }

    /**
     * 耗时操作，慎用
     */
    public static String toJson(Object data1, Object data2) {
        Gson gson = new Gson();
        JsonObject mainObject = gson.toJsonTree(data1).getAsJsonObject();
        JsonObject extraObject = gson.toJsonTree(data2).getAsJsonObject();
        for (Map.Entry<String, JsonElement> entry : extraObject.entrySet()) {
            mainObject.add(entry.getKey(), entry.getValue());
        }
        return mainObject.toString();
    }
}
