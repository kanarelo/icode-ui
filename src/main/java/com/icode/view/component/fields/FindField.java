/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.icode.view.component.fields;

/**
 *
 * @author Nes
 */
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import com.icode.resources.ResourceUtils;
import com.icode.view.app.MenuAppFrame;
import com.icode.view.autocomplete.AutoCompleteTextField;
import com.icode.view.binding.ValueEditor;

/**
 * An advanced text-field component supporting validation, binding, formatting,
 * auto-completion, and water-marking
 */
public class FindField extends AutoCompleteTextField implements ValueEditor<Object> {

    private boolean required;
    private final String watermark = "Search";
    private transient boolean lock;
    private transient boolean valid;

    /**
     * Creates a field
     */
    protected FindField() {
        super();
        this.init();
    }

    /**
     *
     * @param completeMatches
     * @param useWindow
     */
    public FindField(Object[] completeMatches, boolean useWindow) {
        super(completeMatches, useWindow);
        this.init();
    }

    /**
     *
     * @param completeMatches
     */
    public FindField(Object[] completeMatches) {
        super(completeMatches);
        this.init();
    }

    /**
     *
     * @param text
     */
    public FindField(String text) {
        this();
        this.setText(text);
    }

    public FindField(String text, int i) {
        this(text);
        this.setColumns(i);
    }

    public FindField(int i) {
        this();
        this.setColumns(i);
    }

    FindField(Object[] strArray, int i) {
        this(strArray);
        this.setColumns(i);
    }

    private void init() {
        setDocument(new FieldDocument());
        valid = true;
    }

    /**
     * Updates the content, auto-completion turned off
     * @param text the new content
     */
    @Override
    public void setText(String text) {
        lock = true;
        super.setText(text);
        lock = false;
    }

    /**
     * Sets the required property, used for validation
     * @param required true for non empty field requirement
     */
    public void setRequired(boolean required) {
        this.required = required;
        checkValidity();
    }

    /**
     * Overwrite this method to use a custom validation
     * @param text the actual content
     * @return true if the text was valid
     * @see #remove(String, String)
     */
    protected boolean isValid(String text) {
        return true;
    }

    /**
     * Overwrite this method to update the content as typed,
     * e.g. to convert the typed text upper case
     * @param offset the location of this substring
     * @param text the substring typed
     * @return the updated content
     */
    protected String prepareText(int offset, String text) {
        return text;
    }

    /**
     * Overwrite this method to support auto-completion
     * @param text the current content of the field
     * @return the text to append and select
     * @see #getAutoCompleteValue(Set, String)
     */
    protected String getAutoCompleteValue(String text) {
        return null;
    }

    /**
     * Helper method to find the first possible completion text
     * @param values list of possible values
     * @param text the current content of the field
     * @return the text to append and select
     * @see #getAutoCompleteValue(String)
     */
    protected final String getAutoCompleteValue(Set<String> values, String text) {
        for (String complete : values) {
            if (complete.regionMatches(true, 0, text, 0, text.length())) {
                return complete.substring(text.length());
            }
        }
        return null;
    }

    /**
     * Overwrite this method to format the text when the focus was lost,
     * e.g. add separators to a number
     * @param text the current content of the field
     * @return the formatted text
     * @see #remove(String, String)
     */
    protected String getFormattedText(String text) {
        return text;
    }

    /**
     * Helper method to remove chars from a text,
     * e.g. used for account number formatting and validation
     * @param text the current content of the field
     * @param chars characters to remove
     * @return the text excluding the given chars
     * @see #isValid(String)
     * @see #getFormattedText(String)
     */
    protected final String remove(String text, String chars) {
        StringBuffer sb = null;
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (chars.indexOf(c) != -1) {
                if (sb == null) {
                    sb = new StringBuffer(text.substring(0, i));
                }
            } else if (sb != null) {
                sb.append(c);
            }
        }
        return (sb != null) ? sb.toString() : text;
    }

    /**
     * @see ValueEditor#setContent(Object)
     */
    public void setContent(Object value) {
        setText((value != null) ? value.toString() : null);
    }

    /**
     * @see ValueEditor#getContent()
     */
    public Object getContent() {
        String text = getText();
        return (text.length() > 0) ? text : null;
    }

    /**
     * @see ValueEditor#getValidity()
     */
    public boolean getValidity() {
        return valid;
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

    private class FieldDocument extends PlainDocument implements FocusListener {

        private FieldDocument() {
            addFocusListener(this);
        }

        @Override
        public void insertString(int offset, String string, AttributeSet attributes)
                throws BadLocationException {
            string = prepareText(offset, string);

            int selectionStart = -1, selectionEnd = -1;
            if (!lock && (offset == getLength())) {
                String text = getText(0, getLength()) + string;
                String complete = getAutoCompleteValue(text);
                if (complete != null) {
                    selectionStart = text.length();
                    selectionEnd = selectionStart + complete.length();
                    string += complete;
                }
            }
            super.insertString(offset, string, attributes);
            if (selectionStart != -1) {
                select(selectionStart, selectionEnd);
            }
            checkValidity();
        }

        @Override
        public void remove(int offset, int length) throws BadLocationException {
            super.remove(offset, length);
            checkValidity();
        }

        public void focusGained(FocusEvent e) {
        }

        public void focusLost(FocusEvent e) {
            if (valid) {
                String text = FindField.this.getText().trim();
                String formatted = (text.length() == 0) ? "" : getFormattedText(text);
                if (!formatted.equals(text)) {
                    setText(formatted);
                }
            }
        }
    }

    private void checkValidity() {
        String text = getText().trim();
        boolean v = (text.length() == 0) ? !required : isValid(text);
        if (valid != v) {
            valid = v;
        }

        // fire change event to the listeners
        for (ChangeListener listener : listenerList.getListeners(ChangeListener.class)) {
            listener.stateChanged(new ChangeEvent(this));
        }
    }

    /**
     * Overrides the method to paint water-mark and status icon
     */
    @Override
    public void paint(Graphics g) {
        Graphics2D g2D = ResourceUtils.init(g);
        super.paint(g2D);

        g2D.setColor(Color.gray);

        g.setColor(Color.DARK_GRAY);
        Insets is = getInsets();

        if ((watermark != null) && (getText().length() == 0)) {
            g2D.setColor(Color.GRAY);
            g2D.drawString(watermark, is.left, is.top + g2D.getFontMetrics().getAscent());
        }
    }

    private ImageIcon searchIcon = new javax.swing.ImageIcon(MenuAppFrame.class.getResource("/com/icode/images/16/searchIcon.png"));
    
    /**
     * Paints the validation status for an invalid component
     * @param g graphics context
     * @param component the component
     * @param warning true for warning, false for error colors
     */
    public static void paintStatus(Graphics g, JComponent component, boolean warning) {
        ResourceUtils.init(g);
        int y = component.getHeight() - 8;
        g.setColor(warning ? Color.GREEN : Color.RED);
        g.fillOval(0, y, 8, 8);
        g.setColor(warning ? Color.GRAY : Color.WHITE);
        g.drawLine(2, y + 2, 5, y + 5);
        g.drawLine(5, y + 2, 2, y + 5);
    }

    /**
     * Helper method to calculate the field's preferred size
     * based on the given text not character number
     * @param text the reference content for calculation
     * @return the calculated size
     */
    protected Dimension getSize(String text) {
        Insets is = getInsets();
        FontMetrics fm = getFontMetrics(getFont());
        return new Dimension(
                fm.stringWidth(text) + is.left + is.right + 2,
                fm.getAscent() + fm.getDescent() + is.top + is.bottom);
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
}
