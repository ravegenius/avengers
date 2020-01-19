package com.jason.avengers.accessibility.helpers;

import android.accessibilityservice.AccessibilityService;
import android.text.TextUtils;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.jason.avengers.accessibility.OAAccessibilityService;
import com.jason.avengers.accessibility.common.Utils;

/**
 * 解析 Systemui 监听
 *
 * @author jason
 */
public class SystemuiHelper extends Helper {

    public static CharSequence PackageName = "com.android.systemui";
    private static final int MAX_HANDLE_TIMES = 3;

    @Override
    public void onAccessibilityEvent(AccessibilityService service, AccessibilityEvent accessibilityEvent) {
        if ("com.android.systemui.recents.RecentsActivity".equals(accessibilityEvent.getClassName())) {
            Utils.log("操作界面 >>>>>> 查找OA【" + OAAccessibilityService.PACKAGENAME + "】", false);
            mTargetInfo = deepFindRecentsTargetInfo(accessibilityEvent.getSource(), "网易OA");
        } else {
            Utils.log("操作界面 >>>>>> 暂无查找【" + OAAccessibilityService.PACKAGENAME + "】", false);
            mTargetInfo = null;
        }
        super.onAccessibilityEvent(service, accessibilityEvent);
    }

    private AccessibilityNodeInfo deepFindRecentsTargetInfo(AccessibilityNodeInfo accessibilityNodeInfo, String text) {
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
                targetInfo = deepFindRecentsTargetInfo(accessibilityNodeInfo.getChild(i), text);
                if (targetInfo != null) {
                    break;
                }
            }
        }
        return targetInfo;
    }

    @Override
    public void handle(AccessibilityService service) {
        if (mTargetInfo != null && checkHandleTimesIsOver(MAX_HANDLE_TIMES)) {
            Utils.log("任何界面 >>>>>> 多次调起【" + OAAccessibilityService.PACKAGENAME + "】", true);
            // 有时点击无动于衷......
            Utils.performTargetActionClick(mTargetInfo);
            return;
        }
        Utils.performGlobalActionBack(service);
    }
}
