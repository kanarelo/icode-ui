/* Copyright 2008-2009 Robert Bajzat. All rights reserved. Use is subject to license terms. */
package com.icode.view.component.fields;

import java.awt.Dimension;
import java.util.regex.Pattern;

/**
 * Field to edit text values
 */
public class StringField extends AbstractField {

    private int minLength, maxLength;
    private Pattern pattern;

    /**
     * 
     * @param completeMatches
     */
    public StringField(Object[] completeMatches) {
        super(completeMatches);
        this.setColumns(16);
    }

    /**
     *
     * @param completeMatches
     * @param useWindow
     */
    public StringField(Object[] completeMatches, boolean useWindow) {
        super(completeMatches, useWindow);
        this.setColumns(16);
    }

    /**
     *
     * @param text
     */
    public StringField(String text) {
        this();
        this.setText(text);
    }

    /**
     * Creates a string field
     */
    public StringField() {
        this(16);
    }

    /**
     * Creates a string field
     * @param columns the number of characters to calculate component's width
     */
    public StringField(int columns) {
        setColumns(columns);
    }

    /**
     * Creates a string field
     * @param columns the number of characters
     * @param required true for non empty field requirement
     * @param minLength minimum length of the text, or null
     * @param maxLength maximum length of the text, or null
     */
    public StringField(int columns, boolean required, Integer minLength, Integer maxLength) {
        setColumns(columns);
        setRequired(required);
        this.minLength = minLength;
        this.maxLength = maxLength;
    }

    /**
     * Sets a pattern for validation
     * @param pattern a regular expression, or null
     */
    public void setPattern(String pattern) {
        this.pattern = (pattern != null) ? Pattern.compile(pattern) : null;
    }

    /**
     * Checks the validity
     */
    @Override
    protected boolean isValid(String text) {
        int n = text.length();
        if ((minLength > 0) && (n < minLength)) {
            return false;
        }
        if ((maxLength > 0) && (n > maxLength)) {
            return false;
        }
        if ((pattern != null) && !pattern.matcher(text).matches()) {
            return false;
        }
        return true;
    }
}
