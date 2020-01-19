package com.jason.avengers.accessibility.helpers;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;

public class NewsHelper extends Helper {

    public static CharSequence PackageName = "com.netease.news.activity";

    @Override
    public void onAccessibilityEvent(AccessibilityService service, AccessibilityEvent accessibilityEvent) {
        super.onAccessibilityEvent(service, accessibilityEvent);
    }
}
