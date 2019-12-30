package com.jason.avengers.common.database.entity;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.NameInDb;

@Entity
@NameInDb("LOG")
public class LogDBEntity extends BaseDBEntity {

    @NameInDb("MSG")
    private String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
