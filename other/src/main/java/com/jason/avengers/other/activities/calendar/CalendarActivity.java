package com.jason.avengers.other.activities.calendar;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.jason.avengers.common.base.BaseActivity;
import com.jason.avengers.common.router.RouterBuilder;
import com.jason.avengers.common.router.RouterPath;
import com.jason.avengers.other.R;
import com.jason.avengers.other.presenters.CalendarPresenter;
import com.jason.avengers.other.views.CalendarView;
import com.jason.weekview.DateTimeInterpreter;
import com.jason.weekview.MonthLoader;
import com.jason.weekview.WeekView;
import com.jason.weekview.WeekViewEvent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * @author Jason
 */
@Route(path = RouterPath.OTHER_CALENDAR)
public class CalendarActivity extends BaseActivity<CalendarPresenter, CalendarView>
        implements CalendarView,
        WeekView.EventLongPressListener, WeekView.EmptyViewLongPressListener,
        WeekView.EventClickListener, MonthLoader.MonthChangeListener {

    private static final int TYPE_ONE_DAY_VIEW = 1;
    private static final int TYPE_THREE_DAY_VIEW = 3;
    private static final int TYPE_WEEK_VIEW = 7;
    private int mWeekViewType = TYPE_THREE_DAY_VIEW;

    private WeekView mWeekView;
    private int mWeekViewColumnGap;
    private int mWeekViewTextSize10, mWeekViewTextSize12;
    private List<WeekViewEvent> mEvents = new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mWeekViewColumnGap = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics());
        mWeekViewTextSize10 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics());
        mWeekViewTextSize12 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics());

        setContentView(R.layout.other_activity_calendar);

        // Get a reference for the week view in the layout.
        mWeekView = findViewById(R.id.weekView);

        // Show a toast message about the touched event.
        mWeekView.setOnEventClickListener(this);

        // The week view has infinite scrolling horizontally. We have to provide the events of a
        // month every time the month changes on the week view.
        mWeekView.setMonthChangeListener(this);

        // Set long press listener for events.
        mWeekView.setEventLongPressListener(this);

        // Set long press listener for empty view
        mWeekView.setEmptyViewLongPressListener(this);

        mWeekView.setColumnGap(mWeekViewColumnGap);

        // Set up a date time interpreter to interpret how the date and time will be formatted in
        // the week view. This is optional.
        setupDateTimeInterpreter(false);
    }

    /**
     * Set up a date time interpreter which will show short date values when in week view and long
     * date values otherwise.
     *
     * @param shortDate True if the date values should be short.
     */
    private void setupDateTimeInterpreter(final boolean shortDate) {
        mWeekView.setDateTimeInterpreter(new DateTimeInterpreter() {
            @Override
            public String interpretDate(Calendar date) {
                SimpleDateFormat weekdayNameFormat = new SimpleDateFormat("EEE", Locale.getDefault());
                String weekday = weekdayNameFormat.format(date.getTime());
                SimpleDateFormat format = new SimpleDateFormat("M/d", Locale.getDefault());

                // All android api level do not have a standard way of getting the first letter of
                // the week day name. Hence we get the first char programmatically.
                // Details: http://stackoverflow.com/questions/16959502/get-one-letter-abbreviation-of-week-day-of-a-date-in-java#answer-16959657
                if (shortDate) {
                    weekday = String.valueOf(weekday.charAt(0));
                }
                return weekday.toUpperCase() + format.format(date.getTime());
            }

            @Override
            public String interpretTime(int hour) {
                if (hour >= 2 && hour < 6) {
                    return "凌晨" + hour;
                } else if (hour >= 6 && hour < 11) {
                    return "上午" + hour;
                } else if (hour >= 11 && hour < 14) {
                    return "中午" + hour;
                } else if (hour >= 14 && hour < 19) {
                    return "下午" + hour;
                } else if (hour >= 19 && hour < 23) {
                    return "晚间" + hour;
                } else {
                    return "半夜" + hour;
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.calendar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        setupDateTimeInterpreter(id == R.id.action_week_view);

        if (id == R.id.action_owners) {
            RouterBuilder.INSTANCE.build(RouterPath.OTHER_CALENDAR_OWNER)
                    .navigation(this);
            return true;
        } else if (id == R.id.action_events) {
            RouterBuilder.INSTANCE.build(RouterPath.OTHER_CALENDAR_EVENT)
                    .navigation(this);
            return true;
        } else if (id == R.id.action_cleanup) {
            new AlertDialog.Builder(CalendarActivity.this)
                    .setTitle(R.string.other_dialog_title_alter)
                    .setMessage(getString(R.string.other_dialog_msg, item.getTitle()))
                    .setNegativeButton(R.string.other_dialog_negative_btn_label, null)
                    .setPositiveButton(R.string.other_dialog_positive_btn_label, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            getPresenter().cleanupData();
                        }
                    })
                    .create().show();
            return true;
        } else if (id == R.id.action_clear) {
            new AlertDialog.Builder(CalendarActivity.this)
                    .setTitle(R.string.other_dialog_title_alter)
                    .setMessage(getString(R.string.other_dialog_msg, item.getTitle()))
                    .setNegativeButton(R.string.other_dialog_negative_btn_label, null)
                    .setPositiveButton(R.string.other_dialog_positive_btn_label, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            getPresenter().clearData();
                        }
                    })
                    .create().show();
            return true;
        } else if (id == R.id.action_today) {
            mWeekView.goToToday();
            return true;
        } else if (id == R.id.action_day_view) {
            if (mWeekViewType != TYPE_ONE_DAY_VIEW) {
                item.setChecked(!item.isChecked());
                mWeekViewType = TYPE_ONE_DAY_VIEW;
                mWeekView.setNumberOfVisibleDays(TYPE_ONE_DAY_VIEW);

                // Lets change some dimensions to best fit the view.
                mWeekView.setTextSize(mWeekViewTextSize12);
                mWeekView.setEventTextSize(mWeekViewTextSize12);
            }
            return true;
        } else if (id == R.id.action_three_day_view) {
            if (mWeekViewType != TYPE_THREE_DAY_VIEW) {
                item.setChecked(!item.isChecked());
                mWeekViewType = TYPE_THREE_DAY_VIEW;
                mWeekView.setNumberOfVisibleDays(TYPE_THREE_DAY_VIEW);

                // Lets change some dimensions to best fit the view.
                mWeekView.setTextSize(mWeekViewTextSize12);
                mWeekView.setEventTextSize(mWeekViewTextSize12);
            }
            return true;
        } else if (id == R.id.action_week_view) {
            if (mWeekViewType != TYPE_WEEK_VIEW) {
                item.setChecked(!item.isChecked());
                mWeekViewType = TYPE_WEEK_VIEW;
                mWeekView.setNumberOfVisibleDays(TYPE_WEEK_VIEW);

                // Lets change some dimensions to best fit the view.
                mWeekView.setTextSize(mWeekViewTextSize10);
                mWeekView.setEventTextSize(mWeekViewTextSize10);
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getPresenter().queryData();
    }

    @Override
    protected CalendarPresenter initPresenter() {
        return new CalendarPresenter();
    }

    @Override
    protected CalendarView initAttachView() {
        return this;
    }

    @Override
    public List<? extends WeekViewEvent> onMonthChange(int newYear, int newMonth) {
        // Return only the events that matches newYear and newMonth.
        List<WeekViewEvent> matchedEvents = new ArrayList<WeekViewEvent>();
        for (WeekViewEvent event : mEvents) {
            if (eventMatches(event, newYear, newMonth)) {
                matchedEvents.add(event);
            }
        }
        return matchedEvents;
    }

    /**
     * Checks if an event falls into a specific year and month.
     *
     * @param event The event to check for.
     * @param year  The year.
     * @param month The month.
     * @return True if the event matches the year and month.
     */
    private boolean eventMatches(WeekViewEvent event, int year, int month) {
        return (event.getStartTime().get(Calendar.YEAR) == year && event.getStartTime().get(Calendar.MONTH) == month - 1) || (event.getEndTime().get(Calendar.YEAR) == year && event.getEndTime().get(Calendar.MONTH) == month - 1);
    }

    @Override
    public void onEventClick(WeekViewEvent event, RectF eventRect) {
        new AlertDialog.Builder(CalendarActivity.this)
                .setTitle(R.string.other_dialog_title_alter)
                .setMessage(event.getName())
                .setPositiveButton(R.string.other_dialog_positive_btn_label, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .create().show();
    }

    @Override
    public void onEventLongPress(final WeekViewEvent event, RectF eventRect) {
        new AlertDialog.Builder(CalendarActivity.this)
                .setTitle(R.string.other_dialog_title_alter)
                .setMessage(getString(R.string.other_dialog_msg, getString(R.string.app_delete)))
                .setNegativeButton(R.string.other_dialog_negative_btn_label, null)
                .setPositiveButton(R.string.other_dialog_positive_btn_label, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mEvents.remove(event);
                        mWeekView.notifyDatasetChanged();
                        getPresenter().remove(event);
                    }
                })
                .create().show();
    }

    @Override
    public void onEmptyViewLongPress(Calendar calendar) {
        Calendar startTime = (Calendar) calendar.clone();
        startTime.set(Calendar.MINUTE, 0);
        Calendar endTime = (Calendar) calendar.clone();
        endTime.set(Calendar.MINUTE, 0);
        endTime.add(Calendar.HOUR, 1);

        RouterBuilder.INSTANCE.build(RouterPath.OTHER_CALENDAR_EVENT_ADD)
                .withSerializable(EventAddActivity.PARAMS_START_TIME, startTime)
                .withSerializable(EventAddActivity.PARAMS_END_TIME, endTime)
                .navigation(this);
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void notifyData(List<WeekViewEvent> weekViewEventList) {
        mEvents = weekViewEventList;
        mWeekView.notifyDatasetChanged();
    }
}
