/* Copyright 2008-2009 Robert Bajzat. All rights reserved. Use is subject to license terms. */
package com.icode.view.container;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;

import com.icode.resources.ResourceUtils;
import com.icode.view.border.LineBorder;
import com.icode.view.component.fields.StringField;

/**
 * An internal dialog shown in a bubble bellow/above its invoker component
 */
public class PopupDialog extends JPanel {

	public static final int OK_OPTION = 0;
	public static final int CANCEL_OPTION = 1;
	public static final int CLOSED_OPTION = 2;
	public static final int DEFAULT_OPTION = 3;
	public static final int NO_OPTION = 4;
	public static final int YES_NO_CANCEL_OPTION = 5;
	public static final int YES_OPTION = 6;
	private Bubble bubble;
	private JButton okButton, cancelButton;
	private MessageType messageType;
	private int myOption = -1;
	private boolean translucent = true;
	private StringField txtSearch;

	/**
	 * Creates an empty dialog component
	 */
	private PopupDialog() {
		setLayout(new BorderLayout());
	}

	public int getValue() {
		return (myOption >= 0 && myOption <= 6) ? myOption : -1;
	}

	/**
	 * Creates a dialog including an icon, a title, a message, and two buttons
	 * 
	 * @param messageType
	 *            defines the style, thus the icon of the dialog. Possible
	 *            values are JOptionPane's ERROR_MESSAGE, INFORMATION_MESSAGE,
	 *            WARNING_MESSAGE, QUESTION_MESSAGE, and PLAIN_MESSAGE
	 * @param title
	 *            the title text
	 * @param message
	 *            the description text
	 * @param ok
	 *            the label of the default button
	 * @param cancel
	 *            the label of the escape button
	 */
	public PopupDialog(MessageType messageType, String title, String message,
			String ok, String cancel, boolean translucent) {
		this();
		this.messageType = messageType;
		this.translucent = translucent;
		add(createHeader(messageType, title, message), BorderLayout.NORTH);
		add(createFooter(ok, cancel), BorderLayout.SOUTH);
	}

	public PopupDialog(MessageType messageType, String title, String message,
			String ok, boolean translucent) {
		this();
		this.messageType = messageType;
		this.translucent = translucent;
		add(createHeader(messageType, title, message), BorderLayout.NORTH);
		add(createFooter(ok, null), BorderLayout.SOUTH);
	}

	public PopupDialog(MessageType messageType, String title, String message,
			String ok, String cancel) {
		this(messageType, title, message, ok, cancel, true);
	}

	public PopupDialog(MessageType messageType, String title, String message,
			String ok) {
		this(messageType, title, message, ok, true);
	}

	/**
	 * 
	 * @param title
	 * @param message
	 * @param sw
	 * @param items
	 */
	public PopupDialog(String title, String message) {
		this();
		add(createFooter("Search", "Cancel"), BorderLayout.SOUTH);
		add(createSearchDialogHeader(title, message), BorderLayout.NORTH);
	}

	public PopupDialog(String title, JComponent formContainer,
			boolean lowerButtons) {
		this(title, formContainer, lowerButtons, false);
	}

	public PopupDialog(String title, JComponent formContainer,
			boolean lowerButtons, boolean translucent) {
		this();
		this.translucent = translucent;
		add(createHeader(null, title, null), BorderLayout.NORTH);
		add(formContainer, BorderLayout.CENTER);

		if (lowerButtons) {
			add(createFooter("Ok", "Cancel"), BorderLayout.SOUTH);
		}
	}

	private JPanel createSearchDialogHeader(String title, String message) {
		JPanel header = new JPanel(new BorderLayout(8, 0));
		header.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));

		header.add(new JLabel(ResourceUtils.getIcon("/icons/48/bSearch.png")),
				BorderLayout.WEST);
		JPanel texts = new JPanel(new BorderLayout(0, 8));
		JLabel titleLabel = new JLabel(title);
		titleLabel.setForeground(Color.WHITE);
		titleLabel.setFont(UIManager.getFont("Label.boldfont"));
		texts.add(titleLabel, BorderLayout.NORTH);
		JLabel messageLabel = new JLabel("<html><div style=\"width: " + 220
				+ "px;\">" + message + "</div></html>");
		messageLabel.setForeground(new Color(205, 205, 205));
		texts.add(messageLabel, BorderLayout.CENTER);

		JPanel widgets = new JPanel();

		txtSearch = new StringField();
		txtSearch.requestFocus();
		txtSearch.setColumns(25);
		txtSearch.setWatermark("Search");
		widgets.add(txtSearch);

		texts.add(widgets, BorderLayout.SOUTH);
		header.add(texts, BorderLayout.CENTER);

		this.okButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if (txtSearch.getText() != null) {
					if (!txtSearch.getText().isEmpty()) {
					}
				}
			}
		});

		return header;
	}

	public PopupDialog(JPanel Center, MessageType messageType, String title,
			String message, String ok, String cancel) {
		this();
		this.messageType = messageType;
		add(createHeader(messageType, title, message), BorderLayout.NORTH);
		add(Center, BorderLayout.CENTER);
		add(createFooter(ok, cancel), BorderLayout.SOUTH);
	}

	/**
	 * Creates a dialog including the exception's class name as title, message
	 * as description. The icon type depends on exception type. A text-area
	 * component includes the stack trace
	 * 
	 * @param exception
	 *            an exception the show
	 */
	public PopupDialog(Throwable exception) {
		this();

		Throwable root = exception;
		while (root.getCause() != null) {
			root = root.getCause();
		}

		this.messageType = (exception instanceof Error) ? MessageType.ERROR
				: MessageType.WARNING;
		add(createHeader(messageType, root.getClass().getName(),
				root.getLocalizedMessage()), BorderLayout.NORTH);

		StringBuilder stack = new StringBuilder(7);
		for (Throwable e = exception; e != null; e = e.getCause()) {
			if (stack.length() > 0) {
				stack.append("\nCaused by: ");
				stack.append(e.getClass().getName());
				stack.append(": ");
				stack.append(e.getLocalizedMessage());
				stack.append('\n');
			}
			for (StackTraceElement elemen : e.getStackTrace()) {
				stack.append("  at ");
				stack.append(elemen.toString());
				stack.append('\n');
			}
		}

		JPanel content = new JPanel(new BorderLayout());
		content.setBorder(new LineBorder(Color.LIGHT_GRAY, 1, 0, 0, 0, 8, 0, 8,
				0));
		JTextArea textArea = new JTextArea(stack.toString(), 12, 42);
		textArea.setEditable(false);
		content.add(new JScrollPane(textArea), BorderLayout.CENTER);
		add(content, BorderLayout.CENTER);

		add(createFooter(null, "Close"), BorderLayout.SOUTH);
	}

	/**
	 * Pops up the dialog and wait for its closing
	 * 
	 * @param invoker
	 *            the parent of this dialog box
	 * @return true if the default button was pressed
	 */
	public boolean showModal(JComponent invoker) {
		if (bubble != null) {
			throw new IllegalStateException();
		}
		bubble = new Bubble(invoker, this, okButton, cancelButton, translucent);
		return bubble.waitForClose();
	}

	/**
	 * Pops up the dialog and returns immediately
	 * 
	 * @param invoker
	 *            the parent of this dialog box
	 */
	public void showMessage(JComponent invoker) {
		if (bubble != null) {
			throw new IllegalStateException();
		}
		bubble = new Bubble(invoker, this, okButton, cancelButton, translucent);
	}

	private JPanel createHeader(MessageType messageType, String title,
			String message) {
		JPanel header = new JPanel(new BorderLayout(8, 0));
		header.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

		if (messageType != null) {
			if (messageType != MessageType.PLAIN_MESSAGE) {
				header.add(
						new JLabel(
								UIManager
										.getIcon("OptionPane."
												+ ((messageType == MessageType.ERROR) ? "error"
														: (messageType == MessageType.QUESTION) ? "question"
																: (messageType == MessageType.WARNING) ? "warning"
																		: "information")
												+ "Icon")), BorderLayout.WEST);
			}
		}
		JPanel texts = new JPanel(new BorderLayout(0, 8));
		JLabel titleLabel = new JLabel(title);
		if (translucent) {
			titleLabel.setForeground(Color.WHITE);
		}
		titleLabel.setFont(UIManager.getFont("Label.headerfont"));
		texts.add(titleLabel, BorderLayout.NORTH);

		if (message != null) {
			JLabel messageLabel = new JLabel("<html><div style=\"width: " + 240
					+ "px;\">" + message + "</div></html>");
			if (translucent) {
				messageLabel.setForeground(new Color(205, 205, 205));
			}
			texts.add(messageLabel, BorderLayout.CENTER);
		}
		header.add(texts, BorderLayout.CENTER);
		return header;
	}

	private JPanel createFooter(String ok, String cancel) {
		JPanel footer = new JPanel(new BorderLayout());
		footer.setBorder(new LineBorder(Color.LIGHT_GRAY, 1, 0, 0, 0, 8, 0, 0,
				0));
		JPanel buttons = new JPanel(new GridLayout(1, 0, 4, 0));

		if (ok != null) {
			okButton = new JButton(ok);
			okButton.putClientProperty("JButton.buttonType", "textured");
			buttons.add(okButton);
		}

		if (cancel != null) {
			cancelButton = new JButton(cancel);
			cancelButton.putClientProperty("JButton.buttonType", "textured");
			buttons.add(cancelButton);
		}

		footer.add(buttons, BorderLayout.EAST);
		return footer;
	}

	/**
	 * Removes the dialog
	 */
	public void close() {
		if (bubble != null) {
			bubble.dismiss();
			bubble = null;
		}
	}

	public String getSearchTerm() {
		return txtSearch.getText();
	}
}
