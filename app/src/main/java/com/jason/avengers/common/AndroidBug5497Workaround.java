package com.jason.avengers.common;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Build;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import com.jason.avengers.base.BaseApplication;
import com.jason.core.utils.SystemUtils;

/**
 * Created by bjwangmingxian on 2017/10/11.
 * <p>
 * 解决输入框被软键盘隐藏的问题 @See http://www.jianshu.com/p/a95a1b84da11 </br>
 * 保证这个对象要在setContentView之后初始化
 */

public class AndroidBug5497Workaround {

    // For more information, see https://code.google.com/p/android/issues/detail?id=5497

    private ViewTreeObserver.OnGlobalLayoutListener mOnGlobalLayoutListener = null;
    private FrameLayout.LayoutParams mFrameLayoutParams;
    private FrameLayout.LayoutParams mOrigLayoutParams;
    private boolean mIsContentHeightMeasured = false;
    private int mUsableHeightPrevious;
    private View mChildOfContent;
    private int mContentHeight;


    public AndroidBug5497Workaround(Activity activity) {

        if (activity != null && Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            FrameLayout content = (FrameLayout) activity.findViewById(android.R.id.content);
            mChildOfContent = content.getChildAt(0);
            if (mChildOfContent != null) {
                mFrameLayoutParams = (FrameLayout.LayoutParams)
                        mChildOfContent.getLayoutParams();
                mOrigLayoutParams = new FrameLayout.LayoutParams(mFrameLayoutParams);
                addGlobalLayoutListener();
            }
        }
    }

    /**
     * 全屏状态下如果还有OnGlobalLayoutListener监听会导致播放器内容显示不全
     * 因此需要在Activity变成全屏前手动移除监听，变成非全屏前手动添加监听
     */
    public void addGlobalLayoutListener() {

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            removeGlobalLayoutListener();

            mOnGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
                public void onGlobalLayout() {
                    if (!mIsContentHeightMeasured) {
                        //兼容华为等机型
                        mContentHeight = mChildOfContent.getHeight();
                        mIsContentHeightMeasured = true;
                    }
                    possiblyResizeChildOfContent();
                }
            };
            mChildOfContent.getViewTreeObserver()
                    .addOnGlobalLayoutListener(mOnGlobalLayoutListener);
        }
    }

    public void removeGlobalLayoutListener() {

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            if (mOnGlobalLayoutListener != null && mChildOfContent != null) {
                mFrameLayoutParams = new FrameLayout.LayoutParams(mOrigLayoutParams);
                mChildOfContent.setLayoutParams(mFrameLayoutParams);
                mChildOfContent.getViewTreeObserver()
                        .removeOnGlobalLayoutListener(mOnGlobalLayoutListener);
                mOnGlobalLayoutListener = null;
            }
        }
    }

    /**
     * 重新调整跟布局的高度
     */
    private void possiblyResizeChildOfContent() {

        int usableHeightNow = computeUsableHeight();

        //当前可见高度和上一次可见高度不一致 布局变动
        if (usableHeightNow != mUsableHeightPrevious) {
            int usableHeightSansKeyboard = mChildOfContent.getRootView().getHeight();
            int heightDifference = usableHeightSansKeyboard - usableHeightNow;
            if (heightDifference > (usableHeightSansKeyboard / 4)) {
                // keyboard probably just became visible
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
                    mFrameLayoutParams.height = usableHeightSansKeyboard - heightDifference +
                            SystemUtils.Screen.getStatusBarHeight(BaseApplication.getInstance());
                } else {
                    mFrameLayoutParams.height = usableHeightSansKeyboard - heightDifference;
                }
            } else {
                mFrameLayoutParams.height = mContentHeight;
            }

            mChildOfContent.setLayoutParams(mFrameLayoutParams);
            mChildOfContent.requestLayout();
            mUsableHeightPrevious = usableHeightNow;
        }
    }

    /**
     * 计算mChildOfContent可见高度
     *
     * @return
     */
    private int computeUsableHeight() {
        Rect r = new Rect();
        mChildOfContent.getWindowVisibleDisplayFrame(r);
        return (r.bottom - r.top);
    }

}
