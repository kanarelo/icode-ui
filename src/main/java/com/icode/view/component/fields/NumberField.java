package com.icode.view.component.fields;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.ParsePosition;

public abstract class NumberField extends AbstractField {

	protected NumberFormat format;
	protected Number min;
	protected Number max;

	public NumberField(Object[] completeMatches, boolean useWindow) {
		super(completeMatches, useWindow);
	}
	
    public NumberField(Number min, Number max) {
    	this.min = min;
        this.max = max;
    }
    
    public NumberField(int columns, Number min, Number max) {
    	this(min, max);
        setColumns(columns);
    }

	public NumberField(Object[] completeMatches) {
		super(completeMatches);
	}

	public void setGroupingUsed(boolean use) {
	    format.setGroupingUsed(use);
	}

	public NumberField(String text) {
		super(text);
	}

	public NumberField() {
		super();
	}

	/**
	 * Updates the number value
	 * @param value the new value
	 */
	public void setContent(Number value) {
	    setText((value != null) ? format.format(value) : "");
	}

	/**
	 * Returns the valid number that the component edits
	 * @return the current value
	 * @throws RuntimeException for invalid content
	 */
	@Override
	public Number getContent() {
	    try {
	        String text = getText();
	        return (text.length() > 0) ? format.parse(text) : null;
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
        Number value = format.parse(text, position);
        if (value == null) {
            return false;
        }
        if (position.getIndex() != text.length()) {
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
            return format.format(format.parse(text));
        } catch (ParseException exc) {
            return text;
        }
    }
}