package com.jason.avengers.common.widgets.label.base;

import android.support.annotation.DrawableRes;

/**
 * 标签类
 *
 * @author Jason
 * @blame Jason
 */
public class LabelText {

    /**
     * 标签
     */
    private CharSequence label;
    /**
     * 标签颜色
     */
    private int color;
    /**
     * 标签字号
     */
    private int textSizePx = LabelConfig.TEXT_SIZE;
    /**
     * 图标
     */
    @DrawableRes
    private int imageRes;
    /**
     * 图标
     */
    @DrawableRes
    private int align = LabelConfig.IMG_ALIGN_RIGHT;

    public CharSequence getLabel() {
        return label;
    }

    public void setLabel(CharSequence label) {
        this.label = label;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getTextSizePx() {
        return textSizePx;
    }

    public void setTextSizePx(int textSizePx) {
        this.textSizePx = textSizePx;
    }

    public int getImageRes() {
        return imageRes;
    }

    public void setImageRes(@DrawableRes int imageRes) {
        this.imageRes = imageRes;
    }

    public int getAlign() {
        return align;
    }

    public void setAlign(int align) {
        this.align = align;
    }
}
