package com.jason.avengers.other.calendar.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.jason.avengers.common.base.BaseActivity;
import com.jason.avengers.common.base.BasePresenter;
import com.jason.avengers.common.base.BaseView;
import com.jason.avengers.common.database.ObjectBoxBuilder;
import com.jason.avengers.common.database.entity.other.calendar.CalendarEventDBEntity;
import com.jason.avengers.common.database.entity.other.calendar.CalendarOwnerDBEntity;
import com.jason.avengers.common.router.RouterBuilder;
import com.jason.avengers.common.router.RouterPath;
import com.jason.avengers.other.R;
import com.jason.avengers.other.calendar.CalendarCommon;
import com.jason.avengers.other.calendar.adapters.EventsAdapter;
import com.jason.avengers.other.calendar.beans.Event;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.objectbox.Box;

/**
 * @author Jason
 */
@Route(path = RouterPath.OTHER_CALENDAR_EVENT)
public class EventsActivity extends BaseActivity {

    private Box<CalendarEventDBEntity> mEventBox = ObjectBoxBuilder.INSTANCE.getBoxStore().boxFor(CalendarEventDBEntity.class);
    private Box<CalendarOwnerDBEntity> mOwnerBox = ObjectBoxBuilder.INSTANCE.getBoxStore().boxFor(CalendarOwnerDBEntity.class);

    private int mQueryStyle = CalendarCommon.QUERY_STYLE_DEFAULT;

    private RecyclerView mEventsView;
    private EventsAdapter mEventsAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.other_activity_calendar_events);

        initView();
    }

    private void initView() {
        mEventsView = findViewById(R.id.calendar_events_view);

        mEventsView.setLayoutManager(new LinearLayoutManager(this));
        mEventsView.setAdapter(mEventsAdapter = new EventsAdapter(getLayoutInflater()));
    }

    @Override
    protected void onResume() {
        super.onResume();
        queryData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.calendar_events, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_add) {
            Calendar startTime = Calendar.getInstance();
            startTime.set(Calendar.MINUTE, 0);
            startTime.set(Calendar.SECOND, 0);
            startTime.set(Calendar.MILLISECOND, 0);
            Calendar endTime = (Calendar) startTime.clone();
            endTime.add(Calendar.HOUR, 1);

            RouterBuilder.INSTANCE.build(RouterPath.OTHER_CALENDAR_EVENT_ADD)
                    .withSerializable(EventAddActivity.PARAMS_START_TIME, startTime)
                    .withSerializable(EventAddActivity.PARAMS_END_TIME, endTime)
                    .navigation(this);
            return true;
        } else if (id == R.id.action_default_view) {
            if (mQueryStyle != CalendarCommon.QUERY_STYLE_DEFAULT) {
                mQueryStyle = CalendarCommon.QUERY_STYLE_DEFAULT;
                item.setChecked(!item.isChecked());
                queryData();
            }
            return true;
        } else if (id == R.id.action_day_view) {
            if (mQueryStyle != CalendarCommon.QUERY_STYLE_DAY) {
                mQueryStyle = CalendarCommon.QUERY_STYLE_DAY;
                item.setChecked(!item.isChecked());
                queryData();
            }
            return true;
        } else if (id == R.id.action_week_view) {
            if (mQueryStyle != CalendarCommon.QUERY_STYLE_WEEK) {
                mQueryStyle = CalendarCommon.QUERY_STYLE_WEEK;
                item.setChecked(!item.isChecked());
                queryData();
            }
            return true;
        } else if (id == R.id.action_month_view) {
            if (mQueryStyle != CalendarCommon.QUERY_STYLE_MONTH) {
                mQueryStyle = CalendarCommon.QUERY_STYLE_MONTH;
                item.setChecked(!item.isChecked());
                queryData();
            }
            return true;
        } else if (id == R.id.action_cleanup) {
            new AlertDialog.Builder(EventsActivity.this)
                    .setTitle("提示")
                    .setMessage("确认是否清理过期")
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            List<CalendarEventDBEntity> eventEntities = mEventBox.query().build().find();
                            Date now = new Date();

                            List<CalendarEventDBEntity> overdueEventEntities = CalendarCommon.findOverdueEventEntities(eventEntities, now);
                            mEventBox.remove(overdueEventEntities);
                        }
                    }).create().show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void queryData() {
        List<CalendarEventDBEntity> eventEntities = mEventBox.query().build().find();
        List<CalendarOwnerDBEntity> ownerDBEntities = mOwnerBox.query().build().find();

        Date now = new Date();
        List<Event> events = CalendarCommon.buildEventsByEventEntities(eventEntities, ownerDBEntities, mQueryStyle, now);
        mEventsAdapter.notifyData(events);
    }

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected BaseView initAttachView() {
        return null;
    }
}
