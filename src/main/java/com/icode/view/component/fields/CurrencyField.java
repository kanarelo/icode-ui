/* Copyright 2008-2009 Robert Bajzat. All rights reserved. Use is subject to license terms. */
package com.icode.view.component.fields;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.logging.Logger;

/**
 * Field to edit currency values including a decimal number and the currency number
 */
public class CurrencyField extends AbstractField {

    private final NumberFormat format;
    private final boolean leftSymbol;

    {
        String pattern = ((DecimalFormat) NumberFormat.getCurrencyInstance()).toPattern();
        leftSymbol = pattern.startsWith("\u00A4");
        format = new DecimalFormat(leftSymbol ? pattern.substring(1)
                : pattern.endsWith("\u00A4") ? pattern.substring(0, pattern.length() - 1) : pattern);
    }

    /**
     * Creates a currency field
     */
    public CurrencyField() {
        setColumns(5);
        setHorizontalAlignment(leftSymbol ? LEFT : RIGHT);
    }

    /**
     * 
     * @param i
     */
    public CurrencyField(int i) {
        setColumns(i);
        setHorizontalAlignment(leftSymbol ? LEFT : RIGHT);
    }

    /**
     * Updates the currency value
     * @param value the new value
     */
    public void setContent(Float value) {
        setText((value != null) ? format.format(value) : "");
    }

    /**
     * Returns the valid value that the component edits
     * @return the current value
     * @throws RuntimeException for invalid content
     */
    @Override
    public Float getContent() {
        try {
            String text = getText();
            return (text.length() > 0) ? format.parse(text).floatValue() : null;
        } catch (ParseException exc) {
            throw new RuntimeException(exc);
        }
    }

    /**
     * Checks the validity
     */
    @Override
    protected boolean isValid(String text) {
        ParsePosition position = new ParsePosition(0);
        Number integer = format.parse(text, position);
        if (integer == null) {
            return false;
        }
        if (position.getIndex() != text.length()) {
            return false;
        }
        return true;
    }

    /**
     * Reformats the decimal number
     */
    @Override
    protected String getFormattedText(String text) {
        try {
            return format.format(format.parse(text).doubleValue());
        } catch (ParseException exc) {
            return text;
        }
    }

    /**
     * Overwrites the method to increase border for the currency symbol
     */
    @Override
    public Insets getInsets() {
        Insets is = super.getInsets();
        String symbol = format.getCurrency().getSymbol();
        int w = getFontMetrics(getFont()).stringWidth(symbol) + 3;
        if (leftSymbol) {
            is.left += w;
        } else {
            is.right += w;
        }
        return is;
    }

    /**
     * Overwrites the method to paint currency symbol
     */
    @Override
    protected void paintBorder(Graphics g) {
        super.paintBorder(g);
        Insets is = getInsets();
        FontMetrics fm = g.getFontMetrics();
        String symbol = format.getCurrency().getSymbol();
        g.setColor(Color.darkGray);
        g.drawString(symbol,
                leftSymbol ? (is.left - fm.stringWidth(symbol) - 3) : (getWidth() - is.right + 3),
                is.top + (getHeight() - is.top - is.bottom + fm.getAscent() - fm.getDescent()) / 2);
    }
    private static final Logger LOG = Logger.getLogger(CurrencyField.class.getName());
}
