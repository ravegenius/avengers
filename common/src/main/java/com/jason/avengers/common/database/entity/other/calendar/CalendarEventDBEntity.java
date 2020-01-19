package com.jason.avengers.common.database.entity.other.calendar;

import com.jason.avengers.common.database.entity.BaseDBEntity;

import java.util.Date;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.NameInDb;
import io.objectbox.annotation.TargetIdProperty;
import io.objectbox.relation.ToOne;

@Entity
@NameInDb("CALENDAREVENT")
public class CalendarEventDBEntity extends BaseDBEntity {

    @NameInDb("START_TIME")
    private Date startTime;

    @NameInDb("END_TIME")
    private Date endTime;

    @NameInDb("LEVEL")
    private String level;

    @NameInDb("MONEY")
    private double money;

    @TargetIdProperty("OWNER_ID")
    private ToOne<CalendarOwnerDBEntity> owner;

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getLevel() {
        return level;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public double getMoney() {
        return money;
    }

    public ToOne<CalendarOwnerDBEntity> getOwner() {
        return owner;
    }

    public void setOwner(ToOne<CalendarOwnerDBEntity> owner) {
        this.owner = owner;
    }
}
