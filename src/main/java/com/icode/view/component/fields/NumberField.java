package com.icode.view.component.fields;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.ParsePosition;

public abstract class NumberField extends AbstractField {

	protected NumberFormat format;
	protected Integer min;
	protected Integer max;

	public NumberField(Object[] completeMatches, boolean useWindow) {
		super(completeMatches, useWindow);
	}
	
    public NumberField(Integer min, Integer max) {
    	this.min = min;
        this.max = max;
    }
    
    public NumberField(Integer columns, Integer min, Integer max) {
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