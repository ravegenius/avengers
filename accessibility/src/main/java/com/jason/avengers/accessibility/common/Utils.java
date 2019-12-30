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
import com.jason.avengers.common.database.entity.LogDBEntity;

import java.util.Calendar;
import java.util.List;

import io.objectbox.Box;

/**
 * 工具
 *
 * @author Jason
 */
public class Utils {

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
     * 判断adb调试模式是否打开
     *
     * @param context
     * @return
     */
    @SuppressLint("InlinedApi")
    public static boolean isAdbEnabled(Context context) {
        boolean isAdbEnabled = Settings.Secure.getInt(context.getContentResolver(), Settings.Global.ADB_ENABLED, 0) > 0;
        return isAdbEnabled && OAAccessibilityService.IS_ADB_ENABLED;
    }

    /**
     * 校验打开时间
     *
     * @return
     */
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

        Utils.log(String.format("校验时间 >>>>>> 星期%d %d:%d %s", week - 1, hour, minute, clockTimes.toString()), false);
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
        Utils.log(String.format("执行打卡 >>>>>> 星期%d %d:%d", week - 1, hour, minute), true);
        return true;
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
        entity.setMsg(msg);
        Box<LogDBEntity> logBox = ObjectBoxBuilder.INSTANCE.getBoxStore().boxFor(LogDBEntity.class);
        logBox.put(entity);
    }
}
