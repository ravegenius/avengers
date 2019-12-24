package com.jason.avengers.common.service.main;

import android.graphics.Rect;
import android.text.TextUtils;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.jason.avengers.common.service.OAAccessibilityService;
import com.jason.avengers.common.service.common.AccountInfo;

public enum MainHelper {
    /**
     * 单例
     */
    DO;

    private AccessibilityNodeInfo mTargetInfo;

    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        switch (accessibilityEvent.getEventType()) {
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                if (AccountInfo.INFO.isClocked()) {
                    mTargetInfo = deepFindMainTabTargetInfo(accessibilityEvent.getSource(), "Rect(0, 0 - 105, 54)");
                } else {
                    mTargetInfo = deepFindMainTabTargetInfo(accessibilityEvent.getSource(), "移动打卡");
                }
                Log.wtf(OAAccessibilityService.TAG, "找到目标 >>>>>> " + mTargetInfo);
                break;
            default:
                break;
        }
    }

    private AccessibilityNodeInfo deepFindMainTabTargetInfo(AccessibilityNodeInfo accessibilityNodeInfo, String text) {
        if (accessibilityNodeInfo == null || TextUtils.isEmpty(accessibilityNodeInfo.getClassName())) {
            return null;
        }
        if (accessibilityNodeInfo.getChildCount() > 0) {
        }
        AccessibilityNodeInfo targetInfo = null;
        if (accessibilityNodeInfo.getChildCount() > 0) {
            for (int i = 0; i < accessibilityNodeInfo.getChildCount(); i++) {
                targetInfo = deepFindMainTabTargetInfo(accessibilityNodeInfo.getChild(i), text);
                if (targetInfo != null) {
                    break;
                }
            }
        } else {
            if (AccountInfo.INFO.isClocked()) {
                if (TextUtils.equals("android.widget.ImageView", accessibilityNodeInfo.getClassName())) {
                    Rect rect = new Rect();
                    accessibilityNodeInfo.getBoundsInParent(rect);
                    if (TextUtils.equals(text, rect.toString())) {
                        targetInfo = accessibilityNodeInfo;
                        if (!targetInfo.isClickable()) {
                            targetInfo = null;
                        }
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

    public void handleMain() {
        if (mTargetInfo == null) {
            return;
        }
        if (AccountInfo.INFO.isClocked()) {
            Log.wtf(OAAccessibilityService.TAG, "进入设置 >>>>>> ");
        } else {
            Log.wtf(OAAccessibilityService.TAG, "进入打卡 >>>>>> ");
        }
        mTargetInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
    }

    public void init() {
    }

    public void clear() {
    }
}
