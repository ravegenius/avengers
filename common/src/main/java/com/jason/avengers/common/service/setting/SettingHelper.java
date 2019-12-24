package com.jason.avengers.common.service.setting;

import android.accessibilityservice.AccessibilityService;
import android.text.TextUtils;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.jason.avengers.common.service.OAAccessibilityService;
import com.jason.avengers.common.service.common.AccountInfo;

public enum SettingHelper {
    /**
     * 单例
     */
    DO;

    private AccessibilityNodeInfo mTargetInfo;

    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        switch (accessibilityEvent.getEventType()) {
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                mTargetInfo = deepFindLogoutTargetInfo(accessibilityEvent.getSource(), "退出登录");
                Log.wtf(OAAccessibilityService.TAG, "找到目标 >>>>>> " + mTargetInfo);
                break;
            default:
                break;
        }
    }

    private AccessibilityNodeInfo deepFindLogoutTargetInfo(AccessibilityNodeInfo accessibilityNodeInfo, String text) {
        if (accessibilityNodeInfo == null || TextUtils.isEmpty(accessibilityNodeInfo.getClassName())) {
            return null;
        }
        AccessibilityNodeInfo targetInfo = null;
        if (accessibilityNodeInfo.getChildCount() > 0) {
            for (int i = 0; i < accessibilityNodeInfo.getChildCount(); i++) {
                targetInfo = deepFindLogoutTargetInfo(accessibilityNodeInfo.getChild(i), text);
                if (targetInfo != null) {
                    break;
                }
            }
        } else {
            if (!TextUtils.isEmpty(accessibilityNodeInfo.getText())
                    && TextUtils.equals(accessibilityNodeInfo.getText(), text)) {
                targetInfo = accessibilityNodeInfo;
                if (!targetInfo.isClickable()) {
                    targetInfo = null;
                }
            }
        }
        return targetInfo;
    }

    public void handleSetting(AccessibilityService service) {
        if (mTargetInfo == null) {
            service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
            return;
        }
        Log.wtf(OAAccessibilityService.TAG, "退出登录 >>>>>> " + mTargetInfo);
        mTargetInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        AccountInfo.INFO.setClocked(false);
    }

    public void init() {
        mTargetInfo = null;
    }

    public void clear() {
        mTargetInfo = null;
    }
}
