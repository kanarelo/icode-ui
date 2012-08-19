package com.icode.view.binding;

import java.lang.reflect.Method;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

class BoundItem<E> implements ChangeListener {

	private Method getter, setter;
	private ValueEditor<?> component;
	private Object originalValue;
	private Object currentValue;
	private boolean changed;
	private boolean invalid;
	private Binding<E> binding;
	private static final Object multipleValue = ":multiple";

	BoundItem(String name, ValueEditor<?> component, Binding<E> binding,
			Class<E> clazz) {
		this.binding = binding;
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

	void add(E... values) {
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
		setInvalid(false);
		component.setContent((currentValue != multipleValue) ? currentValue
				: null);
		setInvalid(!component.getValidity());
	}

	public void stateChanged(ChangeEvent e) {
		currentValue = component.getContent();
		boolean different = !equals(originalValue, currentValue);
		boolean wrong = !component.getValidity();
		if (changed != different) {
			changed = different;
			binding.setChanged(changed ? 1 : -1);
		}
		if (isInvalid() != wrong) {
			setInvalid(wrong);
			binding.setInvalid(isInvalid() ? 1 : -1);
		}
	}

	void reset() {
		currentValue = originalValue;
		changed = false;
		setInvalid(false);
		component.setContent((currentValue != multipleValue) ? currentValue
				: null);
		setInvalid(!component.getValidity());
	}

	void submit(E... values) {
		if (changed && !isInvalid() && (values != null)) {
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

	public boolean isInvalid() {
		return invalid;
	}

	public void setInvalid(boolean invalid) {
		this.invalid = invalid;
	}
}
