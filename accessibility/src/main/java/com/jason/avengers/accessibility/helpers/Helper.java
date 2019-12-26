package com.jason.avengers.accessibility.helpers;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.jason.avengers.accessibility.OAAccessibilityService;
import com.jason.avengers.accessibility.common.Utils;

public class Helper {

    protected AccessibilityNodeInfo mTargetInfo;
    protected CharSequence mClassName;

    public void onAccessibilityEvent(AccessibilityService service, AccessibilityEvent accessibilityEvent) {
        mClassName = accessibilityEvent.getClassName();
        Utils.log("【" + OAAccessibilityService.PACKAGENAME + "】找到目标 >>>>>> " + mTargetInfo);
    }

    public void handle(AccessibilityService service) {
        if (mTargetInfo == null) {
            Utils.performGlobalActionBack(service);
            return;
        }
        Utils.performTargetActionClick(mTargetInfo);
    }

    public void init() {
        mTargetInfo = null;
    }

    public void clear() {
        mTargetInfo = null;
    }
}
