package com.jason.avengers.accessibility.helpers;

import android.accessibilityservice.AccessibilityService;
import android.text.TextUtils;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.jason.avengers.accessibility.OAAccessibilityService;
import com.jason.avengers.accessibility.common.Utils;

/**
 * 解析 Avengers 监听
 *
 * @author jason
 */
public class AvengersHelper extends Helper {

    public static CharSequence PackageName = "com.jason.avengers";

    @Override
    public void onAccessibilityEvent(AccessibilityService service, AccessibilityEvent accessibilityEvent) {
        if ("com.jason.avengers.main.activities.MainActivity".equals(accessibilityEvent.getClassName())) {
            Utils.log("【" + OAAccessibilityService.PACKAGENAME + "】处理事件 >>>>>> 主界面", true);
            mTargetInfo = deepFindDevelopTargetInfo(accessibilityEvent.getSource(), "开发者模式");
        } else {
            Utils.log("【" + OAAccessibilityService.PACKAGENAME + "】处理事件 >>>>>> 未知事件", false);
            mTargetInfo = null;
        }
        super.onAccessibilityEvent(service, accessibilityEvent);
    }

    private AccessibilityNodeInfo deepFindDevelopTargetInfo(AccessibilityNodeInfo accessibilityNodeInfo, String text) {
        if (accessibilityNodeInfo == null || TextUtils.isEmpty(accessibilityNodeInfo.getClassName())) {
            return null;
        }
        AccessibilityNodeInfo targetInfo = null;
        if (accessibilityNodeInfo.getChildCount() > 0) {
            for (int i = 0; i < accessibilityNodeInfo.getChildCount(); i++) {
                targetInfo = deepFindDevelopTargetInfo(accessibilityNodeInfo.getChild(i), text);
                if (targetInfo != null) {
                    break;
                }
            }
        } else {
            if (!TextUtils.isEmpty(accessibilityNodeInfo.getText())
                    && TextUtils.equals(accessibilityNodeInfo.getText(), text)) {
                targetInfo = accessibilityNodeInfo.getParent();
                if (!targetInfo.isClickable()) {
                    targetInfo = null;
                }
            }
        }
        return targetInfo;
    }
}
