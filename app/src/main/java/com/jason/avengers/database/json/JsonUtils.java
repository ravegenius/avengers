package com.jason.avengers.database.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.objectbox.relation.ToOne;

/**
 * Created by jason on 2018/8/3.
 */

public class JsonUtils {

    public static String toJson(Object object) {
        Gson gson = new GsonBuilder()
                .registerTypeHierarchyAdapter(JsonDBEntity.class, new JsonBoxDeserializer<>())
                .registerTypeAdapter(ToOne.class, new JsonToOneSerializer())
                .create();
        return gson.toJson(object);
    }

    public static <T extends JsonDBEntity> T fromJson(String json, Class<T> clazz) {
        Gson gson = new GsonBuilder()
                .registerTypeHierarchyAdapter(JsonDBEntity.class, new JsonBoxDeserializer<>())
                .registerTypeAdapter(ToOne.class, new JsonToOneSerializer())
                .create();
        return gson.fromJson(json, clazz);
    }
}
