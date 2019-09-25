package com.jason.avengers.common.widgets.label.span;

import com.jason.avengers.common.widgets.label.base.LabelClickListener;

/**
 * 标签Span接口
 *
 * @author Jason
 * @blame Jason
 */
public interface ILabelSpan {

    CharSequence label();

    boolean isNonSize();

    boolean isClickableSpan();

    LabelClickListener labelClickListener();

    int position();

    int length();

    boolean hasImageSpan();
}
