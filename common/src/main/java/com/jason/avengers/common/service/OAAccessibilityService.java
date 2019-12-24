package com.jason.avengers.common.service;

import android.accessibilityservice.AccessibilityService;
import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.jason.avengers.common.service.clock.ClockHelper;
import com.jason.avengers.common.service.common.AccountInfo;
import com.jason.avengers.common.service.common.TimeChecker;
import com.jason.avengers.common.service.gesture.GestureHelper;
import com.jason.avengers.common.service.login.LoginHelper;
import com.jason.avengers.common.service.main.MainHelper;
import com.jason.avengers.common.service.setting.SettingHelper;
import com.jason.avengers.common.service.upload.UploadHelper;

/**
 * 打卡Service
 */
public class OAAccessibilityService extends AccessibilityService {

    public static final String TAG = "OAService";

    private static final long DELAY_MILLIS = 30 * 1000;
    private static final int IDEL = 0,
            LOGIN = IDEL + 1,
            MAIN = LOGIN + 1,
            GESTURE = MAIN + 1,
            CLOCK = GESTURE + 1,
            SETTING = CLOCK + 1,
            UPLOAD = SETTING + 1;

    private int mState = IDEL;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };
    private Runnable mHeartBeat = new Runnable() {
        @Override
        public void run() {
            Log.wtf(TAG, "心跳 >> >> >> >> >> >> >> >> >> >>");
            mHandler.postDelayed(this, DELAY_MILLIS);
            try {
                if (mState == IDEL) {
                    handleIdel();
                } else if (mState == LOGIN) {
                    if (TimeChecker.checkClockTime()) {
                        handleLogin();
                    }
                } else if (mState == MAIN) {
                    handleMain();
                } else if (mState == GESTURE) {
                    handleGesture();
                } else if (mState == CLOCK) {
                    handleClock();
                } else if (mState == SETTING) {
                    handleSetting();
                } else if (mState == UPLOAD) {
                    handleUpload();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    private void handleIdel() {
        performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
    }

    private void handleLogin() {
        LoginHelper.DO.handleLogin();
    }

    private void handleMain() {
        MainHelper.DO.handleMain();
    }

    private void handleGesture() {
        GestureHelper.DO.handleGesture(this);
    }

    private void handleClock() {
        ClockHelper.DO.handleClock(this);
    }

    private void handleSetting() {
        SettingHelper.DO.handleSetting(this);
    }

    private void handleUpload() {
        UploadHelper.DO.handleUpload(this);
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        Log.wtf(TAG, "授权成功");
        init();
    }

    private void init() {
        mState = IDEL;
        AccountInfo.INFO.init();
        LoginHelper.DO.init();
        MainHelper.DO.init();
        GestureHelper.DO.init();
        ClockHelper.DO.init();
        SettingHelper.DO.init();
        UploadHelper.DO.init();
        mHandler.removeCallbacksAndMessages(null);
        mHandler.postDelayed(mHeartBeat, DELAY_MILLIS);
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        Log.wtf(TAG, "监控事件 >>>>>> " + accessibilityEvent.toString());
        if (accessibilityEvent.getEventType() != AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            return;
        }
        try {
            if ("com.netease.oa.ui.security.LoginActivityV5".equals(accessibilityEvent.getClassName())) {
                Log.wtf(TAG, "处理事件 >>>>>> 账号登录");
                mState = LOGIN;
                LoginHelper.DO.onAccessibilityEvent(accessibilityEvent);
            } else if ("com.netease.oa.ui.main.MainTabActivityV6".equals(accessibilityEvent.getClassName())) {
                Log.wtf(TAG, "处理事件 >>>>>> 主界面");
                mState = MAIN;
                MainHelper.DO.onAccessibilityEvent(accessibilityEvent);
            } else if ("com.netease.oa.ui.security.GestureUnlcokActivity".equals(accessibilityEvent.getClassName())
                    || "com.netease.oa.ui.security.TouchIdAndPatternActivity".equals(accessibilityEvent.getClassName())) {
                Log.wtf(TAG, "处理事件 >>>>>> 手势密码");
                mState = GESTURE;
                GestureHelper.DO.onAccessibilityEvent(accessibilityEvent);
            } else if ("com.netease.oa.ui.submodules.moveclockin.MovingClockMainActivity".equals(accessibilityEvent.getClassName())) {
                Log.wtf(TAG, "处理事件 >>>>>> 移动打卡");
                mState = CLOCK;
                ClockHelper.DO.onAccessibilityEvent(accessibilityEvent);
            } else if ("com.netease.oa.ui.setting.SettingActivity".equals(accessibilityEvent.getClassName())) {
                Log.wtf(TAG, "处理事件 >>>>>> 设置退出");
                mState = SETTING;
                SettingHelper.DO.onAccessibilityEvent(accessibilityEvent);
            } else if ("com.netease.oa.ui.submodules.moveclockin.ClockInTipDialog".equals(accessibilityEvent.getClassName())) {
                Log.wtf(TAG, "处理事件 >>>>>> 打卡结果");
                //无论成功失败只记录点击操作
                AccountInfo.INFO.setClocked(true);
                if (accessibilityEvent.getText().toString().contains("打卡成功")) {
                    Log.wtf(TAG, "打卡成功 >>>>>>");
                } else {
                    Log.wtf(TAG, "打卡失败 >>>>>>");
                }
            } else if ("android.support.v7.app.c".equals(accessibilityEvent.getClassName())
                    && accessibilityEvent.getText().toString().contains("发现新版本")) {
                Log.wtf(TAG, "处理事件 >>>>>> 发现新版本");
                mState = UPLOAD;
                UploadHelper.DO.onAccessibilityEvent(accessibilityEvent);
            } else {
                Log.wtf(TAG, "处理事件 >>>>>> 未知事件");
                mState = IDEL;
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
            Log.i(TAG, "深度查找 Parent >>>>>>" + accessibilityNodeInfo);
            for (int i = 0; i < accessibilityNodeInfo.getChildCount(); i++) {
                dfs(accessibilityNodeInfo.getChild(i));
            }
        } else {
            Log.i(TAG, "深度查找 Child >>>>>>" + accessibilityNodeInfo);
        }
    }

    @Override
    public void onInterrupt() {
        Log.wtf(TAG, "授权中断");
        clear();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.wtf(TAG, "服务销毁");
        clear();
    }

    private void clear() {
        mState = IDEL;
        AccountInfo.INFO.clear();
        LoginHelper.DO.clear();
        MainHelper.DO.clear();
        ClockHelper.DO.clear();
        GestureHelper.DO.clear();
        SettingHelper.DO.clear();
        UploadHelper.DO.clear();
        mHandler.removeCallbacksAndMessages(null);
    }

    /**
     * 打开辅助服务的设置
     */
    public static void openAccessibilityServiceSettings(Activity context) {
        try {
            Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
