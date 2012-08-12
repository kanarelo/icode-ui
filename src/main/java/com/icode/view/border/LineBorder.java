/* Copyright 2008-2009 Robert Bajzat. All rights reserved. Use is subject to license terms. */
package com.icode.view.border;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.border.Border;

/**
 * A simple line border with different line widths on the sides
 */
public class LineBorder implements Border {

    private Color color;
    private int top, left, bottom, right;
    private Insets insets;

    /**
     * Creates a new border width line color and line widths
     * @param color the color of a line
     * @param top width of the top line, can be zero
     * @param left width of the left line
     * @param bottom width of the bottom line
     * @param right width of the right line
     */
    public LineBorder(Color color, int top, int left, int bottom, int right) {
        this(color, top, left, bottom, right, 0, 0, 0, 0);
    }

    /**
     * Creates a new line border width paddings
     * @param color the color of a line
     * @param top width of the top line, can be zero
     * @param left width of the left line
     * @param bottom width of the bottom line
     * @param right width of the right line
     * @param topPadding gap on the top
     * @param leftPadding gap on the left
     * @param bottomPadding gap on the bottom
     * @param rightPadding gap on the right
     */
    public LineBorder(Color color, int top, int left, int bottom, int right,
            int topPadding, int leftPadding, int bottomPadding, int rightPadding) {
        this.color = color;
        this.top = top;
        this.left = left;
        this.bottom = bottom;
        this.right = right;
        insets = new Insets(top + topPadding, left + leftPadding,
                bottom + bottomPadding, right + rightPadding);
    }

    /**
     * @see Border#getBorderInsets(Component)
     */
    public Insets getBorderInsets(Component c) {
        return insets;
    }

    /**
     * @see Border#isBorderOpaque()
     */
    public boolean isBorderOpaque() {
        return true;
    }

    /**
     * @see Border#paintBorder(Component, Graphics, int, int, int, int)
     */
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        g.setColor(color);
        if (top > 0) {
            g.fillRect(x, y, width, top);
        }
        if (left > 0) {
            g.fillRect(x, y, left, height);
        }
        if (bottom > 0) {
            g.fillRect(x, y + height - bottom, width, bottom);
        }
        if (right > 0) {
            g.fillRect(x + width - right, y, right, height);
        }
    }
}
