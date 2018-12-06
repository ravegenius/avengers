package com.jason.avengers.common.widgets;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
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
import android.util.Log;
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
    private static final String TIP_EXPAND_TEXT = "[展开]";
    private static final String TIP_FOLD_TEXT = "[收起]";
    private static final int DEFAULT_MAX_VALUE = 4;

    // 可视最大行数
    private int mShowMaxLine;
    private int mRangeMaxLine;

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
    // 提示文字坐标
    private float minX, maxX, minY, maxY;
    // 收起全文不在一行显示时
    private float middleY1, middleY2;
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
        mRangeMaxLine = arr.getInt(R.styleable.FoldTextView_ftvRangeMaxLine, mShowMaxLine);
        if (mRangeMaxLine > mShowMaxLine) {
            mRangeMaxLine = mShowMaxLine;
        }

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
    public void setText(final CharSequence text, final BufferType type) {
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

    private void setFoldText(final BufferType type) {
        final SpannableStringBuilder spannable = new SpannableStringBuilder(mOriginalText);
        if (mFoldShow) {
            spannable.append(mFoldText);
            int color = mFoldColor == null ? getTextColors().getDefaultColor() : mFoldColor.getDefaultColor();
            spannable.setSpan(new ForegroundColorSpan(color), spannable.length() - mFoldText.length(), spannable.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
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

    private void setFoldText(Layout layout, BufferType type) {
        if (layout == null) return;
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
            middleY2 = middleY1 + getLineSpacingExtra();
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

    private void setExpandText(final BufferType type) {
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

    private void setExpandText(Layout layout, BufferType type) {
        if (layout == null) return;
        if (layout.getLineCount() > mShowMaxLine) {
            isOverMaxLine = true;
            SpannableStringBuilder spannable = new SpannableStringBuilder();

            float viewWidth = getMeasuredWidth()
                    - getPaddingLeft() - getCompoundPaddingLeft()
                    - getPaddingRight() - getCompoundPaddingRight()
                    - getTextSize();
            float lineWidth = getLayout().getLineWidth(mRangeMaxLine - 1);
            int start = layout.getLineStart(mRangeMaxLine - 1);
            int end = layout.getLineEnd(mRangeMaxLine - 1);
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
                String calculateTempText = "";
                tempTextWidth = 0f;
                int calculateEllipsisPosition = 0;
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
                minX = getPaddingLeft() + tempTextWidth + getTextWidth(ELLIPSIZE_END);
                maxX = minX + getTextWidth(mExpandText);

                spannable.append(calculateText);
                spannable.append(ELLIPSIZE_END);
                spannable.append(mExpandText);
                int color = mExpandColor == null ? getTextColors().getDefaultColor() : mExpandColor.getDefaultColor();
                spannable.setSpan(new ForegroundColorSpan(color), spannable.length() - mExpandText.length(), spannable.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
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

        Paint paint = new Paint();
        paint.setColor(isExpanded ? Color.BLUE : Color.RED);
        paint.setAlpha(100);
        if (minX < maxX) {
            canvas.drawRect(minX, minY, maxX, maxY, paint);
        } else {
            canvas.drawRect(minX, minY, getWidth() - getPaddingRight(), middleY1, paint);
            canvas.drawRect(getPaddingLeft(), middleY2, maxX, maxY, paint);
        }

        if ((isExpanded && !mFoldable) || (!isExpanded && !mExpandable)) {
            minX = maxX = minY = maxY = 0;
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
                    if (delTime < 3 * ViewConfiguration.getTapTimeout() && isInRange(event.getX(), event.getY())) {
                        isExpanded = !isExpanded;
                        setText(mOriginalText);
                        if (onTipClickListener != null) {
                            Log.i(TAG, "FoldTextView onTipClick isExpanded:" + isExpanded);
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

    public FoldTextView setOnTipClickListener(FoldTextView.OnTipClickListener onTipClickListener) {
        this.onTipClickListener = onTipClickListener;
        return this;
    }

    public interface OnTipClickListener {
        void onTipClick(boolean isExpanded);
    }
}