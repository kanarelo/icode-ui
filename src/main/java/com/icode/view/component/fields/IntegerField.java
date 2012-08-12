/* Copyright 2008-2009 Robert Bajzat. All rights reserved. Use is subject to license terms. */
package com.icode.view.component.fields;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.ParsePosition;

/**
 * Field to edit numbers
 */
public class IntegerField extends AbstractField {
    private static final NumberFormat format = NumberFormat.getIntegerInstance();

    static{
        format.setGroupingUsed(false);
    }

    public void setGroupingUsed(boolean use){
        format.setGroupingUsed(use);
    }

    private Integer min, max;

    /**
     *
     * @param completeMatches
     */
    public IntegerField(Object[] completeMatches) {
        super(completeMatches);
    }

    /**
     * 
     * @param completeMatches
     * @param useWindow
     */
    public IntegerField(Object[] completeMatches, boolean useWindow) {
        super(completeMatches, useWindow);
    }

    /**
     *
     * @param text
     */
    public IntegerField(String text) {
        super(text);
    }

    /**
     * Creates a number field
     * @param min minimum accepted value, or null
     * @param max maximum accepted value, or null
     */
    public IntegerField(Integer min, Integer max) {
    	this.min = min;
        this.max = max;
    }

    /**
     * Creates a number field
     * @param required true for non empty field requirement
     * @param min minimum accepted value, or null
     * @param max maximum accepted value, or null
     */
    public IntegerField(Integer columns, Integer min, Integer max, boolean required) {
    	this(min, max);
        setColumns(columns);
        setRequired(required);
    }
    
    public IntegerField(Integer columns, Integer min, Integer max) {
    	this(columns, min, max, false);
    }

    public IntegerField(){
        this(16);
    }

    public IntegerField(int columns) {
        setColumns(columns);
    }

    /**
     * Updates the number value
     * @param value the new value
     */
    public void setContent(Integer value) {
        setText((value != null) ? format.format(value) : "");
    }

    /**
     * Returns the valid number that the component edits
     * @return the current value
     * @throws RuntimeException for invalid content
     */
    @Override
    public Integer getContent() {
        try {
            String text = getText();
            return (text.length() > 0) ? format.parse(text).intValue() : null;
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
        int value = integer.intValue();
        if ((min != null) && (value < min.intValue())) {
            return false;
        }
        if ((max != null) && (value > max.intValue())) {
            return false;
        }
        return true;
    }

    /**
     * Reformats the number
     */
    @Override
    protected String getFormattedText(String text) {
        try {
            return format.format(format.parse(text).longValue());
        } catch (ParseException exc) {
            return text;
        }
    }
}
