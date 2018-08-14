package com.jason.avengers.database.json;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.lang.reflect.Type;

import io.objectbox.relation.ToMany;
import io.objectbox.relation.ToOne;

/**
 * Created by jason on 2018/8/3.
 */

public abstract class JsonDBEntity {

    protected abstract void fromJson(JsonObject jsonObject, JsonDeserializationContext context);

    protected <T extends JsonDBEntity> void fromJsonToMany(ToMany<T> toMany, Class<T> type,
                                                           JsonElement jsonElement, JsonDeserializationContext context) {
        if (toMany == null)
            return;
        toMany.clear();
        if (jsonElement.isJsonArray()) {
            JsonArray array = jsonElement.getAsJsonArray();
            for (int i = 0; i < array.size(); i++) {
                JsonElement element = array.get(i);
                T item = context.deserialize(element, type);
                toMany.add(item);
            }
        }
    }

    protected <T extends JsonDBEntity> void fromJsonToOne(ToOne<T> toOne, Type typeOfT,
                                                          JsonElement jsonElement, JsonDeserializationContext context) {
        if (toOne == null)
            return;
        try {
            T one = ((Class<T>) typeOfT).newInstance();
            one.fromJson(jsonElement.getAsJsonObject(), context);
            toOne.setTarget(one);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
