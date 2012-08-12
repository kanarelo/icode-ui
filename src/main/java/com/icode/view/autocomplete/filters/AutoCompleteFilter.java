package com.icode.view.autocomplete.filters;

/*
 * AutoCompleteFilter.java
 *
 * Created on October 27, 2005, 3:56 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */


import javax.swing.JTextField;

/**
 * A filter that will attempt to autocomplete enties into a textfield with the string representations
 * of objects in a given array.
 * 
 * Add this filter class to the Document of the text field.
 * 
 * The first match in the array is the one used to autocomplete. So sort your array by most important
 * objects first.
 * @author neilcochrane
 */
public class AutoCompleteFilter extends AbstractAutoCompleteFilter
{
    /** Creates a new instance of AutoCompleteFilter
     * @param completerObjs an array of objects used to attempt completion
     * @param textField the text component to receive the completion
     */
    public AutoCompleteFilter(Object[] completerObjs, JTextField textField)
    {
        _objectList = completerObjs;
        _textField = textField;
    }   
    
    /**
     *
     * @return
     */
    @Override
    public int getCompleterListSize()
    {
      return getObjectList().length;
    }

    /**
     *
     * @param i
     * @return
     */
    @Override
    public Object getCompleterObjectAt(int i)
    {
      return getObjectList()[i];
    }

    /**
     *
     * @return
     */
    @Override
    public JTextField getTextField()
    {
      return _textField;
    }

    /**
     * Set the list of objects to match against.
     * @param objectsToMatch
     */
    public void setCompleterMatches(Object[] objectsToMatch)
    {
        setObjectList(objectsToMatch);
        setFirstSelectedIndex(-1);
    }
        
    /**
     *
     */
    private JTextField _textField;
    /**
     *
     */
    private Object[]   _objectList;

    /**
     * @param textField the _textField to set
     */
    public void setTextField(JTextField textField) {
        this._textField = textField;
    }

    /**
     * @return the _objectList
     */
    public Object[] getObjectList() {
        return _objectList;
    }

    /**
     * @param objectList the _objectList to set
     */
    public void setObjectList(Object[] objectList) {
        this._objectList = objectList;
    }
}
