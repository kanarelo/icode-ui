/* Copyright 2008-2009 Robert Bajzat. All rights reserved. Use is subject to license terms. */
package com.icode.view.app;

import java.awt.Color;
import java.awt.Container;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JComponent;
import javax.swing.RootPaneContainer;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

import com.icode.resources.ResourceUtils;

/**
 * Manager for bubble tool tip components
 */
public class TipManager extends JComponent {

	private static TipManager manager;
	private Listener listener;
	private Timer timer;
	private JComponent target;
	private String[] text;

	private TipManager() {
		listener = new Listener();
		timer = new Timer(0, listener);
		timer.setRepeats(false);
		setFont(UIManager.getFont("Label.font"));
	}

	private class Listener implements MouseListener, AncestorListener,
			ActionListener {

		public void mouseEntered(MouseEvent e) {
			target = (JComponent) e.getSource();
			timer.setInitialDelay(750);
			timer.start();
		}

		public void mouseExited(MouseEvent e) {
			if (timer.isRunning()) {
				timer.stop();
			}
			hideTip();
		}

		public void mouseClicked(MouseEvent e) {
		}

		public void mousePressed(MouseEvent e) {
		}

		public void mouseReleased(MouseEvent e) {
		}

		public void ancestorRemoved(AncestorEvent e) {
			if (timer.isRunning()) {
				timer.stop();
			}
			hideTip();
		}

		public void ancestorAdded(AncestorEvent e) {
		}

		public void ancestorMoved(AncestorEvent e) {
		}

		public void actionPerformed(ActionEvent e) {
			if (getParent() == null) {
				String tip = null;
				try {
					tip = target.getToolTipText();
				} catch (java.lang.NullPointerException ex) {
				}

				if ((tip == null) || !target.isVisible()) {
					return;
				}
				text = tip.split("\n");
				showTip();
				timer.setInitialDelay(4000);
				timer.start();
			} else {
				hideTip();
			}
		}
	}

	private void showTip() {
		JComponent glassPane = (JComponent) ((RootPaneContainer) SwingUtilities
				.windowForComponent(target)).getGlassPane();

		FontMetrics fm = getFontMetrics(getFont());
		int w = 0, fh = fm.getHeight(), h = fm.getAscent() + fm.getDescent()
				+ 12 + 6;
		for (int i = 0; i < text.length; i++) {
			w = Math.max(w, fm.stringWidth(text[i]) + 12);
			if (i > 0) {
				h += fh;
			}
		}
		Point p = new Point();
		p = SwingUtilities.convertPoint(target, p, glassPane);
		int x = p.x + (target.getWidth()) / 2 - 18, y = p.y
				+ target.getHeight();

		glassPane.setLayout(null);
		setBounds(x, y, w, h);
		glassPane.add(this);
		glassPane.setVisible(true);
	}

	private void hideTip() {
		Container glassPane = getParent();
		if (glassPane != null) {
			target = null;
			text = null;
			glassPane.remove(this);
			glassPane.setVisible(false);
		}
	}

	/**
	 * Overwrites method to paint the component
	 */
	@Override
	protected void paintComponent(Graphics g) {
		ResourceUtils.init(g);
		g.setColor(new Color(0xbb000000, true));
		g.fillRoundRect(0, 6, getWidth(), getHeight() - 6, 12, 12);
		g.fillPolygon(new int[] { 12, 16, 22 }, new int[] { 6, 0, 6 }, 3);
		g.setColor(Color.white);
		FontMetrics fm = g.getFontMetrics();
		int fa = 12 + fm.getAscent(), fh = fm.getHeight();
		for (int i = 0; i < text.length; i++) {
			g.drawString(text[i], 6, fa);
			fa += fh;
		}
	}

	/**
	 * Registers a component for tooltip management
	 * 
	 * @param component
	 *            a JComponent object to add
	 */
	public static void register(JComponent component) {
		ToolTipManager.sharedInstance().unregisterComponent(component);
		if (manager == null) {
			manager = new TipManager();
		}
		component.addMouseListener(manager.listener);
		component.addAncestorListener(manager.listener);
	}
}
