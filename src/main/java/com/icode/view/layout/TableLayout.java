/* Copyright 2008-2009 Robert Bajzat. All rights reserved. Use is subject to license terms. */
package com.icode.view.layout;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager2;

/**
 * A grid based layout manager. The result is very similar to
 * GridBagLayout but it's a simple and easy to use manager.
 * Note: it is under construction, it'll be radically changed.
 */
public class TableLayout implements LayoutManager2 {

    /**
     * @return 
     * @see LayoutManager2#preferredLayoutSize(Container) */
    public Dimension preferredLayoutSize(Container parent) {
        int n = parent.getComponentCount();
        int[] widths = new int[2], heights = new int[n / 2];
        for (int i = 0; i < n; i++) {
            Dimension d = parent.getComponent(i).getPreferredSize();
            widths[i % 2] = Math.max(widths[i % 2], d.width);
            heights[i / 2] = Math.max(heights[i / 2], d.height);
        }
        int width = 0;
        for (int i = 0; i < widths.length; i++) {
            width += widths[i];
        }
        int height = 0;
        for (int i = 0; i < heights.length; i++) {
            height += heights[i];
        }
        Insets is = parent.getInsets();
        return new Dimension(is.left + width + Math.max(widths.length - 1, 0) * 4 + is.right,
                is.top + height + Math.max(heights.length - 1, 0) * 4 + is.bottom);
    }

    /** @see LayoutManager2#layoutContainer(Container) */
    public void layoutContainer(Container parent) {
        int n = parent.getComponentCount();
        Dimension[] ds = new Dimension[n];
        int[] widths = new int[2], heights = new int[n / 2];
        for (int i = 0; i < n; i++) {
            ds[i] = parent.getComponent(i).getPreferredSize();
            widths[i % 2] = Math.max(widths[i % 2], ds[i].width);
            heights[i / 2] = Math.max(heights[i / 2], ds[i].height);
        }
        Insets is = parent.getInsets();
        int[] xs = new int[2], ys = new int[n / 2];
        for (int i = 0; i < xs.length; i++) {
            xs[i] = (i == 0) ? is.left : xs[i - 1] + widths[i - 1] + 4;
        }
        for (int i = 0; i < ys.length; i++) {
            ys[i] = (i == 0) ? is.top : ys[i - 1] + heights[i - 1] + 4;
        }
        for (int i = 0; i < n; i++) {
            parent.getComponent(i).setBounds(xs[i % 2], ys[i / 2], ds[i].width, heights[i / 2]);
        }
    }

    /**
     * @param target
     * @return
     * @see LayoutManager2#maximumLayoutSize(Container) */
    public Dimension maximumLayoutSize(Container target) {
        return preferredLayoutSize(target);
    }

    /**
     * @return
     * @see LayoutManager2#minimumLayoutSize(Container) */
    public Dimension minimumLayoutSize(Container parent) {
        return preferredLayoutSize(parent);
    }

    /** @see LayoutManager2#addLayoutComponent(String, Component) */
    public void addLayoutComponent(String name, Component comp) {
    }

    /** @see LayoutManager2#addLayoutComponent(Component, Object) */
    public void addLayoutComponent(Component comp, Object constraints) {
    }

    /** @see LayoutManager2#removeLayoutComponent(Component) */
    public void removeLayoutComponent(Component comp) {
    }

    /**
     * @param target
     * @return
     * @see LayoutManager2#getLayoutAlignmentX(Container) */
    public float getLayoutAlignmentX(Container target) {
        return 0;
    }

    /**
     * @param target
     * @return
     * @see LayoutManager2#getLayoutAlignmentY(Container) */
    public float getLayoutAlignmentY(Container target) {
        return 0;
    }

    /**
     * @param target
     * @see LayoutManager2#invalidateLayout(Container) */
    public void invalidateLayout(Container target) {
    }
}
