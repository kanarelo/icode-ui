/* Copyright 2008-2009 Robert Bajzat. All rights reserved. Use is subject to license terms. */
package com.icode.view.binding;

import javax.swing.event.ChangeListener;


/**
 * Components editing a value must implement this interface
 * @param <E> the type of the binded and edited object
 */
public interface ValueEditor<E> {

    /**
     * Updates the content of the editor component
     * @param value the new content
     */
    void setContent(Object value);

    /**
     * Returns the valid value that the component edits
     * @return the value
     */
    Object getContent();

    /**
     * Checks the validity of the content
     * @return true for valid content, false for invalid
     */
    boolean getValidity();

    /**
     * Adds a listener to the editor component
     * @param listener a listener to listen for changes
     */
    void addChangeListener(ChangeListener listener);

    /**
     * Removes the added listener from the component
     * @param listener an already added listener
     */
    void removeChangeListener(ChangeListener listener);
}
