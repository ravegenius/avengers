package com.jason.avengers.accessibility.helpers;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.jason.avengers.accessibility.OAAccessibilityService;
import com.jason.avengers.accessibility.common.AccountInfo;
import com.jason.avengers.accessibility.common.Utils;

import java.util.ArrayList;
import java.util.List;

public class OAHelper extends Helper {

    public static CharSequence PackageName = "com.netease.oa";
    private static final boolean isN = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N;
    private static final int POINTS_SIZE = 9;

    @Override
    public void onAccessibilityEvent(AccessibilityService service, AccessibilityEvent accessibilityEvent) {
        mClassName = accessibilityEvent.getClassName();
        if ("com.netease.oa.ui.security.LoginActivityV5".equals(mClassName)) {
            Utils.log("【" + OAAccessibilityService.PACKAGENAME + "】处理事件 >>>>>> 账号登录");
            mTargetInfo = deepLoginTargetInfo(accessibilityEvent.getSource(), "登 录");
        } else if ("com.netease.oa.ui.main.MainTabActivityV6".equals(mClassName)) {
            Utils.log("【" + OAAccessibilityService.PACKAGENAME + "】处理事件 >>>>>> 主界面");
            if (AccountInfo.INFO.isClocked()) {
                mTargetInfo = deepFindMainTargetInfo(accessibilityEvent.getSource(), "Rect(0, 0 - 105, 54)");
            } else {
                mTargetInfo = deepFindMainTargetInfo(accessibilityEvent.getSource(), "移动打卡");
            }
        } else if ("com.netease.oa.ui.security.GestureUnlcokActivity".equals(mClassName)
                || "com.netease.oa.ui.security.TouchIdAndPatternActivity".equals(mClassName)) {
            Utils.log("【" + OAAccessibilityService.PACKAGENAME + "】处理事件 >>>>>> 手势密码");
            if (isN) {
                mTargetInfo = deepFindGestureUnlcokTargetInfo(accessibilityEvent.getSource(), "Rect(0, 0 - 870, 870)");
            } else {
                mTargetInfo = deepFindLoginTargetInfo(accessibilityEvent.getSource(), "账号密码登录");
            }
        } else if ("com.netease.oa.ui.submodules.moveclockin.MovingClockMainActivity".equals(mClassName)) {
            Utils.log("【" + OAAccessibilityService.PACKAGENAME + "】处理事件 >>>>>> 移动打卡");
            if (AccountInfo.INFO.isClocked()) {
                mTargetInfo = deepFindMoveclockinTargetInfo(accessibilityEvent.getSource(), "android.widget.ImageButton");
            } else {
                mTargetInfo = deepFindMoveclockinTargetInfo(accessibilityEvent.getSource(), "打卡");
            }
        } else if ("com.netease.oa.ui.setting.SettingActivity".equals(mClassName)) {
            Utils.log("【" + OAAccessibilityService.PACKAGENAME + "】处理事件 >>>>>> 设置退出");
            mTargetInfo = deepFindLogoutTargetInfo(accessibilityEvent.getSource(), "退出登录");
        } else if ("com.netease.oa.ui.submodules.moveclockin.ClockInTipDialog".equals(mClassName)) {
            //无论成功失败只记录点击操作
            AccountInfo.INFO.setClocked(true);
            if (accessibilityEvent.getText().toString().contains("打卡成功")) {
                Utils.log("【" + OAAccessibilityService.PACKAGENAME + "】处理事件 >>>>>> 打卡成功");
            } else {
                Utils.log("【" + OAAccessibilityService.PACKAGENAME + "】处理事件 >>>>>> 打卡失败");
            }
            mTargetInfo = null;
        } else if ("android.support.v7.app.c".equals(mClassName)
                && accessibilityEvent.getText().toString().contains("发现新版本")) {
            Utils.log("【" + OAAccessibilityService.PACKAGENAME + "】处理事件 >>>>>> 发现新版本");
            mTargetInfo = deepFindUploadTargetInfo(accessibilityEvent.getSource(), "稍后");
        } else {
            Utils.log("【" + OAAccessibilityService.PACKAGENAME + "】处理事件 >>>>>> 未知事件");
            mTargetInfo = null;
        }
        super.onAccessibilityEvent(service, accessibilityEvent);
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
                    Utils.log("【" + OAAccessibilityService.PACKAGENAME + "】处理事件 >>>>>> 设置账号");
                    Bundle arguments = new Bundle();
                    arguments.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, AccountInfo.ACCOUNT);
                    accessibilityNodeInfo.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments);
                } else {
                    Utils.log("【" + OAAccessibilityService.PACKAGENAME + "】处理事件 >>>>>> 设置密码");
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

    private AccessibilityNodeInfo deepFindMainTargetInfo(AccessibilityNodeInfo accessibilityNodeInfo, String text) {
        if (accessibilityNodeInfo == null || TextUtils.isEmpty(accessibilityNodeInfo.getClassName())) {
            return null;
        }
        if (accessibilityNodeInfo.getChildCount() > 0) {
        }
        AccessibilityNodeInfo targetInfo = null;
        if (accessibilityNodeInfo.getChildCount() > 0) {
            for (int i = 0; i < accessibilityNodeInfo.getChildCount(); i++) {
                targetInfo = deepFindMainTargetInfo(accessibilityNodeInfo.getChild(i), text);
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

    @Override
    public void handle(AccessibilityService service) {
        if ("com.netease.oa.ui.security.LoginActivityV5".equals(mClassName)) {
            if (AccountInfo.INFO.isClocked()) {
                AccountInfo.INFO.setClocked(false);
                Utils.performGlobalActionHome(service);
                return;
            }
        } else if ("com.netease.oa.ui.security.GestureUnlcokActivity".equals(mClassName)
                || "com.netease.oa.ui.security.TouchIdAndPatternActivity".equals(mClassName)) {
            if (isN && mTargetInfo != null) {
                Rect rect = new Rect();
                mTargetInfo.getBoundsInScreen(rect);
                Path path = buildPath(rect);
                Utils.log("【" + OAAccessibilityService.PACKAGENAME + "】处理事件 >>>>>> 滑动解锁");
                service.dispatchGesture(new GestureDescription.Builder().addStroke(new GestureDescription.StrokeDescription
                        (path, 0, 800)).build(), null, null);
                return;
            }
        }
        super.handle(service);
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
}
