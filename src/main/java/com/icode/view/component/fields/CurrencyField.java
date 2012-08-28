/* Copyright 2008-2009 Robert Bajzat. All rights reserved. Use is subject to license terms. */
package com.icode.view.component.fields;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.logging.Logger;

/**
 * Field to edit currency values including a decimal number and the currency
 * number
 */
public class CurrencyField extends DecimalField {
	private final boolean leftSymbol = true;

	public CurrencyField() {
		setColumns(15);
		setHorizontalAlignment(leftSymbol ? LEFT : RIGHT);
		String pattern = ((DecimalFormat) NumberFormat.getCurrencyInstance())
				.toPattern();
		format = new DecimalFormat(leftSymbol ? pattern.substring(1)
				: pattern.endsWith("\u00A4") ? pattern.substring(0,
						pattern.length() - 1) : pattern);
		format.setGroupingUsed(true);
	}

	/**
	 * 
	 * @param i
	 */
	public CurrencyField(int i) {
		this();
		setColumns(i);
	}

	/**
	 * Overwrites the method to increase border for the currency symbol
	 */
	@Override
	public Insets getInsets() {
		Insets is = super.getInsets();
		String symbol = "Kes. ";
		int w = getFontMetrics(getFont()).stringWidth(symbol) + 1;
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
		String symbol = "Kes. ";
		format.getCurrency().getSymbol();
		g.setColor(Color.darkGray);
		((Graphics2D) g).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g.drawString(
				symbol,
				leftSymbol ? (is.left - fm.stringWidth(symbol))
						: (getWidth() - is.right),
				is.top
						+ (getHeight() - is.top - is.bottom + fm.getAscent() - fm
								.getDescent()) / 2);
	}

	private static final Logger LOG = Logger.getLogger(CurrencyField.class
			.getName());
}
