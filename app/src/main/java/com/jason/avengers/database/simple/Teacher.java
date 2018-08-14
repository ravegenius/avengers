package com.jason.avengers.database.simple;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.jason.avengers.database.json.JsonDBEntity;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.NameInDb;
import io.objectbox.relation.ToMany;

/**
 * Created by jason on 2018/7/31.
 */

@Entity
public class Teacher extends JsonDBEntity {

    @Id
    @NameInDb("ID")
    public long id;

    public ToMany<Student> students;

    @Override
    protected void fromJson(JsonObject jsonObject, JsonDeserializationContext context) {
        fromJsonToMany(students, Student.class, jsonObject.get("students"), context);
        id = jsonObject.get("id").getAsLong();
    }
}
