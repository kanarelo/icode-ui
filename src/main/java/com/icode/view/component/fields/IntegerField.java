/* Copyright 2008-2009 Robert Bajzat. All rights reserved. Use is subject to license terms. */
package com.icode.view.component.fields;

import java.text.NumberFormat;

/**
 * Field to edit numbers
 */
public class IntegerField extends NumberField {
	{
		format = NumberFormat.getIntegerInstance();
		format.setGroupingUsed(false);
	}

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

	public IntegerField() {
		this(16);
	}

	public IntegerField(int columns) {
		setColumns(columns);
	}
}
