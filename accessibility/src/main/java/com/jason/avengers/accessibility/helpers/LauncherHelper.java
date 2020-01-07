package com.jason.avengers.accessibility.helpers;

import android.accessibilityservice.AccessibilityService;
import android.text.TextUtils;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.jason.avengers.accessibility.OAAccessibilityService;
import com.jason.avengers.accessibility.common.Utils;

/**
 * 解析 Launcher 监听
 *
 * @author jason
 */
public class LauncherHelper extends Helper {

    public static CharSequence PackageName = "com.miui.home";

    @Override
    public void onAccessibilityEvent(AccessibilityService service, AccessibilityEvent accessibilityEvent) {
        if ("com.miui.home.launcher.Launcher".equals(accessibilityEvent.getClassName())) {
            Utils.log("【" + OAAccessibilityService.PACKAGENAME + "】处理事件 >>>>>> Launcher", false);
            mTargetInfo = deepFindOpenTargetInfo(accessibilityEvent.getSource(), "网易OA");
        } else {
            Utils.log("【" + OAAccessibilityService.PACKAGENAME + "】处理事件 >>>>>> 未知事件", false);
            mTargetInfo = null;
        }
        super.onAccessibilityEvent(service, accessibilityEvent);
    }

    private AccessibilityNodeInfo deepFindOpenTargetInfo(AccessibilityNodeInfo accessibilityNodeInfo, String text) {
        if (accessibilityNodeInfo == null || TextUtils.isEmpty(accessibilityNodeInfo.getClassName())) {
            return null;
        }
        AccessibilityNodeInfo targetInfo = null;

        if (!TextUtils.isEmpty(accessibilityNodeInfo.getContentDescription())
                && accessibilityNodeInfo.getContentDescription().toString().contains(text)) {
            targetInfo = accessibilityNodeInfo;
            if (!targetInfo.isClickable()) {
                targetInfo = null;
            }
        }
        if (targetInfo == null && accessibilityNodeInfo.getChildCount() > 0) {
            for (int i = 0; i < accessibilityNodeInfo.getChildCount(); i++) {
                targetInfo = deepFindOpenTargetInfo(accessibilityNodeInfo.getChild(i), text);
                if (targetInfo != null) {
                    break;
                }
            }
        }
        return targetInfo;
    }

    @Override
    public void handle(AccessibilityService service) {
        if (Utils.checkClockTime(service)) {
            super.handle(service);
        }
    }
}
