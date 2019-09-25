package com.jason.avengers.common.widgets.label.base;

import android.graphics.Paint;

/**
 * 标签背景类
 *
 * @author Jason
 * @blame Jason
 */
public class LabelRect {

    /**
     * 背景颜色
     */
    private int color;
    /**
     * 背景是否镂空
     */
    private Paint.Style style = Paint.Style.FILL_AND_STROKE;
    /**
     * 背景圆角弧度
     */
    private int radiusPx = LabelConfig.RADIUS;
    /**
     * 背景边框宽度
     */
    private float strokeWidthPx = LabelConfig.STROKE_WIDTH;

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public Paint.Style getStyle() {
        return style;
    }

    public void setStyle(Paint.Style style) {
        this.style = style;
    }

    public int getRadiusPx() {
        return radiusPx;
    }

    public void setRadiusPx(int radiusPx) {
        this.radiusPx = radiusPx;
    }

    public float getStrokeWidthPx() {
        return strokeWidthPx;
    }

    public void setStrokeWidthPx(float strokeWidthPx) {
        this.strokeWidthPx = strokeWidthPx;
    }
}
