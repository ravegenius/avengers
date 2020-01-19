package com.jason.avengers.accessibility.common;

import android.accessibilityservice.AccessibilityService;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;

import com.jason.avengers.accessibility.OAAccessibilityService;
import com.jason.avengers.common.database.ObjectBoxBuilder;
import com.jason.avengers.common.database.entity.accessibility.LogDBEntity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import io.objectbox.Box;

/**
 * 工具
 *
 * @author Jason
 */
public class Utils {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
    private static final String logFormat = "[%s] %s";

    private static final Random mRandom = new Random();

    /**
     * 随机数生成
     *
     * @param start
     * @param end
     * @return
     */
    public static int random(int start, int end) {
        return mRandom.nextInt(end - start + 1) + start;
    }

    /**
     * 生成定时打卡时间字符串
     *
     * @param clockTime
     * @return
     */
    public static String buildclockTimesStr(Integer clockTime) {
        StringBuilder clockTimesStr = new StringBuilder();
        int hour = clockTime / 60;
        int minute = clockTime % 60;
        if (hour < 10) {
            clockTimesStr.append(0);
        }
        clockTimesStr.append(hour);
        clockTimesStr.append(":");
        if (minute < 10) {
            clockTimesStr.append(0);
        }
        clockTimesStr.append(minute);
        return clockTimesStr.toString();
    }

    /**
     * 打开辅助服务的设置
     */
    public static void startAccessibilityServiceSettings(Context context) {
        try {
            Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 打开开发者模式界面
     */
    public static void startDevelopmentActivity(Context context) {
        try {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 点击Back
     *
     * @param service
     */
    public static void performGlobalActionBack(@NonNull AccessibilityService service) {
        Utils.log("【" + OAAccessibilityService.PACKAGENAME + "】处理事件 >>>>>> 点击Back", false);
        service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
    }

    /**
     * 点击Home
     *
     * @param service
     */
    public static void performGlobalActionHome(@NonNull AccessibilityService service) {
        Utils.log("【" + OAAccessibilityService.PACKAGENAME + "】处理事件 >>>>>> 点击Home", false);
        service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_HOME);
    }

    /**
     * 点击Recent
     *
     * @param service
     */
    public static void performGlobalActionRecent(@NonNull AccessibilityService service) {
        Utils.log("【" + OAAccessibilityService.PACKAGENAME + "】处理事件 >>>>>> 点击Recent", false);
        service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_RECENTS);
    }

    /**
     * 点击Click
     *
     * @param targetInfo
     */
    public static void performTargetActionClick(@NonNull AccessibilityNodeInfo targetInfo) {
        Utils.log("【" + OAAccessibilityService.PACKAGENAME + "】处理事件 >>>>>> 点击Click", false);
        targetInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
    }

    /**
     * 日志 wtf
     *
     * @param msg
     * @param saveable
     */
    public static void log(String msg, boolean saveable) {
        Log.wtf("OAService", msg);
        if (saveable) {
            saveLog(msg);
        }
    }

    /**
     * 日志 info
     *
     * @param msg
     */
    public static void logI(String msg) {
        Log.i("OAService", msg);
    }

    /**
     * 保存 log
     *
     * @param msg
     */
    private static void saveLog(String msg) {
        LogDBEntity entity = new LogDBEntity();
        entity.setMsg(String.format(logFormat, sdf.format(new Date()), msg));
        Box<LogDBEntity> logBox = ObjectBoxBuilder.INSTANCE.getBoxStore().boxFor(LogDBEntity.class);
        logBox.put(entity);
    }

    /**
     * 基础时间校验
     *
     * @return
     */
    public static boolean checkTime() {
        Calendar calendar = Calendar.getInstance();
        //星期
        int week = calendar.get(Calendar.DAY_OF_WEEK);
        //小时
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        //分钟
        int minute = calendar.get(Calendar.MINUTE);
        //当前时间
        int currentTime = hour * 60 + minute;

        Utils.log(String.format("校验时间 >>>>>> 星期%d %d:%d", week - 1, hour, minute), false);

        //星期天和星期六返回false
        if (!AccountInfo.WORK_DAYS.contains(week)) {
            return false;
        }
        //初始化时间
        if (initTimes(currentTime)) {
            return false;
        }
        return true;
    }

    /**
     * 校验打卡时间
     *
     * @return
     */
    public static boolean checkClockTime(Context context) {
        Calendar calendar = Calendar.getInstance();
        //星期
        int week = calendar.get(Calendar.DAY_OF_WEEK);
        //小时
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        //分钟
        int minute = calendar.get(Calendar.MINUTE);
        //当前时间
        int currentTime = hour * 60 + minute;
        //打卡时间
        List<Integer> clockTimes = AccountInfo.INFO.getClockTimes();

        //初始化时间
        if (initTimes(currentTime)) {
            return false;
        }
        //处理完所有事件
        if (clockTimes.isEmpty()) {
            return false;
        }
        if (!checkClockHour(context, hour, minute)) {
            return false;
        }
        Utils.log(String.format("开始打卡 >>>>>> 星期%d %d:%d <%s>", week - 1, hour, minute, AccountInfo.INFO.getClockTimesStr()), true);
        return true;
    }

    /**
     * 判断小时是否在打卡范围内
     *
     * @param context
     * @param hour
     * @return
     */
    private static boolean checkClockHour(Context context, int hour, int minute) {
        if (checkAdbEnabled(context)) {
            return minute % 3 == 0;
        } else {
            return (AccountInfo.CLOCK_HOUR.contains(hour) && minute % 3 == 0)
                    || (hour >= 10 && hour < 20 && minute % 3 == 0);
        }
    }

    /**
     * 校验打卡时间
     *
     * @param context
     * @return
     */
    public static boolean checkClockRealTime(Context context) {
        boolean result = false;
        Calendar calendar = Calendar.getInstance();
        //星期
        int week = calendar.get(Calendar.DAY_OF_WEEK);
        //小时
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        //分钟
        int minute = calendar.get(Calendar.MINUTE);
        //当前时间
        int currentTime = hour * 60 + minute;
        //打卡时间
        List<Integer> clockTimes = AccountInfo.INFO.getClockTimes();

        //初始化时间
        if (initTimes(currentTime)) {
            return false;
        }
        //处理完所有事件
        if (!clockTimes.isEmpty()) {
            int clockTime = clockTimes.get(0);
            if (clockTime <= currentTime) {
                result = true;
                clockTimes.remove(0);
            }
        }
        result = checkAdbEnabled(context) || result;
        if (result) {
            Utils.log(String.format("执行打卡 >>>>>> 星期%d %d:%d <%s>", week - 1, hour, minute, AccountInfo.INFO.getClockTimesStr()), true);
        } else {
            Utils.log(String.format("跳过打卡 >>>>>> 星期%d %d:%d <%s>", week - 1, hour, minute, AccountInfo.INFO.getClockTimesStr()), true);
        }
        return result;
    }

    /**
     * 初始化时间
     *
     * @param currentTime
     * @return
     */
    private static boolean initTimes(int currentTime) {
        if (AccountInfo.INIT_TIMES.contains(currentTime)) {
            AccountInfo.INFO.initClockTimes();
            return true;
        }
        return false;
    }

    /**
     * 判断是否是开发者模式 或者是否是自定义的开发者模式
     *
     * @param context
     * @return
     */
    private static boolean checkAdbEnabled(Context context) {
        return isAdbEnabled(context) || OAAccessibilityService.IS_CLOCK_ADB_ENABLED;
    }

    /**
     * 判断adb调试模式是否打开
     *
     * @param context
     * @return
     */
    @SuppressLint("InlinedApi")
    private static boolean isAdbEnabled(Context context) {
        return Settings.Secure.getInt(context.getContentResolver(), Settings.Global.ADB_ENABLED, 0) > 0;
    }
}
