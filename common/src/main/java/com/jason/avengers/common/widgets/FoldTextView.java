package com.jason.avengers.common.widgets;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v7.widget.AppCompatTextView;
import android.text.Layout;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.ViewTreeObserver;

import com.jason.avengers.common.R;

/**
 * Created by Jason on 2018/6/28 10:26.
 * 折叠textView
 */
public class FoldTextView extends AppCompatTextView {

    private static final String TAG = "FoldTextView";
    private static final String ELLIPSIZE_END = "...";
    protected static final String TIP_EXPAND_TEXT = "展开";
    protected static final String TIP_ALL_TEXT = "全文";
    private static final String TIP_FOLD_TEXT = "收起";
    private static final int DEFAULT_MAX_VALUE = 4;

    // 可视最大行数
    protected int mShowMaxLine;
    private int mRangeMaxLine;

    // 折叠
    private String mFoldText;
    private ColorStateList mFoldColor;
    private boolean mFoldable;
    private boolean mFoldShow;

    // 展开
    protected String mExpandText;
    private ColorStateList mExpandColor;
    protected boolean mExpandable;
    protected boolean mExpandShow;
    // 展开文案是否加粗，默认不加粗
    protected boolean mExpandBold;
    // 原始文本
    private CharSequence mOriginalText;
    // 原始文本的行数
    protected int mOriginalLineCount;
    // 提示文字坐标
    protected float minX, maxX, minY, maxY;
    // 收起全文不在一行显示时
    protected float middleY1, middleY2;
    // 点击时间
    private long clickTime;
    // 是否已执行PreDraw
    private boolean isPreDrawDone;
    // 是否超过最大行数
    private boolean isOverMaxLine;
    // 当前状态是否已展开
    private boolean isExpanded;
    // 是否直接显示文字
    private boolean isImmediately;
    // 是否需要计算 减少监听
    private boolean isNeedCalculate;
    private OnTipClickListener onTipClickListener;
    // 是否需要展开或收起的递归请求 不使用OnGlobalLayoutListener因为回调时间间隔过长
    private Runnable mSetTextRunnable;
    // 是否需要展开或收起的递归请求间隔时间
    private long mSetTextRunnableDelayedTime = 100L;

    public FoldTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FoldTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setEllipsize(TextUtils.TruncateAt.END);
        setMaxLines(Integer.MAX_VALUE);

        initTypedParams(context, attrs);
    }

    protected void initTypedParams(Context context, AttributeSet attrs) {
        TypedArray arr = context.obtainStyledAttributes(attrs, R.styleable.FoldTextView);
        mShowMaxLine = arr.getInt(R.styleable.FoldTextView_ftvMaxLine, DEFAULT_MAX_VALUE);
        mRangeMaxLine = arr.getInt(R.styleable.FoldTextView_ftvRangeMaxLine, mShowMaxLine);
        if (mRangeMaxLine > mShowMaxLine) {
            mRangeMaxLine = mShowMaxLine;
        }

        mExpandText = arr.getString(R.styleable.FoldTextView_ftvExpandText);
        mExpandColor = arr.getColorStateList(R.styleable.FoldTextView_ftvExpandColor);
        mExpandable = arr.getBoolean(R.styleable.FoldTextView_ftvExpandable, false);
        mExpandShow = arr.getBoolean(R.styleable.FoldTextView_ftvExpandShow, true);
        mExpandBold = arr.getBoolean(R.styleable.FoldTextView_ftvExpandBold, false);

        mFoldText = arr.getString(R.styleable.FoldTextView_ftvFoldText);
        mFoldColor = arr.getColorStateList(R.styleable.FoldTextView_ftvFoldColor);
        mFoldable = arr.getBoolean(R.styleable.FoldTextView_ftvFoldable, false);
        mFoldShow = arr.getBoolean(R.styleable.FoldTextView_ftvFoldShow, false);
        arr.recycle();

        if (TextUtils.isEmpty(mExpandText)) {
            mExpandText = TIP_EXPAND_TEXT;
        }
        if (TextUtils.isEmpty(mFoldText)) {
            mFoldText = TIP_FOLD_TEXT;
        }
        if (!mExpandShow) {
            mExpandText = "";
        }
    }

    /**
     * @param preDrawDone
     * @deprecated 未解决跟贴盖楼宽度动态复用改变导致计算文字宽度出错
     * 可能存在性能卡顿问题 需要修改成根据ItemType生成Holder实例来复用FoldTextView 不需要重新计算宽度
     */
    public void setPreDrawDone(boolean preDrawDone) {
        isPreDrawDone = preDrawDone;
    }

    @Override
    public void setText(final CharSequence text, final BufferType type) {
        if (isImmediately) {
            super.setText(text, type);
            return;
        }
        minX = maxX = minY = maxY = middleY1 = middleY2 = 0f;
        mOriginalText = childCustomSpan(text);
        isNeedCalculate = true;
        if (TextUtils.isEmpty(mOriginalText)) {
            isNeedCalculate = false;
        } else if (mOriginalText.length() <= 15 * mShowMaxLine) {
            isNeedCalculate = false;
        }
        if (TextUtils.isEmpty(text) || mShowMaxLine == 0) {
            super.setText(text, type);
        } else if (isExpanded) {
            //文字已展开
            if (!isPreDrawDone) {
                getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        getViewTreeObserver().removeOnPreDrawListener(this);
                        isPreDrawDone = true;
                        setFoldText(type);
                        return true;
                    }
                });
            } else {
                setFoldText(type);
            }
        } else {
            //文字未展开
            if (!isPreDrawDone) {
                getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        getViewTreeObserver().removeOnPreDrawListener(this);
                        isPreDrawDone = true;
                        setExpandText(type);
                        return true;
                    }
                });
            } else {
                setExpandText(type);
            }
        }
    }

    protected void setFoldText(final BufferType type) {
        SpannableStringBuilder spannable = new SpannableStringBuilder(mOriginalText);
        if (mFoldShow) {
            spannable.append(mFoldText);
            int color = mFoldColor == null ? getTextColors().getDefaultColor() : mFoldColor.getDefaultColor();
            spannable.setSpan(new ForegroundColorSpan(color), spannable.length() - mFoldText.length(), spannable.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        }
        super.setText(spannable, type);
        Layout layout = getLayout();
        if (layout == null) {
            if (mSetTextRunnable == null) {
                mSetTextRunnable = buildSetExpandTextRunnable(type);
            }
            postDelayed(mSetTextRunnable, mSetTextRunnableDelayedTime);
        } else {
            setFoldText(layout, type);
        }
    }

    protected void setFoldText(Layout layout, BufferType type) {
        if (layout == null) {
            return;
        }
        minX = getPaddingLeft() + layout.getPrimaryHorizontal(getText().toString().lastIndexOf(mFoldText.charAt(0)));
        maxX = getPaddingLeft() + layout.getSecondaryHorizontal(getText().toString().lastIndexOf(mFoldText.charAt(mFoldText.length() - 1)) + 1);
        Rect bound = new Rect();
        if (maxX < minX) {
            if (getLineCount() < 2) {
                return;
            }
            //不在同一行
            layout.getLineBounds(getLineCount() - 2, bound);
            minY = getPaddingTop() + bound.top;
            middleY1 = minY + getPaint().getFontMetrics().descent - getPaint().getFontMetrics().ascent;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                middleY2 = middleY1 + getLineSpacingExtra();
            } else {
                middleY2 = middleY1;
            }
            maxY = middleY2 + getPaint().getFontMetrics().descent - getPaint().getFontMetrics().ascent;
        } else {
            if (getLineCount() < 1) {
                return;
            }
            //同一行
            layout.getLineBounds(getLineCount() - 1, bound);
            minY = getPaddingTop() + bound.top;
            maxY = minY + getPaint().getFontMetrics().descent - getPaint().getFontMetrics().ascent;
        }
    }

    protected void setExpandText(final BufferType type) {
        Layout layout = getLayout();
        if (layout == null || !layout.getText().equals(mOriginalText)) {
            super.setText(mOriginalText, type);
            layout = getLayout();
        }
        if (!isNeedCalculate) {
            return;
        }
        if (layout == null) {
            if (mSetTextRunnable == null) {
                mSetTextRunnable = buildSetExpandTextRunnable(type);
            }
            postDelayed(mSetTextRunnable, mSetTextRunnableDelayedTime);
        } else {
            setExpandText(layout, type);
        }
    }

    protected Runnable buildSetExpandTextRunnable(final BufferType type) {
        return new Runnable() {
            @Override
            public void run() {
                Layout layout = getLayout();
                if (layout != null) {
                    if (isExpanded) {
                        setFoldText(layout, type);
                    } else {
                        setExpandText(layout, type);
                    }
                } else {
                    postDelayed(this, mSetTextRunnableDelayedTime);
                }
            }
        };
    }

    protected void setExpandText(Layout layout, BufferType type) {
        if (layout == null || TextUtils.isEmpty(mOriginalText)) {
            return;
        }
        mOriginalLineCount = layout.getLineCount();
        if (layout.getLineCount() > mShowMaxLine) {
            isOverMaxLine = true;
            SpannableStringBuilder spannable = new SpannableStringBuilder();
            float viewWidth = layout.getWidth();
            int start = layout.getLineStart(mRangeMaxLine - 1);
            int end = layout.getLineEnd(mRangeMaxLine - 1);
            int length = mOriginalText.length();
            boolean isCalculated = false;
            int calculateEllipsisPosition = 0;
            float ellipsisWidth = getTextWidth(ELLIPSIZE_END + mExpandText);
            String tempText;
            if (length > end) {
                isCalculated = true;
                tempText = mOriginalText.subSequence(start, end).toString().replace("\n", "");
            } else if (length > start) {
                tempText = mOriginalText.subSequence(start, length).toString();
            } else {
                tempText = "";
            }

            float tempTextWidth = getTextWidth(tempText);
            if (tempTextWidth <= viewWidth && length <= end) {
                isCalculated = false;
            } else {
                if (tempTextWidth + (isCalculated ? ellipsisWidth : 0) > viewWidth) {
                    float remainWidth = viewWidth - ellipsisWidth;
                    // 多余出一些位置可以保证文案不换行
                    float extRemainWidth = 0.25f * ellipsisWidth;
                    if (remainWidth > extRemainWidth) {
                        remainWidth = remainWidth - extRemainWidth;
                    }
                    do {
                        calculateEllipsisPosition = calculateEllipsisPosition + 1;
                        if (!TextUtils.isEmpty(tempText) && tempText.length() > calculateEllipsisPosition) {
                            tempTextWidth = getTextWidth(tempText.subSequence(0, calculateEllipsisPosition).toString());
                        }
                    } while (tempTextWidth < remainWidth
                            && calculateEllipsisPosition + 1 < tempText.length());
                    if (calculateEllipsisPosition > 0) {
                        calculateEllipsisPosition -= 1;
                    }
                    isCalculated = calculateEllipsisPosition != 0;
                } else {
                    if (tempText.length() > 0) {
                        calculateEllipsisPosition = tempText.length() - 1;
                    }
                }
            }

            if (!isCalculated) {
                spannable.append(mOriginalText);
            } else {
                minX = getPaddingLeft()
                        + getTextWidth(tempText.subSequence(0, calculateEllipsisPosition).toString())
                        + getTextWidth(ELLIPSIZE_END);
                maxX = minX + getTextWidth(mExpandText);

                spannable.append(mOriginalText.subSequence(0, start + calculateEllipsisPosition));
                spannable.append(ELLIPSIZE_END);
                spannable.append(mExpandText);
                int color = mExpandColor == null ? getTextColors().getDefaultColor() : mExpandColor.getDefaultColor();
                spannable.setSpan(new ForegroundColorSpan(color), spannable.length() - mExpandText.length(), spannable.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                if (mExpandBold) {
                    spannable.setSpan(new StyleSpan(Typeface.BOLD), spannable.length() - mFoldText.length(), spannable.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                }
            }
            super.setText(spannable, type);
        } else {
            isOverMaxLine = false;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isOverMaxLine && !isExpanded) {
            minY = getHeight() - (getPaint().getFontMetrics().descent - getPaint().getFontMetrics().ascent) - getPaddingBottom();
            maxY = getHeight() - getPaddingBottom();
        }
        if ((isExpanded && !mFoldable) || (!isExpanded && !mExpandable)) {
            minX = maxX = minY = maxY = 0;
        }
        /* 用于展示点击区域便于修改
        if (BuildConfig.DEBUG) {
            Paint paint = new Paint();
            paint.setColor(Color.RED);
            paint.setAlpha(80);
            canvas.drawRect(minX, minY, maxX, maxY, paint);
        }
        */
    }

    protected float getTextWidth(String text) {
        Paint paint = getPaint();
        return paint.measureText(text);
    }

    //可不可以展开
    public FoldTextView setExpandable(boolean expandable) {
        mExpandable = expandable;
        return this;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if ((mFoldable && isExpanded) || (mExpandable && !isExpanded)) {
            switch (event.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                    if (handleActionDown(event)) {
                        return true;
                    }
                    break;
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:
                    if (handleActionCancelOrUp(event)) {
                        return true;
                    }
                    break;
                default:
                    break;
            }
        }
        return super.onTouchEvent(event);
    }

    protected boolean handleActionDown(MotionEvent event) {
        clickTime = System.currentTimeMillis();
        if (!isClickable()) {
            if (isInRange(event.getX(), event.getY())) {
                return true;
            }
        }
        return false;
    }

    protected boolean handleActionCancelOrUp(MotionEvent event) {
        long delTime = System.currentTimeMillis() - clickTime;
        clickTime = 0L;
        if (delTime < 3 * ViewConfiguration.getTapTimeout() && isInRange(event.getX(), event.getY())) {
            isExpanded = !isExpanded;
            setText(mOriginalText);
            cancelLongPress();
            if (onTipClickListener != null) {
                onTipClickListener.onTipClick(isExpanded);
            }
            return true;
        }
        return false;
    }

    protected boolean isInRange(float x, float y) {
        if (minX < maxX) {
            return x >= minX && x <= maxX && y >= minY && y <= maxY;
        } else {
            return x <= maxX && y >= middleY2 && y <= maxY || x >= minX && y >= minY && y <= middleY1;
        }
    }

    public FoldTextView setShowMaxLines(int maxLines) {
        mShowMaxLine = maxLines;
        if (mRangeMaxLine > mShowMaxLine) {
            mRangeMaxLine = mShowMaxLine;
        }
        return this;
    }

    public FoldTextView setRangeMaxLines(int maxLines) {
        mRangeMaxLine = maxLines;
        if (mRangeMaxLine > mShowMaxLine) {
            mShowMaxLine = mRangeMaxLine;
        }
        return this;
    }

    public FoldTextView setFoldColor(ColorStateList color) {
        mFoldColor = color;
        return this;
    }

    public FoldTextView setExpandColor(ColorStateList color) {
        mExpandColor = color;
        return this;
    }

    public FoldTextView setExpanded(boolean expand) {
        isExpanded = expand;
        return this;
    }

    public FoldTextView setImmediately(boolean immediately) {
        isImmediately = immediately;
        return this;
    }

    public FoldTextView setOnTipClickListener(FoldTextView.OnTipClickListener onTipClickListener) {
        this.onTipClickListener = onTipClickListener;
        return this;
    }

    public String getExpandText() {
        return mExpandText;
    }

    /**
     * 交由子类实现
     * 如需额外修改文本内容(例如在头部添加"精选"文案)，需在setText之前做处理
     * <p>
     * 调用时机：1、不添加"全文"时
     * 2、添加"全文"后
     * 3、点击"全文"展开后
     *
     * @param origin
     */
    protected CharSequence childCustomSpan(CharSequence origin) {
        return origin;
    }

    public interface OnTipClickListener {
        void onTipClick(boolean isExpanded);
    }

    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setText(mOriginalText);
    }
}