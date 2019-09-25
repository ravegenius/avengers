package com.jason.avengers.common.widgets.label.base;

/**
 * 标签参数类
 *
 * @author Jason
 * @blame Jason
 */
public class LabelParams {
    /**
     * 左右Padding
     */
    private int paddingPx = LabelConfig.PADDING;
    /**
     * 居左实际文字距离
     */
    private int leftMarginPx = LabelConfig.LEFT_MARGIN;
    /**
     * 居右实际文字距离
     */
    private int rightMarginPx = LabelConfig.RIGHT_MARGIN;
    /**
     * 出标签外内容为空需要将内容修改 并且这是width为0
     */
    private boolean isNonSize = LabelConfig.IS_NON_SIZE;

    public int getPaddingPx() {
        return paddingPx;
    }

    public void setPaddingPx(int paddingPx) {
        this.paddingPx = paddingPx;
    }

    public int getLeftMarginPx() {
        return leftMarginPx;
    }

    public void setLeftMarginPx(int leftMarginPx) {
        this.leftMarginPx = leftMarginPx;
    }

    public int getRightMarginPx() {
        return rightMarginPx;
    }

    public void setRightMarginPx(int rightMarginPx) {
        this.rightMarginPx = rightMarginPx;
    }

    public boolean isNonSize() {
        return isNonSize;
    }

    public void setNonSize(boolean nonSize) {
        isNonSize = nonSize;
    }
}
