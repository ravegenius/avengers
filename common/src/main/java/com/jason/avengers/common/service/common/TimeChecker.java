package com.jason.avengers.common.service.common;

import android.util.Log;

import com.jason.avengers.common.service.OAAccessibilityService;

import java.util.Calendar;
import java.util.List;

public class TimeChecker {

    public static boolean checkClockTime() {
        Calendar calendar = Calendar.getInstance();
        //星期
        int week = calendar.get(Calendar.DAY_OF_WEEK);
        //小时
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        //分钟
        int minute = calendar.get(Calendar.MINUTE);
        //打卡时间
        List<Integer> clockTimes = AccountInfo.INFO.getClockTimes();

        Log.wtf(OAAccessibilityService.TAG, String.format("校验时间 >>>>>> 星期%d %d:%d %s", week - 1, hour, minute, clockTimes.toString()));
        //星期天和星期六返回false
        if (week == 1 || week == 7) {
            return false;
        }
        if (hour == 1 && minute <= 5) {
            AccountInfo.INFO.initClockTimes();
            return false;
        }
        //处理完所有事件
        if (clockTimes.isEmpty()) {
            return false;
        }
        int currentTime = hour * 100 + minute;
        //是否到随机时间
        if (!clockTimes.contains(currentTime)) {
            return false;
        }

        clockTimes.remove((Integer) currentTime);
        Log.wtf(OAAccessibilityService.TAG, String.format("执行打卡 >>>>>> 星期%d %d:%d", week - 1, hour, minute));
        return true;
    }
}
