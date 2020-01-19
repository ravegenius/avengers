package com.jason.avengers.other.calendar.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.jason.avengers.common.base.BaseActivity;
import com.jason.avengers.common.database.ObjectBoxBuilder;
import com.jason.avengers.common.database.entity.other.calendar.CalendarEventDBEntity;
import com.jason.avengers.common.database.entity.other.calendar.CalendarOwnerDBEntity;
import com.jason.avengers.common.router.RouterPath;
import com.jason.avengers.other.R;
import com.jason.avengers.other.calendar.CalendarCommon;
import com.jason.avengers.other.calendar.CalendarPresenter;
import com.jason.avengers.other.calendar.CalendarView;
import com.jason.avengers.other.calendar.adapters.EventsAdapter;
import com.jason.avengers.other.calendar.beans.Event;
import com.jason.core.utils.SoftKeyboardUtils;

import java.util.Date;
import java.util.List;

import io.objectbox.Box;

/**
 * @author Jason
 */
@Route(path = RouterPath.OTHER_CALENDAR_OWNER_DETAIL)
public class OwnerDetailActivity extends BaseActivity<CalendarPresenter, CalendarView> {

    public static final String PARAMS_ID = "id";

    private Box<CalendarOwnerDBEntity> mOwnerBox = ObjectBoxBuilder.INSTANCE.getBoxStore().boxFor(CalendarOwnerDBEntity.class);
    private CalendarOwnerDBEntity mOwnerEntity;

    private long mId = 0;
    private int mQueryStyle = CalendarCommon.QUERY_STYLE_DEFAULT;
    private TextView mEditLabel;
    private Switch mEditSwitch;
    private EditText mNameView, mLocationView;
    private TextView mStatisticsView;
    private RecyclerView mEventsView;
    private EventsAdapter mEventsAdapter;
    private int mTotalEventCount, mDoneEventCount;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!getIntent().hasExtra(PARAMS_ID)) {
            finish();
        }
        mId = getIntent().getLongExtra(PARAMS_ID, 0);
        if (mId == 0) {
            finish();
        }

        setContentView(R.layout.other_activity_calendar_owner_detail);
        initView();
    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (mEditSwitch == null || !mEditSwitch.isChecked()) {
                return;
            }
            if (TextUtils.isEmpty(mNameView.getText()) || TextUtils.isEmpty(mLocationView.getText())) {
                return;
            }
            if (TextUtils.equals(mOwnerEntity.getOwner(), mNameView.getText())
                    && TextUtils.equals(mOwnerEntity.getLocaltion(), mLocationView.getText())) {
                return;
            }
            if (mOwnerEntity != null) {
                mOwnerEntity.setOwner(mNameView.getText().toString());
                mOwnerEntity.setLocaltion(mLocationView.getText().toString());
            }
            mOwnerBox.put(mOwnerEntity);
        }
    };

    private void initView() {
        mNameView = findViewById(R.id.owner_name);
        mNameView.setFocusable(false);
        mNameView.addTextChangedListener(textWatcher);
        mLocationView = findViewById(R.id.owner_location);
        mLocationView.setFocusable(false);
        mLocationView.addTextChangedListener(textWatcher);
        mStatisticsView = findViewById(R.id.owner_events_statistics);
        mEventsView = findViewById(R.id.owner_events);
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
        getMenuInflater().inflate(R.menu.calendar_owner_detail, menu);
        MenuItem switchItem = menu.findItem(R.id.action_edit);
        mEditLabel = switchItem.getActionView().findViewById(R.id.action_edit_label);
        mEditSwitch = switchItem.getActionView().findViewById(R.id.action_edit_switch);
        mEditSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mNameView.setFocusableInTouchMode(isChecked);
                mNameView.setFocusable(isChecked);
                mLocationView.setFocusableInTouchMode(isChecked);
                mLocationView.setFocusable(isChecked);
                if (isChecked) {
                    SoftKeyboardUtils.show(OwnerDetailActivity.this);
                } else {
                    SoftKeyboardUtils.hide(OwnerDetailActivity.this);
                }
                mEditLabel.setText(isChecked
                        ? R.string.other_owner_detail_menu_action_edit_on
                        : R.string.other_owner_detail_menu_action_edit_off);
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_edit_switch) {
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
        } else if (id == R.id.action_delete) {
            new AlertDialog.Builder(OwnerDetailActivity.this)
                    .setTitle("提示")
                    .setMessage("确认是否删除")
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mOwnerBox.remove(mId);
                            finish();
                        }
                    }).create().show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void queryData() {
        mTotalEventCount = mDoneEventCount = 0;
        mOwnerEntity = mOwnerBox.get(mId);
        mNameView.setText(mOwnerEntity.getOwner());
        mLocationView.setText(mOwnerEntity.getLocaltion());
        List<CalendarEventDBEntity> eventEntities = mOwnerEntity.getEvents();

        Date now = new Date();
        List<Event> events = CalendarCommon.buildEventsByEventEntities(eventEntities, null, mQueryStyle, now);
        if (events != null && !events.isEmpty()) {
            for (Event event : events) {
                if (!event.isGroup()) {
                    mTotalEventCount++;
                    if (event.isDone()) {
                        mDoneEventCount++;
                    }
                }
            }
        }

        mStatisticsView.setText(String.format("共计%d节课，已完成%d节课", mTotalEventCount, mDoneEventCount));
        mEventsAdapter.notifyData(events);
    }

    @Override
    protected CalendarPresenter initPresenter() {
        return null;
    }

    @Override
    protected CalendarView initAttachView() {
        return null;
    }
}
