/* Copyright 2008-2009 Robert Bajzat. All rights reserved. Use is subject to license terms. */
package com.icode.view.table;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.NumberFormat;
import java.util.logging.Logger;

import javax.swing.JComponent;
import javax.swing.ListModel;
import javax.swing.UIManager;

/**
 * Cell is a binding object to connect a row's value's field
 * and its graphic representation.
 * Note: it is under construction, it'll be radically changed.
 */
public class Cell {

    private String name;
    private Method getMethod, setMethod;
    private boolean newLine;
    private transient int x, y, width, height;
    private Color color = Color.black;
    private Font font = UIManager.getFont("List.font");
    private Format format;
    private boolean rightAligned;
    private boolean staticText;
    private static final Format progress = new DecimalFormat();

    /**
     * Creates a new cell
     * @param name the name of the getter (and for editable cell setter) method
     */
    public Cell(String name) {
        this.name = name;
    }

    void init(Class<?> clazz) {
        if (staticText || (name == null)) {
            return;
        }
        name = Character.toUpperCase(name.charAt(0)) + name.substring(1);
        try {
            try {
                getMethod = clazz.getMethod("get" + name);
            } catch (Throwable exc) {
                getMethod = clazz.getMethod("is" + name);
            }
        } catch (Exception exc) {
            throw new RuntimeException(exc);
        }
    }

    /**
     * Sets the rendering color
     * @param color the color of the text
     * @return the cell itself
     */
    public Cell setColor(Color color) {
        this.color = color;
        return this;
    }

    /**
     * Sets the rendering font
     * @param font the font used to render the text
     * @return the cell itself
     */
    public Cell setFont(Font font) {
        this.font = font;
        return this;
    }

    /**
     * Sets the format to convert the object to string
     * @param format the format
     * @return the cell itself
     */
    public Cell setFormat(Format format) {
        this.format = format;
        return this;
    }

    /**
     * Sets that the value is rendered as integer
     * @return the cell itself
     */
    public Cell setIntFormat() {
        rightAligned = true;
        return setFormat(NumberFormat.getIntegerInstance());
    }

    /**
     * Sets that the value is rendered as percent number
     * @return the cell itself
     */
    public Cell setPercentFormat() {
        rightAligned = true;
        return setFormat(NumberFormat.getPercentInstance());
    }

    /**
     *  Sets that the value is rendered as currency number
     * @return the cell itself
     */
    public Cell setCurrencyFormat() {
        rightAligned = true;
        return setFormat(NumberFormat.getCurrencyInstance());
    }

    /**
     *  Sets that the value is rendered as date
     * @return the cell itself
     */
    public Cell setDateFormat() {
        return setFormat(DateFormat.getDateInstance(DateFormat.LONG));
    }

    /**
     *  Sets that the integer value is rendered as progress bar
     * @return the cell itself
     */
    public Cell setProgressFormat() {
        return setFormat(progress);
    }

    /**
     * The cell name is used as static text not as getter/setter name
     * @return the cell itself
     */
    public Cell setStatic() {
        staticText = true;
        rightAligned = true;
        return this;
    }

    private String format(Object value) {
        return (value == null) ? ""
                : (format != null) ? format.format(value) : value.toString();
    }

    /**
     * Sets the cell editable
     * @return the cell itself
     */
    public Cell setEditable() {
        return this;
    }

    /**
     * Starts a new row for this cell
     * @return the cell itself
     */
    public Cell newLine() {
        setNewLine(true);
        return this;
    }

    private Object getValue(Object item) {
        try {
            return getMethod.invoke(item);
        } catch (Exception exc) {
            throw new RuntimeException(exc);
        }
    }

    private void setValue(Object item, Object value) {
        try {
            setMethod.invoke(item, value);
        } catch (Exception exc) {
            throw new RuntimeException(exc);
        }
    }

    Dimension getSize(Table<?> list) {
        if (format == progress) {
            return new Dimension(60, 7);
        }

        FontMetrics fm = list.getFontMetrics(font);
        if (staticText) {
            return new Dimension(fm.stringWidth(name), fm.getAscent() + fm.getDescent());
        }

        ListModel model = list.getModel();
        int _width = 0;
        for (int i = 0, n = model.getSize(); i < n; i++) {
            Object item = model.getElementAt(i);
            if (item == null) {
                continue;
            }
            Object value = getValue(item);
            if (value == null) {
                continue;
            }
            _width = Math.max(_width, fm.stringWidth(format(value)));
        }
        return new Dimension(_width, fm.getAscent() + fm.getDescent());
    }

    void setBounds(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    boolean isInside(int mx, int my) {
        return (x <= mx) && (y <= my)
                && (x + width > mx) && (y + height > my);
    }

    void paintHover(Graphics2D g) {
        g.fillRoundRect(x - 4, y, width + 8, height, 4, 4);
    }

    void render(Graphics2D g, Object item, boolean selected) {
        g.setFont(font);
        if (!selected) {
            g.setColor(color);
        }
        if (format == progress) {
            int value = Math.max(0, Math.min(((Number) getValue(item)).intValue(), 100));
            int w = Math.min(width, 80), length = value * w / 100,
                    h = 7, py = y + (height - h) / 2;

            Color _color = g.getColor();
            if (length < w) {
                g.setColor(new Color(0xa0a0a0));
                g.drawRect(x, py, w - 1, h - 1);
                g.setPaint(new GradientPaint(
                        0, py + 1, new Color(0xc0c0c0), 0, py + h - 2, new Color(0xe0e0e0)));
                g.fillRect(x + 1, py + 1, w - 2, h - 2);
            }

            if (length > 0) {
                g.setColor(new Color(0xe06000));
                g.drawRect(x, py, length - 1, h - 1);
                g.setPaint(new GradientPaint(
                        0, py + 1, new Color(0xf0c070), 0, py + h - 2, new Color(0xf07030)));
                g.fillRect(x + 1, py + 1, length - 2, h - 2);
            }
            g.setColor(_color);
        } else {
            String text = staticText ? name : format(getValue(item));
            FontMetrics fm = g.getFontMetrics();
            int fx = x;
            if (rightAligned) {
                fx += width - fm.stringWidth(text);
            }
            g.drawString(text, fx, y + (height + fm.getAscent() - fm.getDescent() + 1) / 2);
        }
    }

    /**
     * @return the newLine
     */
    public boolean isNewLine() {
        return newLine;
    }

    /**
     * @param newLine the newLine to set
     */
    public void setNewLine(boolean newLine) {
        this.newLine = newLine;
    }


    private static class Header extends JComponent {

        private String text;

        private Header(String text) {
            this.text = text;

            setBackground(UIManager.getColor("TableHeader.background"));
            setForeground(UIManager.getColor("TableHeader.foreground"));
            setFont(UIManager.getFont("TableHeader.font"));
            setBorder(UIManager.getBorder("TableHeader.cellBorder"));
        }

        @Override
        public Dimension getPreferredSize() {
            Insets is = getInsets();
            FontMetrics fm = getFontMetrics(getFont());
            return new Dimension(is.left + is.right + fm.stringWidth(text),
                    is.top + is.bottom + fm.getAscent() + fm.getDescent());
        }

        @Override
        public void paint(Graphics g) {
            super.paint(g);
            Insets is = getInsets();
            g.drawString(text, is.left, is.top + g.getFontMetrics().getAscent());
        }
    }
    private static final Logger LOG = Logger.getLogger(Cell.class.getName());
}
