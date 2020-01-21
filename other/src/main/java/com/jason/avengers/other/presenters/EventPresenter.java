package com.jason.avengers.other.presenters;

import android.text.TextUtils;

import com.jason.avengers.common.base.BasePresenter;
import com.jason.avengers.common.database.ObjectBoxBuilder;
import com.jason.avengers.common.database.entity.other.calendar.CalendarEventDBEntity;
import com.jason.avengers.common.database.entity.other.calendar.CalendarEventDBEntity_;
import com.jason.avengers.common.database.entity.other.calendar.CalendarOwnerDBEntity;
import com.jason.avengers.other.beans.EventBean;
import com.jason.avengers.other.common.CalendarCommon;
import com.jason.avengers.other.views.EventView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.objectbox.Box;
import io.objectbox.relation.ToOne;

/**
 * @author Jason
 */
public class EventPresenter extends BasePresenter<EventView> {

    private EventView mView;
    private Box<CalendarEventDBEntity> mEventBox = ObjectBoxBuilder.INSTANCE.getBoxStore().boxFor(CalendarEventDBEntity.class);
    private Box<CalendarOwnerDBEntity> mOwnerBox = ObjectBoxBuilder.INSTANCE.getBoxStore().boxFor(CalendarOwnerDBEntity.class);


    @Override
    protected void attach(EventView view) {
        mView = view;
    }

    @Override
    protected void detach() {
        mView = null;

        if (mEventBox != null) {
            mEventBox.closeThreadResources();
        }
    }

    public void queryData(int queryStyle) {
        Date now = new Date();
        List<CalendarEventDBEntity> eventEntities = mEventBox.query().build().find();
        List<CalendarOwnerDBEntity> ownerDBEntities = mOwnerBox.query().build().find();

        List<EventBean> eventBeans = CalendarCommon.buildEventsByEventEntities(eventEntities, ownerDBEntities, queryStyle, now);
        mView.notifyView(eventBeans);
    }

    public void removeOverdue() {
        Date now = new Date();
        List<CalendarEventDBEntity> eventEntities = mEventBox.query().build().find();
        List<CalendarEventDBEntity> overdueEventEntities = CalendarCommon.findOverdueEventEntities(eventEntities, now);
        mEventBox.remove(overdueEventEntities);
    }

    public List<String> getOwnerList() {
        List<String> ownerList = new ArrayList<>();
        List<CalendarOwnerDBEntity> entities = mOwnerBox.query().build().find();
        for (CalendarOwnerDBEntity entity : entities) {
            ownerList.add(entity.getOwner());
        }
        return ownerList;
    }

    public CalendarEventDBEntity getEventDBEntity(long id) {
        return mEventBox.get(id);
    }

    public CalendarOwnerDBEntity getOwnerDBEntity(long ownerId) {
        return mOwnerBox.get(ownerId);
    }

    public CalendarOwnerDBEntity buildOwnerDBEntity(String owner) {
        List<CalendarOwnerDBEntity> entities = mOwnerBox.query().build().find();
        CalendarOwnerDBEntity sourceEntity = null;
        for (CalendarOwnerDBEntity entity : entities) {
            if (TextUtils.equals(owner, entity.getOwner())) {
                sourceEntity = entity;
                break;
            }
        }
        return sourceEntity;
    }

    public void addModeSubmit(CalendarOwnerDBEntity sourceEntity, Calendar dateTime,
                              Calendar startTime, Calendar endTime, String level, double money,
                              String timeStr, String cycleStr, boolean isCycleChecked) {
        if (isCycleChecked) {
            List<CalendarEventDBEntity> entities = createMany(sourceEntity, dateTime, startTime, endTime,
                    level, money, timeStr, cycleStr);
            if (entities != null && !entities.isEmpty()) {
                mEventBox.put(entities);
            }
        } else {
            CalendarEventDBEntity entity = createOne(sourceEntity, dateTime, startTime, endTime,
                    level, money);
            if (entity != null) {
                mEventBox.put(entity);
            }
        }
    }

    private List<CalendarEventDBEntity> createMany(CalendarOwnerDBEntity sourceEntity, Calendar dateTime,
                                                   Calendar startTime, Calendar endTime, String level, double money,
                                                   String timeStr, String cycleStr) {
        int times = CalendarCommon.Times.indexOf(timeStr);
        if (times < 0) {
            return null;
        }

        times += 2;
        List<CalendarEventDBEntity> entities = null;
        CalendarEventDBEntity entity;
        for (int index = 0; index < times; index++) {
            if (index > 0) {
                if (CalendarCommon.EACH_DAY.equals(cycleStr)) {
                    dateTime.add(Calendar.DAY_OF_MONTH, 1);
                } else if (CalendarCommon.EACH_WEEK.equals(cycleStr)) {
                    dateTime.add(Calendar.WEEK_OF_MONTH, 1);
                } else if (CalendarCommon.EACH_MONTH.equals(cycleStr)) {
                    dateTime.add(Calendar.MONTH, 1);
                }
            }
            entity = createOne(sourceEntity, dateTime, startTime, endTime, level, money);
            if (entities == null) {
                entities = new ArrayList<>();
            }
            entities.add(entity);
        }
        return entities;
    }

    private CalendarEventDBEntity createOne(CalendarOwnerDBEntity sourceEntity, Calendar dateTime,
                                            Calendar startTime, Calendar endTime, String level, double money) {
        Calendar newStartTime = (Calendar) dateTime.clone();
        newStartTime.set(Calendar.HOUR_OF_DAY, startTime.get(Calendar.HOUR_OF_DAY));
        newStartTime.set(Calendar.MINUTE, startTime.get(Calendar.MINUTE));

        Calendar newEndTime = (Calendar) dateTime.clone();
        newEndTime.set(Calendar.HOUR_OF_DAY, endTime.get(Calendar.HOUR_OF_DAY));
        newEndTime.set(Calendar.MINUTE, endTime.get(Calendar.MINUTE));

        CalendarEventDBEntity entity = new CalendarEventDBEntity();
        entity.setStartTime(newStartTime.getTime());
        entity.setEndTime(newEndTime.getTime());
        entity.setLevel(level);
        entity.setMoney(money);
        ToOne<CalendarOwnerDBEntity> toOne = new ToOne<>(entity, CalendarEventDBEntity_.owner);
        toOne.setTarget(sourceEntity);
        entity.setOwner(toOne);
        return entity;
    }

    public void detailModeSubmit(long id, Calendar dateTime, Calendar startTime, Calendar endTime, String level, double money) {
        Calendar newStartTime = (Calendar) dateTime.clone();
        newStartTime.set(Calendar.HOUR_OF_DAY, startTime.get(Calendar.HOUR_OF_DAY));
        newStartTime.set(Calendar.MINUTE, startTime.get(Calendar.MINUTE));

        Calendar newEndTime = (Calendar) dateTime.clone();
        newEndTime.set(Calendar.HOUR_OF_DAY, endTime.get(Calendar.HOUR_OF_DAY));
        newEndTime.set(Calendar.MINUTE, endTime.get(Calendar.MINUTE));

        CalendarEventDBEntity eventDBEntity = getEventDBEntity(id);
        eventDBEntity.setStartTime(newStartTime.getTime());
        eventDBEntity.setEndTime(newEndTime.getTime());
        eventDBEntity.setLevel(level);
        eventDBEntity.setMoney(money);

        mEventBox.put(eventDBEntity);
    }

    public void delete(long id) {
        mEventBox.remove(id);
    }
}
