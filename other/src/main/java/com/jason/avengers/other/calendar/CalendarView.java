package com.jason.avengers.other.calendar;

import android.content.Context;

import com.jason.avengers.common.base.BaseView;
import com.jason.weekview.WeekViewEvent;

import java.util.List;

/**
 * @author Jason
 */
public interface CalendarView extends BaseView {

    Context getContext();

    void notifyData(List<WeekViewEvent> weekViewEventList);
}
