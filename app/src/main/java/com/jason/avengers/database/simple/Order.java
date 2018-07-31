package com.jason.avengers.database.simple;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.NameInDb;
import io.objectbox.annotation.TargetIdProperty;
import io.objectbox.relation.ToOne;

/**
 * Created by jason on 2018/7/31.
 */

@Entity
public class Order {

    @Id
    @NameInDb("ID")
    public long id;

    @TargetIdProperty("CUSTOMERID")
    public ToOne<Customer> customer;

}
