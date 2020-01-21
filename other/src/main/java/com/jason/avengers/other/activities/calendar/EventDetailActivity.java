package com.jason.avengers.other.activities.calendar;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SwitchCompat;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.BasePickerView;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.jason.avengers.common.base.BaseActivity;
import com.jason.avengers.common.database.entity.other.calendar.CalendarEventDBEntity;
import com.jason.avengers.common.database.entity.other.calendar.CalendarOwnerDBEntity;
import com.jason.avengers.common.router.RouterPath;
import com.jason.avengers.common.widgets.inputfilter.CashierInputFilter;
import com.jason.avengers.other.R;
import com.jason.avengers.other.common.CalendarCommon;
import com.jason.avengers.other.presenters.EventPresenter;
import com.jason.avengers.other.views.EventView;
import com.jason.core.utils.SoftKeyboardUtils;
import com.jason.core.utils.ToastUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author Jason
 */
@Route(path = RouterPath.OTHER_CALENDAR_EVENT_DETAIL)
public class EventDetailActivity extends BaseActivity<EventPresenter, EventView>
        implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    public static final String PARAMS_START_TIME = "start_time";
    public static final String PARAMS_END_TIME = "end_time";
    public static final String PARAMS_OWNER = "owner";
    public static final String PARAMS_ID = "id";

    private List<String> mOwnerList;
    private List<String> mLevelList;
    private List<String> mCycleList;
    private List<String> mTimesList;

    private long mId;
    private boolean isAddMode;

    private Calendar mDateTime;
    private Calendar mStartTime;
    private Calendar mEndTime;

    private String mOwner;
    private String mLevel;
    private double mMoney;

    private String mCycle;
    private String mTimes;

    private TextView mDateView, mStartTimeView, mEndTimeView;

    private TextView mOwnerView, mLevelView, mMoneyView;

    private SwitchCompat mCycleSwitch;
    private View mCycleSwitchLabelView, mCycleLabelView, mTimesLabelView;
    private TextView mCycleView, mTimesView;
    private BasePickerView mPickerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent() == null) {
            finish();
        }
        setContentView(R.layout.other_activity_calendar_evet_detail);

        initData();
        initView();
    }

    private void initData() {
        mId = getIntent().getLongExtra(PARAMS_ID, 0);
        isAddMode = mId == 0;

        if (isAddMode) {
            initEditModeData();
        } else {
            initDetailModeData();
        }
    }

    private void initEditModeData() {
        mStartTime = (Calendar) getIntent().getSerializableExtra(PARAMS_START_TIME);
        mEndTime = (Calendar) getIntent().getSerializableExtra(PARAMS_END_TIME);
        mOwner = getIntent().getStringExtra(PARAMS_OWNER);

        mOwnerList = new ArrayList<>();
        mOwnerList.add(CalendarCommon.EMPTY);
        mOwnerList.addAll(getPresenter().getOwnerList());
        mLevelList = CalendarCommon.Levels;
        mCycleList = CalendarCommon.Cycles;
        mTimesList = CalendarCommon.Times;

        if (mStartTime == null || mEndTime == null) {
            mStartTime = Calendar.getInstance();
            mStartTime.set(Calendar.MINUTE, 0);
            mStartTime.set(Calendar.SECOND, 0);
            mStartTime.set(Calendar.MILLISECOND, 0);
            mEndTime = (Calendar) mStartTime.clone();
            mEndTime.add(Calendar.HOUR, 1);
        }

        if (TextUtils.isEmpty(mOwner)) {
            mOwner = mOwnerList.get(0);
        }
        if (TextUtils.isEmpty(mLevel)) {
            mLevel = mLevelList.get(0);
        }
        if (mMoney == 0) {
            mMoney = CalendarCommon.DEFAULT_MONEY;
        }

        if (TextUtils.isEmpty(mCycle)) {
            mCycle = mCycleList.get(0);
        }
        if (TextUtils.isEmpty(mTimes)) {
            mTimes = mTimesList.get(0);
        }

        mDateTime = (Calendar) mStartTime.clone();
        mDateTime.set(Calendar.HOUR_OF_DAY, 0);
        mDateTime.set(Calendar.MINUTE, 0);
        mDateTime.set(Calendar.SECOND, 0);
    }

    private void initDetailModeData() {
        CalendarEventDBEntity mEventDBEntity = getPresenter().getEventDBEntity(mId);

        mStartTime = Calendar.getInstance();
        mStartTime.setTime(mEventDBEntity.getStartTime());
        mEndTime = (Calendar) mStartTime.clone();
        mEndTime.setTime(mEventDBEntity.getEndTime());
        mLevel = mEventDBEntity.getLevel();
        mMoney = mEventDBEntity.getMoney();

        long ownerId = mEventDBEntity.getOwner().getTargetId();
        CalendarOwnerDBEntity ownerDBEntity = getPresenter().getOwnerDBEntity(ownerId);
        mOwner = ownerDBEntity.getOwner();

        mLevelList = CalendarCommon.Levels;

        mDateTime = (Calendar) mStartTime.clone();
        mDateTime.set(Calendar.HOUR_OF_DAY, 0);
        mDateTime.set(Calendar.MINUTE, 0);
        mDateTime.set(Calendar.SECOND, 0);
    }

    private void initView() {
        mDateView = findViewById(R.id.event_detail_date);
        mStartTimeView = findViewById(R.id.event_detail_start_time);
        mEndTimeView = findViewById(R.id.event_detail_end_time);

        mOwnerView = findViewById(R.id.event_detail_owner);
        mLevelView = findViewById(R.id.event_detail_level);
        mMoneyView = findViewById(R.id.event_detail_money);

        mCycleSwitchLabelView = findViewById(R.id.event_detail_cycle_switch_label);
        mCycleSwitch = findViewById(R.id.event_detail_cycle_switch);
        mCycleLabelView = findViewById(R.id.event_detail_cycle_label);
        mCycleView = findViewById(R.id.event_detail_cycle);
        mTimesLabelView = findViewById(R.id.event_detail_times_label);
        mTimesView = findViewById(R.id.event_detail_times);

        setViewData();
        setViewListener();
    }

    private void setViewData() {
        mDateView.setText(CalendarCommon.yyyyMMdd_SDF.format(mDateTime.getTime()));
        mStartTimeView.setText(CalendarCommon.HHmm_SDF.format(mStartTime.getTime()));
        mEndTimeView.setText(CalendarCommon.HHmm_SDF.format(mEndTime.getTime()));

        mOwnerView.setText(mOwner);
        mLevelView.setText(mLevel);
        mMoneyView.setText(String.valueOf(mMoney));

        if (isAddMode) {
            if (mCycleSwitch.isChecked()) {
                mCycleView.setVisibility(View.VISIBLE);
                mCycleLabelView.setVisibility(View.VISIBLE);
                mTimesView.setVisibility(View.VISIBLE);
                mTimesLabelView.setVisibility(View.VISIBLE);
            } else {
                mCycleView.setVisibility(View.GONE);
                mCycleLabelView.setVisibility(View.GONE);
                mTimesView.setVisibility(View.GONE);
                mTimesLabelView.setVisibility(View.GONE);
            }
            mCycleView.setText(mCycle);
            mTimesView.setText(mTimes);
        } else {
            mCycleSwitchLabelView.setVisibility(View.GONE);
            mCycleSwitch.setVisibility(View.GONE);
            mCycleView.setVisibility(View.GONE);
            mCycleLabelView.setVisibility(View.GONE);
            mTimesView.setVisibility(View.GONE);
            mTimesLabelView.setVisibility(View.GONE);
        }
    }

    private void setViewListener() {
        mDateView.setOnClickListener(this);
        mStartTimeView.setOnClickListener(this);
        mEndTimeView.setOnClickListener(this);

        if (isAddMode) {
            mOwnerView.setOnClickListener(this);
        }

        mLevelView.setOnClickListener(this);
        InputFilter[] filters = {new CashierInputFilter()};
        mMoneyView.setFilters(filters);
        mMoneyView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(mMoneyView.getText())) {
                    mMoney = 0;
                } else {
                    mMoney = Double.valueOf(mMoneyView.getText().toString());
                }
            }
        });

        if (isAddMode) {
            mCycleSwitch.setOnCheckedChangeListener(this);
            mCycleSwitch.setOnClickListener(this);
            mCycleView.setOnClickListener(this);
            mTimesView.setOnClickListener(this);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.calendar_event_detail, menu);
        if (isAddMode) {
            menu.removeItem(R.id.action_delete);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_submit) {
            submit();
            return true;
        } else if (id == R.id.action_delete) {
            new AlertDialog.Builder(EventDetailActivity.this)
                    .setTitle(R.string.other_dialog_title_alter)
                    .setMessage(getString(R.string.other_dialog_msg, item.getTitle()))
                    .setNegativeButton(R.string.other_dialog_negative_btn_label, null)
                    .setPositiveButton(R.string.other_dialog_positive_btn_label, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            delete();
                        }
                    })
                    .create().show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void submit() {
        if (isAddMode) {
            addModeSubmit();
        } else {
            detailModeSubmit();
        }
    }

    private void addModeSubmit() {
        if (!checkOwner()) {
            ToastUtils.showShortToast("请选择拥有者!");
            return;
        }

        CalendarOwnerDBEntity sourceEntity = getPresenter().buildOwnerDBEntity(mOwner);
        if (sourceEntity == null) {
            ToastUtils.showShortToast("请选择拥有者!");
            return;
        }

        getPresenter().addModeSubmit(sourceEntity, mDateTime, mStartTime, mEndTime,
                mLevel, mMoney, mTimes, mCycle, mCycleSwitch.isChecked());
        finish();
    }

    private boolean checkOwner() {
        return mOwnerList.indexOf(mOwner) > 0;
    }

    private void detailModeSubmit() {
        getPresenter().detailModeSubmit(mId, mDateTime, mStartTime, mEndTime, mLevel, mMoney);
        finish();
    }

    private void delete() {
        getPresenter().delete(mId);
        finish();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (mCycleSwitch.isChecked()) {
            mCycleView.setVisibility(View.VISIBLE);
            mCycleLabelView.setVisibility(View.VISIBLE);
            mTimesView.setVisibility(View.VISIBLE);
            mTimesLabelView.setVisibility(View.VISIBLE);
        } else {
            mCycleView.setVisibility(View.GONE);
            mCycleLabelView.setVisibility(View.GONE);
            mTimesView.setVisibility(View.GONE);
            mTimesLabelView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        SoftKeyboardUtils.hide(this);
        if (mPickerView != null) {
            mPickerView.dismiss();
        }
        int vid = v.getId();
        if (vid == R.id.event_detail_date) {
            mPickerView = buildDateOptionsPickerBuilder();
        } else if (vid == R.id.event_detail_start_time) {
            mPickerView = buildStartTimePickerBuilder();
        } else if (vid == R.id.event_detail_end_time) {
            mPickerView = buildEndTimePickerBuilder();
        } else if (vid == R.id.event_detail_owner) {
            mPickerView = buildOwnerOptionsPickerBuilder();
        } else if (vid == R.id.event_detail_level) {
            mPickerView = buildLevelOptionsPickerBuilder();
        } else if (vid == R.id.event_detail_cycle_switch) {
            mPickerView = null;
        } else if (vid == R.id.event_detail_cycle) {
            mPickerView = buildCycleOptionsPickerBuilder();
        } else if (vid == R.id.event_detail_times) {
            mPickerView = buildTimesOptionsPickerBuilder();
        }

        if (mPickerView != null) {
            mPickerView.show();
        }
    }

    private BasePickerView buildOwnerOptionsPickerBuilder() {
        OptionsPickerView<String> pickerView = new OptionsPickerBuilder(this,
                new OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int options2, int options3, View v) {
                        mOwner = mOwnerList.get(options1);
                        mOwnerView.setText(mOwner);
                    }
                })
                .build();
        pickerView.setPicker(mOwnerList);
        if (mOwnerList.contains(mOwner)) {
            pickerView.setSelectOptions(mOwnerList.indexOf(mOwner));
        }
        return pickerView;
    }

    private BasePickerView buildLevelOptionsPickerBuilder() {
        OptionsPickerView<String> pickerView = new OptionsPickerBuilder(this,
                new OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int options2, int options3, View v) {
                        mLevel = mLevelList.get(options1);
                        mLevelView.setText(mLevel);
                    }
                })
                .build();
        pickerView.setPicker(mLevelList);
        if (mLevelList.contains(mLevel)) {
            pickerView.setSelectOptions(mLevelList.indexOf(mLevel));
        }
        return pickerView;
    }

    private BasePickerView buildStartTimePickerBuilder() {
        TimePickerView pickerView = new TimePickerBuilder(this,
                new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        mStartTime.setTime(date);
                        mStartTimeView.setText(CalendarCommon.HHmm_SDF.format(mStartTime.getTime()));
                        if (!checkDate(date, mEndTime.getTime())) {
                            mEndTime = (Calendar) mStartTime.clone();
                            mEndTime.add(Calendar.HOUR, 1);
                            mEndTimeView.setText(CalendarCommon.HHmm_SDF.format(mEndTime.getTime()));
                        }
                    }
                })
                .setType(new boolean[]{false, false, false, true, true, false})
                .setDate(mStartTime)
                .build();
        return pickerView;
    }

    private BasePickerView buildEndTimePickerBuilder() {
        TimePickerView pickerView = new TimePickerBuilder(this,
                new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        mEndTime.setTime(date);
                        mEndTimeView.setText(CalendarCommon.HHmm_SDF.format(mEndTime.getTime()));
                        if (!checkDate(mStartTime.getTime(), date)) {
                            mStartTime = (Calendar) mEndTime.clone();
                            mStartTime.add(Calendar.HOUR, -1);
                            mStartTimeView.setText(CalendarCommon.HHmm_SDF.format(mStartTime.getTime()));
                        }
                    }
                })
                .setType(new boolean[]{false, false, false, true, true, false})
                .setDate(mEndTime)
                .build();
        return pickerView;
    }

    private BasePickerView buildCycleOptionsPickerBuilder() {
        OptionsPickerView<String> pickerView = new OptionsPickerBuilder(this,
                new OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int options2, int options3, View v) {
                        mCycle = mCycleList.get(options1);
                        mCycleView.setText(mCycle);
                    }
                })
                .build();
        pickerView.setPicker(mCycleList);
        if (mCycleList.contains(mCycle)) {
            pickerView.setSelectOptions(mCycleList.indexOf(mCycle));
        }
        return pickerView;
    }

    private BasePickerView buildTimesOptionsPickerBuilder() {
        OptionsPickerView<String> pickerView = new OptionsPickerBuilder(this,
                new OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int options2, int options3, View v) {
                        mTimes = mTimesList.get(options1);
                        mTimesView.setText(mTimes);
                    }
                })
                .build();
        pickerView.setPicker(mTimesList);
        if (mTimesList.contains(mTimes)) {
            pickerView.setSelectOptions(mTimesList.indexOf(mTimes));
        }
        return pickerView;
    }

    private BasePickerView buildDateOptionsPickerBuilder() {
        Calendar startDate = (Calendar) mDateTime.clone();
        startDate.set(Calendar.MONTH, 0);
        startDate.set(Calendar.DAY_OF_MONTH, 1);
        Calendar endDate = (Calendar) mDateTime.clone();
        endDate.add(Calendar.YEAR, 1);
        endDate.set(Calendar.MONTH, 11);
        endDate.set(Calendar.DAY_OF_MONTH, 21);
        TimePickerView pickerView = new TimePickerBuilder(this,
                new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        if (mDateTime == null) {
                            mDateTime = Calendar.getInstance();
                        }
                        mDateTime.setTime(date);
                        mDateView.setText(CalendarCommon.yyyyMMdd_SDF.format(mDateTime.getTime()));
                    }
                })
                .setType(new boolean[]{true, true, true, false, false, false})
                .setDate(mDateTime)
                .setRangDate(startDate, endDate)
                .build();
        return pickerView;
    }

    private boolean checkDate(Date start, Date end) {
        return start.before(end);
    }

    @Override
    protected EventPresenter initPresenter() {
        return new EventPresenter();
    }

    @Override
    protected EventView initAttachView() {
        return null;
    }
}
