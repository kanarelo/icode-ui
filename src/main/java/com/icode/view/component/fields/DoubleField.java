/* Copyright 2008-2009 Robert Bajzat. All rights reserved. Use is subject to license terms. */
package com.icode.view.component.fields;

import java.text.DecimalFormat;

/**
 * Field to edit numbers
 */
public class DoubleField extends NumberField {
    {
    	format = new DecimalFormat("#000.00");
        format.setGroupingUsed(true);
    }

    /**
     *
     * @param completeMatches
     */
    public DoubleField(Object[] completeMatches) {
        super(completeMatches);
    }

    /**
     * 
     * @param completeMatches
     * @param useWindow
     */
    public DoubleField(Object[] completeMatches, boolean useWindow) {
        super(completeMatches, useWindow);
    }

    public DoubleField(){
        this(16);
    }

    public DoubleField(int columns) {
        setColumns(columns);
    }
    
    @Override
	public Number getContent() {
    	Number content = super.getContent();
    	return content != null ? new Double(content.toString()) : 0.0;
	}
}
