package com.jason.avengers.common.service.clock;

import android.accessibilityservice.AccessibilityService;
import android.text.TextUtils;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.jason.avengers.common.service.OAAccessibilityService;
import com.jason.avengers.common.service.common.AccountInfo;

public enum ClockHelper {
    /**
     * 单例
     */
    DO;

    private AccessibilityNodeInfo mTargetInfo;

    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        switch (accessibilityEvent.getEventType()) {
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                if (AccountInfo.INFO.isClocked()) {
                    mTargetInfo = deepFindMoveclockinTargetInfo(accessibilityEvent.getSource(), "android.widget.ImageButton");
                } else {
                    mTargetInfo = deepFindMoveclockinTargetInfo(accessibilityEvent.getSource(), "打卡");
                }
                Log.wtf(OAAccessibilityService.TAG, "找到目标 >>>>>> " + mTargetInfo);
                break;
            default:
                break;
        }
    }

    private AccessibilityNodeInfo deepFindMoveclockinTargetInfo(AccessibilityNodeInfo accessibilityNodeInfo, CharSequence text) {
        if (accessibilityNodeInfo == null || TextUtils.isEmpty(accessibilityNodeInfo.getClassName())) {
            return null;
        }
        AccessibilityNodeInfo targetInfo = null;
        if (accessibilityNodeInfo.getChildCount() > 0) {
            for (int i = 0; i < accessibilityNodeInfo.getChildCount(); i++) {
                targetInfo = deepFindMoveclockinTargetInfo(accessibilityNodeInfo.getChild(i), text);
                if (targetInfo != null) {
                    break;
                }
            }
        } else {
            if (AccountInfo.INFO.isClocked()) {
                if (TextUtils.equals(text, accessibilityNodeInfo.getClassName())) {
                    targetInfo = accessibilityNodeInfo;
                    if (!targetInfo.isClickable()) {
                        targetInfo = null;
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
        }
        return targetInfo;
    }

    public void handleClock(AccessibilityService service) {
        if (mTargetInfo == null) {
            service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
            return;
        }
        if (AccountInfo.INFO.isClocked()) {
            Log.wtf(OAAccessibilityService.TAG, "打卡返回 >>>>>> " + mTargetInfo);
        } else {
            Log.wtf(OAAccessibilityService.TAG, "点击打卡 >>>>>> " + mTargetInfo);
        }
        mTargetInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);

    }

    public void init() {
        mTargetInfo = null;
    }

    public void clear() {
        mTargetInfo = null;
    }
}
