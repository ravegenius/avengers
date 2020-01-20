package com.jason.avengers.other.activities.calendar;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.SwitchCompat;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
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
import com.jason.avengers.common.database.ObjectBoxBuilder;
import com.jason.avengers.common.database.entity.other.calendar.CalendarEventDBEntity;
import com.jason.avengers.common.database.entity.other.calendar.CalendarEventDBEntity_;
import com.jason.avengers.common.database.entity.other.calendar.CalendarOwnerDBEntity;
import com.jason.avengers.common.router.RouterPath;
import com.jason.avengers.common.widgets.inputfilter.CashierInputFilter;
import com.jason.avengers.other.R;
import com.jason.avengers.other.common.CalendarCommon;
import com.jason.avengers.other.presenters.CalendarPresenter;
import com.jason.avengers.other.views.CalendarView;
import com.jason.core.utils.ToastUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.objectbox.Box;
import io.objectbox.relation.ToOne;

/**
 * @author Jason
 */
@Route(path = RouterPath.OTHER_CALENDAR_EVENT_ADD)
public class EventAddActivity extends BaseActivity<CalendarPresenter, CalendarView>
        implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    public static final String PARAMS_START_TIME = "start_time";
    public static final String PARAMS_END_TIME = "end_time";
    public static final String PARAMS_OWNER = "owner";
    public static final String PARAMS_CYCLE = "cycle";
    public static final String PARAMS_TIMES = "times";

    private Box<CalendarOwnerDBEntity> mOwnerBox = ObjectBoxBuilder.INSTANCE.getBoxStore().boxFor(CalendarOwnerDBEntity.class);
    private Box<CalendarEventDBEntity> mEventBox = ObjectBoxBuilder.INSTANCE.getBoxStore().boxFor(CalendarEventDBEntity.class);

    private List<String> mOwnerList;
    private List<String> mLevelList;
    private List<String> mCycleList;
    private List<String> mTimesList;

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
    private View mCycleLabelView, mTimesLabelView;
    private TextView mCycleView, mTimesView;

    private Button mSubmitView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent() == null) {
            finish();
        }
        setContentView(R.layout.other_activity_calendar_add_evet);

        initData();
        initView();
    }

    private void initData() {
        mOwnerList = new ArrayList<>();
        mOwnerList.add(CalendarCommon.EMPTY);
        List<CalendarOwnerDBEntity> entities = mOwnerBox.query().build().find();
        for (CalendarOwnerDBEntity entity : entities) {
            mOwnerList.add(entity.getOwner());
        }
        mLevelList = CalendarCommon.Levels;
        mCycleList = CalendarCommon.Cycles;
        mTimesList = CalendarCommon.Times;

        mStartTime = (Calendar) getIntent().getSerializableExtra(PARAMS_START_TIME);
        mEndTime = (Calendar) getIntent().getSerializableExtra(PARAMS_END_TIME);
        mOwner = getIntent().getStringExtra(PARAMS_OWNER);
        mCycle = getIntent().getStringExtra(PARAMS_CYCLE);
        mTimes = getIntent().getStringExtra(PARAMS_TIMES);

        mDateTime = (Calendar) mStartTime.clone();
        mDateTime.set(Calendar.HOUR_OF_DAY, 0);
        mDateTime.set(Calendar.MINUTE, 0);
        mDateTime.set(Calendar.SECOND, 0);

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
    }

    private void initView() {
        mDateView = findViewById(R.id.calendar_event_date);
        mStartTimeView = findViewById(R.id.calendar_event_start_time);
        mEndTimeView = findViewById(R.id.calendar_event_end_time);

        mOwnerView = findViewById(R.id.calendar_event_owner);
        mLevelView = findViewById(R.id.calendar_event_level);
        mMoneyView = findViewById(R.id.calendar_event_money);

        mCycleSwitch = findViewById(R.id.calendar_event_cycle_switch);
        mCycleLabelView = findViewById(R.id.calendar_event_cycle_label);
        mCycleView = findViewById(R.id.calendar_event_cycle);
        mTimesLabelView = findViewById(R.id.calendar_event_times_label);
        mTimesView = findViewById(R.id.calendar_event_times);

        mSubmitView = findViewById(R.id.calendar_event_submit);

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
    }

    private void setViewListener() {
        mDateView.setOnClickListener(this);
        mStartTimeView.setOnClickListener(this);
        mEndTimeView.setOnClickListener(this);

        mOwnerView.setOnClickListener(this);
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

        mCycleSwitch.setOnCheckedChangeListener(this);
        mCycleView.setOnClickListener(this);
        mTimesView.setOnClickListener(this);

        mSubmitView.setOnClickListener(this);
    }

    @Override
    protected CalendarPresenter initPresenter() {
        return null;
    }

    @Override
    protected CalendarView initAttachView() {
        return null;
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
        BasePickerView pickerView = null;
        int vid = v.getId();
        if (vid == R.id.calendar_event_date) {
            pickerView = buildDateOptionsPickerBuilder();
        } else if (vid == R.id.calendar_event_start_time) {
            pickerView = buildStartTimePickerBuilder();
        } else if (vid == R.id.calendar_event_end_time) {
            pickerView = buildEndTimePickerBuilder();
        } else if (vid == R.id.calendar_event_owner) {
            pickerView = buildOwnerOptionsPickerBuilder();
        } else if (vid == R.id.calendar_event_level) {
            pickerView = buildLevelOptionsPickerBuilder();
        } else if (vid == R.id.calendar_event_cycle) {
            pickerView = buildCycleOptionsPickerBuilder();
        } else if (vid == R.id.calendar_event_times) {
            pickerView = buildTimesOptionsPickerBuilder();
        } else if (vid == R.id.calendar_event_submit) {
            submit();
            return;
        }

        if (pickerView != null) {
            pickerView.show();
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
                .setItemVisibleCount(4)
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
                .setItemVisibleCount(4)
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
                        if (!checkDate(date, mEndTime.getTime())) {
                            ToastUtils.showShortToast("开始时间不能晚于结束时间!");
                            return;
                        }

                        if (mStartTime == null) {
                            mStartTime = Calendar.getInstance();
                        }
                        mStartTime.setTime(date);
                        mStartTimeView.setText(CalendarCommon.HHmm_SDF.format(mStartTime.getTime()));
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
                        if (!checkDate(mStartTime.getTime(), date)) {
                            ToastUtils.showShortToast("开始时间不能晚于结束时间!");
                            return;
                        }
                        if (mEndTime == null) {
                            mEndTime = Calendar.getInstance();
                        }
                        mEndTime.setTime(date);
                        mEndTimeView.setText(CalendarCommon.HHmm_SDF.format(mEndTime.getTime()));
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
                .setItemVisibleCount(4)
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
                .setItemVisibleCount(4)
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

    private void submit() {
        if (!checkOwner()) {
            ToastUtils.showShortToast("请选择拥有者!");
            return;
        }

        CalendarOwnerDBEntity sourceEntity = buildOwnerDBEntity();
        if (sourceEntity == null) {
            ToastUtils.showShortToast("请选择拥有者!");
            return;
        }

        Calendar dateTime = (Calendar) mDateTime.clone();
        if (mCycleSwitch.isChecked()) {
            List<CalendarEventDBEntity> entities = createMany(sourceEntity, dateTime);
            if (entities != null && !entities.isEmpty()) {
                mEventBox.put(entities);
            }
        } else {
            CalendarEventDBEntity entity = createOne(sourceEntity, dateTime);
            if (entity != null) {
                mEventBox.put(entity);
            }
        }
        finish();
    }

    private CalendarOwnerDBEntity buildOwnerDBEntity() {
        List<CalendarOwnerDBEntity> entities = mOwnerBox.query().build().find();
        CalendarOwnerDBEntity sourceEntity = null;
        for (CalendarOwnerDBEntity entity : entities) {
            if (TextUtils.equals(mOwner, entity.getOwner())) {
                sourceEntity = entity;
                break;
            }
        }
        return sourceEntity;
    }

    private List<CalendarEventDBEntity> createMany(CalendarOwnerDBEntity sourceEntity, Calendar dateTime) {
        int times = CalendarCommon.Times.indexOf(mTimes);
        if (times < 0) {
            return null;
        }

        times += 2;
        List<CalendarEventDBEntity> entities = null;
        CalendarEventDBEntity entity;
        for (int index = 0; index < times; index++) {
            if (index > 0) {
                if (CalendarCommon.EACH_DAY.equals(mCycle)) {
                    dateTime.add(Calendar.DAY_OF_MONTH, 1);
                } else if (CalendarCommon.EACH_WEEK.equals(mCycle)) {
                    dateTime.add(Calendar.WEEK_OF_MONTH, 1);
                } else if (CalendarCommon.EACH_MONTH.equals(mCycle)) {
                    dateTime.add(Calendar.MONTH, 1);
                }
            }
            entity = createOne(sourceEntity, dateTime);
            if (entities == null) {
                entities = new ArrayList<>();
            }
            entities.add(entity);
        }
        return entities;
    }

    private CalendarEventDBEntity createOne(CalendarOwnerDBEntity sourceEntity, Calendar dateTime) {
        Calendar startTime = (Calendar) dateTime.clone();
        startTime.set(Calendar.HOUR_OF_DAY, mStartTime.get(Calendar.HOUR_OF_DAY));
        startTime.set(Calendar.MINUTE, mStartTime.get(Calendar.MINUTE));

        Calendar endTime = (Calendar) dateTime.clone();
        endTime.set(Calendar.HOUR_OF_DAY, mEndTime.get(Calendar.HOUR_OF_DAY));
        endTime.set(Calendar.MINUTE, mEndTime.get(Calendar.MINUTE));

        CalendarEventDBEntity entity = new CalendarEventDBEntity();
        entity.setStartTime(startTime.getTime());
        entity.setEndTime(endTime.getTime());
        entity.setLevel(mLevel);
        entity.setMoney(mMoney);
        ToOne<CalendarOwnerDBEntity> toOne = new ToOne<>(entity, CalendarEventDBEntity_.owner);
        toOne.setTarget(sourceEntity);
        entity.setOwner(toOne);
        return entity;
    }

    private boolean checkOwner() {
        return mOwnerList.indexOf(mOwner) > 0;
    }
}
