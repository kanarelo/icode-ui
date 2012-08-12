package com.icode.view.autocomplete;

import java.util.Vector;

import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;

import com.icode.view.autocomplete.filters.AutoCompleteComboBoxFilter;

/**
 * An editable combo class that will autocomplete the user entered text to the entries
 * in the combo drop down. 
 * 
 * You can directly add auto-complete to existing JComboBox derived classes
 * using:
 * AutoCompleteComboBoxFilter.addCompletion(yourCombo);
 * 
 * @author ncochran
 */
public class AutoCompleteComboBox extends JComboBox {

    /**
     *
     * @param aModel
     */
    public AutoCompleteComboBox(ComboBoxModel aModel) {
        super(aModel);
        // TODO Auto-generated constructor stub
    }

    /**
     *
     * @param items
     */
    public AutoCompleteComboBox(Object[] items) {
        super(items);
        _init();
    }

    /**
     *
     * @param items
     */
    public AutoCompleteComboBox(Vector<?> items) {
        super(items);
        // TODO Auto-generated constructor stub
    }

    /**
     *
     */
    public AutoCompleteComboBox() {
        super();
        // TODO Auto-generated constructor stub
    }

    private void _init() {
        setEditable(true);

        _filter = AutoCompleteComboBoxFilter.addCompletionMechanism(this);
    }

    /**
     *
     * @return
     */
    public boolean isCaseSensitive() {
        return _filter.isCaseSensitive();
    }

    /**
     *
     * @return
     */
    public boolean isCorrectingCase() {
        return _filter.isCorrectingCase();
    }

    /**
     *
     * @param caseSensitive
     */
    public void setCaseSensitive(boolean caseSensitive) {
        _filter.setCaseSensitive(caseSensitive);
    }

    /**
     *
     * @param correctCase
     */
    public void setCorrectCase(boolean correctCase) {
        _filter.setCorrectCase(correctCase);
    }
    private AutoCompleteComboBoxFilter _filter;
}
