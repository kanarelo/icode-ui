/* Copyright 2008-2009 Robert Bajzat. All rights reserved. Use is subject to license terms. */
package com.icode.view.component.fields;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;

/**
 * Field to edit numbers
 */
public class DecimalField extends NumberField {
    {
    	format = new DecimalFormat("#0.00");
        format.setGroupingUsed(false);
    }

    /**
     *
     * @param completeMatches
     */
    public DecimalField(Object[] completeMatches) {
        super(completeMatches);
    }

    /**
     * 
     * @param completeMatches
     * @param useWindow
     */
    public DecimalField(Object[] completeMatches, boolean useWindow) {
        super(completeMatches, useWindow);
    }

    public DecimalField(){
        this(16);
    }

    public DecimalField(int columns) {
        setColumns(columns);
    }
    
    @Override
	public Number getContent() {
    	Number content = super.getContent();
    	return content != null ? new BigDecimal(content.toString()) : 0.0;
	}
}
