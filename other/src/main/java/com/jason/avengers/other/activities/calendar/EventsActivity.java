package com.jason.avengers.other.activities.calendar;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.jason.avengers.common.base.BaseActivity;
import com.jason.avengers.common.router.RouterBuilder;
import com.jason.avengers.common.router.RouterPath;
import com.jason.avengers.other.R;
import com.jason.avengers.other.adapters.EventsAdapter;
import com.jason.avengers.other.beans.EventBean;
import com.jason.avengers.other.common.CalendarCommon;
import com.jason.avengers.other.holders.EventHolder;
import com.jason.avengers.other.listeners.EventClickListener;
import com.jason.avengers.other.presenters.EventPresenter;
import com.jason.avengers.other.views.EventView;

import java.util.List;

/**
 * @author Jason
 */
@Route(path = RouterPath.OTHER_CALENDAR_EVENT)
public class EventsActivity extends BaseActivity<EventPresenter, EventView> implements EventView {

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
        mEventsView = findViewById(R.id.events_view);
        mEventsView.setLayoutManager(new LinearLayoutManager(this));
        mEventsView.setAdapter(mEventsAdapter = new EventsAdapter(getLayoutInflater(),
                new EventClickListener() {
                    @Override
                    public void onEventClickListener(EventHolder holder, View view) {
                        if (holder == null
                                || holder.eventBean == null
                                || holder.eventBean.getId() <= 0) {
                            return;
                        }
                        RouterBuilder.INSTANCE.build(RouterPath.OTHER_CALENDAR_EVENT_DETAIL)
                                .withLong(EventDetailActivity.PARAMS_ID, holder.eventBean.getId())
                                .navigation(EventsActivity.this);
                    }

                    @Override
                    public void onOwnerClickListener(EventHolder holder, View view) {
                        if (holder == null
                                || holder.eventBean == null
                                || holder.eventBean.getOwnerBean() == null
                                || holder.eventBean.getOwnerBean().getId() <= 0) {
                            return;
                        }
                        RouterBuilder.INSTANCE.build(RouterPath.OTHER_CALENDAR_OWNER_DETAIL)
                                .withLong(OwnerDetailActivity.PARAMS_ID, holder.eventBean.getOwnerBean().getId())
                                .navigation(EventsActivity.this);
                    }
                }));
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
            RouterBuilder.INSTANCE.build(RouterPath.OTHER_CALENDAR_EVENT_DETAIL)
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
                    .setTitle(R.string.other_dialog_title_alter)
                    .setMessage(getString(R.string.other_dialog_msg, item.getTitle()))
                    .setNegativeButton(R.string.other_dialog_negative_btn_label, null)
                    .setPositiveButton(R.string.other_dialog_positive_btn_label, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            getPresenter().cleanup();
                            queryData();
                        }
                    })
                    .create().show();
            return true;
        } else if (id == R.id.action_clear) {
            new AlertDialog.Builder(EventsActivity.this)
                    .setTitle(R.string.other_dialog_title_alter)
                    .setMessage(getString(R.string.other_dialog_msg, item.getTitle()))
                    .setNegativeButton(R.string.other_dialog_negative_btn_label, null)
                    .setPositiveButton(R.string.other_dialog_positive_btn_label, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            getPresenter().clear();
                            queryData();
                        }
                    })
                    .create().show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void queryData() {
        if (getPresenter() != null) {
            getPresenter().queryData(mQueryStyle);
        }
    }

    @Override
    protected EventPresenter initPresenter() {
        return new EventPresenter();
    }

    @Override
    protected EventView initAttachView() {
        return this;
    }

    @Override
    public void notifyView(List<EventBean> eventBeans) {
        mEventsAdapter.notifyData(eventBeans);
    }
}
