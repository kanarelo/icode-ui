/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.icode.view.container;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPopupMenu;
import javax.swing.JToolBar;
import javax.swing.UIManager;

import com.icode.resources.ResourceUtils;
import com.icode.view.app.TipManager;
import com.icode.view.border.LineBorder;
import com.icode.view.component.PressButton;

/**
 * Wrapper component for tool-bar
 */
public class YellowButtonStrip extends JToolBar {

    /**
     * Creates a non-floatable toolbar with specific border and background, and box layout
     */
    public YellowButtonStrip() {
        setFloatable(false);
        setBorder(new LineBorder(Color.lightGray, 0, 0, 1, 0, 0, 8, 0, 8));
        setBackground(new Color(0xdddddd));
        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
    }

    /**
     * Overwrites the method to fix the minimum heigh of the component
     */
    @Override
    public Dimension getPreferredSize() {
        Dimension d = super.getPreferredSize();
        d.height = Math.max(d.height, 32);
        return d;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        int h = this.getPreferredSize().height - 1;
        int w = this.getWidth() - 1;
        int x = 0;
        int y = 0;
        Graphics2D g2 = ResourceUtils.init(g);
        g2.setPaint(new GradientPaint(w * .25F, x, new Color(255, 246, 189), w * .25F, h, new Color(255, 236, 130)));
        g2.fillRect(0, 1, w + 1, h - 1);
    }

    @Override
    public void paintBorder(Graphics g) {
        int h = this.getPreferredSize().height - 1;
        int w = this.getWidth() - 1;
        int x = 0;
        int y = 0;
        Graphics2D g2 = ResourceUtils.init(g);
        g2.setColor(new Color(180, 165, 85));
        g2.drawLine(0, 0, w, 0);
        g2.drawLine(0, h - 1, w + 1, h - 1);
    }

    /**
     * Adds a new label with bold font
     * @param text the text for the label
     * @return the created component
     */
    public JLabel addTitleLabel(String text) {
        JLabel title = new JLabel(text);
        add(title);
        return title;
    }

    /**
     * Adda a strut with the given width
     * @param width width of the strut
     */
    public void addStrut(int width) {
        add(Box.createHorizontalStrut(width));
    }

    /**
     * Adds a strut with the given width and a glue
     * @param width width of the strut, can be zero
     */
    public void addGlue(int width) {
        if (width > 0) {
            add(Box.createHorizontalStrut(width));
        }
        add(Box.createHorizontalGlue());
    }

    /**
     * Adds a new custom painted button
     * @param clazz the icon path is relative to its package
     * @param path the icon's path
     * @param tip a tooltip text
     * @return the created button component
     */
    public ToolButton addButton(Class<?> clazz, String path, String tip) {
        return addButton(clazz, path, tip, null);
    }

    /**
     * Adds a new custom painted button
     * @param path the icon's path
     * @param tip a tooltip text
     * @return the created button component
     */
    public ToolButton addButton(String path, String tip) {
        return addButton(null, path, tip, null);
    }

    /**
     * Adds a new custom painted button
     * @param path the icon's path
     * @param tip a tooltip text
     * @param popup
     * @return the created button component
     */
    public ToolButton addButton(String path, String tip, JPopupMenu popup) {
        ToolButton button = new ToolButton(path, tip, popup);
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
     * @param clazz the icon path is relative to its package
     * @param path the icon's path
     * @param tip a tooltip text
     * @param popup a popup menu component
     * @return the created button component
     */
    public ToolButton addButton(Class<?> clazz, String path, String tip, JPopupMenu popup) {
        ToolButton button = new ToolButton(clazz, path, tip, popup);
        addStrut(4);
        add(button);
        return button;
    }

    public static class ToolButton extends PressButton {

        private Icon icon, disabledIcon;

        public void setDisabledIcon(Icon disabledIcon) {
            this.disabledIcon = disabledIcon;
        }

        public void setIcon(Icon icon) {
            this.icon = icon;
        }

        public Icon getDisabledIcon() {
            return disabledIcon;
        }

        public Icon getIcon() {
            return icon;
        }
        private JPopupMenu popup;

        public JPopupMenu getPopup() {
            return popup;
        }

        public ToolButton(String path, String tip, JPopupMenu popup) {
            icon = ResourceUtils.getIcon(path);
            setFocusable(false);
            setToolTipText(tip);
            TipManager.register(this);
            this.popup = popup;
        }

        public ToolButton(Class<?> clazz, String path, String tip, JPopupMenu popup) {
            this(path, tip, popup);
            if (clazz != null) {
                icon = ResourceUtils.getIcon(clazz, path);
            }
        }

        public ToolButton(Class<?> clazz, String path) {
            if (clazz != null) {
                icon = ResourceUtils.getIcon(clazz, path);
            }
        }

        public ToolButton(String path) {
            this(YellowButtonStrip.class, path);
        }

        @Override
		protected void paint(Graphics2D g,
                boolean inside, boolean pressed, boolean focused) {
            if (isEnabled()) {
                /*
                if (pressed || inside) {
                g.setColor((pressed && inside) ? Color.gray : Color.lightGray);
                g.fillRoundRect(0, 0, getWidth(), getHeight(), 6, 6);
                }
                 *
                 */
                if (pressed && inside) {
                    g.setColor((pressed && inside) ? new Color(255, 228, 138) : new Color(250, 233, 166));
                    g.fillRoundRect(0, 0, getWidth(), getHeight(), 6, 6);
                    g.setColor((pressed && inside) ? new Color(194, 118, 43) : new Color(237, 198, 85));
                    if (inside && !pressed) {
                        g.setColor(new Color(246, 200, 133));
                        g.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 6, 6);
                        g.setColor(Color.WHITE);
                        g.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 6, 6);
                    }
                    g.setColor((pressed && inside) ? new Color(194, 118, 43) : new Color(237, 198, 85));
                    g.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 6, 6);
                } else if (pressed || inside) {
                    g.setColor((pressed && inside) ? new Color(255, 228, 138) : new Color(250, 233, 166));
                    g.fillRoundRect(0, 0, getWidth(), getHeight(), 6, 6);
                    g.setColor((pressed && inside) ? new Color(194, 118, 43) : new Color(237, 198, 85));
                    if (inside && !pressed) {
                        g.setColor(new Color(246, 200, 133));
                        g.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 6, 6);
                        g.setColor(Color.WHITE);
                        g.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 6, 6);
                    }
                    g.setColor((pressed && inside) ? new Color(194, 118, 43) : new Color(237, 198, 85));
                    g.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 6, 6);
                }
                icon.paintIcon(this, g, 2, 2);
            } else {
                if (disabledIcon == null) {
                    disabledIcon = UIManager.getLookAndFeel().getDisabledIcon(this, icon);
                }
                disabledIcon.paintIcon(this, g, 2, 2);
            }
            if (popup != null) {
                g.setColor(isEnabled() ? Color.darkGray : Color.gray);
                int x = getWidth() - 8, y = getHeight() - 4;
                g.fillPolygon(new int[]{x, x + 8, x + 4}, new int[]{y, y, y + 4}, 3);
            }
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(icon.getIconWidth() + 4, icon.getIconHeight() + 4);
        }

        @Override
        protected void actionPerformed() {
            if (popup != null) {
                popup.show(this, 0, getHeight());
            }
        }
    }

    public PressButton addStringButton(String title) {
        return addStringButton(title, null);
    }

    public PressButton addStringButton(String title, JPopupMenu popup) {
        PressButton button = new StringToolButton(title, popup);
        addStrut(4);
        add(button);
        return button;
    }
    private static List<StringToolButton> stringButtons = new ArrayList<StringToolButton>(5);

    private static final class StringToolButton extends PressButton {

        private JPopupMenu popup = null;
        private String label;
        private int w;
        private int h;

        StringToolButton(String label, JPopupMenu popup) {
            super();
            this.label = label;
            setFocusable(false);
            this.popup = popup;
        }

        private StringToolButton(String title) {
            this(title, null);
        }
        private static final Color focusedCol = new Color(133, 133, 133);
        private static final Color pressedCol = new Color(103, 103, 103);
        private static final int arc = 6;

        @Override
		protected void paint(Graphics2D g, boolean inside, boolean pressed, boolean focused) {
            w = getWidth() - 1;
            h = getHeight() - 1;
            if (isEnabled()) {
                if (pressed || inside) {
                    if (inside && !pressed) {
                        g.setPaint(new GradientPaint(0, 1, new Color(254, 248, 214), 0, h - 1, new Color(253, 239, 168)));
                        g.fillRoundRect(0, 0, w, h, arc, arc);
                    } else if (pressed && inside) {
                        g.setColor(new Color(255, 236, 131));
                        g.fillRoundRect(0, 0, w, h, arc, arc);
                        g.setColor(new Color(0xDACA70));
                        g.drawRoundRect(1, 1, w - 2, h - 2, arc, arc);
                        g.setColor(new Color(243, 225, 125));
                        g.drawRoundRect(2, 2, w - 4, h - 4, arc, arc);
                    }
                    g.setColor(Color.BLACK);
                } else {
                    g.setPaint(new GradientPaint(w * .75F, 0, new Color(255, 246, 189), w * .75F, h, new Color(255, 236, 130)));
                    g.fillRoundRect(0, 0, w, h, arc, arc);
                }
                g.setColor(new Color(180, 165, 85));
                g.drawRoundRect(0, 0, w, h, arc, arc);
                g.setColor(Color.BLACK);
                g.drawString(label, ((w / 2) - (getLabelWidth() / 2)), ((h / 2) + (getLabelHeight() / 2)) - 3);
            }
        }

        @Override
        public void paintBorder(Graphics g) {
//            g.setColor(new Color(180, 165, 85));
//            g.drawRect(0, 0, w, h);
        }

        public int getLabelWidth() {
            FontMetrics fm = this.getFontMetrics(this.getFont());
            return fm.stringWidth(label);
        }

        @Override
        public Font getFont(){
            Font f = super.getFont();
            return f.deriveFont(Font.BOLD, f.getSize()*.9F);
        }

        public int getLabelHeight() {
            FontMetrics fm = this.getFontMetrics(this.getFont());
            return fm.getHeight();
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(getLabelWidth() + 25, this.getParent().getHeight() - 8);
        }

        @Override
        protected void actionPerformed() {
            if (popup != null) {
                popup.show(this, 0, getHeight());
            }
        }
    }
}
