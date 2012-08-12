/* Copyright 2008-2009 Robert Bajzat. All rights reserved. Use is subject to license terms. */
package com.icode.view.container;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JComponent;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;

import com.icode.util.Commons;

/**
 * A split pane where the knob is outside
 */
public class SplitPane extends JComponent {

    private boolean horizontal;
    private transient int divider = -1;

    /**
     * Creates a new split pane with contents and orientation
     * @param left the left (or top) component
     * @param right the right (or bottom) component
     * @param orientation horizontal or vertical,
     * use JSplitPane's HORIZONTAL_SPLIT or VERTICAL_SPLIT value
     */
    public SplitPane(JComponent left, JComponent right, int orientation) {
        setLayout(null);
        add(left);
        add(right);
        horizontal = (orientation == JSplitPane.HORIZONTAL_SPLIT);
    }

    /**
     * Overwrites the method to calculate the preferred size
     */
    @Override
	public Dimension getPreferredSize() {
        int w = 0, h = 0;
        for (int i = 0; i < getComponentCount(); i++) {
            Dimension d = getComponent(i).getPreferredSize();
            w += ((i == 0) && (divider != -1)) ? divider : d.width;
            h = Math.max(h, d.height);
        }
        Insets is = getInsets();
        return new Dimension(w + is.left + is.right, h + is.top + is.bottom);
    }
    
    /**
     * Overwrites the method to layout the left and right (or top and bottom) content
     */
    @Override
	public void doLayout() {
        if ((getComponentCount() == 0) || (getWidth() == 0)) {
            return;
        }

        Component left = getComponent(0);
        int lw = left.getPreferredSize().width;
        if (divider == -1) {
            divider = Math.max(24, Math.min(lw, getWidth() - 24));
        }
        Insets is = getInsets();
        int h = getHeight() - is.top - is.bottom;
        left.setBounds(is.left, is.top, divider, h);

        for (int i = 1; i < getComponentCount(); i++) {
            getComponent(i).setBounds(
	    		is.left + divider, 
	    		is.top,
	            getWidth() - is.left - is.right - divider, 
	            h
            );
        }
    }

    private void setDivider(int divider) {
        divider = Math.max(24, Math.min(divider, getWidth() - 24));
        if (this.divider != divider) {
            this.divider = divider;
            revalidate();
            Commons.repaintAncestry(this);
        }
    }

    /**
     * Creates a knob component to adjust the split pane divider value
     * @return the knob component
     */
    public JComponent getKnob() {
        return new Knob();
    }

    private class Knob extends JComponent
            implements MouseListener, MouseMotionListener {

        private transient int offset = -1;

        private Knob() {
            setCursor(Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR));
            addMouseListener(this);
            addMouseMotionListener(this);
        }

        @Override
		public Dimension getPreferredSize() {
            return new Dimension(13, 10);
        }

        /** Overwritten to return preferred size for box layout */
        @Override
		public Dimension getMinimumSize() {
            return getPreferredSize();
        }

        /** Overwritten to return preferred size for box layout */
        @Override
		public Dimension getMaximumSize() {
            return getPreferredSize();
        }

        @Override
		protected void paintComponent(Graphics g) { // TODO vertical + above
            g.setColor(Color.gray);
            int x = (getWidth() - 7) / 2, y = (getHeight() - 10) / 2;
            for (int i = 0; i < 3; i++) {
                g.drawLine(x + i * 3, y, x + i * 3, y + 10);
            }
        }

        public void mousePressed(MouseEvent e) {
            Point p = SwingUtilities.convertPoint(this, e.getX(), e.getY(), SplitPane.this);
            offset = divider - (horizontal ? p.x : p.y);
        }

        public void mouseDragged(MouseEvent e) {
            Point p = SwingUtilities.convertPoint(this, e.getX(), e.getY(), SplitPane.this);
            setDivider(offset + (horizontal ? p.x : p.y));
        }

        public void mouseClicked(MouseEvent e) {
        }

        public void mouseEntered(MouseEvent e) {
        }

        public void mouseExited(MouseEvent e) {
        }

        public void mouseReleased(MouseEvent e) {
        }

        public void mouseMoved(MouseEvent e) {
        }
    }
}
