package com.jason.avengers.accessibility.helpers;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;

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
        Utils.log("操作界面 >>>>>> 暂无查找【" + OAAccessibilityService.PACKAGENAME + "】", false);
        mTargetInfo = null;
        super.onAccessibilityEvent(service, accessibilityEvent);
    }
}
