/* Copyright 2008-2009 Robert Bajzat. All rights reserved. Use is subject to license terms. */
package com.icode.view.table;

import java.awt.Cursor;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * Table component supporting multiline content, binding, and editing
 * 
 * @param <E>
 *            the type of the objects added as rows
 */
public class Table<E> extends JList {

	private TableRenderer renderer;

	/**
	 * Creates a table
	 */
	public Table() {
		setModel(new DefaultListModel());
		Listeners listener = new Listeners();
		getSelectionModel().addListSelectionListener(listener);
		addMouseListener(listener);
		addMouseMotionListener(listener);
		addAncestorListener(listener);
	}

	public void setCells(Class<E> clazz, Cell... cells) {
		setCellRenderer(renderer = getRenderer(clazz, cells));
	}

	protected TableRenderer getRenderer(Class<E> clazz, Cell... cells) {
		return new TableRenderer(this, clazz, cells);
	}

	/**
	 * Add the given row to the table
	 * 
	 * @param item
	 *            an object to add
	 */
	public void addItems(List<E> items) {
		for (E item : items) {
			addItem(item);
		}
	}

	/**
	 * Add the given row to the table
	 * 
	 * @param item
	 *            an object to add
	 */
	public void addItem(E item) {
		((DefaultListModel) getModel()).addElement(item);
	}
		
	/**
	 * Remove all items
	 * 
	 * @param item
	 *            an object to add
	 */
	public void removeAllItems() {
		((DefaultListModel) getModel()).removeAllElements();
	}

	/**
	 * Removes the given row
	 * 
	 * @param value
	 *            the value object
	 * @return true if the value was a row of this table
	 */
	public boolean removeItem(E value) {
		return ((DefaultListModel) getModel()).removeElement(value);
	}

	/**
	 * Selects the last row
	 */
	public void selectLast() {
		int i = getModel().getSize() - 1;
		setSelectedIndex(i);
		scrollRectToVisible(getCellBounds(i, i));
	}

	/**
	 * Returns the selected rows
	 * 
	 * @return the list of selected rows
	 */
	@SuppressWarnings("unchecked")
	public List<E> getSelectedItems() {
		List<E> selecteds = new ArrayList<E>();
		for (Object item : getSelectedValues()) {
			selecteds.add((E) item);
		}
		return selecteds;
	}

	/**
	 * Listener to listen to table selection changes and double clicks
	 * 
	 * @param <E>
	 *            the type of the objects added as rows
	 */
	public static interface Listener<E> extends EventListener {

		/**
		 * A row's selection was changed
		 * 
		 * @param e
		 *            the selection event
		 */
		public void selectionChanged(ListSelectionEvent e);

		/**
		 * Double clicked on a row
		 * 
		 * @param item
		 *            the row's value
		 */
		public void doubleClicked(E item);
	}

	/**
	 * Adds a new listener to the listener list
	 * 
	 * @param listener
	 *            a listener to listen to table selection changes and double
	 *            clicks
	 */
	public void addListListener(Listener<E> listener) {
		listenerList.add(Listener.class, listener);
	}

	/**
	 * Removes the given listener
	 * 
	 * @param listener
	 *            an already added listener to be removed
	 */
	public void removeListListener(Listener<E> listener) {
		listenerList.remove(Listener.class, listener);
	}

	private class Listeners implements ListSelectionListener, MouseListener,
			MouseMotionListener, AncestorListener {

		private int insideIndex = -1;
		private Cell insideItem;

		public void valueChanged(ListSelectionEvent e) {
			for (Listener<E> listener : listenerList
					.getListeners(Listener.class)) {
				listener.selectionChanged(e);
			}
		}

		@SuppressWarnings("unchecked")
		public void mouseClicked(MouseEvent e) {
			if (e.getClickCount() > 1) {
				int index = locationToIndex(e.getPoint());
				if (index != -1) {
					E value = (E) getModel().getElementAt(index);
					for (Listener<E> listener : listenerList
							.getListeners(Listener.class)) {
						listener.doubleClicked(value);
					}
				}
			}
		}

		public void mouseEntered(MouseEvent e) {
			moved(e);
		}

		public void mouseExited(MouseEvent e) {
		}

		public void mousePressed(MouseEvent e) {
		}

		public void mouseReleased(MouseEvent e) {
		}

		public void mouseMoved(MouseEvent e) {
			moved(e);
		}

		public void mouseDragged(MouseEvent e) {
		}

		private void moved(MouseEvent e) {
			int index = locationToIndex(e.getPoint());
			Cell item = null;
			if (index != -1) {
				Rectangle bounds = getCellBounds(index, index);
				item = renderer.getRendererItemAt(e.getX() - bounds.x, e.getY()
						- bounds.y);
			}
			if ((insideIndex != index) || (insideItem != item)) {
				insideIndex = index;
				insideItem = item;
				setCursor(Cursor
						.getPredefinedCursor(((insideItem != null)) ? Cursor.HAND_CURSOR
								: Cursor.DEFAULT_CURSOR));
			}
		}

		public void ancestorAdded(AncestorEvent event) {
			if (!(getParent().getParent() instanceof JScrollPane)) {
				return;
			}
		}

		public void ancestorMoved(AncestorEvent event) {
		}

		public void ancestorRemoved(AncestorEvent event) {
		}
	}

	@SuppressWarnings("unchecked")
	public void find(String text) {
		clearSelection();
		if (text.length() == 0) {
			return;
		}
		ListModel model = getModel();
		for (int i = 0, n = model.getSize(); i < n; i++) {
			E item = (E) model.getElementAt(i);
			if (matches(item, text)) {
				setSelectedIndex(i);
				scrollRectToVisible(getCellBounds(i, i));
				break;
			}
		}
	}

	protected boolean matches(E item, String text) {
		return false; // name.regionMatches(true, 0, text, 0, text.length());
	}

	private static final Logger LOG = Logger.getLogger(Table.class.getName());
}
