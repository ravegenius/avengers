package com.jason.avengers.database.simple;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.jason.avengers.database.json.JsonDBEntity;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.NameInDb;
import io.objectbox.annotation.TargetIdProperty;
import io.objectbox.relation.ToOne;

/**
 * Created by jason on 2018/7/31.
 */

@Entity
public class Order extends JsonDBEntity {

    @Id
    @NameInDb("ID")
    public long id;

    @TargetIdProperty("CUSTOMERID")
    public ToOne<Customer> customer;

    @Override
    public void fromJson(JsonObject jsonObject, JsonDeserializationContext context) {
        filling(customer, Customer.class, jsonObject.get("customer"), context);
    }
}
