package com.jason.avengers.database.simple;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.NameInDb;

/**
 * Created by jason on 2018/7/31.
 */

@Entity
public class Teacher {

    @Id
    @NameInDb("ID")
    public long id;
}
