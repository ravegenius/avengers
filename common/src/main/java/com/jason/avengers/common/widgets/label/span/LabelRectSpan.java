package com.jason.avengers.common.widgets.label.span;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.text.style.ReplacementSpan;
import android.util.Log;

import com.jason.avengers.common.widgets.label.base.LabelClickListener;
import com.jason.avengers.common.widgets.label.base.LabelConfig;
import com.jason.avengers.common.widgets.label.base.LabelParams;
import com.jason.avengers.common.widgets.label.base.LabelRect;
import com.jason.avengers.common.widgets.label.base.LabelText;

/**
 * 标签带背景Span
 *
 * @author Jason
 * @blame Jason
 */
public class LabelRectSpan extends ReplacementSpan implements ILabelSpan {
    /**
     * 标签
     */
    private LabelText mLabelText;
    /**
     * 标签背景
     */
    private LabelRect mLabelRect;
    /**
     * 设置标签背景边宽
     */
    private float mStrokeWidthPx;
    /**
     * 设置标签背景左右Padding
     */
    private int mPaddingPx;
    /**
     * 居左距离
     */
    private int mLeftMarginPx;
    /**
     * 居右距离
     */
    private int mRightMarginPx;
    /**
     * 出标签外内容为空需要将内容修改 并且这是width为0
     */
    private boolean isNonSize;
    /**
     * 是否可点击
     */
    private boolean isClickableSpan;
    /**
     * 点击事件
     */
    private LabelClickListener mLabelClickListener;
    /**
     * Span宽度，计算得出
     */
    private int mSize;
    /**
     * 背景开始位置，计算得出
     */
    private float mStart;
    /**
     * 背景结束位置，计算得出
     */
    private float mEnd;
    /**
     * 背景Top位置，计算得出
     */
    private float mTop;
    /**
     * 背景Bottom位置，计算得出
     */
    private float mBottom;
    /**
     * 文字基线，计算得出
     */
    private float mBaseLine;

    public LabelRectSpan(@NonNull LabelText labelText, @NonNull LabelRect labelRect) {
        this(labelText, labelRect, null);
    }

    public LabelRectSpan(@NonNull LabelText labelText, @NonNull LabelRect labelRect, LabelParams labelParams) {
        this(labelText, labelRect, labelParams, null);
    }

    public LabelRectSpan(@NonNull LabelText labelText, @NonNull LabelRect labelRect, LabelParams labelParams, LabelClickListener listener) {
        this.mLabelText = labelText;
        this.mLabelRect = labelRect;
        this.mStrokeWidthPx = labelRect.getStrokeWidthPx();
        this.mPaddingPx = labelParams == null ? 0 : labelParams.getPaddingPx();
        this.mLeftMarginPx = labelParams == null ? 0 : labelParams.getLeftMarginPx();
        this.mRightMarginPx = labelParams == null ? 0 : labelParams.getRightMarginPx();
        this.isNonSize = labelParams == null || labelParams.isNonSize();
        this.isClickableSpan = listener != null;
        this.mLabelClickListener = listener;
    }

    @Override
    public int getSize(@NonNull Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {
        Log.i("Jason", "getSize");
        if (Float.isNaN(mStrokeWidthPx)) {
            mStrokeWidthPx = paint.getStrokeWidth();
        }
        float textSize = paint.getTextSize();
        float labelSize;
        if (mLabelText.getTextSizePx() != 0) {
            paint.setTextSize(mLabelText.getTextSizePx());
            labelSize = paint.measureText(mLabelText.getLabel(), 0, mLabelText.getLabel().length());
            paint.setTextSize(textSize);
        } else {
            labelSize = paint.measureText(mLabelText.getLabel(), 0, mLabelText.getLabel().length());
        }
        mSize = (int) ((mPaddingPx * 2.0f) + (mStrokeWidthPx * 2.0f) + labelSize);
        return (mSize + mLeftMarginPx + mRightMarginPx) * (isNonSize ? 0 : 1);
    }

    @Override
    public void draw(@NonNull Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, @NonNull Paint paint) {
        mStart = x + mLeftMarginPx;
        mEnd = mStart + mSize;

        Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
        mBaseLine = y;
        mTop = mBaseLine + fontMetrics.ascent + mStrokeWidthPx;
        mBottom = mBaseLine + fontMetrics.descent - mStrokeWidthPx;

        if (mLabelText.getTextSizePx() != 0) {
            Paint paint1 = new Paint();
            paint1.setTextSize(mLabelText.getTextSizePx());
            fontMetrics = paint1.getFontMetricsInt();
            // 公式 【mTop + (((mBottom - mTop) - (fontMetrics1.descent - fontMetrics1.ascent)) / 2) - fontMetrics1.ascent】
            // 简化后
            mBaseLine = (mTop + mBottom - fontMetrics.descent - fontMetrics.ascent) / 2;
            mTop = mBaseLine + fontMetrics.ascent + mStrokeWidthPx;
            mBottom = mBaseLine + fontMetrics.descent - mStrokeWidthPx;
        }

        drawLabelRect(canvas);
        drawLabelText(canvas, paint);
    }

    private void drawLabelRect(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(mLabelRect.getColor());
        paint.setAntiAlias(true);
        paint.setStrokeWidth(mStrokeWidthPx);

        RectF oval = new RectF(mStart, mTop, mEnd, mBottom);
        paint.setStyle(mLabelRect.getStyle());
        canvas.drawRoundRect(oval, mLabelRect.getRadiusPx(), mLabelRect.getRadiusPx(), paint);
    }

    private void drawLabelText(Canvas canvas, Paint paint) {
        if (mLabelText.getTextSizePx() != 0) {
            paint.setTextSize(mLabelText.getTextSizePx());
        }
        if (mLabelText.getColor() != 0) {
            paint.setColor(mLabelText.getColor());
        }
        paint.setAntiAlias(true);
        paint.setTextAlign(Paint.Align.CENTER);

        float textCenterX = (mEnd - mStart) / 2 + mStart;
        canvas.drawText(mLabelText.getLabel().toString(), textCenterX, mBaseLine, paint);
    }

    @Override
    public CharSequence label() {
        return hasImageSpan() ? mLabelText.getLabel() + LabelConfig.IMAGESPAN_CHAR : mLabelText.getLabel();
    }

    @Override
    public boolean isNonSize() {
        return isNonSize;
    }

    @Override
    public boolean isClickableSpan() {
        return isClickableSpan;
    }

    @Override
    public LabelClickListener labelClickListener() {
        return mLabelClickListener;
    }

    @Override
    public int position() {
        return 0;
    }

    @Override
    public int length() {
        return mLabelText.getLabel() == null
                ? 0
                : (hasImageSpan() ? mLabelText.getLabel().length() + 1 : mLabelText.getLabel().length());
    }

    @Override
    public boolean hasImageSpan() {
        return mLabelText.getImageRes() != 0;
    }
}
