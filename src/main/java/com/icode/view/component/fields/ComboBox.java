/* Copyright 2008-2009 Robert Bajzat. All rights reserved. Use is subject to license terms. */
package com.icode.view.component.fields;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.logging.Logger;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.commons.lang.RandomStringUtils;

import com.icode.resources.ResourceUtils;
import com.icode.view.binding.ValueEditor;

/**
 * Advanced combobox component with validation and multiline items
 */
@SuppressWarnings({ "serial", "rawtypes" })
public class ComboBox extends JComboBox implements ValueEditor<Object> {
	private boolean required;

	/**
	 * Creates a combobox component
	 */
	public ComboBox() {
		super(new DefaultComboBoxModel());
		putClientProperty("JComboBox.isSquare", Boolean.TRUE);
		Renderer _renderer = new Renderer();
		setRenderer(_renderer);
		addItemListener(_renderer);
		setName(RandomStringUtils.randomAlphanumeric(10));
	}

	public ComboBox(Object[] items) {
		this();
		this.setModel(new DefaultComboBoxModel(items));
	}

	/**
	 * Adds a new row to its list
	 * 
	 * @param value
	 *            a value binded to the row, can be null too
	 * @param title
	 *            the main text
	 * @param decription
	 *            the second auxiliary text
	 * @return the component itself
	 */
	public ComboBox add(Object value, String title, String decription) {
		((DefaultComboBoxModel) getModel()).addElement(new Item(value, title,
				decription));
		setSelectedIndex(-1);
		return this;
	}

	public void setHoverStyle() {
		hoverStyle = true;
		setOpaque(false);
	}

	/**
	 * Adds a new row to its list
	 * 
	 * @param items
	 * @return the component itself
	 */
	public ComboBox add(Item... items) {
		for (Item item : items) {
			((DefaultComboBoxModel) getModel()).addElement(item);
			setSelectedIndex(-1);
		}
		return this;
	}

	/**
	 * Sets the required property, used for validation
	 * 
	 * @param required
	 *            true for non empty selection requirement
	 * @return
	 */
	public ComboBox setRequired(boolean required) {
		this.required = required;
		return this;
	}

	/**
	 * Returns the selected item's binded value object
	 * 
	 * @return the selected item's value, or null for empty selection
	 * @see ValueEditor#getContent()
	 */
	public Object getContent() {
		Object selected = getSelectedItem();
		return (selected != null) ? ((Item) selected).value : null;
	}

	/**
	 * Changes selection
	 * 
	 * @param value
	 *            an item's binded value object
	 * @see ValueEditor#setContent(Object)
	 */
	public void setContent(Object value) {
		int index = -1;
		for (int i = 0; i < getItemCount(); i++) {
			Object ivalue = ((Item) getItemAt(i)).value;
			if ((value == ivalue) || ((value != null) && value.equals(ivalue))) {
				index = i;
				break;
			}
		}
		setSelectedIndex(index);
	}

	/**
	 * Checks the validity of the selection, empty selection for required
	 * component is invalid
	 * 
	 * @return true for valid selection, false for invalid
	 * @see ValueEditor#getValidity()
	 */
	public boolean getValidity() {
		return required ? (getSelectedIndex() != -1) : true;
	}

	/**
	 * @see ValueEditor#addChangeListener(ChangeListener)
	 */
	public void addChangeListener(ChangeListener listener) {
		listenerList.add(ChangeListener.class, listener);
	}

	/**
	 * @see ValueEditor#removeChangeListener(ChangeListener)
	 */
	public void removeChangeListener(ChangeListener listener) {
		listenerList.remove(ChangeListener.class, listener);
	}

	public static class Item {

		private Object value;
		private String title;
		private String decription;

		public Item(Object value, String title, String decription) {
			this.value = value;
			this.title = title;
			this.decription = decription;
		}

		public String getDecription() {
			return decription;
		}

		public void setDecription(String decription) {
			this.decription = decription;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public Object getValue() {
			return value;
		}

		public void setValue(Object value) {
			this.value = value;
		}

		@Override
		public String toString() {
			return value.toString();
		}
	}

	/** Overwritten to return preferred size for box layout */
	@Override
	public Dimension getMinimumSize() {
		return getPreferredSize();
	}

	/** Overwritten to return preferred size for box layout */
	@Override
	public Dimension getMaximumSize() {
		return getPreferredSize();
	}

	private final Font plain = UIManager.getFont("Label.font"),
			small = UIManager.getFont("Label.smallfont");
	private boolean hoverStyle;

	private class Renderer extends DefaultListCellRenderer implements
			ItemListener {

		private Item item;
		private int index;
		private boolean selected;

		@Override
		public Component getListCellRendererComponent(JList list, Object value,
				int index, boolean selected, boolean focused) {
			item = (Item) value;
			this.index = index;
			this.selected = selected;
			return super.getListCellRendererComponent(list, null, index,
					selected, focused);
		}

		@Override
		public Dimension getPreferredSize() {
			if (item == null) {
				return new Dimension();
			}
			Insets is = getInsets();
			FontMetrics pfm = getFontMetrics(plain);
			if (index == -1) {
				return new Dimension(is.left + is.right
						+ pfm.stringWidth(item.title) + 1/* mac */, is.top
						+ is.bottom + pfm.getHeight());
			}

			FontMetrics sfm = getFontMetrics(small);
			return new Dimension(is.left
					+ is.right
					+ Math.max(pfm.stringWidth(item.title),
							sfm.stringWidth(item.decription)) + 8, is.top
					+ is.bottom + pfm.getHeight() + sfm.getAscent()
					+ sfm.getDescent() + 4);
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			if (item == null) {
				return;
			}
			ResourceUtils.init(g);
			Insets is = getInsets();
			g.setFont(plain);
			FontMetrics pfm = g.getFontMetrics();
			if (index == -1) {
				g.drawString(item.title, is.left,
						(getHeight() + is.top - is.bottom + pfm.getAscent()
								- pfm.getDescent() + 1) / 2);
			} else {
				g.drawString(item.title, is.left + 4, is.top + pfm.getAscent()
						+ 2);
				g.setFont(small);
				FontMetrics sfm = g.getFontMetrics();
				if (!selected) {
					g.setColor(Color.darkGray);
				}
				g.drawString(item.decription, is.left + 4,
						is.top + pfm.getHeight() + sfm.getAscent() + 2);
			}
		}

		public void itemStateChanged(ItemEvent e) {
			for (ChangeListener listener : ComboBox.this.listenerList
					.getListeners(ChangeListener.class)) {
				listener.stateChanged(new ChangeEvent(this));
			}
		}
	}

	/**
	 * Overwritten to return different size for hover styled component
	 */
	public Dimension getPreferredSize() {
		if (hoverStyle) {
			FontMetrics fm = getFontMetrics(getFont());
			int w = 0, h = fm.getAscent() + fm.getDescent() + 4;
			for (int i = 0, n = getItemCount(); i < n; i++) {
				w = Math.max(fm.stringWidth(((Item) getItemAt(i)).title), w);
			}
			return new Dimension(w + 10 + (h / 2) * 2, h);
		} else {
			return super.getPreferredSize();
		}
	}

	/**
	 * Overwritten to paint hover styled component and validity status icon
	 */
	public void paint(Graphics g) {
		if (hoverStyle) {
			Graphics2D g2 = ResourceUtils.init(g);
			int w = getWidth(), h = getHeight();

			g2.setPaint(new GradientPaint(0, 0, new Color(0xa5a5a5), 0, h - 1,
					new Color(0x989898)));
			g.fillRoundRect(0, 0, w, h, h, h);
			g2.setPaint(new GradientPaint(0, 1, Color.white, 0, h - 3,
					new Color(0xdfdfdf)));
			g.fillRoundRect(1, 1, w - 2, h - 2, h - 2, h - 2);

			Item item = (Item) getSelectedItem();
			g.setColor(Color.black);
			if (item != null) {
				FontMetrics fm = g.getFontMetrics();
				g.drawString(item.title, h / 2, 2 + fm.getAscent());
			}
			g.setColor(Color.darkGray);
			int x = w - h / 2 - 8, y = (h - 8 + 1) / 2;
			g.fillPolygon(new int[] { x, x + 8, x + 4 }, new int[] { y, y,
					y + 8 }, 3);
		} else {
			super.paint(g);
		}
		if (!getValidity())
			AbstractField.paintStatus(g, this, true);
	}

	private static final Logger LOG = Logger
			.getLogger(ComboBox.class.getName());
}
