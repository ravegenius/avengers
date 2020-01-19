package com.jason.avengers.accessibility.common;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 用户信息
 *
 * @author Jason
 */
public enum AccountInfo {
    /**
     * 单例
     */
    INFO;

    public static final String PASSPORT = "13401073452";
    public static final String PASSWORD = "RGjason8513053";

    public static final List<Integer> WORK_DAYS = new ArrayList<>();

    static {
        WORK_DAYS.add(1);
        WORK_DAYS.add(2);
        WORK_DAYS.add(3);
        WORK_DAYS.add(4);
        WORK_DAYS.add(5);
        WORK_DAYS.add(6);
    }

    public static final List<Integer> INIT_TIMES = new ArrayList<>();

    static {
        INIT_TIMES.add(60);
        INIT_TIMES.add(60 + 1);
        INIT_TIMES.add(60 + 2);
        INIT_TIMES.add(60 + 3);
        INIT_TIMES.add(60 + 4);
        INIT_TIMES.add(60 + 5);
    }

    private static final int CLOCK_HOUR_8 = 8;
    private static final int CLOCK_HOUR_9 = 9;
    private static final int CLOCK_HOUR_20 = 20;
    private static final int CLOCK_HOUR_21 = 21;
    private static final int CLOCK_HOUR_22 = 22;

    public static final List<Integer> CLOCK_HOUR = new ArrayList<>();

    static {
        CLOCK_HOUR.add(CLOCK_HOUR_8);
        CLOCK_HOUR.add(CLOCK_HOUR_9);
        CLOCK_HOUR.add(CLOCK_HOUR_20);
        CLOCK_HOUR.add(CLOCK_HOUR_21);
        CLOCK_HOUR.add(CLOCK_HOUR_22);
    }

    private List<Integer> mPoints = new ArrayList<>();
    private List<Integer> mClockTimes = new ArrayList<>();
    private boolean isClocked = false;

    public List<Integer> getPoints() {
        return mPoints;
    }

    public List<Integer> getClockTimes() {
        return mClockTimes;
    }

    public String getClockTimesStr() {
        if (mClockTimes == null || mClockTimes.isEmpty()) {
            return "暂无";
        }
        StringBuilder clockTimesStr = new StringBuilder();
        for (Integer clockTime : mClockTimes) {
            clockTimesStr.append(Utils.buildclockTimesStr(clockTime));
            clockTimesStr.append(", ");
        }
        return clockTimesStr.toString();
    }

    public boolean isClocked() {
        return isClocked;
    }

    public void setClocked(boolean isClocked) {
        this.isClocked = isClocked;
    }

    public void init() {
        setClocked(false);
        initPoints();
        initClockTimes();
    }

    private void initPoints() {
        mPoints.clear();
        mPoints.add(0);
        mPoints.add(1);
        mPoints.add(2);
        mPoints.add(4);
        mPoints.add(6);
        mPoints.add(7);
        mPoints.add(8);
        Utils.log("初始化 mPoints<" + mPoints + ">", false);
    }

    public void initClockTimes() {
        Calendar calendar = Calendar.getInstance();
        //小时
        int newHour = calendar.get(Calendar.HOUR_OF_DAY);
        mClockTimes.clear();
        for (int hour : CLOCK_HOUR) {
            if (hour < newHour) {
                continue;
            }
            int random = 0;
            if (hour == CLOCK_HOUR_8) {
                random = Utils.random(30, 55);
            } else if (hour == CLOCK_HOUR_9) {
                random = Utils.random(5, 30);
            } else if (hour == CLOCK_HOUR_20) {
                random = Utils.random(20, 40);
            } else if (hour == CLOCK_HOUR_21) {
                random = Utils.random(30, 55);
            } else if (hour == CLOCK_HOUR_22) {
                random = Utils.random(5, 30);
            }
            mClockTimes.add(hour * 60 + random);
        }
        Utils.log("初始化 mClockTimes<" + getClockTimesStr() + ">", true);
    }

    public void clear() {
        setClocked(false);
        mPoints.clear();
        mClockTimes.clear();
    }
}
