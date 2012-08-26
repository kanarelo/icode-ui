package com.icode.view.component;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Window;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.icode.resources.ResourceUtils;

public class MenuList extends JPanel implements AdjustmentListener,
		AncestorListener, WindowFocusListener, FocusListener, MouseListener,
		MouseWheelListener {

	private JScrollBar scrollBar;
	private transient boolean windowFocused;
	private transient boolean focused;
	private Item selected;

	public MenuList() {
		super(null);
		addAncestorListener(this);
		addMouseListener(this);
		addMouseWheelListener(this);
		addFocusListener(this);
		this.scrollBar = new JScrollBar(1);
		this.scrollBar.addAdjustmentListener(this);
		add(this.scrollBar);
	}

	public Item getSelected() {
		return this.selected;
	}

	public void addSeparator() {
		add(new Separator());
	}

	public void addChangeListener(ChangeListener paramChangeListener) {
		this.listenerList.add(ChangeListener.class, paramChangeListener);
	}

	public void removeChangeListener(ChangeListener paramChangeListener) {
		this.listenerList.remove(ChangeListener.class, paramChangeListener);
	}

	private void select(Item paramItem) {
		if (this.selected == paramItem) {
			return;
		}
		if (this.selected != null) {
			this.selected.repaint();
		}
		if (paramItem != null) {
			paramItem.repaint();
		}
		this.selected = paramItem;
		for (ChangeListener localChangeListener : this.listenerList
				.getListeners(ChangeListener.class)) {
			localChangeListener.stateChanged(new ChangeEvent(this));
		}
	}

	@Override
	public boolean isFocusable() {
		return true;
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(190, 360);
	}

	@Override
	public void doLayout() {
		int n = getComponentCount();
		int[] hs = new int[n];
		int sh = 0;
		for (int i = 1; i < n; i++) {
			hs[i] = getComponent(i).getPreferredSize().height;
			sh += hs[i];
		}

		int offset = 0, w = getWidth(), h = getHeight();
		if (sh <= getHeight()) {
			scrollBar.setVisible(false);
		} else {
			scrollBar
					.setValues(
							offset = Math.max(0,
									Math.min(scrollBar.getValue(), sh - h)), h,
							0, sh);
			int sw = scrollBar.getPreferredSize().width;
			scrollBar.setBounds(w - sw, 0, sw, h);
			scrollBar.setVisible(true);
			w -= sw;
		}

		int y = 0;
		for (int i = 1; i < n; i++) {
			getComponent(i).setBounds(0, y - offset, w, hs[i]);
			y += hs[i];
		}
	}

	@Override
	protected void paintComponent(Graphics paramGraphics) {
		Border localBorder = UIManager
				.getBorder("List.sourceListBackgroundPainter");
		if (localBorder != null) {
			localBorder.paintBorder(this, paramGraphics, 0, 0, getWidth(),
					getHeight());
		} else {
			paramGraphics.setColor(new Color((this.windowFocused) ? 14081509
					: 15263976));
			paramGraphics.fillRect(0, 0, getWidth(), getHeight());
		}
	}

	public void mouseWheelMoved(MouseWheelEvent paramMouseWheelEvent) {
		if (!this.scrollBar.isVisible()) {
			return;
		}
		this.scrollBar.setValue(this.scrollBar.getValue()
				+ paramMouseWheelEvent.getUnitsToScroll() * 8);
	}

	public void adjustmentValueChanged(AdjustmentEvent paramAdjustmentEvent) {
		revalidate();
		repaint();
	}

	public void ancestorAdded(AncestorEvent paramAncestorEvent) {
		Window localWindow = SwingUtilities.windowForComponent(this);
		localWindow.addWindowFocusListener(this);
		setWindowFocused(localWindow.isFocused());
	}

	public void ancestorMoved(AncestorEvent paramAncestorEvent) {
	}

	public void ancestorRemoved(AncestorEvent paramAncestorEvent) {
	}

	public void windowGainedFocus(WindowEvent paramWindowEvent) {
		setWindowFocused(true);
	}

	public void windowLostFocus(WindowEvent paramWindowEvent) {
		setWindowFocused(false);
	}

	private void setWindowFocused(boolean paramBoolean) {
		if (this.windowFocused == paramBoolean) {
			return;
		}
		this.windowFocused = paramBoolean;
		repaint();
	}

	public void focusGained(FocusEvent paramFocusEvent) {
		setFocused(true);
	}

	public void focusLost(FocusEvent paramFocusEvent) {
		setFocused(false);
	}

	private void setFocused(boolean paramBoolean) {
		if (this.focused == paramBoolean) {
			return;
		}
		this.focused = paramBoolean;
		if (this.selected == null) {
			return;
		}
		this.selected.repaint();
	}

	public void mousePressed(MouseEvent paramMouseEvent) {
		requestFocus();
	}

	public void mouseClicked(MouseEvent paramMouseEvent) {
	}

	public void mouseEntered(MouseEvent paramMouseEvent) {
	}

	public void mouseExited(MouseEvent paramMouseEvent) {
	}

	public void mouseReleased(MouseEvent paramMouseEvent) {
	}

	@Override
	protected void addImpl(Component paramComponent, Object paramObject,
			int paramInt) {
		super.addImpl(paramComponent, paramObject, paramInt);
		revalidate();
	}

	@Override
	public void remove(int paramInt) {
		if (getComponent(paramInt) == this.selected) {
			select(null);
		}
		super.remove(paramInt);
		revalidate();
	}

	private class Separator extends JComponent {

		private Separator() {
		}

		@Override
		public Dimension getPreferredSize() {
			return new Dimension(0, 4);
		}

		@Override
		protected void paintComponent(Graphics paramGraphics) {
			paramGraphics.setColor(new Color(13948116));
			paramGraphics.drawLine(8, 1, getWidth() - 9, 1);
			paramGraphics.setColor(new Color(15263976));
			paramGraphics.drawLine(8, 2, getWidth() - 9, 2);
		}
	}

	public static class Item extends JComponent implements MouseListener {

		private String text;
		private String count;

		public Item(String paramString) {
			this.text = paramString;
			setFont(UIManager.getFont("Label.boldfont"));
			addMouseListener(this);
		}

		public void setCount(String paramString) {
			this.count = paramString;
			repaint();
		}

		@Override
		public Dimension getPreferredSize() {
			FontMetrics localFontMetrics = getFontMetrics(getFont());
			return new Dimension(localFontMetrics.stringWidth(this.text) + 16,
					localFontMetrics.getAscent()
							+ localFontMetrics.getDescent() + 8);
		}

		@Override
		protected void paintComponent(Graphics paramGraphics) {
			MenuList localMenuList = (MenuList) getParent();
			Graphics2D localGraphics2D = ResourceUtils.init(paramGraphics);
			int i = (localMenuList.selected == this) ? 1 : 0;
			boolean bool1 = localMenuList.windowFocused;
			boolean bool2 = localMenuList.focused;
			if (i != 0) {
				Border localObject = UIManager
						.getBorder((bool2) ? "List.sourceListFocusedSelectionBackgroundPainter"
								: "sourceListSelectionBackgroundPainter");
				if (localObject != null) {
					localObject.paintBorder(this, localGraphics2D, 0, 0,
							getWidth(), getHeight());
				} else {
					paramGraphics.setColor(new Color((bool1) ? 9543872
							: (bool2) ? 4554952 : 9934743));
					paramGraphics.drawLine(0, 0, getWidth() - 1, 0);
					localGraphics2D.setPaint(new GradientPaint(0.0F, 1.0F,
							new Color((bool1) ? 10662352 : (bool2) ? 6067158
									: 11908533), 0.0F, getHeight() - 1,
							new Color((bool1) ? 7307946 : (bool2) ? 1463466
									: 9079434)));
					paramGraphics.fillRect(0, 1, getWidth(), getHeight() - 1);
				}
			}
			paramGraphics.setFont(UIManager.getFont((i != 0) ? "Label.boldfont"
					: "Label.font"));
			Object localObject = paramGraphics.getFontMetrics();
			int j = 4 + ((FontMetrics) localObject).getAscent();
			if (i != 0) {
				paramGraphics.setColor(new Color(-2147483648, true));
				paramGraphics.drawString(this.text, 8, j + 1);
			}
			paramGraphics.setColor((i != 0) ? Color.white : Color.black);
			paramGraphics.drawString(this.text, 8, j);
			if (this.count == null) {
				return;
			}
			paramGraphics.setFont(UIManager.getFont("Label.boldfont"));
			localObject = paramGraphics.getFontMetrics();
			int k = ((FontMetrics) localObject).stringWidth(this.count);
			int l = getWidth() - k - 8 - 8;
			paramGraphics.setColor((i != 0) ? Color.white : new Color(
					(bool1) ? 8233181 : 10921638));
			paramGraphics.fillRoundRect(l, 4, k + 8,
					((FontMetrics) localObject).getAscent()
							+ ((FontMetrics) localObject).getDescent(), 14, 14);
			paramGraphics.setColor((i != 0) ? Color.gray : (bool2) ? new Color(
					8233181) : Color.white);
			paramGraphics.drawString(this.count, l + 4,
					4 + ((FontMetrics) localObject).getAscent());
		}

		public void mousePressed(MouseEvent paramMouseEvent) {
			MenuList localMenuList = (MenuList) getParent();
			localMenuList.requestFocus();
			localMenuList.select(this);
		}

		public void mouseClicked(MouseEvent paramMouseEvent) {
		}

		public void mouseEntered(MouseEvent paramMouseEvent) {
		}

		public void mouseExited(MouseEvent paramMouseEvent) {
		}

		public void mouseReleased(MouseEvent paramMouseEvent) {
		}
	}

	public static class Group extends JComponent {

		private String text;

		public Group(String paramString) {
			this.text = paramString.toUpperCase();
			setForeground(new Color(-1610612736, true));
			setFont(UIManager.getFont("Label.boldfont"));
		}

		@Override
		public Dimension getPreferredSize() {
			FontMetrics localFontMetrics = getFontMetrics(getFont());
			return new Dimension(localFontMetrics.stringWidth(this.text) + 16,
					localFontMetrics.getAscent()
							+ localFontMetrics.getDescent() + 12);
		}

		@Override
		protected void paintComponent(Graphics paramGraphics) {
			ResourceUtils.init(paramGraphics);
			FontMetrics localFontMetrics = paramGraphics.getFontMetrics();
			paramGraphics.setColor(Color.white);
			paramGraphics.drawString(this.text, 8,
					9 + localFontMetrics.getAscent());
			paramGraphics.setColor(getForeground());
			paramGraphics.drawString(this.text, 8,
					8 + localFontMetrics.getAscent());
		}
	}
}
