package com.jason.avengers.other.calendar.beans;

import java.io.Serializable;

/**
 * @author Jason
 */
public class Owner implements Serializable {

    private long id;
    private String name;
    private int color;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
