package com.jason.avengers.database.simple;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.NameInDb;
import io.objectbox.relation.ToMany;

/**
 * Created by jason on 2018/7/31.
 */

@Entity
public class Student {

    @Id
    @NameInDb("ID")
    public long id;

    public ToMany<Teacher> teachers;
}
