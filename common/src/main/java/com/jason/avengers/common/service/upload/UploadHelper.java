package com.jason.avengers.common.service.upload;

import android.accessibilityservice.AccessibilityService;
import android.text.TextUtils;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.jason.avengers.common.service.OAAccessibilityService;

public enum UploadHelper {
    /**
     * 单例
     */
    DO;

    private AccessibilityNodeInfo mTargetInfo;

    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        switch (accessibilityEvent.getEventType()) {
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                mTargetInfo = deepFindUploadTargetInfo(accessibilityEvent.getSource(), "稍后");
                Log.wtf(OAAccessibilityService.TAG, "找到目标 >>>>>> " + mTargetInfo);
                break;
            default:
                break;
        }
    }

    private AccessibilityNodeInfo deepFindUploadTargetInfo(AccessibilityNodeInfo accessibilityNodeInfo, String text) {
        if (accessibilityNodeInfo == null || TextUtils.isEmpty(accessibilityNodeInfo.getClassName())) {
            return null;
        }
        AccessibilityNodeInfo targetInfo = null;
        if (accessibilityNodeInfo.getChildCount() > 0) {
            for (int i = 0; i < accessibilityNodeInfo.getChildCount(); i++) {
                targetInfo = deepFindUploadTargetInfo(accessibilityNodeInfo.getChild(i), text);
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

    public void handleUpload(AccessibilityService service) {
        if (mTargetInfo == null) {
            service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
            return;
        }
        Log.wtf(OAAccessibilityService.TAG, "稍后安装 >>>>>> " + mTargetInfo);
        mTargetInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
    }

    public void init() {
        mTargetInfo = null;
    }

    public void clear() {
        mTargetInfo = null;
    }
}
