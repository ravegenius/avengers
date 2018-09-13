package com.jason.avengers.common.widgets;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.AppCompatTextView;
import android.text.Layout;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.jason.avengers.common.R;

/**
 * Created by Jason on 2018/6/28 10:26.
 * 折叠textView
 */
public class FoldTextView extends AppCompatTextView {

    private static final String ELLIPSIZE_END = "...";
    private static final String TIP_EXPAND_TEXT = "展开";
    private static final String TIP_FOLD_TEXT = "收起";
    private static final int DEFAULT_MAX_VALUE = 4;

    // 可视最大行数
    private int mShowMaxLine;

    // 折叠
    private String mFoldText;
    private ColorStateList mFoldColor;
    private boolean mFoldable;
    private boolean mFoldShow;

    // 展开
    private String mExpandText;
    private ColorStateList mExpandColor;
    private boolean mExpandable;
    // 原始文本
    private CharSequence mOriginalText;
    // 原始文本的行数
    private int mOriginalLineCount;
    // 提示文字坐标
    private float minX, maxX, minY, maxY;
    // 收起全文不在一行显示时
    private float middleY;
    // 点击时间
    private long clickTime;
    // 是否已执行PreDraw
    private boolean isPreDrawDone;
    // 是否超过最大行数
    private boolean isOverMaxLine;
    // 当前状态是否已展开
    private boolean isExpanded;

    private OnTipClickListener onTipClickListener;

    public FoldTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FoldTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setEllipsize(TextUtils.TruncateAt.END);
        setMaxLines(Integer.MAX_VALUE);

        initTypedParams(context, attrs);
    }

    private void initTypedParams(Context context, AttributeSet attrs) {
        TypedArray arr = context.obtainStyledAttributes(attrs, R.styleable.FoldTextView);
        mShowMaxLine = arr.getInt(R.styleable.FoldTextView_ftvMaxLine, DEFAULT_MAX_VALUE);

        mExpandText = arr.getString(R.styleable.FoldTextView_ftvExpandText);
        mExpandColor = arr.getColorStateList(R.styleable.FoldTextView_ftvExpandColor);
        mExpandable = arr.getBoolean(R.styleable.FoldTextView_ftvExpandable, false);

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
    }

    @Override
    public void setText(final CharSequence text, final TextView.BufferType type) {
        mOriginalText = text;
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

    private void setFoldText(final TextView.BufferType type) {
        final SpannableStringBuilder spannable = new SpannableStringBuilder(mOriginalText);
        if (mFoldShow) {
            spannable.append(mFoldText);
            spannable.setSpan(new ForegroundColorSpan(mFoldColor.getDefaultColor()), spannable.length() - mFoldText.length(), spannable.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        }
        super.setText(spannable, type);
        Layout layout = getLayout();
        if (layout == null) {
            getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onGlobalLayout() {
                    getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    setFoldText(getLayout(), type);
                }
            });
        } else {
            setFoldText(layout, type);
        }
    }

    private void setFoldText(Layout layout, TextView.BufferType type) {
        if (layout == null) return;
        minX = getPaddingLeft() + layout.getPrimaryHorizontal(getText().toString().lastIndexOf(mFoldText.charAt(0)) - 1);
        maxX = getPaddingLeft() + layout.getSecondaryHorizontal(getText().toString().lastIndexOf(mFoldText.charAt(mFoldText.length() - 1)) + 1);
        Rect bound = new Rect();
        if (maxX < minX) {
            //不在同一行
            layout.getLineBounds(getLineCount() - 1, bound);
            minY = getPaddingTop() + bound.top;
            middleY = minY + getPaint().getFontMetrics().descent - getPaint().getFontMetrics().ascent;
            maxY = middleY + getPaint().getFontMetrics().descent - getPaint().getFontMetrics().ascent;
        } else {
            //同一行
            layout.getLineBounds(getLineCount() - 1, bound);
            minY = getPaddingTop() + bound.top;
            maxY = minY + getPaint().getFontMetrics().descent - getPaint().getFontMetrics().ascent;
        }
    }

    private void setExpandText(final TextView.BufferType type) {
        Layout layout = getLayout();
        if (layout == null || !layout.getText().equals(mOriginalText)) {
            super.setText(mOriginalText, type);
            layout = getLayout();
        }
        if (layout == null) {
            getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onGlobalLayout() {
                    getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    setExpandText(getLayout(), type);
                }
            });
        } else {
            setExpandText(layout, type);
        }
    }

    private void setExpandText(Layout layout, TextView.BufferType type) {
        if (layout == null) return;
        mOriginalLineCount = layout.getLineCount();
        if (layout.getLineCount() > mShowMaxLine) {
            isOverMaxLine = true;
            SpannableStringBuilder spannable = new SpannableStringBuilder();

            float viewWidth = getMeasuredWidth()
                    - getPaddingLeft() - getCompoundPaddingLeft()
                    - getPaddingRight() - getCompoundPaddingRight()
                    - getTextSize();
            float lineWidth = getLayout().getLineWidth(mShowMaxLine - 1);
            int start = layout.getLineStart(mShowMaxLine - 1);
            int end = layout.getLineEnd(mShowMaxLine - 1);
            int length = mOriginalText.length();
            boolean isCalculated = false;
            String calculateText = mOriginalText.toString();
            StringBuilder builder = new StringBuilder(ELLIPSIZE_END).append(mExpandText);
            float ellipsisWidth = getPaint().measureText(builder.toString());
            String tempText;
            if (length > end) {
                isCalculated = true;
                tempText = mOriginalText.subSequence(start, end).toString().replace("\n", "");
                calculateText = mOriginalText.subSequence(0, start).toString() + tempText;
            } else {
                tempText = mOriginalText.subSequence(start, length).toString();
            }

            float tempTextWidth = getPaint().measureText(tempText);
            if (lineWidth == tempTextWidth && length <= end) {
                isCalculated = false;
            } else if (tempTextWidth + (isCalculated ? ellipsisWidth : 0) > viewWidth) {
                float remainWidth = (viewWidth - ellipsisWidth);

                tempTextWidth = 0f;
                int calculateEllipsisPosition = 0;
                String calculateTempText = "";
                while (remainWidth > 0
                        && tempTextWidth < remainWidth
                        && calculateEllipsisPosition + 1 <= tempText.length()) {
                    calculateEllipsisPosition++;
                    calculateTempText = tempText.subSequence(0, calculateEllipsisPosition).toString();
                    tempTextWidth = getPaint().measureText(calculateTempText);
                }
                calculateText = mOriginalText.subSequence(0, start).toString() + calculateTempText;
                isCalculated = calculateEllipsisPosition != 0;
            }

            if (!isCalculated) {
                spannable.append(mOriginalText);
            } else {
                spannable.append(calculateText);
                spannable.append(ELLIPSIZE_END);
                spannable.append(mExpandText);
                spannable.setSpan(new ForegroundColorSpan(mExpandColor.getDefaultColor()), spannable.length() - mExpandText.length(), spannable.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
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
            minX = getWidth() - getPaddingLeft() - getPaddingRight() - getTextWidth(mExpandText);
            maxX = getWidth() - getPaddingLeft() - getPaddingRight();
            minY = getHeight() - (getPaint().getFontMetrics().descent - getPaint().getFontMetrics().ascent) - getPaddingBottom();
            maxY = getHeight() - getPaddingBottom();
        }
    }

    private float getTextWidth(String text) {
        Paint paint = getPaint();
        return paint.measureText(text);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if ((mFoldable && isExpanded) || (mExpandable && !isExpanded)) {
            switch (event.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                    clickTime = System.currentTimeMillis();
                    if (!isClickable()) {
                        if (isInRange(event.getX(), event.getY())) {
                            return true;
                        }
                    }
                    break;

                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:
                    long delTime = System.currentTimeMillis() - clickTime;
                    clickTime = 0L;
                    if (delTime < ViewConfiguration.getTapTimeout() && isInRange(event.getX(), event.getY())) {
                        isExpanded = !isExpanded;
                        setText(mOriginalText);
                        if (onTipClickListener != null) {
                            onTipClickListener.onTipClick(isExpanded);
                        }
                        return true;
                    }
                    break;
                default:
                    break;
            }
        }
        return super.onTouchEvent(event);
    }

    private boolean isInRange(float x, float y) {
        if (minX < maxX) {
            return x >= minX && x <= maxX && y >= minY && y <= maxY;
        } else {
            return x <= maxX && y >= middleY && y <= maxY || x >= minX && y >= minY && y <= middleY;
        }
    }

    public FoldTextView setFoldMaxLines(int maxLines) {
        mShowMaxLine = maxLines;
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

    public FoldTextView setOnTipClickListener(FoldTextView.OnTipClickListener onTipClickListener) {
        this.onTipClickListener = onTipClickListener;
        return this;
    }

    public interface OnTipClickListener {
        void onTipClick(boolean isExpanded);
    }
}