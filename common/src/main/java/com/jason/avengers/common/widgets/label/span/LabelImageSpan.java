package com.jason.avengers.common.widgets.label.span;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.style.ImageSpan;

import com.jason.avengers.common.widgets.label.base.LabelClickListener;
import com.jason.avengers.common.widgets.label.base.LabelConfig;
import com.jason.avengers.common.widgets.label.base.LabelParams;
import com.jason.avengers.common.widgets.label.base.LabelText;

/**
 * 标签带图片Span
 *
 * @author Jason
 * @blame Jason
 */
public class LabelImageSpan extends ImageSpan implements ILabelSpan {


    private LabelText mLabelText;
    /**
     * img靠左或者靠右
     */
    private int mLabelImgAlign;
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
    /**
     * img宽度
     */
    private int mImageWidth;

    public LabelImageSpan(@NonNull Context context, @NonNull LabelText labelText) {
        this(context, labelText, null);
    }

    public LabelImageSpan(@NonNull Context context, @NonNull LabelText labelText, LabelParams labelParams) {
        this(context, labelText, labelParams, null);
    }

    public LabelImageSpan(@NonNull Context context, @NonNull LabelText labelText, LabelParams labelParams, LabelClickListener listener) {
        super(context, labelText.getImageRes());
        this.mLabelText = labelText;
        this.mLabelImgAlign = labelText.getAlign();
        this.mPaddingPx = labelParams == null ? 0 : labelParams.getPaddingPx();
        this.mLeftMarginPx = labelParams == null ? 0 : labelParams.getLeftMarginPx();
        this.mRightMarginPx = labelParams == null ? 0 : labelParams.getRightMarginPx();
        this.isNonSize = labelParams == null || labelParams.isNonSize();
        this.isClickableSpan = listener != null;
        this.mLabelClickListener = listener;
    }

    @Override
    public int getSize(@NonNull Paint paint, CharSequence text, int start, int end, @Nullable Paint.FontMetricsInt fm) {
        float textSize = paint.getTextSize();
        float labelSize;
        if (mLabelText.getTextSizePx() != 0) {
            paint.setTextSize(mLabelText.getTextSizePx());
            labelSize = paint.measureText(mLabelText.getLabel(), 0, mLabelText.getLabel().length());
            paint.setTextSize(textSize);
        } else {
            labelSize = paint.measureText(mLabelText.getLabel(), 0, mLabelText.getLabel().length());
        }

        Drawable d = getDrawable();
        Rect rect = d.getBounds();
        mImageWidth = rect.right;

        mSize = (int) (mPaddingPx * 2.0f + labelSize);
        return (mSize + mLeftMarginPx + mRightMarginPx + mImageWidth) * (isNonSize ? 0 : 1);
    }

    @Override
    public void draw(@NonNull Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, @NonNull Paint paint) {
        mStart = x + mLeftMarginPx;
        mEnd = mStart + mSize;

        Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
        mBaseLine = y;
        mTop = mBaseLine + fontMetrics.ascent;
        mBottom = mBaseLine + fontMetrics.descent;

        if (mLabelText.getTextSizePx() != 0) {
            Paint paint1 = new Paint();
            paint1.setTextSize(mLabelText.getTextSizePx());
            fontMetrics = paint1.getFontMetricsInt();
            // 公式 【mTop + (((mBottom - mTop) - (fontMetrics1.descent - fontMetrics1.ascent)) / 2) - fontMetrics1.ascent】
            // 简化后
            mBaseLine = (mTop + mBottom - fontMetrics.descent - fontMetrics.ascent) / 2;
            mTop = mBaseLine + fontMetrics.ascent;
            mBottom = mBaseLine + fontMetrics.descent;
        }

        drawLabelText(canvas, paint);

        super.draw(canvas, text, start, end,
                mLabelImgAlign == LabelConfig.IMG_ALIGN_LEFT ? (mStart + mPaddingPx) : (mEnd - mPaddingPx),
                top, y, bottom, paint);
    }

    private void drawLabelText(Canvas canvas, Paint paint) {
        if (mLabelText.getTextSizePx() != 0) {
            paint.setTextSize(mLabelText.getTextSizePx());
        }
        if (mLabelText.getColor() != 0) {
            paint.setColor(mLabelText.getColor());
        }
        paint.setAntiAlias(true);
        paint.setTextAlign(Paint.Align.LEFT);

        float textCenterX = mStart + mPaddingPx + (mLabelImgAlign == LabelConfig.IMG_ALIGN_LEFT ? mImageWidth : 0);
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
                : (hasImageSpan() ? mLabelText.getLabel().length() + LabelConfig.IMAGESPAN_CHAR.length() : mLabelText.getLabel().length());
    }

    @Override
    public boolean hasImageSpan() {
        return mLabelText.getImageRes() != 0;
    }
}
