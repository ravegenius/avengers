package com.jason.avengers.common.widgets.label.span;

import android.support.annotation.NonNull;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import com.jason.avengers.common.widgets.label.base.LabelClickListener;

/**
 * 标签点击Span
 *
 * @author Jason
 * @blame Jason
 */
public class LabelClickableSpan extends ClickableSpan {

    private LabelClickListener mLabelClickListener;

    public LabelClickableSpan(LabelClickListener listener) {
        mLabelClickListener = listener;
    }

    @Override
    public void onClick(@NonNull View widget) {
        if (mLabelClickListener != null) {
            mLabelClickListener.onLabelClick(widget);
        }
    }

    @Override
    public void updateDrawState(@NonNull TextPaint ds) {
        ds.setUnderlineText(false);
    }
}