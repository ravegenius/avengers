package com.jason.core.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Rect;
import android.os.Process;
import android.text.TextUtils;
import android.util.DisplayMetrics;

/**
 * @function 屏幕工具
 * @auther: Created by yinglan
 * @time: 16/3/16
 */
public class SystemUtils {

    public static class App {
        /**
         * 判断是否为主进程
         *
         * @param context
         * @return
         */
        public static boolean isMainProcess(Context context) {
            String packageName = context.getPackageName();
            String processName = getProcessName(context);
            if (packageName != null && packageName.equals(processName)) {
                return true;
            }
            return false;
        }


        /**
         * 获取当前进程名
         *
         * @param context
         * @return
         */
        public static String getProcessName(Context context) {
            String processName = null;
            // 获取ActivityManager
            ActivityManager am = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE));

            // 有的手机ActivityManager为空
            if (am == null || am.getRunningAppProcesses() == null) {
                return processName;
            }
            int count = 0;
            while (true) {
                for (ActivityManager.RunningAppProcessInfo info : am.getRunningAppProcesses()) {
                    if (info.pid == Process.myPid()) {
                        processName = info.processName;
                        break;
                    }
                }

                // 返回进程名
                if (!TextUtils.isEmpty(processName)) {
                    return processName;
                }

                // Take a rest and again
                try {
                    // 最多执行3次
                    if (count > 3) {
                        return context.getPackageName();
                    }
                    Thread.sleep(10L);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                count++;
            }
        }
    }

    public static class Screen {
        /**
         * 获取屏幕内容高度
         *
         * @param activity
         * @return
         */
        public static int getScreenHeightWithoutStatusBar(Activity activity) {
            DisplayMetrics dm = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
            int result = 0;
            int resourceId = activity.getResources()
                    .getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                result = activity.getResources().getDimensionPixelSize(resourceId);
            }
            int screenHeight = dm.heightPixels - result;
            return screenHeight;
        }

        /**
         * 获取屏幕宽度
         *
         * @param activity
         * @return
         */
        public static int getScreenWidth(Activity activity) {
            DisplayMetrics dm = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
            return dm.widthPixels;
        }


        /**
         * 获取手机状态栏高度
         *
         * @param context
         * @return
         */
        public static int getStatusBarHeight(Context context) {
            int result = 0;
            int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                result = context.getResources().getDimensionPixelSize(resourceId);
            }
            return result;
        }

        /**
         * 获取手机状态栏高度
         *
         * @param activity
         * @return
         */
        @SuppressWarnings("ResourceType")
        public static int getStatusBarHeight(Activity activity) {
            int result = 0;
            if (activity != null) {
                Rect rect = new Rect();
                activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
                result = rect.top;
                if (result != 0) {
                    return result;
                }
            }
            return getStatusBarHeight(activity);
        }
    }


    public static class Dimen {
        /**
         * dp转px
         *
         * @param context
         * @param dipValue
         * @return
         */
        public static int dp2px(Context context, float dipValue) {
            final float scale = context.getResources().getDisplayMetrics().density;
            return (int) (dipValue * scale + 0.5f);
        }
    }

}