package com.jason.avengers.common.widgets.label.base;

import android.graphics.Color;
import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.method.Touch;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

/**
 * 标签点击事件触发
 *
 * @author Jason
 * @blame Jason
 */
public class LabelMovementMethod extends LinkMovementMethod {

    private View.OnClickListener mClickListener;

    public LabelMovementMethod(View.OnClickListener listener) {
        mClickListener = listener;
    }

    @Override
    public boolean onTouchEvent(TextView widget, Spannable buffer,
                                MotionEvent event) {
        int action = event.getAction();

        if (action == MotionEvent.ACTION_DOWN ||
                action == MotionEvent.ACTION_UP ||
                action == MotionEvent.ACTION_MOVE) {
            int x = (int) event.getX();
            int y = (int) event.getY();

            x -= widget.getTotalPaddingLeft();
            y -= widget.getTotalPaddingTop();

            x += widget.getScrollX();
            y += widget.getScrollY();

            Layout layout = widget.getLayout();
            int line = layout.getLineForVertical(y);
            int off = layout.getOffsetForHorizontal(line, x);

            float lineRight = layout.getLineRight(line);
            boolean moreThentextEnd = (x - lineRight) > 20;

            ClickableSpan[] link = buffer.getSpans(off, off, ClickableSpan.class);

            if (!moreThentextEnd && link.length != 0) {
                if (action == MotionEvent.ACTION_DOWN) {
                    buffer.setSpan(new BackgroundColorSpan(Color.TRANSPARENT),
                            buffer.getSpanStart(link[0]), buffer.getSpanEnd(link[0]),
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    Selection.setSelection(buffer,
                            buffer.getSpanStart(link[0]),
                            buffer.getSpanEnd(link[0]));
                } else if (action == MotionEvent.ACTION_UP) {
                    link[0].onClick(widget);
                    buffer.setSpan(new BackgroundColorSpan(Color.TRANSPARENT),
                            buffer.getSpanStart(link[0]), buffer.getSpanEnd(link[0]),
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    Selection.removeSelection(buffer);
                } else {
                    buffer.setSpan(new BackgroundColorSpan(Color.TRANSPARENT),
                            buffer.getSpanStart(link[0]), buffer.getSpanEnd(link[0]),
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    Selection.removeSelection(buffer);
                }
            } else {
                Selection.removeSelection(buffer);

                if (action == MotionEvent.ACTION_UP) {
                    if (mClickListener != null) {
                        mClickListener.onClick(widget);
                    }
                }
            }
            return true;
        }
        return Touch.onTouchEvent(widget, buffer, event);
    }
}