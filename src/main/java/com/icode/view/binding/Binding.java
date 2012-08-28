/* Copyright 2008-2009 Robert Bajzat. All rights reserved. Use is subject to license terms. */
package com.icode.view.binding;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

/**
 * To connect a plain object's fields to editor components
 * 
 * @param <E>
 *            the type of the binded object
 */
public class Binding<E> {

	private Class<E> clazz;
	private List<BoundItem<E>> items;
	private EventListenerList listenerList;
	private E[] values;
	private transient boolean lock;
	private int changed;
	private int invalid;

	/**
	 * Creates a bind object for the given class
	 * 
	 * @param clazz
	 *            the class of the binded object
	 */
	public Binding(Class<E> clazz) {
		this.clazz = clazz;
		items = new ArrayList<BoundItem<E>>();
		listenerList = new EventListenerList();
	}

	/**
	 * Adds a new bind connecting a class' field and an editor component
	 * 
	 * @param name
	 *            the name of the class' field
	 * @param component
	 *            an editor component
	 * @throws RuntimeException
	 *             if setter/getter methods were not found
	 */
	public void addBinding(String name, ValueEditor<?> component) {
		items.add(new BoundItem<E>(name, component, this, clazz));
	}

	/**
	 * Binds one object or a list of objects to show and edit by the editor
	 * components
	 * 
	 * @param values
	 *            one object or a list of objects to start to edit
	 */
	public void setValues(E... values) {
		lock = true;
		changed = 0;
		invalid = 0;
		for (BoundItem<E> item : items) {
			item.add(values);
			if (item.isInvalid()) {
				invalid++;
			}
		}
		this.values = values;
		lock = false;
		fireChangeListener();
	}

	/**
	 * Resets the editor components' values to the original ones
	 * 
	 * @return the binded object(s)
	 */
	public E[] reset() {
		lock = true;
		changed = 0;
		invalid = 0;
		for (BoundItem<E> item : items) {
			item.reset();
			if (item.isInvalid()) {
				invalid++;
			}
		}
		lock = false;
		fireChangeListener();
		return values;
	}

	/**
	 * Updates the binded objects' values
	 * 
	 * @return the binded and now updated object(s)
	 */
	public E[] submit() {
		if (invalid > 0) {
			throw new RuntimeException();
		}
		lock = true;
		changed = 0;
		for (BoundItem<E> item : items) {
			item.submit(values);
		}
		lock = false;
		fireChangeListener();
		return values;
	}

	/**
	 * Adds a listener to listen to value or validation change
	 * 
	 * @param listener a listener to listen for changes
	 */
	public void addChangeListener(ChangeListener listener) {
		listenerList.add(ChangeListener.class, listener);
	}

	/**
	 * Removes the added listener
	 * 
	 * @param listener
	 *            an already added listener
	 */
	public void removeChangeListener(ChangeListener listener) {
		listenerList.remove(ChangeListener.class, listener);
	}

	void setChanged(int diff) {
		if (lock) {
			return;
		}
		changed += diff;
		if ((changed == 0) || (changed - diff == 0)) {
			fireChangeListener();
		}
	}

	/**
	 * Returns whether the binded object was edited
	 * 
	 * @return true if at least one field was changed
	 */
	public boolean isChanged() {
		return changed > 0;
	}

	void setInvalid(int diff) {
		if (lock) {
			return;
		}
		invalid += diff;
		if ((invalid == 0) || (invalid - diff == 0)) {
			fireChangeListener();
		}
	}

	/**
	 * Returns whether the edited object is valid
	 * 
	 * @return true if all the edited fields had valid value
	 */
	public boolean isValid() {
		return invalid == 0;
	}

	private void fireChangeListener() {
		for (ChangeListener listener : listenerList
				.getListeners(ChangeListener.class)) {
			listener.stateChanged(new ChangeEvent(this));
		}
	}

	private static final Logger LOG = Logger.getLogger(Binding.class.getName());
}
