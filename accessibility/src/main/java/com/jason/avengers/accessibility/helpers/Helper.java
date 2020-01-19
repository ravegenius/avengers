package com.jason.avengers.accessibility.helpers;

import android.accessibilityservice.AccessibilityService;
import android.text.TextUtils;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.jason.avengers.accessibility.OAAccessibilityService;
import com.jason.avengers.accessibility.common.Utils;

/**
 * 解析 基础 监听
 *
 * @author jason
 */
public class Helper {

    protected AccessibilityNodeInfo mTargetInfo;
    protected CharSequence mClassName;
    protected CharSequence mLastClassName;
    protected int mHandleTimes;

    public void onAccessibilityEvent(AccessibilityService service, AccessibilityEvent accessibilityEvent) {
        mClassName = accessibilityEvent.getClassName();
        Utils.log("【" + OAAccessibilityService.PACKAGENAME + "】找到目标 >>>>>> " + mTargetInfo, false);
    }

    public void handle(AccessibilityService service) {
    }

    public void init() {
        mTargetInfo = null;
    }

    public void clear() {
        mTargetInfo = null;
    }

    public boolean checkHandleTimesIsOver(int maxTimes) {
        if (TextUtils.isEmpty(mLastClassName) || !mLastClassName.equals(mClassName)) {
            mLastClassName = mClassName;
            mHandleTimes = 0;
        } else {
            mHandleTimes++;
        }
        if (mHandleTimes >= maxTimes) {
            mHandleTimes = 0;
            return true;
        }
        return false;
    }
}
