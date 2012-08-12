package com.icode.view.component.fields;

import java.awt.Graphics;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.icode.view.binding.ValueEditor;

public class RichTextArea extends JScrollPane
        implements ValueEditor<Object>, DocumentListener {

    private JTextArea textArea = new JTextArea(4, 12);
    private boolean required;

    public RichTextArea() {
        setViewportView(this.textArea);
        this.textArea.getDocument().addDocumentListener(this);
    }

    public void setRequired(boolean paramBoolean) {
        this.required = paramBoolean;
    }

    public void setContent(Object paramObject) {
        this.textArea.setText((paramObject != null) ? paramObject.toString() : null);
    }

    public Object getContent() {
        return this.textArea.getText();
    }

    public boolean getValidity() {
        return this.textArea.getText().length() > 0;
    }

    public void addChangeListener(ChangeListener paramChangeListener) {
        this.listenerList.add(ChangeListener.class, paramChangeListener);
    }

    public void removeChangeListener(ChangeListener paramChangeListener) {
        this.listenerList.remove(ChangeListener.class, paramChangeListener);
    }

    private void fireChangeListener() {
        for (ChangeListener localChangeListener : this.listenerList.getListeners(ChangeListener.class)) {
            localChangeListener.stateChanged(new ChangeEvent(this));
        }
    }

    public void changedUpdate(DocumentEvent paramDocumentEvent) {
        fireChangeListener();
    }

    public void insertUpdate(DocumentEvent paramDocumentEvent) {
        fireChangeListener();
    }

    public void removeUpdate(DocumentEvent paramDocumentEvent) {
        fireChangeListener();
    }

    @Override
	public void paint(Graphics paramGraphics) {
        super.paint(paramGraphics);
        if (getValidity()) {
            return;
        }
    }
}

/* Location:           C:\Users\Nes\Desktop\Thinet\flient.jar
 * Qualified Name:     thinlet.ui.RichTextArea
 * JD-Core Version:    0.5.4
 */
