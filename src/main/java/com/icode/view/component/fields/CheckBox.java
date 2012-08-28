package com.icode.view.component.fields;

import javax.swing.JCheckBox;

import com.icode.view.binding.ValueEditor;

@SuppressWarnings("serial")
public class CheckBox extends JCheckBox implements ValueEditor<Object>{

	public void setContent(Object value) {
		this.setSelected((Boolean) value);
	}
	
	public Object getContent() {
		return this.isSelected();
	}

	public boolean getValidity() {
		return true;
	}

}
