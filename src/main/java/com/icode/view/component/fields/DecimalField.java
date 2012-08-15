/* Copyright 2008-2009 Robert Bajzat. All rights reserved. Use is subject to license terms. */
package com.icode.view.component.fields;

import java.text.DecimalFormat;

/**
 * Field to edit numbers
 */
public class DecimalField extends NumberField {
    {
    	format = DecimalFormat.getNumberInstance();
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
}
