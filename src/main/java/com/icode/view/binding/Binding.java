/* Copyright 2008-2009 Robert Bajzat. All rights reserved. Use is subject to license terms. */
package com.icode.view.binding;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

/**
 * To connect a plain object's fields to editor components
 * @param <E> the type of the binded object
 */
public class Binding<E> {

    private Class<E> clazz;
    private List<Item> items;
    private EventListenerList listenerList;
    private E[] values;
    private transient boolean lock;
    private int changed;
    private int invalid;

    /**
     * Creates a bind object for the given class
     * @param clazz the class of the binded object
     */
    public Binding(Class<E> clazz) {
        this.clazz = clazz;
        items = new ArrayList<Item>();
        listenerList = new EventListenerList();
    }

    /**
     * Adds a new bind connecting a class' field and an editor component
     * @param name the name of the class' field
     * @param component an editor component
     * @throws RuntimeException if setter/getter methods were not found
     */
    public void addBinding(String name, ValueEditor<?> component) {
        items.add(new Item(name, component));
    }

    /**
     * Binds one object or a list of objects to show and edit by the editor components
     * @param values one object or a list of objects to start to edit
     */
    public void setValues(E... values) {
        lock = true;
        changed = 0;
        invalid = 0;
        for (Item item : items) {
            item.add(values);
            if (item.invalid) {
                invalid++;
            }
        }
        this.values = values;
        lock = false;
        fireChangeListener();
    }

    /**
     * Resets the editor components' values to the original ones
     * @return the binded object(s)
     */
    public E[] reset() {
        lock = true;
        changed = 0;
        invalid = 0;
        for (Item item : items) {
            item.reset();
            if (item.invalid) {
                invalid++;
            }
        }
        lock = false;
        fireChangeListener();
        return values;
    }

    /**
     * Updates the binded objects' values
     * @return the binded and now updated object(s)
     */
    public E[] submit() {
        if (invalid > 0) {
            throw new RuntimeException();
        }
        lock = true;
        changed = 0;
        for (Item item : items) {
            item.submit(values);
        }
        lock = false;
        fireChangeListener();
        return values;
    }

    /**
     * Adds a listener to listen to value or validation change
     * @param listener a listener to listen for changes
     */
    public void addChangeListener(ChangeListener listener) {
        listenerList.add(ChangeListener.class, listener);
    }

    /**
     * Removes the added listener
     * @param listener an already added listener
     */
    public void removeChangeListener(ChangeListener listener) {
        listenerList.remove(ChangeListener.class, listener);
    }

    private void setChanged(int diff) {
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
     * @return true if at least one field was changed
     */
    public boolean isChanged() {
        return changed > 0;
    }

    private void setInvalid(int diff) {
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
     * @return true if all the edited fields had valid value
     */
    public boolean isValid() {
        return invalid == 0;
    }

    private void fireChangeListener() {
        for (ChangeListener listener : listenerList.getListeners(ChangeListener.class)) {
            listener.stateChanged(new ChangeEvent(this));
        }
    }
    private static final Object multipleValue = ":multiple";

    private class Item implements ChangeListener {

        private Method getter, setter;
        private ValueEditor<?> component;
        private Object originalValue;
        private Object currentValue;
        private boolean changed;
        private boolean invalid;

        private Item(String name, ValueEditor<?> component) {
            name = Character.toUpperCase(name.charAt(0)) + name.substring(1);
            try {
                try {
                    getter = clazz.getMethod("get" + name);
                } catch (NoSuchMethodException nsme) {
                    getter = clazz.getMethod("is" + name);
                }
                setter = clazz.getMethod("set" + name, getter.getReturnType());
            } catch (Exception exc) {
                throw new RuntimeException(exc);
            }

            this.component = component;
            component.addChangeListener(this);
        }

        private void add(E... values) {
            try {
                originalValue = getValueOf(values[0]);
                for (int i = 1; i < values.length; i++) {
                    if (!equals(originalValue, getValueOf(values[i]))) {
                        originalValue = multipleValue;
                        break;
                    }
                }
            } catch (Exception exc) {
                throw new RuntimeException(exc);
            }
            currentValue = originalValue;
            changed = false;
            invalid = false;
            component.setContent((currentValue != multipleValue) ? currentValue : null);
            invalid = !component.getValidity();
        }

        public void stateChanged(ChangeEvent e) {
            currentValue = component.getContent();
            boolean different = !equals(originalValue, currentValue);
            boolean wrong = !component.getValidity();
            if (changed != different) {
                changed = different;
                setChanged(changed ? 1 : -1);
            }
            if (invalid != wrong) {
                invalid = wrong;
                setInvalid(invalid ? 1 : -1);
            }
        }

        private void reset() {
            currentValue = originalValue;
            changed = false;
            invalid = false;
            component.setContent((currentValue != multipleValue) ? currentValue : null);
            invalid = !component.getValidity();
        }

        private void submit(E... values) {
            if (changed && !invalid && (values != null)) {
                try {
                    for (E target : values) {
                        setValueOf(target, currentValue);
                    }
                } catch (Exception exc) {
                    throw new RuntimeException(setter.getName(), exc);
                }
                originalValue = currentValue;
                changed = false;
            }
        }

        private Object getValueOf(E target) throws Exception {
            return getter.invoke(target);
        }

        private void setValueOf(E target, Object value) throws Exception {
            setter.invoke(target, value);
        }

        private boolean equals(Object a, Object b) {
            return (a == b) || ((a != null) && a.equals(b));
        }
    }
    private static final Logger LOG = Logger.getLogger(Binding.class.getName());
}
