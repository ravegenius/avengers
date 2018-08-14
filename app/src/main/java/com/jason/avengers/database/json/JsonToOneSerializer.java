package com.jason.avengers.database.json;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

import io.objectbox.relation.ToOne;

public class JsonToOneSerializer implements JsonSerializer<ToOne> {

    @Override
    public JsonElement serialize(ToOne src, Type typeOfSrc, JsonSerializationContext context) {
        return null;
    }
}