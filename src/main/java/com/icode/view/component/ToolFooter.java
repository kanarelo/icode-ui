package com.icode.view.component;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JPanel;

public class ToolFooter extends JPanel {

    public ToolFooter() {
        setLayout(new BoxLayout(this, 2));
    }

    public JComponent addButton(Icon paramIcon, String paramString, ActionListener paramActionListener) {
        ToolButton localToolButton = new ToolButton(paramIcon, paramString, paramActionListener);
        add(localToolButton);
        return localToolButton;
    }

    @Override
	public Dimension getPreferredSize() {
        Insets localInsets = getInsets();
        return new Dimension(0 + localInsets.left + localInsets.right, 20 + localInsets.top + localInsets.bottom);
    }

    @Override
	protected void paintComponent(Graphics paramGraphics) {
        Insets localInsets = getInsets();
        int i = localInsets.left;
        int j = localInsets.top;
        int k = getWidth() - localInsets.left - localInsets.right;
        int l = getHeight() - localInsets.top - localInsets.bottom;
        int i1 = l / 2;
        paramGraphics.setColor(Color.white);
        paramGraphics.fillRect(i, j, k, i1);
        paramGraphics.setColor(new Color(15658734));
        paramGraphics.fillRect(i, j + l - i1, k, l - i1);
    }

    public static class MinusIcon
            implements Icon {

        public int getIconWidth() {
            return 8;
        }

        public int getIconHeight() {
            return 8;
        }

        public void paintIcon(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2) {
            paramGraphics.fillRect(paramInt1, paramInt2 + 3, 8, 2);
        }
    }

    public static class PlusIcon
            implements Icon {

        public int getIconWidth() {
            return 8;
        }

        public int getIconHeight() {
            return 8;
        }

        public void paintIcon(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2) {
            paramGraphics.fillRect(paramInt1 + 3, paramInt2, 2, 8);
            paramGraphics.fillRect(paramInt1, paramInt2 + 3, 8, 2);
        }
    }

    private static class ToolButton extends PressButton {

        private Icon icon;

        private ToolButton(Icon paramIcon, String paramString, ActionListener paramActionListener) {
            this.icon = paramIcon;
            setToolTipText(paramString);
            addActionListener(paramActionListener);
        }

        @Override
		public Dimension getPreferredSize() {
            return new Dimension(21, 20);
        }

        @Override
		protected void paint(Graphics2D paramGraphics2D, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3) {
            paramGraphics2D.setColor(new Color(11184810));
            paramGraphics2D.fillRect(getWidth() - 1, 0, 1, getHeight());
            if (paramBoolean2) {
                paramGraphics2D.setColor(Color.lightGray);
                paramGraphics2D.fillRect(0, 0, getWidth() - 1, getHeight());
            }
            paramGraphics2D.setColor((isEnabled()) ? Color.gray : (paramBoolean1) ? Color.darkGray : Color.lightGray);
            this.icon.paintIcon(this, paramGraphics2D, (getWidth() - 1 - this.icon.getIconWidth()) / 2, (getHeight() - this.icon.getIconHeight()) / 2);
        }

        @Override
        protected void actionPerformed() {
        }
    }
}

/* Location:           C:\Users\Nes\Desktop\Thinet\flient.jar
 * Qualified Name:     thinlet.panel.ToolFooter
 * JD-Core Version:    0.5.4
 */
