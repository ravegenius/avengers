package com.jason.avengers.common.service.gesture;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.annotation.SuppressLint;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.jason.avengers.common.service.OAAccessibilityService;
import com.jason.avengers.common.service.common.AccountInfo;

import java.util.ArrayList;
import java.util.List;

public enum GestureHelper {
    /**
     * 单例
     */
    DO;

    private static final int POINTS_SIZE = 9;

    private AccessibilityNodeInfo mTargetInfo;
    private boolean isN = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N;

    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        switch (accessibilityEvent.getEventType()) {
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                if (isN) {
                    mTargetInfo = deepFindGestureUnlcokTargetInfo(accessibilityEvent.getSource(), "Rect(0, 0 - 870, 870)");
                } else {
                    mTargetInfo = deepFindLoginTargetInfo(accessibilityEvent.getSource(), "账号密码登录");
                }
                Log.wtf(OAAccessibilityService.TAG, "找到目标 >>>>>> " + mTargetInfo);
                break;
            default:
                break;
        }
    }

    private AccessibilityNodeInfo deepFindGestureUnlcokTargetInfo(AccessibilityNodeInfo accessibilityNodeInfo, CharSequence text) {
        if (accessibilityNodeInfo == null || TextUtils.isEmpty(accessibilityNodeInfo.getClassName())) {
            return null;
        }
        AccessibilityNodeInfo targetInfo = null;
        if (accessibilityNodeInfo.getChildCount() > 0) {
            for (int i = 0; i < accessibilityNodeInfo.getChildCount(); i++) {
                targetInfo = deepFindGestureUnlcokTargetInfo(accessibilityNodeInfo.getChild(i), text);
                if (targetInfo != null) {
                    break;
                }
            }
        } else {
            if (TextUtils.equals("android.view.View", accessibilityNodeInfo.getClassName())) {
                Rect rect = new Rect();
                accessibilityNodeInfo.getBoundsInParent(rect);
                if (TextUtils.equals(text, rect.toString())) {
                    targetInfo = accessibilityNodeInfo;
                }
            }
        }
        return targetInfo;
    }

    private AccessibilityNodeInfo deepFindLoginTargetInfo(AccessibilityNodeInfo accessibilityNodeInfo, CharSequence text) {
        if (accessibilityNodeInfo == null || TextUtils.isEmpty(accessibilityNodeInfo.getClassName())) {
            return null;
        }
        AccessibilityNodeInfo targetInfo = null;
        if (accessibilityNodeInfo.getChildCount() > 0) {
            for (int i = 0; i < accessibilityNodeInfo.getChildCount(); i++) {
                targetInfo = deepFindLoginTargetInfo(accessibilityNodeInfo.getChild(i), text);
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

    @SuppressLint("NewApi")
    public void handleGesture(AccessibilityService service) {
        if (mTargetInfo == null) {
            return;
        }
        if (isN) {
            Rect rect = new Rect();
            mTargetInfo.getBoundsInScreen(rect);
            Path path = buildPath(rect);
            Log.wtf(OAAccessibilityService.TAG, "滑动解锁 >>>>>> " + mTargetInfo + " >>>>>> " + path);
            service.dispatchGesture(new GestureDescription.Builder().addStroke(new GestureDescription.StrokeDescription
                    (path, 0, 800)).build(), null, null);
        } else {
            Log.wtf(OAAccessibilityService.TAG, "账号登录 >>>>>> 低版本(低于7.0) 无法实现");
            mTargetInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        }
    }

    private Path buildPath(Rect rect) {
        float baseX = rect.left;
        float baseCellWidth = (rect.right - rect.left) / 3.00F;
        float baseCellCenterX = baseCellWidth / 2.00F;
        float baseY = rect.top;
        float baseCellHeight = (rect.bottom - rect.top) / 3.00F;
        float baseCellCenterY = baseCellHeight / 2.00F;

        List<Float[]> pathPoints = new ArrayList<>();
        int row = 0, column = 0;
        for (int index = 0; index < POINTS_SIZE; index++) {
            row = index % 3;
            column = index / 3;
            pathPoints.add(new Float[]{baseX + baseCellCenterX + (baseCellWidth * row),
                    baseY + baseCellCenterY + (baseCellHeight * column)});
        }
        //线性的path代表手势路径,点代表按下,封闭的没用
        Path path = new Path();
        boolean isFirstPointXY = true;
        for (int point : AccountInfo.INFO.getPoints()) {
            Float[] pointXY = pathPoints.get(point);
            if (isFirstPointXY) {
                isFirstPointXY = false;
                path.moveTo(pointXY[0], pointXY[1]);
            } else {
                path.lineTo(pointXY[0], pointXY[1]);
            }
        }
        return path;
    }

    public void init() {
        mTargetInfo = null;
    }

    public void clear() {
        mTargetInfo = null;
    }
}
