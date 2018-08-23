package com.jason.avengers.common.database.json;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class JsonBoxDeserializer<T extends JsonDBEntity> implements JsonDeserializer<T> {

    @Override
    public T deserialize(JsonElement json, Type typeOfSrc, JsonDeserializationContext context) throws JsonParseException {
        try {
            T var = ((Class<T>) typeOfSrc).newInstance();
            var.fromJson(json.getAsJsonObject(), context);
            return var;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (Throwable e) {
            e.printStackTrace();
        }

        return null;
    }
}