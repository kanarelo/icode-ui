/* Copyright 2008-2009 Robert Bajzat. All rights reserved. Use is subject to license terms. */
package com.icode.view.component;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JComponent;

import com.icode.resources.ResourceUtils;
import com.icode.util.Commons;

/**
 * Abstract button to overwrite, it's very similar to other buttons
 */
public abstract class PressButton extends JComponent implements MouseListener, FocusListener {
    private transient boolean inside;
    private transient boolean pressed;
    private transient boolean focused;

    protected PressButton() {
        addMouseListener(this);
        addFocusListener(this);
    }

    @Override
    public void setEnabled(boolean enabled) {
        if (enabled != isEnabled()) {
            super.setEnabled(enabled);
            if (enabled) {
                addMouseListener(this);
            } else {
                removeMouseListener(this);
                setInside(pressed = false);
            }
        }
    }

    public void addActionListener(ActionListener listener) {
        listenerList.add(ActionListener.class, listener);
    }

    public void removeActionListener(ActionListener listener) {
        listenerList.remove(ActionListener.class, listener);
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
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = ResourceUtils.init(g);
        paint(g2, isInside(), isPressed(), isFocused());
    }

    /**
     * 
     * @param g
     * @param inside
     * @param pressed
     * @param focused
     */
    protected abstract void paint(Graphics2D g, boolean inside, boolean pressed, boolean focused);

    public void mouseClicked(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
        setInside(true);
        Commons.repaintAncestry(this, 1);
    }

    public void mouseExited(MouseEvent e) {
        setInside(false);
        Commons.repaintAncestry(this, 1);
    }

    public void mousePressed(MouseEvent e) {
        setPressed(true);
        Commons.repaintAncestry(this, 1);
        if (isFocusable()) {
            requestFocus();
        }
    }

    public void mouseReleased(MouseEvent e) {
        setPressed(false);
        if (isInside()) {
            for (ActionListener listener : listenerList.getListeners(ActionListener.class)) {
                listener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
            }
            actionPerformed();
        }
        Commons.repaintAncestry(this, 1);
    }

    protected void actionPerformed() {
    }

    public void focusGained(FocusEvent e) {
        setFocused(true);
        Commons.repaintAncestry(this, 1);
    }

    public void focusLost(FocusEvent e) {
        setFocused(false);
        Commons.repaintAncestry(this, 1);
    }

    /**
     * @return the inside
     */
    protected boolean isInside() {
        return inside;
    }

    /**
     * @param inside the inside to set
     */
    protected void setInside(boolean inside) {
        this.inside = inside;
    }

    /**
     * @return the pressed
     */
    protected boolean isPressed() {
        return pressed;
    }

    /**
     * @param pressed the pressed to set
     */
    protected void setPressed(boolean pressed) {
        this.pressed = pressed;
    }

    /**
     * @return the focused
     */
    protected boolean isFocused() {
        return focused;
    }

    /**
     * @param focused the focused to set
     */
    protected void setFocused(boolean focused) {
        this.focused = focused;
    }
}
