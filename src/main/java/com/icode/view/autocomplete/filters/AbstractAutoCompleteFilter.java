package com.icode.view.autocomplete.filters;

import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;

/**
 *
 * @author Nes
 */
abstract public class AbstractAutoCompleteFilter extends DocumentFilter {

    /**
     *
     * @return
     */
    abstract public int getCompleterListSize();

    /**
     *
     * @param i
     * @return
     */
    abstract public Object getCompleterObjectAt(int i);

    /**
     *
     * @return
     */
    abstract public JTextField getTextField();

    @Override
    public void replace(FilterBypass filterBypass, int offset, int length, String string, AttributeSet attributeSet)
            throws BadLocationException {
        super.replace(filterBypass, offset, length, string, attributeSet);
        Document doc = filterBypass.getDocument();
        setPreText(doc.getText(0, doc.getLength()));
        setFirstSelectedIndex(-1);

        for (int i = 0; i < getCompleterListSize(); i++) {
            String objString = getCompleterObjectAt(i).toString();

            if ((isCase())
                    ? objString.equals(getPreText())
                    : objString.equalsIgnoreCase(getPreText())) {
                setFirstSelectedIndex(i);

                if (isCorrective()) {
                    filterBypass.replace(0, getPreText().length(), objString, attributeSet);
                }
                break;
            }

            if (objString.length() <= getPreText().length()) {
                continue;
            }

            String objStringStart = objString.substring(0, getPreText().length());

            if ((isCase())
                    ? objStringStart.equals(getPreText())
                    : objStringStart.equalsIgnoreCase(getPreText())) {
                String objStringEnd = objString.substring(getPreText().length());
                if (isCorrective()) {
                    filterBypass.replace(0, getPreText().length(), objString, attributeSet);
                } else {
                    filterBypass.insertString(getPreText().length(), objStringEnd, attributeSet);
                }

                getTextField().select(getPreText().length(), doc.getLength());
                setFirstSelectedIndex(i);
                break;
            }
        }
    }

    @Override
    public void insertString(FilterBypass filterBypass, int offset, String string, AttributeSet attributeSet)
            throws BadLocationException {
        super.insertString(filterBypass, offset, string, attributeSet);
    }

    @Override
    public void remove(FilterBypass filterBypass, int offset, int length)
            throws BadLocationException {
        super.remove(filterBypass, offset, length);
    }

    /**
     *
     * @param caseSensitive
     */
    public void setCaseSensitive(boolean caseSensitive) {
        setCase(caseSensitive);
    }

    /**
     *
     * @return
     */
    public boolean isCaseSensitive() {
        return isCase();
    }

    /**
     * Will change the user entered part of the string to match the case of the matched item.
     *
     * e.g.
     * "europe/lONdon" would be corrected to "Europe/London"
     *
     * This option only makes sense if case sensitive is turned off
     * @param correctCase
     */
    public void setCorrectCase(boolean correctCase) {
        setCorrective(correctCase);
    }

    /**
     *
     * @return
     */
    public boolean isCorrectingCase() {
        return isCorrective();
    }

    /**
     *
     * @return the index of the first object in the object array that can match
     * the user entered string (-1 if no object is currently being used as a match)
     */
    public int getLeadingSelectedIndex() {
        return getFirstSelectedIndex();
    }
    /**
     *
     */
    /**
     *
     */
    private String _preText;
    private boolean _case = false;
    private boolean _corrective = true;
    private int _firstSelectedIndex = -1;

    /**
     * @return the _preText
     */
    protected String getPreText() {
        return _preText;
    }

    /**
     * @param preText the _preText to set
     */
    protected void setPreText(String preText) {
        this._preText = preText;
    }

    /**
     * @return the _case
     */
    protected boolean isCase() {
        return _case;
    }

    /**
     * @param _case
     */
    protected void setCase(boolean _case) {
        this._case = _case;
    }

    /**
     * @return the _corrective
     */
    protected boolean isCorrective() {
        return _corrective;
    }

    /**
     * @param corrective the _corrective to set
     */
    protected void setCorrective(boolean corrective) {
        this._corrective = corrective;
    }

    /**
     * @return the _firstSelectedIndex
     */
    protected int getFirstSelectedIndex() {
        return _firstSelectedIndex;
    }

    /**
     * @param firstSelectedIndex the _firstSelectedIndex to set
     */
    protected void setFirstSelectedIndex(int firstSelectedIndex) {
        this._firstSelectedIndex = firstSelectedIndex;
    }
}
