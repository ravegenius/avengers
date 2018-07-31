package com.jason.avengers.other.activities.drawer;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jason.avengers.R;
import com.jason.avengers.base.BaseActivity;
import com.jason.avengers.widgets.scrolllayout.ScrollLayout;
import com.jason.avengers.widgets.viewpager.CustomViewPager;
import com.jason.core.utils.SystemUtils;

/**
 * Created by jason on 2018/4/8.
 */

public class DrawerActivity extends BaseActivity {

    private ScrollLayout mScrollLayout;
    private TextView mFootBtn;
    private CustomViewPager mViewPager;
    private int mPageMargin = 10;
    private int mOffscreenPageLimit = 2;
    private int mPaddingLeftAndRight;
    private float mScaleMax;

    private ScrollLayout.OnScrollChangedListener mOnScrollChangedListener = new ScrollLayout.OnScrollChangedListener() {
        @SuppressLint("NewApi")
        @Override
        public void onScrollProgressChanged(float currentProgress) {
            System.out.println("jason==" + "onScrollProgressChanged>>" + currentProgress);
            if (null != mFootBtn) mFootBtn.setVisibility(View.GONE);
            if (mViewPager.getChildCount() == 0) return;
            if (currentProgress >= 0) {
                float scale = (1 - currentProgress) * (1 - mScaleMax) + mScaleMax;
                mViewPager.setScaleX(scale);
                mViewPager.setScaleY(scale);
            }
        }

        @Override
        public void onScrollFinished(ScrollLayout.Status currentStatus) {
            System.out.println("jason==" + "onScrollFinished>>" + currentStatus);
            if (currentStatus.equals(ScrollLayout.Status.EXIT)) {
                if (null != mFootBtn)
                    mFootBtn.setVisibility(View.VISIBLE);
            } else if (currentStatus.equals(ScrollLayout.Status.CLOSED)) {
                mViewPager.setScrollable(false);
            } else if (currentStatus.equals(ScrollLayout.Status.OPENED)) {
                mViewPager.setScrollable(true);
            }
        }

        @Override
        public void onChildScroll(int top) {
            System.out.println("jason==" + "onChildScroll>>" + top);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        initView();
    }

    private void initView() {
        mPaddingLeftAndRight = (int) (SystemUtils.Screen.getScreenWidth(this) * 0.05f);
        mScaleMax = 1.f - ((float) mPaddingLeftAndRight * 2.0000f + (float) mPageMargin) / (float) SystemUtils.Screen.getScreenWidth(this);


        RelativeLayout relativeLayout = findViewById(R.id.root);
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mScrollLayout != null)
                    mScrollLayout.scrollToExit();
            }
        });

        mFootBtn = findViewById(R.id.foot_btn);
        mFootBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mScrollLayout != null)
                    mScrollLayout.setToOpen();
            }
        });
        mFootBtn.setVisibility(View.GONE);

        mViewPager = findViewById(R.id.content_view_pager);
        mViewPager.setAdapter(new DrawerViewPagerAdapter(getSupportFragmentManager()));
        mViewPager.setOffscreenPageLimit(mOffscreenPageLimit);
        mViewPager.setPageMargin(mPageMargin);
        mViewPager.setScaleX(mScaleMax);
        mViewPager.setScaleY(mScaleMax);
        mViewPager.setScrollable(true);
        mViewPager.setCurrentItem(3);
        mViewPager.addOnPageChangeListener(new CustomViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        mScrollLayout = findViewById(R.id.scroll_down_layout);
        mScrollLayout.setMinOffset(0);
        mScrollLayout.setMaxOffset((int) (SystemUtils.Screen.getScreenHeightWithoutStatusBar(this) * DrawerConfigs.DrawerOffset));
        mScrollLayout.setExitOffset(SystemUtils.Dimen.dp2px(this, 50));
        mScrollLayout.setIsSupportExit(true);
        mScrollLayout.setAllowHorizontalScroll(true);
        mScrollLayout.setOnScrollChangedListener(mOnScrollChangedListener);
        mScrollLayout.setToOpen();
        setScrollLayoutBackgroundAlpha(1.f);
    }

    public void setScrollLayoutDraggable(boolean isDraggable) {
        if (mScrollLayout != null) mScrollLayout.setDraggable(isDraggable);
    }

    private void setScrollLayoutBackgroundAlpha(float currentProgress) {
        int alpha = calculateAlpha(currentProgress);
        if (mScrollLayout != null)
            mScrollLayout.getBackground().setAlpha(alpha);
    }

    private int calculateAlpha(float currentProgress) {
        float precent = 255 * currentProgress;
        if (precent > 150) {
            precent = 150;
        } else if (precent < 55) {
            precent = 55;
        }
        return 255 - (int) precent;
    }

}
