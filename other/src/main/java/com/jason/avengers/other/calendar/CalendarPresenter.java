package com.jason.avengers.other.calendar;

import android.os.Build;

import com.jason.avengers.common.base.BasePresenter;
import com.jason.avengers.common.database.ObjectBoxBuilder;
import com.jason.avengers.common.database.entity.other.calendar.CalendarEventDBEntity;
import com.jason.avengers.common.database.entity.other.calendar.CalendarOwnerDBEntity;
import com.jason.weekview.WeekViewEvent;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.objectbox.Box;
import io.objectbox.relation.ToOne;

/**
 * @author Jason
 */
public class CalendarPresenter extends BasePresenter<CalendarView> {

    private CalendarView mView;
    private Box<CalendarEventDBEntity> mEventBox = ObjectBoxBuilder.INSTANCE.getBoxStore().boxFor(CalendarEventDBEntity.class);

    @Override
    protected void attach(CalendarView view) {
        mView = view;
    }

    @Override
    protected void detach() {
        mView = null;

        if (mEventBox != null) {
            mEventBox.closeThreadResources();
            mEventBox = null;
        }
    }

    public void queryData() {
        if (mEventBox != null) {
            List<CalendarEventDBEntity> entities = mEventBox.query().build().find();

            List<WeekViewEvent> weekViewEvents = new ArrayList<>();
            if (!entities.isEmpty()) {
                WeekViewEvent weekViewEvent;
                for (CalendarEventDBEntity entity : entities) {
                    ToOne<CalendarOwnerDBEntity> toOne = entity.getOwner();
                    if (toOne == null) {
                        continue;
                    }
                    CalendarOwnerDBEntity ownerDBEntity = toOne.getTarget();
                    if (ownerDBEntity == null) {
                        continue;
                    }

                    long id = entity.getId();
                    Calendar startTime = Calendar.getInstance();
                    startTime.setTime(entity.getStartTime());
                    Calendar endTime = Calendar.getInstance();
                    endTime.setTime(entity.getEndTime());
                    String title = CalendarCommon.buildTitle(entity, ownerDBEntity);
                    weekViewEvent = new WeekViewEvent(id, title, startTime, endTime);
                    int colorInt;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        colorInt = mView.getContext().getResources().getColor(ownerDBEntity.getColor(), null);
                    } else {
                        colorInt = mView.getContext().getResources().getColor(ownerDBEntity.getColor());
                    }
                    weekViewEvent.setColor(colorInt);
                    weekViewEvents.add(weekViewEvent);
                }
            }
            mView.notifyData(weekViewEvents);
        }
    }

    public void remove(WeekViewEvent event) {
        if (mEventBox != null) {
            mEventBox.remove(event.getId());
        }
    }

    public void cleanupData() {
        // 清理过期
        List<CalendarEventDBEntity> eventEntities = mEventBox.query().build().find();
        Date now = new Date();

        List<CalendarEventDBEntity> overdueEventEntities = CalendarCommon.findOverdueEventEntities(eventEntities, now);
        mEventBox.remove(overdueEventEntities);
    }

    public void clearData() {
        if (mEventBox != null) {
            mEventBox.removeAll();
        }
    }
}
