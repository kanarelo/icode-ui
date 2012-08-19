/* Copyright 2008-2009 Robert Bajzat. All rights reserved. Use is subject to license terms. */
package com.icode.view.container;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPopupMenu;
import javax.swing.JToolBar;
import javax.swing.UIManager;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

import com.icode.resources.ResourceUtils;
import com.icode.util.Commons;
import com.icode.view.app.TipManager;
import com.icode.view.border.LineBorder;
import com.icode.view.component.PressButton;

/**
 * Wrapper component for tool-bar
 */
public class MenuBar extends JToolBar {

	/**
	 * Creates a non-floatable toolbar with specific border and background, and
	 * box layout
	 */
	public MenuBar() {
		setFloatable(false);
		setBorder(new LineBorder(Color.lightGray, 0, 0, 1, 0, 0, 8, 0, 8));
		setBackground(new Color(0xdddddd));
		setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
	}

	/**
	 * Adds a new label with bold font
	 * 
	 * @param text
	 *            the text for the label
	 * @return the created component
	 */
	public JLabel addTitleLabel(String text) {
		JLabel title = new JLabel(text);
		add(title);
		return title;
	}

	/**
	 * Adda a strut with the given width
	 * 
	 * @param width
	 *            width of the strut
	 */
	public void addStrut(int width) {
		add(Box.createHorizontalStrut(width));
	}

	/**
	 * Adds a strut with the given width and a glue
	 * 
	 * @param width
	 *            width of the strut, can be zero
	 */
	public void addGlue(int width) {
		if (width > 0) {
			add(Box.createHorizontalStrut(width));
		}
		add(Box.createHorizontalGlue());
	}

	/**
	 * Overwrites the method to fix the minimum heigh of the component
	 */
	@Override
	public Dimension getPreferredSize() {
		Dimension d = super.getPreferredSize();
		d.height = Math.max(d.height, 27);
		return d;
	}

	/**
	 * 
	 * @param title
	 * @param popup
	 * @return
	 */
	public PressButton addMenu(String title, JPopupMenu popup) {
		PressButton button = new StringToolButton(title, popup);
		addStrut(4);
		add(button);
		return button;
	}

	private static class StringToolButton extends PressButton {

		private JPopupMenu popup = null;
		private GeneralPath gp = new GeneralPath();
		private String label;
		private boolean popupSelected = false;
		private static FontMetrics fm;
		private static List<StringToolButton> stringButtons = new ArrayList<StringToolButton>(
				5);
		private GradientPaint gpOver;
		private float w;
		private float h;

		StringToolButton(String label, JPopupMenu popup) {
			this.label = label;
			setFocusable(false);
			this.popup = popup;
			stringButtons.add(this);

			popup.addPopupMenuListener(new PopupMenuListener() {

				public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
				}

				public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
					StringToolButton.this.repaint();
				}

				public void popupMenuCanceled(PopupMenuEvent e) {
				}
			});
		}

		private StringToolButton(String title) {
			this(title, null);
		}

		@Override
		protected void paint(Graphics2D g, boolean inside, boolean pressed,
				boolean focused) {
			w = getWidth();
			h = getHeight();
			gpOver = new GradientPaint(0, 0, new Color(0xFFF5CC), 0, h * .75F,
					new Color(0xFFDB75));
			if (isEnabled()) {
				if (pressed || inside) {
					if (inside && !pressed) {
						g.setPaint(gpOver);
						g.fillRect(0, 0, getWidth() - 1, getHeight());
						g.setColor(new Color(241, 209, 77));
						g.drawRect(0, 0, getWidth() - 2, getHeight() - 1);
					}
					g.setColor(Color.BLACK);
				}
				if (popup != null) {
					if (popup.isVisible()) {
						g.setColor(new Color(167, 171, 176));
						g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
						g.setPaint(new GradientPaint(0, 0, new Color(232, 233,
								241), 0, h * .75F, new Color(177, 176, 198)));
						g.fillRect(1, 1, getWidth() - 2, getHeight() - 2);
						g.setColor(Color.BLACK);
						// g.setFont(getFont().deriveFont(Font.BOLD,
						// getFont().getSize() * .86F));
						if (!popupSelected) {
							popupSelected = true;
						}
					} else {
						if (popupSelected) {
							popupSelected = false;
						}
					}
				}
				g.drawString(label, (getPreferredSize().width / 7.5F),
						(getPreferredSize().height / 1.5F));
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			setPressed(false);
			Commons.repaintAncestry(this, 1);
			if (isFocusable()) {
				requestFocus();
			}
		}

		@Override
		public void mousePressed(MouseEvent e) {
			setPressed(true);
			if (isInside()) {
				for (ActionListener listener : listenerList
						.getListeners(ActionListener.class)) {
					listener.actionPerformed(new ActionEvent(this,
							ActionEvent.ACTION_PERFORMED, null));
				}
				actionPerformed();
			}
			Commons.repaintAncestry(this, 1);
		}

		@Override
		public Dimension getPreferredSize() {
			if (fm == null) {
				fm = this.getFontMetrics(this.getFont());
			}
			return new Dimension(fm.stringWidth(label) + 15, this.getParent()
					.getHeight());
		}

		@Override
		protected void actionPerformed() {
			if (popup != null) {
				popup.show(this, 0, getHeight());
			}
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			super.mouseEntered(e);
			for (StringToolButton stb : stringButtons) {
				if (stb.popupSelected) {
					if (popup != null) {
						popup.show(this, 0, getHeight());
					}
				}
			}
		}
	}

	/**
	 * Adds a new custom painted button
	 * 
	 * @param clazz
	 *            the icon path is relative to its package
	 * @param path
	 *            the icon's path
	 * @param tip
	 *            a tooltip text
	 * @return the created button component
	 */
	public PressButton addButton(Class<?> clazz, String path, String tip) {
		return addButton(clazz, path, tip, null);
	}

	/**
	 * Adds a new custom painted button
	 * 
	 * @param path
	 *            the icon's path
	 * @param tip
	 *            a tooltip text
	 * @return the created button component
	 */
	public PressButton addButton(String path, String tip) {
		return addButton(null, path, tip, null);
	}

	/**
	 * Adds a new custom painted button
	 * 
	 * @param path
	 *            the icon's path
	 * @param tip
	 *            a tooltip text
	 * @return the created button component
	 */
	public PressButton addButton(Icon icon, String tip) {
		return addButton(icon, tip, null);
	}

	/**
	 * Adds a new custom painted button
	 * 
	 * @param path
	 *            the icon's path
	 * @param tip
	 *            a tooltip text
	 * @param popup
	 * @return the created button component
	 */
	public PressButton addButton(String path, String tip, JPopupMenu popup) {
		PressButton button = new ToolButton(path, tip, popup);
		addStrut(4);
		add(button);
		return button;
	}

	public JLabel addIcon(String path) {
		JLabel button = new JLabel();
		button.setIcon(ResourceUtils.getIcon(path));
		addStrut(4);
		add(button);
		return button;
	}

	/**
	 * Adds a new custom painted button to show a popup menu
	 * 
	 * @param clazz
	 *            the icon path is relative to its package
	 * @param path
	 *            the icon's path
	 * @param tip
	 *            a tooltip text
	 * @param popup
	 *            a popup menu component
	 * @return the created button component
	 */
	public PressButton addButton(Class<?> clazz, String path, String tip,
			JPopupMenu popup) {
		PressButton button = new ToolButton(clazz, path, tip, popup);
		addStrut(3);
		add(button);
		return button;
	}
	
	public PressButton addButton(Icon icon, String tip,
			JPopupMenu popup) {
		PressButton button = new ToolButton(icon, tip);
		addStrut(3);
		add(button);
		return button;
	}

	public static class ToolButton extends PressButton {

		private Icon icon, disabledIcon;

		public void setIcon(Icon icon) {
			this.icon = icon;
		}

		public void setIcon(String path) {
			this.icon = ResourceUtils.getIcon(path);
		}

		public Icon getIcon() {
			return icon;
		}

		private JPopupMenu popup;
		private int w;
		private int h;
		private GradientPaint gpOver;
		private GradientPaint gpPressed;

		public ToolButton(String path, String tip, JPopupMenu popup) {

			icon = ResourceUtils.getIcon(path);
			setFocusable(false);
			setToolTipText(tip);
			TipManager.register(this);
			this.popup = popup;
		}

		public ToolButton(Class<?> clazz, String path, String tip,
				JPopupMenu popup) {
			this(path, tip, popup);
			if (clazz != null) {
				icon = ResourceUtils.getIcon(clazz, path);
			}
		}

		public ToolButton(Icon icon, String tip) {
			this.icon = icon;
			setToolTipText(tip);
		}

		public ToolButton(Class<?> clazz, String path) {
			if (clazz != null) {
				icon = ResourceUtils.getIcon(clazz, path);
			}
		}

		public ToolButton(String path) {
			this(TitlePanel.class, path);
		}

		@Override
		protected void paint(Graphics2D g, boolean inside, boolean pressed,
				boolean focused) {
			w = getWidth();
			h = getHeight();
			gpOver = new GradientPaint(w * .5f, 0, new Color(255, 224, 102),
					w * .5f, h * 2, new Color(252, 249, 223), true);
			gpPressed = new GradientPaint(0, 1, Color.white, 0, h, new Color(
					250, 233, 166));
			if (isEnabled()) {
				if (pressed && inside) {
					g.setColor(new Color(255, 228, 138));
					g.fillRoundRect(0, 0, getWidth(), getHeight(), 6, 6);
					g.setColor(new Color(194, 118, 43));
					g.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 6, 6);
					g.setColor(new Color(249, 209, 114));
					g.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 6, 6);
				} else if (inside) {
					g.setPaint(gpOver);
					g.fillRoundRect(0, 0, getWidth(), getHeight(), 6, 6);
					g.setColor(new Color(237, 198, 85));
					g.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 6, 6);
					g.setColor(new Color(255, 247, 206));
					g.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 6, 6);
				}

			} else {
				if (disabledIcon == null) {
					disabledIcon = UIManager.getLookAndFeel().getDisabledIcon(
							this, icon);
				}
				disabledIcon.paintIcon(this, g, 3, 3);
			}
			if (popup != null) {
				g.setColor(isEnabled() ? Color.darkGray : Color.gray);
				int x = getWidth() - 8, y = getHeight() - 4;
				g.fillPolygon(new int[] { x, x + 8, x + 4 }, new int[] { y, y,
						y + 4 }, 3);
			}
			g.setColor(isEnabled() ? inside ? Color.darkGray : Color.gray
					: Color.lightGray);
			icon.paintIcon(this, g, (int) ((this.getWidth() / 2.0) - (this.icon
					.getIconWidth() / 2.0)),
					(int) ((this.getHeight() / 2.0) - (this.icon
							.getIconHeight() / 2.0)));

		}

		@Override
		public Dimension getPreferredSize() {
			return new Dimension(Math.max(22, icon.getIconWidth() + 6),
					Math.max(22, icon.getIconHeight() + 6));
		}

		@Override
		protected void actionPerformed() {
			if (popup != null) {
				if (!popup.isShowing()) {
					popup.show(this, 0, getHeight());
				}
			}
		}
	}
}
