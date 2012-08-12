package com.icode.view.autocomplete.filters;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JWindow;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;

/**
 *
 * @author Nes
 */
public class CompleterFilterWithWindow extends AutoCompleteFilter {

    /**
     * @return the MAX_VISIBLE_ROWS
     */
    public static int getMAX_VISIBLE_ROWS() {
        return MAX_VISIBLE_ROWS;
    }

    /**
     * @param aMAX_VISIBLE_ROWS the MAX_VISIBLE_ROWS to set
     */
    public static void setMAX_VISIBLE_ROWS(int aMAX_VISIBLE_ROWS) {
        MAX_VISIBLE_ROWS = aMAX_VISIBLE_ROWS;
    }

    /**
     *
     * @param completerObjs
     * @param textField
     */
    public CompleterFilterWithWindow(Object[] completerObjs, JTextField textField) {
        super(completerObjs, textField);
        _init();
    }

    @Override
    public void insertString(FilterBypass filterBypass, int offset, String string, AttributeSet attributeSet) throws BadLocationException {
        setFilterWindowVisible(false);
        super.insertString(filterBypass, offset, string, attributeSet);
    }

    @Override
    public void remove(FilterBypass filterBypass, int offset, int length) throws BadLocationException {
        setFilterWindowVisible(false);
        super.remove(filterBypass, offset, length);
    }

    @Override
    public void replace(FilterBypass filterBypass, int offset, int length, String string, AttributeSet attributeSet) throws BadLocationException {
        if (isIsAdjusting()) {
            filterBypass.replace(offset, length, string, attributeSet);
            return;
        }

        super.replace(filterBypass, offset, length, string, attributeSet);

        if (getLeadingSelectedIndex() == -1) {
            if (isFilterWindowVisible()) {
                setFilterWindowVisible(false);
            }

            return;
        }

        getLm().setFilter(getPreText());

        if (!isFilterWindowVisible()) {
            setFilterWindowVisible(true);
        } else {
            _setWindowHeight();
        }

        getList().setSelectedValue(getTextField().getText(), true);
    }

    private void _init() {
        setFwl(new FilterWindowListener());
        setLm(new FilterListModel(getObjectList()));
        setTfkl(new TextFieldKeyListener());
        getTextField().addKeyListener(getTfkl());

        EscapeAction escape = new EscapeAction();
        getTextField().registerKeyboardAction(escape, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    /**
     *
     * @return
     */
    public boolean isFilterWindowVisible() {
        return ((getWin() != null) && (getWin().isVisible()));
    }

    /**
     *
     * @param caseSensitive
     */
    @Override
    public void setCaseSensitive(boolean caseSensitive) {
        super.setCaseSensitive(caseSensitive);
        getLm().setCaseSensitive(caseSensitive);
    }

    /**
     *
     * @param visible
     */
    public void setFilterWindowVisible(boolean visible) {
        if (visible) {
            _initWindow();
            getList().setModel(getLm());
            getWin().setVisible(true);
            getTextField().requestFocus();
            getTextField().addFocusListener(getFwl());
        } else {
            if (getWin() == null) {
                return;
            }

            getWin().setVisible(false);
            getWin().removeFocusListener(getFwl());
            Window ancestor = SwingUtilities.getWindowAncestor(getTextField());
            ancestor.removeMouseListener(getFwl());
            getTextField().removeFocusListener(getFwl());
            getTextField().removeAncestorListener(getFwl());
            getList().removeMouseListener(getLml());
            getList().removeListSelectionListener(getLsl());
            setLsl(null);
            setLml(null);
            getWin().dispose();
            setWin(null);
            setList(null);
        }
    }

    private void _initWindow() {
        Window ancestor = SwingUtilities.getWindowAncestor(getTextField());
        setWin(new JWindow(ancestor));
        getWin().addWindowFocusListener(getFwl());
        getTextField().addAncestorListener(getFwl());
        ancestor.addMouseListener(getFwl());
        setLsl(new ListSelListener());
        setLml(new ListMouseListener());

        setList(new JList(getLm()));
        getList().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        getList().setFocusable(false);
        getList().setPrototypeCellValue("Prototype");
        getList().addListSelectionListener(getLsl());
        getList().addMouseListener(getLml());

        setSp(new JScrollPane(getList(), ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER));
        getSp().setFocusable(false);
        getSp().getVerticalScrollBar().setFocusable(false);

        _setWindowHeight();
        getWin().setLocation(getTextField().getLocationOnScreen().x, getTextField().getLocationOnScreen().y + getTextField().getHeight());
        getWin().getContentPane().add(getSp());
    }

    private void _setWindowHeight() {
        int height = getList().getFixedCellHeight() * Math.min(getMAX_VISIBLE_ROWS(), getLm().getSize());
        height += getList().getInsets().top + getList().getInsets().bottom;
        height += getSp().getInsets().top + getSp().getInsets().bottom;

        getWin().setSize(getTextField().getWidth(), height);
        getSp().setSize(getTextField().getWidth(), height); // bottom border fails to draw without this
    }

    @Override
    public void setCompleterMatches(Object[] objectsToMatch) {
        if (isFilterWindowVisible()) {
            setFilterWindowVisible(false);
        }

        super.setCompleterMatches(objectsToMatch);
        getLm().setCompleterMatches(objectsToMatch);
    }

    /**
     * @return the _fwl
     */
    protected FilterWindowListener getFwl() {
        return _fwl;
    }

    /**
     * @param fwl the _fwl to set
     */
    protected void setFwl(FilterWindowListener fwl) {
        this._fwl = fwl;
    }

    /**
     * @return the _win
     */
    protected JWindow getWin() {
        return _win;
    }

    /**
     * @param win the _win to set
     */
    protected void setWin(JWindow win) {
        this._win = win;
    }

    /**
     * @return the _tfkl
     */
    protected TextFieldKeyListener getTfkl() {
        return _tfkl;
    }

    /**
     * @param tfkl the _tfkl to set
     */
    protected void setTfkl(TextFieldKeyListener tfkl) {
        this._tfkl = tfkl;
    }

    /**
     * @return the _lsl
     */
    protected ListSelListener getLsl() {
        return _lsl;
    }

    /**
     * @param lsl the _lsl to set
     */
    protected void setLsl(ListSelListener lsl) {
        this._lsl = lsl;
    }

    /**
     * @return the _lml
     */
    protected ListMouseListener getLml() {
        return _lml;
    }

    /**
     * @param lml the _lml to set
     */
    protected void setLml(ListMouseListener lml) {
        this._lml = lml;
    }

    /**
     * @return the _list
     */
    protected JList getList() {
        return _list;
    }

    /**
     * @param list the _list to set
     */
    protected void setList(JList list) {
        this._list = list;
    }

    /**
     * @return the _sp
     */
    protected JScrollPane getSp() {
        return _sp;
    }

    /**
     * @param sp the _sp to set
     */
    protected void setSp(JScrollPane sp) {
        this._sp = sp;
    }

    /**
     * @return the _lm
     */
    protected FilterListModel getLm() {
        return _lm;
    }

    /**
     * @param lm the _lm to set
     */
    protected void setLm(FilterListModel lm) {
        this._lm = lm;
    }

    /**
     * @return the _isAdjusting
     */
    protected boolean isIsAdjusting() {
        return _isAdjusting;
    }

    /**
     * @param isAdjusting the _isAdjusting to set
     */
    protected void setIsAdjusting(boolean isAdjusting) {
        this._isAdjusting = isAdjusting;
    }

    public class EscapeAction extends AbstractAction {

        public void actionPerformed(ActionEvent e) {
            if (isFilterWindowVisible()) {
                setFilterWindowVisible(false);
            }
        }
    }

    public class FilterWindowListener extends MouseAdapter
            implements AncestorListener, FocusListener, WindowFocusListener {

        public void ancestorMoved(AncestorEvent event) {
            setFilterWindowVisible(false);
        }

        public void ancestorAdded(AncestorEvent event) {
            setFilterWindowVisible(false);
        }

        public void ancestorRemoved(AncestorEvent event) {
            setFilterWindowVisible(false);
        }

        public void focusLost(FocusEvent e) {
            if (e.getOppositeComponent() != getWin()) {
                setFilterWindowVisible(false);
            }
        }

        public void focusGained(FocusEvent e) {
        }

        public void windowLostFocus(WindowEvent e) {
            Window w = e.getOppositeWindow();

            if (w.getFocusOwner() != getTextField()) {
                setFilterWindowVisible(false);
            }
        }

        public void windowGainedFocus(WindowEvent e) {
        }

        @Override
        public void mousePressed(MouseEvent e) {
            setFilterWindowVisible(false);
        }
    }

    public class TextFieldKeyListener extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            if (!((e.getKeyCode() == KeyEvent.VK_DOWN)
                    || (e.getKeyCode() == KeyEvent.VK_UP)
                    || ((e.getKeyCode() == KeyEvent.VK_PAGE_DOWN) && (isFilterWindowVisible()))
                    || ((e.getKeyCode() == KeyEvent.VK_PAGE_UP) && (isFilterWindowVisible()))
                    || (e.getKeyCode() == KeyEvent.VK_ENTER))) {
                return;
            }

            if ((e.getKeyCode() == KeyEvent.VK_DOWN) && !isFilterWindowVisible()) {
                setPreText(getTextField().getText());
                getLm().setFilter(getPreText());

                if (getLm().getSize() > 0) {
                    setFilterWindowVisible(true);
                } else {
                    return;
                }
            }

            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                if (isFilterWindowVisible()) {
                    setFilterWindowVisible(false);
                }

                getTextField().setCaretPosition(getTextField().getText().length());
                return;
            }

            int index = -1;

            if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                index = Math.min(getList().getSelectedIndex() + 1, getList().getModel().getSize() - 1);
            } else if (e.getKeyCode() == KeyEvent.VK_UP) {
                index = Math.max(getList().getSelectedIndex() - 1, 0);
            } else if (e.getKeyCode() == KeyEvent.VK_PAGE_UP) {
                index = Math.max(getList().getSelectedIndex() - getMAX_VISIBLE_ROWS(), 0);
            } else if (e.getKeyCode() == KeyEvent.VK_PAGE_DOWN) {
                index = Math.min(getList().getSelectedIndex() + getMAX_VISIBLE_ROWS(), getList().getModel().getSize() - 1);
            }

            if (index == -1) {
                return;
            }

            getList().setSelectedIndex(index);
            getList().scrollRectToVisible(getList().getCellBounds(index, index));
        }
    }

    public class ListSelListener implements ListSelectionListener {

        public void valueChanged(ListSelectionEvent e) {
            setIsAdjusting(true);
            getTextField().setText(getList().getSelectedValue().toString());
            setIsAdjusting(false);
            getTextField().select(getPreText().length(), getTextField().getText().length());
        }
    }

    public class ListMouseListener extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) {
                setFilterWindowVisible(false);
            }
        }
    }
    /**
     *
     */
    private FilterWindowListener _fwl;
    private JWindow _win;
    private TextFieldKeyListener _tfkl;
    private ListSelListener _lsl;
    private ListMouseListener _lml;
    private JList _list;
    private JScrollPane _sp;
    private FilterListModel _lm;
    private boolean _isAdjusting = false;
    /**
     *
     */
    private static int MAX_VISIBLE_ROWS = 8;
}
