/* Copyright 2008-2009 Robert Bajzat. All rights reserved. Use is subject to license terms. */
package com.icode.view.component.fields;

import java.text.DecimalFormat;
import java.text.ParseException;

/**
 * Field to edit numbers
 */
public class DecimalField extends NumberField {
	public DecimalField(Object[] completeMatches) {
		super(completeMatches);
	}

	public DecimalField() {
		this(16);
	}

	public DecimalField(int columns) {
		setColumns(columns);
		format = new DecimalFormat("#000.00");
		format.setGroupingUsed(true);
	}
	
	public void setContent(Float value) {
	    super.setContent(value);
	}

	public Float getContent() {
		try {
			String text = getText();
			return (text.length() > 0) ? format.parse(text).floatValue() : null;
		} catch (ParseException exc) {
			throw new RuntimeException(exc);
		}
	}
	
	@Override
    protected String getFormattedText(String text) {
        try {
            return format.format(format.parse(text).doubleValue());
        } catch (ParseException exc) {
            return text;
        }
    }
}
