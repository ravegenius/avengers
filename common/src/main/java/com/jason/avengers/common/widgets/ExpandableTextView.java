package com.jason.avengers.common.widgets;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.jason.avengers.common.R;
import com.jason.avengers.common.base.BaseApplication;


/**
 * Created by jason on 2017/10/31.
 */
@SuppressLint("AppCompatCustomView")
public class ExpandableTextView extends TextView {

    private static final String DEFAULT_UNEXPANDED_END_SYMBOL = "\u002e\u002e\u002e";
    private static final String DEFAULT_UNEXPANDED_END_TEXT = "展开";
    private static final String DEFAULT_EXPANDED_END_TEXT = "[收起]";
    private static final boolean DEFAULT_EXPANDED = false;
    private static final boolean DEFAULT_EXPANDABLE = true;
    private static final int DEFAULT_MAXLINES = 5;
    private static int COLOR33;
    private static int COLORBULE;

    private String mUnExpandedEndSymbol;
    private String mUnExpandedEndText;
    private String mExpandedEndText;
    private boolean mExpandable;
    private boolean mExpanded = DEFAULT_EXPANDED;
    private int mMaxLines;

    private SpannableStringBuilder mOriginText;
    private boolean shouldCalculate;
    private OnExpandableTextViewClick mOnExpandableTextViewClick;

    public ExpandableTextView(Context context) {
        this(context, null);
    }

    public ExpandableTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ExpandableTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initValue(context, attrs);
    }

    private void initValue(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ExpandableTextView);
        mUnExpandedEndSymbol = typedArray.getString(R.styleable.ExpandableTextView_evlUnExpandedEndSymbol);
        if (TextUtils.isEmpty(mUnExpandedEndSymbol)) {
            mUnExpandedEndSymbol = DEFAULT_UNEXPANDED_END_SYMBOL;
        }
        mUnExpandedEndText = typedArray.getString(R.styleable.ExpandableTextView_evlUnExpandedEndText);
        if (TextUtils.isEmpty(mUnExpandedEndText)) {
            mUnExpandedEndText = DEFAULT_UNEXPANDED_END_TEXT;
        }
        mExpandedEndText = typedArray.getString(R.styleable.ExpandableTextView_evlExpandedEndText);
        if (TextUtils.isEmpty(mExpandedEndText)) {
            mExpandedEndText = DEFAULT_EXPANDED_END_TEXT;
        }
        mExpandable = typedArray.getBoolean(R.styleable.ExpandableTextView_evlExpandable, DEFAULT_EXPANDABLE);
        mMaxLines = typedArray.getInteger(R.styleable.ExpandableTextView_evlMaxLines, DEFAULT_MAXLINES);
        typedArray.recycle();

        setMovementMethod(LinkMovementMethod.getInstance());
        setEllipsize(TextUtils.TruncateAt.END);
        initTheme();
    }

    private void initTheme() {
        COLOR33 = BaseApplication.getInstance().getResources().getColor(R.color.color_333333);
        COLORBULE = BaseApplication.getInstance().getResources().getColor(R.color.color_3F51B5);
    }

    public void setExpandedText(CharSequence text) {
        setExpandedData(text, false);
    }

    public void setUnExpandedText(CharSequence text) {
        setExpandedData(text, true);
    }

    private void setExpandedData(CharSequence text, boolean isExpanded) {
        mOriginText = new SpannableStringBuilder(text);
        mExpanded = isExpanded;
        setMaxLines(mExpanded ? Integer.MAX_VALUE : mMaxLines);
        setText(text);
        post(new Runnable() {
            @Override
            public void run() {
                shouldCalculate = true;
                invalidate();
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        calculate();
    }

    private void calculate() {
        if (shouldCalculate && null != getLayout()) {
            shouldCalculate = false;
            if (getLayout().getLineCount() < mMaxLines) {
                return;
            }
            boolean isCalculated = false;
            String calculateText = mOriginText.toString();
            if (!mExpanded) {
                int index = mMaxLines - 1;
                float viewWidth = getMeasuredWidth()
                        - getPaddingLeft() - getCompoundPaddingLeft()
                        - getPaddingRight() - getCompoundPaddingRight()
                        - getTextSize();
                float lineWidth = getLayout().getLineWidth(index);
                int lineStart = getLayout().getLineStart(index);
                int lineEnd = getLayout().getLineEnd(index);
                int length = mOriginText.length();
                String temp = "";
                if (!TextUtils.isEmpty(mUnExpandedEndSymbol)) temp += mUnExpandedEndSymbol;
                if (!TextUtils.isEmpty(mUnExpandedEndText)) temp += mUnExpandedEndText;
                float expandedEllipsisWidth = getPaint().measureText(temp);

                String tempText;
                if (length > lineEnd) {
                    isCalculated = true;
                    tempText = mOriginText.subSequence(lineStart, lineEnd).toString().replace("\n", "");
                    calculateText = mOriginText.subSequence(0, lineStart).toString() + tempText;
                } else {
                    tempText = mOriginText.subSequence(lineStart, length).toString();
                }

                float tempTextWidth = getPaint().measureText(tempText);
                if (lineWidth == tempTextWidth && getLayout().getLineCount() == mMaxLines) {
                    isCalculated = false;
                } else if (tempTextWidth + (isCalculated ? expandedEllipsisWidth : 0) > viewWidth) {
                    float remainWidth = viewWidth - expandedEllipsisWidth;

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
                    calculateText = mOriginText.subSequence(0, lineStart).toString() + calculateTempText;
                    isCalculated = calculateEllipsisPosition != 0;
                }
            }
            SpannableStringBuilder calculateResult;
            if (mExpanded) {
                calculateResult = buildExpandedText();
            } else if (!isCalculated) {
                calculateResult = buildOriginText();
            } else {
                calculateResult = buildUnExpandedText(calculateText);
            }
            setTextAfterCalculate(calculateResult);
        }
    }

    private SpannableStringBuilder buildExpandedText() {
        SpannableStringBuilder calculateResult = new SpannableStringBuilder(mOriginText);
        int calculatePosition = calculateResult.length();

        if (!TextUtils.isEmpty(mExpandedEndText)) {
            calculateResult.append(mExpandedEndText);
        }

        calculateResult.setSpan(new ForegroundColorSpan(COLOR33),
                0, calculatePosition,
                Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        calculateResult.setSpan(new ClickableSpan() {
                                    @Override
                                    public void onClick(View widget) {
                                        if (mOnExpandableTextViewClick != null)
                                            mOnExpandableTextViewClick.onClick(widget);
                                    }

                                    @Override
                                    public void updateDrawState(TextPaint ds) {
                                        ds.setUnderlineText(false);
                                        ds.setColor(COLOR33);
                                    }
                                },
                0, calculatePosition,
                Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        calculateResult.setSpan(new ForegroundColorSpan(COLORBULE),
                calculatePosition, calculateResult.length(),
                Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        calculateResult.setSpan(new ClickableSpan() {
                                    @Override
                                    public void onClick(View widget) {
                                        if (mExpandable) {
                                            post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    setExpandedText(mOriginText);
                                                }
                                            });
                                            if (mOnExpandableTextViewClick != null)
                                                mOnExpandableTextViewClick.onExpandedEndTextClick(widget, true);
                                        } else {
                                            if (mOnExpandableTextViewClick != null)
                                                mOnExpandableTextViewClick.onClick(widget);
                                        }
                                    }

                                    @Override
                                    public void updateDrawState(TextPaint ds) {
                                        ds.setUnderlineText(false);
                                        ds.setColor(COLORBULE);
                                    }
                                },
                calculatePosition, calculateResult.length(),
                Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return calculateResult;
    }

    private SpannableStringBuilder buildOriginText() {
        SpannableStringBuilder calculateResult = new SpannableStringBuilder(mOriginText);
        calculateResult.setSpan(new ForegroundColorSpan(COLOR33),
                0, calculateResult.length(),
                Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        calculateResult.setSpan(new ClickableSpan() {
                                    @Override
                                    public void onClick(View widget) {
                                        if (mOnExpandableTextViewClick != null)
                                            mOnExpandableTextViewClick.onClick(widget);
                                    }

                                    @Override
                                    public void updateDrawState(TextPaint ds) {
                                        ds.setUnderlineText(false);
                                        ds.setColor(COLOR33);
                                    }
                                },
                0, calculateResult.length(),
                Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return calculateResult;
    }

    private SpannableStringBuilder buildUnExpandedText(String ellipsisText) {
        SpannableStringBuilder calculateResult = new SpannableStringBuilder(ellipsisText);
        int calculatePosition = calculateResult.length();

        if (!TextUtils.isEmpty(mUnExpandedEndSymbol)) {
            calculateResult.append(mUnExpandedEndSymbol);
            calculatePosition = calculatePosition + mUnExpandedEndSymbol.length();
        }
        if (!TextUtils.isEmpty(mUnExpandedEndText)) {
            calculateResult.append(mUnExpandedEndText);
        }

        calculateResult.setSpan(new ForegroundColorSpan(COLOR33),
                0, calculatePosition,
                Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        calculateResult.setSpan(new ClickableSpan() {
                                    @Override
                                    public void onClick(View widget) {
                                        if (mOnExpandableTextViewClick != null)
                                            mOnExpandableTextViewClick.onClick(widget);
                                    }

                                    @Override
                                    public void updateDrawState(TextPaint ds) {
                                        ds.setUnderlineText(false);
                                        ds.setColor(COLOR33);
                                    }
                                },
                0, calculatePosition,
                Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        calculateResult.setSpan(new ForegroundColorSpan(COLORBULE),
                calculatePosition, calculateResult.length(),
                Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        calculateResult.setSpan(new ClickableSpan() {
                                    @Override
                                    public void onClick(View widget) {
                                        if (mExpandable) {
                                            post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    setUnExpandedText(mOriginText);
                                                }
                                            });
                                            if (mOnExpandableTextViewClick != null)
                                                mOnExpandableTextViewClick.onExpandedEndTextClick(widget, false);
                                        } else {
                                            if (mOnExpandableTextViewClick != null)
                                                mOnExpandableTextViewClick.onClick(widget);
                                        }
                                    }

                                    @Override
                                    public void updateDrawState(TextPaint ds) {
                                        ds.setUnderlineText(false);
                                        ds.setColor(COLORBULE);
                                    }
                                },
                calculatePosition, calculateResult.length(),
                Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return calculateResult;
    }

    private void setTextAfterCalculate(final SpannableStringBuilder calculateResult) {
        post(new Runnable() {
            @Override
            public void run() {
                setText(calculateResult);
            }
        });
    }

    public void setOnExpandableTextViewClick(OnExpandableTextViewClick onExpandableTextViewClick) {
        this.mOnExpandableTextViewClick = onExpandableTextViewClick;
    }

    public interface OnExpandableTextViewClick {

        void onClick(View view);

        void onExpandedEndTextClick(View view, boolean expaned);
    }
}
