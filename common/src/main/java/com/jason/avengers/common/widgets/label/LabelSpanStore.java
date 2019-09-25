package com.jason.avengers.common.widgets.label;

import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;

import com.jason.avengers.common.widgets.label.base.LabelParams;
import com.jason.avengers.common.widgets.label.base.LabelRect;
import com.jason.avengers.common.widgets.label.base.LabelText;
import com.jason.avengers.common.widgets.label.span.ILabelSpan;
import com.jason.avengers.common.widgets.label.span.LabelImageSpan;
import com.jason.avengers.common.widgets.label.span.LabelRectSpan;
import com.jason.avengers.common.widgets.label.span.LabelSpan;

/**
 * 标签Span生成器
 *
 * @author Jason
 * @blame Jason
 */
public class LabelSpanStore {

    public static ILabelSpan LabelSpan(@ColorInt int textColor, CharSequence label) {
        LabelText labelText = new LabelText();
        labelText.setColor(textColor);
        labelText.setLabel(label);

        return new LabelSpan(labelText);
    }

    public static ILabelSpan LabelRectSpan(@ColorInt int textColor, CharSequence label, @ColorInt int rectColor, int radius) {
        LabelText labelText = new LabelText();
        labelText.setColor(textColor);
        labelText.setLabel(label);

        LabelRect labelRect = new LabelRect();
        labelRect.setColor(rectColor);
        labelRect.setRadiusPx(radius);
        labelRect.setStyle(Paint.Style.STROKE);

        return new LabelRectSpan(labelText, labelRect);
    }

    public static ILabelSpan LabelRectSpan(@ColorInt int textColor, CharSequence label, @ColorInt int rectColor, Paint.Style style) {
        LabelText labelText = new LabelText();
        labelText.setColor(textColor);
        labelText.setLabel(label);

        LabelRect labelRect = new LabelRect();
        labelRect.setColor(rectColor);
        labelRect.setStyle(style);

        LabelParams labelParams = new LabelParams();

        return new LabelRectSpan(labelText, labelRect, labelParams);
    }

    public static ILabelSpan LabelImageSpan(@NonNull Context context, @ColorInt int textColor, CharSequence label, @DrawableRes int imgRes) {
        LabelText labelText = new LabelText();
        labelText.setColor(textColor);
        labelText.setLabel(label);
        labelText.setImageRes(imgRes);

        return new LabelImageSpan(context, labelText);
    }

}
