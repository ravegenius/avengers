package com.jason.avengers.other.beans;

import java.io.Serializable;

/**
 * @author Jason
 */
public class EventBean implements Serializable {

    private long id;
    private String eventTime;
    private OwnerBean ownerBean;
    private String level;
    private double money;
    private boolean isGroup;
    private boolean isDone;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEventTime() {
        return eventTime;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    public OwnerBean getOwnerBean() {
        return ownerBean;
    }

    public void setOwnerBean(OwnerBean ownerBean) {
        this.ownerBean = ownerBean;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public boolean isGroup() {
        return isGroup;
    }

    public void setGroup(boolean group) {
        isGroup = group;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }
}