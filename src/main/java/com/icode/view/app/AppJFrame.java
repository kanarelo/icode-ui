/* Copyright 2008-2009 Robert Bajzat. All rights reserved. Use is subject to license terms. */
package com.icode.view.app;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

import com.icode.validation.ValidationEngine;
import com.icode.view.iApplicationMenuControls;

/**
 * Advanced frame supporting Mac application features
 */
public abstract class AppJFrame extends JFrame implements
		iApplicationMenuControls {

	private static final Logger logger = Logger.getLogger(AppJFrame.class
			.getName());

	static {
		try {
			Class.forName(ValidationEngine.class.getName());
			logger.info("Loaded the ValidationEngine");
		} catch (Exception ex) {
			logger.log(Level.SEVERE, "Did not load the ValidationEngine");
		}
	}

	/**
	 * Creates a new frame
	 */
	protected AppJFrame() {
		this.addWindowListener(new Listener());
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
	}

	/**
	 * Sets the frame icon
	 * 
	 * @param path
	 *            the paths is relative to this class' package
	 */
	protected void setIconImage(String path) {
		setIconImage(new ImageIcon(getClass().getClassLoader().getResource(
				getClass().getPackage().getName().replace('.', '/') + '/'
						+ path)).getImage());
	}

	/**
	 * Shows the frame on the center of the screen
	 * 
	 * @param width
	 *            the preferred width of the frame content
	 * @param height
	 *            the preferred width of the frame content
	 */
	protected void showFrame(int width, int height) {
		pack();
		Insets is = getInsets();
		width += is.left + is.right;
		height += is.top + is.bottom;
		Rectangle max = GraphicsEnvironment.getLocalGraphicsEnvironment()
				.getMaximumWindowBounds();
		width = Math.min(max.width, width);
		height = Math.min(max.height, height);
		setBounds(max.x + (max.width - width) / 2, max.y
				+ (max.height - height) / 2, width, height);
		setVisible(true);
	}

	protected void showFrame() {
		showFrame(800, 600);
		setExtendedState(Frame.MAXIMIZED_BOTH);
	}

	private class Listener implements WindowListener {

		public void windowOpened(WindowEvent e) {
			// init();
		}

		public void windowClosing(WindowEvent e) {
			String[] ObjButtons = new String[] { "Yes", "No" };
			int PromptResult = JOptionPane.showOptionDialog(null,
					"Are you sure you want to close?", "System Message",
					JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
					null, ObjButtons, ObjButtons[1]);
			if (PromptResult == 0) {
				System.exit(0);
			}
		}

		public void windowActivated(WindowEvent e) {
		}

		public void windowClosed(WindowEvent e) {
		}

		public void windowDeactivated(WindowEvent e) {
		}

		public void windowDeiconified(WindowEvent e) {
		}

		public void windowIconified(WindowEvent e) {
		}
	}

	private static final Dimension screenDimensions = java.awt.Toolkit
			.getDefaultToolkit().getScreenSize();

	static {
		System.setProperty("apple.laf.useScreenMenuBar", "true");
		System.setProperty("awt.useSystemAAFontSettings", "on");
		int fs = 12;
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception exc) {
		}
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception exc) {}

		Font font = UIManager.getFont("Label.font");
		String os = System.getProperty("os.name");
		if ("Mac OS X".equals(os)) {
			font = new Font("Lucida Grande", Font.PLAIN, fs);
		}else if ("Windows Vista".equals(os)){
			font = new Font("Segoe UI", Font.PLAIN, fs);
		} else if ("Windows XP".equals(os)) {
			font = new Font("Tahoma", Font.PLAIN, fs);
		} else if ("Windows 7".equals(os) || "Windows Vista".equals(os)) {
			font = new Font("Maiandra GD", Font.PLAIN, fs);
		} else if ("Gnome".equals(os)) {
			font = new Font("DejaVu", Font.PLAIN, fs);
		}

		String[] keys = { "Button", "CheckBox", "CheckBoxMenuItem",
				"ColorChooser", "ComboBox", "EditorPane", "FormattedTextField",
				"Label", "List", "Menu", "MenuBar", "MenuItem", "OptionPane",
				"Panel", "PasswordField", "PopupMenu", "ProgressBar",
				"RadioButton", "RadioButtonMenuItem", "ScrollPane", "Spinner",
				"TabbedPane", "Table", "TableHeader", "TextArea", "TextField",
				"TextPane", "TitledBorder", "ToggleButton", "ToolBar",
				"ToolTip", "Tree", "Viewport" };
		for (String key : keys) {
			UIManager.put(key + ".font", font);
		}

		UIManager.put("Button.font", font.deriveFont(15));
		UIManager.put("Label.boldfont", new Font(font.getName(), Font.BOLD,
				font.getSize()));
		UIManager.put("Label.smallfont", new Font(font.getName(), Font.PLAIN,
				font.getSize() - 2));
	}
}
