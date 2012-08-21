/* Copyright 2008-2009 Robert Bajzat. All rights reserved. Use is subject to license terms. */
package com.icode.view.component.fields;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
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

import com.icode.resources.ResourceUtils;
import com.icode.view.binding.ValueEditor;

/**
 * Advanced combobox component with validation and multiline items
 */
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
			if (e.getStateChange() == ItemEvent.SELECTED) {
				for (ChangeListener listener : listenerList
						.getListeners(ChangeListener.class)) {
					listener.stateChanged(new ChangeEvent(this));
				}
			}
		}
	}

	private static final Logger LOG = Logger
			.getLogger(ComboBox.class.getName());
}
