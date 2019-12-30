package com.jason.avengers.accessibility;

import android.accessibilityservice.AccessibilityService;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.jason.avengers.accessibility.common.AccountInfo;
import com.jason.avengers.accessibility.common.Utils;
import com.jason.avengers.accessibility.helpers.AvengersHelper;
import com.jason.avengers.accessibility.helpers.Helper;
import com.jason.avengers.accessibility.helpers.LauncherHelper;
import com.jason.avengers.accessibility.helpers.OAHelper;
import com.jason.avengers.accessibility.helpers.SettingsHelper;
import com.jason.avengers.accessibility.helpers.SystemuiHelper;

/**
 * OA Accessibility Service
 *
 * @author Jason
 */
public class OAAccessibilityService extends AccessibilityService {

    public static CharSequence PACKAGENAME = "";
    public static boolean IS_ADB_ENABLED = false;

    private static final long DELAY_MILLIS = 30 * 1000;

    private AvengersHelper mAvengersHelper = new AvengersHelper();
    private LauncherHelper mLauncherHelper = new LauncherHelper();
    private SystemuiHelper mSystemuiHelper = new SystemuiHelper();
    private SettingsHelper mSettingsHelper = new SettingsHelper();
    private OAHelper mOAHelper = new OAHelper();
    private Helper mHelper = new Helper();

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Utils.log("心跳 >> >> >> >> >> >> >> >> >> >>", false);
            mHandler.sendEmptyMessageDelayed(0, DELAY_MILLIS);
            try {
                if (TextUtils.equals(AvengersHelper.PackageName, PACKAGENAME)) {
                    mAvengersHelper.handle(OAAccessibilityService.this);
                } else if (TextUtils.equals(LauncherHelper.PackageName, PACKAGENAME)) {
                    mLauncherHelper.handle(OAAccessibilityService.this);
                } else if (TextUtils.equals(SystemuiHelper.PackageName, PACKAGENAME)) {
                    mSystemuiHelper.handle(OAAccessibilityService.this);
                } else if (TextUtils.equals(SettingsHelper.PackageName, PACKAGENAME)) {
                    mSettingsHelper.handle(OAAccessibilityService.this);
                } else if (TextUtils.equals(OAHelper.PackageName, PACKAGENAME)) {
                    mOAHelper.handle(OAAccessibilityService.this);
                } else {
                    mHelper.handle(OAAccessibilityService.this);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        Utils.log("授权成功", false);
        init();
    }

    private void init() {
        AccountInfo.INFO.init();
        mAvengersHelper.init();
        mLauncherHelper.init();
        mSystemuiHelper.init();
        mSettingsHelper.init();
        mOAHelper.init();
        mHandler.removeCallbacksAndMessages(null);
        mHandler.sendEmptyMessageDelayed(0, DELAY_MILLIS);
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        if (accessibilityEvent.getEventType() != AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            return;
        }
        PACKAGENAME = accessibilityEvent.getPackageName();
        Utils.log("【" + PACKAGENAME + "】监控事件 >>>>>> " + accessibilityEvent.toString(), false);
        try {
            if (TextUtils.equals(AvengersHelper.PackageName, PACKAGENAME)) {
                mAvengersHelper.onAccessibilityEvent(this, accessibilityEvent);
            } else if (TextUtils.equals(LauncherHelper.PackageName, PACKAGENAME)) {
                mLauncherHelper.onAccessibilityEvent(this, accessibilityEvent);
            } else if (TextUtils.equals(SystemuiHelper.PackageName, PACKAGENAME)) {
                mSystemuiHelper.onAccessibilityEvent(this, accessibilityEvent);
            } else if (TextUtils.equals(SettingsHelper.PackageName, PACKAGENAME)) {
                mSettingsHelper.onAccessibilityEvent(this, accessibilityEvent);
            } else if (TextUtils.equals(OAHelper.PackageName, PACKAGENAME)) {
                mOAHelper.onAccessibilityEvent(this, accessibilityEvent);
            } else {
                mHelper.onAccessibilityEvent(this, accessibilityEvent);
            }
            dfs(accessibilityEvent.getSource());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void dfs(AccessibilityNodeInfo accessibilityNodeInfo) {
        if (accessibilityNodeInfo == null || TextUtils.isEmpty(accessibilityNodeInfo.getClassName())) {
            return;
        }
        if (accessibilityNodeInfo.getChildCount() > 0) {
            Utils.logI("深度查找 Parent >>>>>>" + accessibilityNodeInfo);
            for (int i = 0; i < accessibilityNodeInfo.getChildCount(); i++) {
                dfs(accessibilityNodeInfo.getChild(i));
            }
        } else {
            Utils.logI("深度查找 Child >>>>>>" + accessibilityNodeInfo);
        }
    }

    @Override
    public void onInterrupt() {
        Utils.log("授权中断", false);
        clear();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Utils.log("服务销毁", false);
        clear();
    }

    private void clear() {
        AccountInfo.INFO.clear();
        mAvengersHelper.clear();
        mLauncherHelper.clear();
        mSystemuiHelper.clear();
        mSettingsHelper.clear();
        mOAHelper.clear();
        mHandler.removeCallbacksAndMessages(null);
    }

    public static void startAccessibilityServiceSettings(Context context) {
        Utils.startAccessibilityServiceSettings(context);
    }

    public static void startDevelopmentActivity(Context context) {
        Utils.startDevelopmentActivity(context);
    }

}
