package com.jason.avengers.common.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import com.jason.avengers.common.R;

/**
 * 平行视窗广告ImageView
 * <p>
 * 1. 如果向下超出底部则显示图片最下面的可显示区域 且对齐屏幕下边界
 * 2. 如果已经滑动到底部则显示图片最下面的可显示区域
 * 3. 如果向上超出顶部则显示图片最上面的可显示区域 且对齐屏幕上边界
 * 4. 如果已经滑动到顶部则显示图片最上面的可显示区域
 * 5. 因为 在最顶部时显示图片的最上下面区域 在最底部时显示图片的最上面区域
 * 所以 滑动顶点（Top）位置比例和图片显示区域顶点位置比例是正比例
 * 公式为：图片显示区域顶点位置 / 图片显示区域顶点位置区域 = 滑动顶点位置 /  滑动顶点位置区域
 * 图片显示区域顶点位置           drawableBoundTopY
 * 图片显示区域顶点位置区域        drawableMeasuredHeight - drawableBoundHeight
 * 滑动顶点位置                  parentViewTop
 * 滑动顶点位置区域               scrollHeight - parentViewHeight
 * Created by jason on 2018/5/7.
 */

public class AdParallelImageView extends AppCompatImageView {

    private final float mWHRatio;
    private final int mLayoutLevel;
    private int mScrollHeightCache;
    private int mItemViewHeightCache;
    private int mItemViewTopCache;
    private int mTopHeightCache;
    private boolean isInit = true;

    public AdParallelImageView(Context context) {
        this(context, null);
    }

    public AdParallelImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.AdParallelImageView);
        mWHRatio = typedArray.getFloat(R.styleable.AdParallelImageView_whRatio, 1.0f);
        mLayoutLevel = typedArray.getInt(R.styleable.AdParallelImageView_layoutLevel, 1);
        setScaleType(ScaleType.MATRIX);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measuredWidth = getMeasuredWidth();
        int height = (int) (measuredWidth / mWHRatio);
        if (height != getMeasuredHeight()) {
            setMeasuredDimension(measuredWidth, height);
        }
    }

    public void setDrawableBoundTopY(int parentViewTop, int topHeight) {
        mItemViewTopCache = parentViewTop;
        mTopHeightCache = topHeight;
        invalidate();
    }

    /**
     * 计算移动显示区域Top顶点
     *
     * @param drawable
     * @param scrollHeight
     * @param itemViewHeight
     * @param itemViewTop
     * @param topHeight
     * @return
     */
    private int computeDrawableBoundTopY(@NonNull Drawable drawable, int scrollHeight, int itemViewHeight, int itemViewTop, int topHeight) {
        int drawableBoundTopY = 0;
        int drawableBoundHeight = getMeasuredHeight();
        int drawableMeasuredHeight = (int) ((float) getMeasuredWidth() / (float) drawable.getIntrinsicWidth() * (float) drawable.getIntrinsicHeight());
        if (drawableMeasuredHeight <= drawableBoundHeight) return drawableBoundTopY;

        if (scrollHeight - itemViewTop - topHeight - drawableBoundHeight <= 0) {
            // 如果向下超出底部则显示图片最下面的可显示区域 且对齐屏幕下边界
            // drawableBoundTopY = drawableMeasuredHeight - drawableBoundHeight + (itemViewTop + topHeight + drawableBoundHeight - scrollHeight);
            drawableBoundTopY = drawableMeasuredHeight - scrollHeight + itemViewTop + topHeight;
//        } else if (scrollHeight - itemViewTop - itemViewHeight <= 0) {
//            // 如果已经滑动到底部则显示图片最下面的可显示区域
//            drawableBoundTopY = drawableMeasuredHeight - drawableBoundHeight;
        } else if (itemViewTop + topHeight <= 0) {
            // 如果向上超出顶部则显示图片最上面的可显示区域 且对齐屏幕上边界
            drawableBoundTopY = itemViewTop + topHeight;
//        } else if (itemViewTop <= 0) {
//            // 如果已经滑动到顶部则显示图片最上面的可显示区域
//            drawableBoundTopY = 0;
        } else {
            // 因为 在最顶部时显示图片的最上下面区域 在最底部时显示图片的最上面区域
            // 所以 滑动顶点（Top）位置比例和图片显示区域顶点位置比例是正比例
            // 公式为：图片显示区域顶点位置 / 图片显示区域顶点位置区域 = 滑动顶点位置 /  滑动顶点位置区域
            // 图片显示区域顶点位置           drawableBoundTopY
            // 图片显示区域顶点位置区域        drawableMeasuredHeight - drawableBoundHeight
            // 滑动顶点位置                  parentViewTop
            // 滑动顶点位置区域               scrollHeight - parentViewHeight
            float drawableBoundTopYScope = drawableMeasuredHeight - drawableBoundHeight;
            float scrollTopYScope = scrollHeight - drawableBoundHeight;
            float scrollTopY = itemViewTop + topHeight;
            drawableBoundTopY = (int) (scrollTopY * drawableBoundTopYScope / scrollTopYScope);
        }
        return drawableBoundTopY;
    }

    /**
     * 在onDraw 做计算保证图片初次加载位置正确不需要二次刷新
     *
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        Drawable drawable = getDrawable();
        if (null == drawable) {
            return;
        }

        int w = getWidth();
        int h = (int) (getWidth() * 1.0f / drawable.getIntrinsicWidth() * drawable.getIntrinsicHeight());
        drawable.setBounds(0, 0, w, h);
        canvas.save();
        if (isInit) {
            // 初始化cache数据 需要根据xml中的层级来计算高度
            // 暂时列表只有一个 如果需要多个则需要根据url判断是否需要重新初始化
            View parentView = this;
            for (int index = 0; index < mLayoutLevel; index++) {
                parentView = (View) parentView.getParent();
            }
            mScrollHeightCache = ((View) parentView.getParent()).getMeasuredHeight();
            mItemViewHeightCache = parentView.getMeasuredHeight();
            mItemViewTopCache = parentView.getTop();
            isInit = false;
        }
        // 计算移动显示区域Top顶点
        int drawableBoundTopY = -computeDrawableBoundTopY(drawable, mScrollHeightCache, mItemViewHeightCache, mItemViewTopCache, mTopHeightCache);
        // 通过canvas translate移动显示drawable显示区域
        canvas.translate(0, drawableBoundTopY);
        super.onDraw(canvas);
        canvas.restore();
    }

    /**
     * 静态调用 统一处理API入口 计算【顶】高度并赋值给 AdParallelImageView
     *
     * @param recyclerView
     */
    public static void compute(@NonNull RecyclerView recyclerView, @IdRes int id) {
        if (null != recyclerView.getLayoutManager() && recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            if (null != linearLayoutManager) {
                int fPos = linearLayoutManager.findFirstVisibleItemPosition();
                int lPos = linearLayoutManager.findLastVisibleItemPosition();
                for (int i = fPos; i <= lPos; i++) {
                    View view = linearLayoutManager.findViewByPosition(i);
                    AdParallelImageView adImageView = view.findViewById(id);
                    if (null != adImageView) {
                        int topHeight = computeAdParallelImageViewTopHeight(adImageView);
                        adImageView.setDrawableBoundTopY(view.getTop(), topHeight);
                    }
                }
            }
        }
    }

    /**
     * 计算 AdParallelImageView 到 itemview 【顶】的高度
     *
     * @param adImageView
     */
    private static int computeAdParallelImageViewTopHeight(AdParallelImageView adImageView) {
        return adImageView.getTop();
    }
}
