package com.jason.avengers.database.simple;

import java.io.Serializable;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.NameInDb;
import io.objectbox.relation.ToMany;

/**
 * Created by jason on 2018/7/31.
 */

@Entity
public class Teacher implements Serializable {

    @Id
    @NameInDb("ID")
    public long id;

    public ToMany<Student> students;
}
