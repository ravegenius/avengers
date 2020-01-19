package com.jason.avengers.common.database.entity.other.calendar;

import com.jason.avengers.common.database.entity.BaseDBEntity;

import io.objectbox.annotation.Backlink;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Index;
import io.objectbox.annotation.NameInDb;
import io.objectbox.relation.ToMany;

@Entity
@NameInDb("CALENDAROWNER")
public class CalendarOwnerDBEntity extends BaseDBEntity {

    @Index
    @NameInDb("OWNER_ID")
    private long ownerId;

    @Index
    @NameInDb("OWNER")
    private String owner;

    @NameInDb("LOCALTION")
    private String localtion;

    @NameInDb("COLOR")
    private int color;

    @Backlink
    private ToMany<CalendarEventDBEntity> events;

    public long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(long ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getLocaltion() {
        return localtion;
    }

    public void setLocaltion(String localtion) {
        this.localtion = localtion;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public ToMany<CalendarEventDBEntity> getEvents() {
        return events;
    }

    public void setEvents(ToMany<CalendarEventDBEntity> events) {
        this.events = events;
    }
}
