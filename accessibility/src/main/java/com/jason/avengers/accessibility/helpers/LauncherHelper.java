package com.jason.avengers.accessibility.helpers;

import android.accessibilityservice.AccessibilityService;
import android.text.TextUtils;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.jason.avengers.accessibility.OAAccessibilityService;
import com.jason.avengers.accessibility.common.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 解析 Launcher 监听
 *
 * @author jason
 */
public class LauncherHelper extends Helper {

    public static CharSequence PackageName = "com.miui.home";

    private static final String NetEaseClock = "网易OA";
    private static final String NetEaseNews = "网易新闻";
    private static List<String> mTargetInfoKeys;
    private static final int MAX_HANDLE_TIMES = 5;

    static {
        mTargetInfoKeys = new ArrayList<>();
        mTargetInfoKeys.add(NetEaseClock);
        mTargetInfoKeys.add(NetEaseNews);
    }

    private Map<String, AccessibilityNodeInfo> mTargetInfos = new HashMap<>();

    @Override
    public void onAccessibilityEvent(AccessibilityService service, AccessibilityEvent accessibilityEvent) {
        if ("com.miui.home.launcher.Launcher".equals(accessibilityEvent.getClassName())) {
            Utils.log("【" + OAAccessibilityService.PACKAGENAME + "】处理事件 >>>>>> Launcher", false);
            mTargetInfos.putAll(deepFindOpenTargetInfo(accessibilityEvent.getSource(), mTargetInfoKeys));
        } else {
            Utils.log("【" + OAAccessibilityService.PACKAGENAME + "】处理事件 >>>>>> 未知事件", false);
            mTargetInfos.clear();
        }
        if (mTargetInfos.isEmpty()) {
            initTargetInfos();
        }
        super.onAccessibilityEvent(service, accessibilityEvent);
    }

    private void initTargetInfos() {
        mTargetInfos.clear();
        for (String targetInfoKey : mTargetInfoKeys) {
            mTargetInfos.put(targetInfoKey, null);
        }
    }

    private Map<String, AccessibilityNodeInfo> deepFindOpenTargetInfo(AccessibilityNodeInfo accessibilityNodeInfo, List<String> targetInfoKeys) {
        if (accessibilityNodeInfo == null || TextUtils.isEmpty(accessibilityNodeInfo.getClassName())) {
            return Collections.emptyMap();
        }
        Map<String, AccessibilityNodeInfo> targetInfos = new HashMap<>(targetInfoKeys.size());

        for (String targetInfoKey : targetInfoKeys) {
            if (!TextUtils.isEmpty(accessibilityNodeInfo.getContentDescription())
                    && accessibilityNodeInfo.getContentDescription().toString().contains(targetInfoKey)) {
                if (accessibilityNodeInfo.isClickable()) {
                    targetInfos.put(targetInfoKey, accessibilityNodeInfo);
                }
            }
        }
        if (targetInfos.isEmpty() && accessibilityNodeInfo.getChildCount() > 0) {
            for (int i = 0; i < accessibilityNodeInfo.getChildCount(); i++) {
                targetInfos.putAll(deepFindOpenTargetInfo(accessibilityNodeInfo.getChild(i), targetInfoKeys));
                if (!targetInfos.isEmpty() && targetInfos.size() == targetInfoKeys.size()) {
                    break;
                }
            }
        }
        return targetInfos;
    }

    @Override
    public void handle(AccessibilityService service) {
        if (!Utils.checkTime()) {
            return;
        }

        if (checkHandleTimesIsOver(MAX_HANDLE_TIMES)) {
            Utils.log("任何界面 >>>>>> 停留太久【" + OAAccessibilityService.PACKAGENAME + "】", true);
            Utils.performGlobalActionRecent(service);
            return;
        }

        if (mTargetInfos == null || mTargetInfos.isEmpty()) {
            Utils.log("任何界面 >>>>>> 没有数据【" + OAAccessibilityService.PACKAGENAME + "】", true);
            Utils.performGlobalActionRecent(service);
            return;
        }

        if (Utils.checkClockTime(service)) {
            mTargetInfo = mTargetInfos.get(NetEaseClock);
        } else {
            mTargetInfo = null;
        }

        if (mTargetInfo != null) {
            Utils.performTargetActionClick(mTargetInfo);
        }
    }
}
