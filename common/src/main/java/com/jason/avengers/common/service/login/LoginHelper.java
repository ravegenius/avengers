package com.jason.avengers.common.service.login;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.jason.avengers.common.service.OAAccessibilityService;
import com.jason.avengers.common.service.common.AccountInfo;

public enum LoginHelper {
    /**
     * 单例
     */
    DO;

    private AccessibilityNodeInfo mTargetInfo;

    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        switch (accessibilityEvent.getEventType()) {
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                mTargetInfo = deepLoginTargetInfo(accessibilityEvent.getSource(), "登 录");
                Log.wtf(OAAccessibilityService.TAG, "找到目标 >>>>>> " + mTargetInfo);
                break;
            default:
                break;
        }
    }

    private AccessibilityNodeInfo deepLoginTargetInfo(AccessibilityNodeInfo accessibilityNodeInfo, String text) {
        if (accessibilityNodeInfo == null || TextUtils.isEmpty(accessibilityNodeInfo.getClassName())) {
            return null;
        }

        AccessibilityNodeInfo targetInfo = null;
        if (accessibilityNodeInfo.getChildCount() > 0) {
            for (int i = 0; i < accessibilityNodeInfo.getChildCount(); i++) {
                targetInfo = deepLoginTargetInfo(accessibilityNodeInfo.getChild(i), text);
                if (targetInfo != null) {
                    break;
                }
            }
        } else {
            if (TextUtils.equals("android.widget.EditText", accessibilityNodeInfo.getClassName())) {
                if (!accessibilityNodeInfo.isPassword()) {
                    Log.wtf(OAAccessibilityService.TAG, "设置账号 >>>>>> ");
                    Bundle arguments = new Bundle();
                    arguments.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, AccountInfo.ACCOUNT);
                    accessibilityNodeInfo.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments);
                } else {
                    Log.wtf(OAAccessibilityService.TAG, "设置密码 >>>>>> ");
                    Bundle arguments = new Bundle();
                    arguments.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, AccountInfo.PASSWORD);
                    accessibilityNodeInfo.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments);
                }
            }
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

    public void handleLogin() {
        if (mTargetInfo == null) {
            return;
        }
        Log.wtf(OAAccessibilityService.TAG, "点击登录 >>>>>> " + mTargetInfo);
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
