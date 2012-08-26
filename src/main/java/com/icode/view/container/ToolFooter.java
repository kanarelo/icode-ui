/* Copyright 2008-2009 Robert Bajzat. All rights reserved. Use is subject to license terms. */
package com.icode.view.container;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.UIManager;

import com.icode.resources.ResourceUtils;
import com.icode.view.component.PressButton;

/**
 * Custom painted panel with buttons
 */
public class ToolFooter extends JPanel {

	/**
	 * Creates the panel with box layout
	 */
	public ToolFooter() {
		setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
	}

	/**
	 * Adds new custom painted button
	 * 
	 * @param icon
	 *            the icon
	 * @param tootTip
	 *            the tooltip text for the button
	 * @param listener
	 *            the action listener listening for the button press
	 * @return the created component
	 */
	public PressButton addButton(Icon icon, String tootTip,
			ActionListener listener) {
		ToolButton toolButton = new ToolButton(icon, tootTip, listener);
		add(toolButton);
		return toolButton;
	}

	public PressButton addButton(String path, String tip) {
		ToolButton toolButton = new ToolButton(ResourceUtils.getIcon(path), tip);
		add(toolButton);
		return toolButton;
	}

	/**
	 * Adds new custom painted button with plus icon
	 * 
	 * @param tootTip
	 *            the tooltip text for the button
	 * @param listener
	 *            the action listener listening for the button press
	 * @return the created component
	 */
	public JComponent addPlusButton(String tootTip, ActionListener listener) {
		return addButton(new PlusIcon(), tootTip, listener);
	}

	public JComponent addStarButton(String tootTip, ActionListener listener) {
		return addButton(new StarIcon(), tootTip, listener);
	}

	/**
	 * Adds new custom painted button with minus icon
	 * 
	 * @param tootTip
	 *            the tooltip text for the button
	 * @param listener
	 *            the action listener listening for the button press
	 * @return the created component
	 */
	public PressButton addMinusButton(String tootTip, ActionListener listener) {
		return addButton(new MinusIcon(), tootTip, listener);
	}

	public PressButton addExpandButton(String tootTip, ActionListener listener) {
		return addButton(new ExpandIcon(), tootTip, listener);
	}

	public PressButton addMinimiseButton(String tootTip, ActionListener listener) {
		return addButton(new MinimiseIcon(), tootTip, listener);
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
	 * Overwrites the method to fix the height of the component
	 */
	@Override
	public Dimension getPreferredSize() {
		Insets is = getInsets();
		return new Dimension(0 + is.left + is.right, 20 + is.top + is.bottom);
	}

	/**
	 * Overwrites the method to paint custom background
	 */
	@Override
	protected void paintComponent(Graphics g) {
		Insets is = getInsets();
		int x = is.left, y = is.top, w = getWidth() - is.left - is.right, h = getHeight()
				- is.top - is.bottom, h2 = h / 2;

		g.setColor(new Color(0xeeeeee));
		((Graphics2D) g).setPaint(new GradientPaint(w * .5F, 0, new Color(230,
				234, 238), w * .5F, h, new Color(191, 195, 200)));
		g.fillRect(x, y, w, h);
	}

	private static class ToolButton extends PressButton {

		private Icon icon;
		private GradientPaint gpOver;
		private Icon disabledIcon;
		private static int w;
		private static int h;

		private ToolButton(Icon icon, String tootTip, ActionListener listener) {
			this.icon = icon;
			setToolTipText(tootTip);
			addActionListener(listener);
		}

		private ToolButton(Icon icon, String tootTip) {
			this.icon = icon;
			setToolTipText(tootTip);
		}

		@Override
		public Dimension getPreferredSize() {
			return new Dimension(21, 20);
		}

		@Override
		protected void paint(Graphics2D g, boolean inside, boolean pressed,
				boolean focused) {
			w = getWidth();
			h = getHeight();
			gpOver = new GradientPaint(w * .75F, 0, new Color(255, 224, 102),
					w * .5F, h * 3, new Color(252, 249, 223), true);
			g.setColor(new Color(0xaaaaaa));
			g.fillRect(getWidth() - 1, (int) (getHeight() * .25F), 1,
					(int) (getHeight() * .5F));
			if (isEnabled()) {
				if (pressed && inside) {
					g.setColor((pressed && inside) ? new Color(255, 228, 138)
							: new Color(250, 233, 166));
					g.fillRoundRect(0, 0, getWidth() - 1, getHeight(), 6, 6);
					g.setColor((pressed && inside) ? new Color(194, 118, 43)
							: new Color(237, 198, 85));
					g.drawRoundRect(0, 0, getWidth() - 2, getHeight() - 1, 6, 6);
					g.setColor(new Color(249, 209, 114));
					g.drawRoundRect(1, 1, getWidth() - 4, getHeight() - 3, 6, 6);
				} else if (inside) {
					g.setPaint(gpOver);
					g.fillRoundRect(0, 0, getWidth() - 1, getHeight(), 6, 6);
					g.setColor(new Color(237, 198, 85));
					g.drawRoundRect(0, 0, getWidth() - 2, getHeight() - 1, 6, 6);
					g.setColor(new Color(255, 247, 206));
					g.drawRoundRect(1, 1, getWidth() - 4, getHeight() - 3, 6, 6);
				}
				g.setColor(isEnabled() ? inside ? Color.darkGray : Color.gray
						: Color.lightGray);
				icon.paintIcon(this, g, (getWidth() - 1 - icon.getIconWidth()) / 2,
						(getHeight() - icon.getIconHeight()) / 2);
			}else{
				if (disabledIcon == null) {
                    disabledIcon = UIManager.getLookAndFeel().getDisabledIcon(this, icon);
                }
                disabledIcon.paintIcon(this, g, 2, 2);
			}
		}

		@Override
		protected void actionPerformed() {
		}
	}

	private static class PlusIcon implements Icon {

		int i;
		int j;
		int diameter;
		int symbolWidth;
		int x2;
		int y2;

		PlusIcon() {
			i = getIconWidth();
			j = getIconHeight();
			diameter = 8;
			symbolWidth = 6;
			x2 = (int) (i + ((diameter / 2.0) - (symbolWidth / 2.0))) / 2;
			y2 = (int) (i + ((diameter / 2.0) - (symbolWidth / 2.0))) / 2;
		}

		public int getIconWidth() {
			return 8;
		}

		public int getIconHeight() {
			return 8;
		}

		public void paintIcon(Component c, Graphics g, int x, int y) {
			// g.fillOval(x2, y2, 12, 12);
			// g.setColor(Color.WHITE);
			g.fillRect(x + 3, y, 2, 8);
			g.fillRect(x, y + 3, 8, 2);

		}
	}

	private static class StarIcon implements Icon {

		public int getIconWidth() {
			return 8;
		}

		public int getIconHeight() {
			return 8;
		}

		public void paintIcon(Component c, Graphics g, int x, int y) {
			g.drawString("*", x, y + 4);
		}
	}

	private static class MinusIcon extends PlusIcon implements Icon {

		@Override
		public void paintIcon(Component c, Graphics g, int x, int y) {
			g.fillRect(x, y + 3, 8, 2);
		}
	}

	private static class MinimiseIcon extends PlusIcon implements Icon {

		@Override
		public void paintIcon(Component c, Graphics g, int x, int y) {

			Graphics2D g2 = (Graphics2D) g;
			g2.rotate(Math.toRadians(-45), x, y);

			g.fillRect(x - 2, y + 3, 2, 6);
			g.fillRect(x - 2, y + 7, 6, 2);
		}
	}

	private static class ExpandIcon extends PlusIcon implements Icon {

		@Override
		public void paintIcon(Component c, Graphics g, int x, int y) {

			Graphics2D g2 = (Graphics2D) g;
			g2.rotate(Math.toRadians(135), x, y);
			g.fillRect(x - 2, y - 10, 2, 6);
			g.fillRect(x - 2, y - 6, 6, 2);
		}
	}
}
