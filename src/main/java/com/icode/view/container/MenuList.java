/* Copyright 2008-2009 Robert Bajzat. All rights reserved. Use is subject to license terms. */
package com.icode.view.container;

import java.awt.Adjustable;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RadialGradientPaint;
import java.awt.Window;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.util.logging.Logger;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.icode.resources.ResourceUtils;

/**
 * A navigator component including selectable items, groups, and separators
 */
public class MenuList extends JPanel {

    private Listener listener;
    private JScrollBar scrollBar;
    private transient boolean windowFocused, focused;
    private Item selected;
    private Item inside;
    private Item outside;
    private static final Item dummy = new Item();

    /**
     * Creates a new component
     */
    public MenuList() {
        super(null);
        listener = new Listener();

        addAncestorListener(listener);
        addMouseListener(listener);
        addMouseWheelListener(listener);
        addFocusListener(listener);

        scrollBar = new JScrollBar(Adjustable.VERTICAL);
        scrollBar.addAdjustmentListener(listener);
        add(scrollBar);

    }

    /**
     * Returns the selected item
     * @return the selected item, or null
     */
    public Item getSelected() {
        return selected;
    }

    private class Listener implements AdjustmentListener, AncestorListener, WindowFocusListener,
            FocusListener, MouseListener, MouseWheelListener {

        public void adjustmentValueChanged(AdjustmentEvent e) {
            revalidate();
            repaint(); // win only
        }

        public void ancestorAdded(AncestorEvent event) {
            Window window = SwingUtilities.windowForComponent(MenuList.this);
            window.addWindowFocusListener(this);
            setWindowFocused(window.isFocused());
        }

        public void ancestorMoved(AncestorEvent event) {
        }

        public void ancestorRemoved(AncestorEvent event) {
        }

        public void windowGainedFocus(WindowEvent e) {
            setWindowFocused(true);
        }

        public void windowLostFocus(WindowEvent e) {
            setWindowFocused(false);
        }

        private void setWindowFocused(boolean f) {
            if (windowFocused != f) {
                windowFocused = f;
                repaint();
            }
        }

        public void focusGained(FocusEvent e) {
            setFocused(true);
        }

        public void focusLost(FocusEvent e) {
            setFocused(false);
        }

        private void setFocused(boolean f) {
            if (focused != f) {
                focused = f;
                if (selected != null) {
                    selected.repaint();
                }
            }
        }

        public void mousePressed(MouseEvent e) {
            requestFocus();
        }

        public void mouseClicked(MouseEvent e) {
        }

        public void mouseEntered(MouseEvent e) {
        }

        public void mouseExited(MouseEvent e) {
        }

        public void mouseReleased(MouseEvent e) {
        }

        public void mouseWheelMoved(MouseWheelEvent e) {
            if (scrollBar.isVisible()) {
                scrollBar.setValue(scrollBar.getValue() + e.getUnitsToScroll() * 8);
            }
        }
    }

    /**
     * Adds a new group item to the list
     * @param text the title text of the group
     */
    public void addGroup(String text) {
        add(new Group(text));
    }

    private static class Group extends JComponent {

        private String text;

        Group(String text) {
            this.text = text.toUpperCase();
            setForeground(new Color(0xa0000000, true));
            setFont(UIManager.getFont("Label.boldfont"));
        }

        @Override
        public Dimension getPreferredSize() {
            FontMetrics fm = getFontMetrics(getFont());
            return new Dimension(fm.stringWidth(text) + 16, fm.getAscent() + fm.getDescent() + 12);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2D = ResourceUtils.init(g);
            FontMetrics fm = g2D.getFontMetrics();
            g2D.setColor(Color.white);
            g2D.drawString(text, 8, 9 + fm.getAscent());
            g2D.setColor(getForeground());
            g2D.drawString(text, 8, 8 + fm.getAscent());
        }
    }

    /**
     * Navigation item component
     */
    public static class Item extends JComponent {

        private String text;
        private String count;
        private GeneralPath gp;

        /**
         * Creates a new component
         * @param text the title of the item
         */
        public Item(String text) {
            this.text = text;
            setFont(UIManager.getFont("Label.boldfont"));
            addMouseListener(new Listener());
            this.setBackground(Color.WHITE);
        }

        public Item() {
        }

        /**
         * Sets the text on the right
         * @param count text to show, can be null
         */
        public void setCount(String count) {
            this.count = count;
            repaint();
        }

        /**
         * Overwrites the method to calculate the preferred size
         */
        @Override
        public Dimension getPreferredSize() {
            FontMetrics fm = getFontMetrics(getFont());
            return new Dimension(fm.stringWidth(text) + 16, fm.getAscent() + fm.getDescent() + 16);
        }

        /**
         * Overwrites the method to paint the item
         */
        @Override
        protected void paintComponent(Graphics g) {
            MenuList menuList = (MenuList) getParent();
            Graphics2D g2 = ResourceUtils.init(g);
            gp = new GeneralPath();
            int h = this.getHeight() - 1;
            int w = this.getWidth() - 1;
            int x = 0;
            int y = 0;

            if (h == 0 || w == 0) {
                gp.moveTo(x, y);
                gp.lineTo(x + w, y + h);
            } else {
                gp.moveTo(x, y);
                gp.lineTo(x + w, y);
                gp.lineTo(x + w, y + (h * .25));
                gp.lineTo((x + w) - (h * .25), y + (h * .50));
                gp.lineTo(x + w, y + (h * .75));
                gp.lineTo(x + w, y + h);
                gp.lineTo(x, y + h);
                gp.lineTo(x, y);
            }

            boolean selected = (menuList.selected == this);
            boolean inside = (menuList.inside == this);
            boolean window = menuList.windowFocused;
            boolean focused = menuList.focused;
            if (selected) {
                Border border = UIManager.getBorder(focused
                        ? "List.sourceListFocusedSelectionBackgroundPainter" : "sourceListSelectionBackgroundPainter");
                if (border != null) { // mac
                    border.paintBorder(this, g2, 0, 0, getWidth(), getHeight());
                } else {
                    Color lineColor;
                    Color gradientPaintUpper;
                    Color gradientPaintLower;
                    lineColor = new Color(0x4580c8);
                    gradientPaintUpper = new Color(99, 170, 248);
                    gradientPaintLower = new Color(34, 96, 192);

                    g2.setColor(lineColor);
                    g2.drawLine(0, 0, getWidth() - 1, 0);
                    g2.setPaint(
                            new RadialGradientPaint(new Point2D.Double(getWidth() / 2.0, getHeight() / 2.0),
                            getWidth() / 2.0F,
                            new float[]{0.0f, 1.0f},
                            new Color[]{gradientPaintUpper, gradientPaintLower}));
                    g2.fill(gp);
                    g2.setColor(new Color(7, 63, 151));
                    g2.draw(gp);
                }
            }

            if (inside && !selected) {
                g2.setColor(new Color(229, 239, 250));
                g2.fillRect(0, 1, getWidth() - 1, getHeight() - 1);
                g2.setColor(new Color(82, 125, 224));
                g2.drawLine(0, 0, getWidth() - 1, 0);
                g2.drawLine(0, getHeight() - 1, getWidth() - 1, getHeight() - 1);
            }

            g.setFont(UIManager.getFont(selected ? "Label.boldfont" : "Label.font"));
            FontMetrics fm = g.getFontMetrics();
            int fy = 8 + fm.getAscent();
            if (selected) {
                g.drawString(text, 12, fy + 1);
            }
            g.setColor(selected ? Color.white : Color.black);
            g.drawString(text, 12, fy);

            if (count != null) {
                g.setFont(UIManager.getFont("Label.boldfont"));
                fm = g.getFontMetrics();
                int cw = fm.stringWidth(count), cx = getWidth() - cw - 8 - 8;
                g.setColor((selected) ? Color.white : new Color(window ? 0x7da0dd : 0xa6a6a6));
                g.fillRoundRect(cx, 4, cw + 8, fm.getAscent() + fm.getDescent(), 14, 14);
                g.setColor((selected) ? focused ? new Color(0x7da0dd) : Color.gray : Color.white);
                g.drawString(count, cx + 4, 4 + fm.getAscent());
                g.drawImage(new javax.swing.ImageIcon(getClass().getResource("/com/icode/images/16/right.png")).getImage(), cx + 4, 4 + fm.getAscent(), null);
            }
        }

        private class Listener implements MouseListener {

            public void mousePressed(MouseEvent e) {
                MenuList navigator = (MenuList) getParent();
                navigator.requestFocus();
                navigator.select(Item.this);
            }

            public void mouseClicked(MouseEvent e) {
                MenuList navigator = (MenuList) getParent();
                navigator.requestFocus();
                navigator.inside(Item.this);
            }

            public void mouseEntered(MouseEvent e) {
                MenuList navigator = (MenuList) getParent();
                navigator.requestFocus();
                navigator.inside(Item.this);
            }

            public void mouseExited(MouseEvent e) {
                MenuList navigator = (MenuList) getParent();
                navigator.requestFocus();
                navigator.inside(MenuList.dummy);
                navigator.outside(MenuList.dummy);
            }

            public void mouseReleased(MouseEvent e) {
            }
        }
    }

    /**
     * Adds a new separator to the list
     */
    public void addSeparator() {
        add(new Separator());
    }

    private class Separator extends JComponent {

        private Separator() {
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(0, 4);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2D = ResourceUtils.init(g);
            g2D.setColor(new Color(0xd4d4d4));
            g2D.drawLine(8, 1, getWidth() - 9, 1);
            g2D.setColor(new Color(0xe8e8e8));
            g2D.drawLine(8, 2, getWidth() - 9, 2);
        }
    }

    /**
     * Adds a listener to the component
     * @param listener a listener to listen for selection changes
     */
    public void addChangeListener(ChangeListener listener) {
        listenerList.add(ChangeListener.class, listener);
    }

    /**
     * Removes the added listener from the component
     * @param listener an already added listener
     */
    public void removeChangeListener(ChangeListener listener) {
        listenerList.remove(ChangeListener.class, listener);
    }

    private void inside(Item inside) {
        if (inside != null) {
            this.inside = inside;
            this.repaint();
        }
    }

    private void outside(Item outside) {
        this.outside = outside;
        this.repaint();
    }

    private void select(Item selected) {
        if (this.selected != selected) {
            if (this.selected != null) {
                this.selected.repaint();
            }
            if (selected != null) {
                selected.repaint();
            }
            this.selected = selected;

            for (ChangeListener listnerr : listenerList.getListeners(ChangeListener.class)) {
                listnerr.stateChanged(new ChangeEvent(this));
            }
        }
    }
    
    /**
     * Overwrites the method to make it focusable
     */
    @Override
    public boolean isFocusable() {
        return true;
    }

    /**
     * Overwrites the method to return a fixed size
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(125, 360);
    }

    /**
     * Overwrites the method to layout the list items and the scrollbar
     */
    @Override
    public void doLayout() {
        int n = getComponentCount();
        int[] hs = new int[n];
        int sh = 0;
        for (int i = 1; i < n; i++) {
            hs[i] = getComponent(i).getPreferredSize().height;
            sh += hs[i];
        }

        int offset = 0, w = getWidth(), h = getHeight();
        if (sh <= getHeight()) {
            scrollBar.setVisible(false);
        } else {
            scrollBar.setValues(offset = Math.max(0, Math.min(scrollBar.getValue(), sh - h)), h, 0, sh);
            int sw = scrollBar.getPreferredSize().width;
            scrollBar.setBounds(w - sw, 0, sw, h);
            scrollBar.setVisible(true);
            w -= sw;
        }

        int y = 0;
        for (int i = 1; i < n; i++) {
            getComponent(i).setBounds(0, y - offset, w, hs[i]);
            y += hs[i];
        }
    }

    /**
     * Overwrites the method to paint its background
     */
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        Border bg = UIManager.getBorder("List.sourceListBackgroundPainter");
        if (bg != null) { //mac
            bg.paintBorder(this, g2, 0, 0, getWidth(), getHeight());
        } else {
            GradientPaint paint = new GradientPaint(0.0f, 0.0f, new Color(251, 252, 253), 0.0f, getHeight()*.75F, new Color(225, 229, 233));
            g2.setPaint(paint);
            g2.fillRect(0, 0, getWidth(), getHeight());            
            if (this.selected != null) {
                int aboveSelected = SwingUtilities.convertPoint(selected, 0, 0, this).y;
                int belowSelected = aboveSelected + selected.getHeight();
                g2.fillRect(this.getWidth() - 1, belowSelected, 1, this.getHeight() - belowSelected);
                paint = new GradientPaint(0.0f, 0.0f, new Color(220, 221, 222), 0.0f, getHeight(),  new Color(189, 190, 191));
                g2.setPaint(paint);
                g2.fillRect(this.getWidth() - 2, 0, 1, aboveSelected);
                g2.fillRect(this.getWidth() - 2, belowSelected, 1, this.getHeight() - belowSelected);
                paint = new GradientPaint(0.0f, 0.0f, new Color(244, 245, 246), 0.0f, getHeight(), new Color(222, 225, 229));
                g2.setPaint(paint);
                g2.fillRect(this.getWidth() - 3, 0, 1, aboveSelected);
                g2.fillRect(this.getWidth() - 3, belowSelected, 1, this.getHeight() - belowSelected);
                paint = new GradientPaint(0.0f, 0.0f, new Color(247, 248, 249), 0.0f, getHeight(), new Color(225, 229, 233));
                g2.setPaint(paint);
                g2.fillRect(this.getWidth() - 4, 0, 1, aboveSelected);
                g2.fillRect(this.getWidth() - 4, belowSelected, 1, this.getHeight() - belowSelected);
                paint = new GradientPaint(0.0f, 0.0f, new Color(249, 250, 251), 0.0f, getHeight(), new Color(224, 227, 231));
                g2.setPaint(paint);
                g2.fillRect(this.getWidth() - 5, 0, 1, aboveSelected);
                g2.fillRect(this.getWidth() - 5, belowSelected, 1, this.getHeight() - belowSelected);
            } else {
                paint = new GradientPaint(0.0f, 0.0f, new Color(220, 221, 222), 0.0f, getHeight(), new Color(189, 190, 191));
                g2.setPaint(paint);
                g2.fillRect(this.getWidth() - 1, 0, 1, this.getHeight());
                paint = new GradientPaint(0.0f, 0.0f, new Color(244, 245, 246), 0.0f, getHeight(), new Color(222, 225, 229));
                g2.setPaint(paint);
                g2.fillRect(this.getWidth() - 3, 0, 1, this.getHeight());
                paint = new GradientPaint(0.0f, 0.0f, new Color(247, 248, 249), 0.0f, getHeight(), new Color(225, 229, 233));
                g2.setPaint(paint);
                g2.fillRect(this.getWidth() - 4, 0, 1, this.getHeight());
                paint = new GradientPaint(0.0f, 0.0f, new Color(249, 250, 251), 0.0f, getHeight(), new Color(224, 227, 231));
                g2.setPaint(paint);
                g2.fillRect(this.getWidth() - 5, 0, 1, this.getHeight());
            }
        }
    }

    /**
     * Overwrites the method to revalidate the component
     */
    @Override
    protected void addImpl(Component comp, Object constraints, int index) {
        super.addImpl(comp, constraints, index);
        revalidate();
    }

    /**
     * Overwrites the method to empty the selection if the selected item was removed
     */
    @Override
    public void remove(int index) {
        if (getComponent(index) == selected) {
            select(null);
        }
        super.remove(index);
        revalidate();
    }
    private static final Logger LOG = Logger.getLogger(MenuList.class.getName());
}
