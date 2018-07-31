package com.jason.avengers.database.simple;

import io.objectbox.annotation.Backlink;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.NameInDb;
import io.objectbox.relation.ToMany;

/**
 * Created by jason on 2018/7/31.
 */

@Entity
public class Customer {

    @Id
    @NameInDb("ID")
    public long id;

    // 'to' is optional if only one relation matches
    @Backlink(to = "customer")
    public ToMany<Order> orders;

}
