package com.jason.avengers.common.widgets.label;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

import com.jason.avengers.common.widgets.label.base.LabelMovementMethod;
import com.jason.avengers.common.widgets.label.span.ILabelSpan;
import com.jason.avengers.common.widgets.label.span.LabelClickableSpan;

import java.util.ArrayList;

/**
 * 标签绑定
 *
 * @author Jason
 * @blame Jason
 */
public class LabelBuilder {

    private SpannableStringBuilder content;

    private ArrayList<ILabelSpan> labelSpans;

    private View.OnClickListener clickListener;

    public LabelBuilder content(@NonNull CharSequence content) {
        this.content = new SpannableStringBuilder(content);
        return this;
    }

    /**
     * 设置TextView点击监听 或者是 设置标签以外点击监听
     *
     * @param listener
     * @return
     */
    public LabelBuilder clickListener(View.OnClickListener listener) {
        this.clickListener = listener;
        return this;
    }

    public LabelBuilder addSpan(@NonNull ILabelSpan labelSpan) {
        if (this.labelSpans == null) {
            this.labelSpans = new ArrayList<>();
        }
        this.labelSpans.add(labelSpan);
        return this;
    }

    public void build(TextView target) {
        if (content == null) {
            content = new SpannableStringBuilder();
        }
        boolean allreadySetClickable = false;
        if (labelSpans != null && !labelSpans.isEmpty()) {
            for (ILabelSpan labelSpan : labelSpans) {
                content = addSpan(labelSpan, content);
                if (labelSpan.isClickableSpan() && !allreadySetClickable) {
                    allreadySetClickable = true;
                    target.setMovementMethod(new LabelMovementMethod(clickListener));
                    // 点击后的背景颜色(HighLightColor)属于TextView的属性，Android4.0以上默认是淡绿色，低版本的是黄色。
                    target.setHighlightColor(Color.TRANSPARENT);
                }
            }
        }
        if (!allreadySetClickable && clickListener != null) {
            target.setOnClickListener(clickListener);
        }
        target.setText(content);
    }

    private SpannableStringBuilder addSpan(@NonNull ILabelSpan labelSpan, @NonNull SpannableStringBuilder content) {
        if (labelSpan.isNonSize()) {
            int start = content.length();
            if (labelSpan.position() < 0 || labelSpan.position() > start) {
                content.append(labelSpan.label());
            } else {
                start = labelSpan.position();
                content.insert(start, labelSpan.label());
            }
            content.setSpan(new ForegroundColorSpan(Color.TRANSPARENT), start, start + labelSpan.length(),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        int start = content.length();
        if (labelSpan.position() < 0 || labelSpan.position() > start) {
            content.append(labelSpan.label());
        } else {
            start = labelSpan.position();
            content.insert(start, labelSpan.label());
        }
        content.setSpan(labelSpan, start, start + labelSpan.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        if (labelSpan.hasImageSpan()) {

        }

        if (labelSpan.isClickableSpan()) {
            content.setSpan(new LabelClickableSpan(labelSpan.labelClickListener()),
                    start, start + labelSpan.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return content;
    }

}
