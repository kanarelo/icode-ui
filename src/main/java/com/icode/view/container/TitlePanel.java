/* Copyright 2008-2009 Robert Bajzat. All rights reserved. Use is subject to license terms. */
package com.icode.view.container;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.RadialGradientPaint;
import java.awt.event.MouseEvent;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
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
public class TitlePanel extends JToolBar {
    private JLabel title;

    public JLabel getTitle() {
        return title;
    }

    /**
     * Creates a non-floatable toolbar with specific border and background, and box layout
     */
    public TitlePanel() {
        setFloatable(false);
        setBorder(new LineBorder(Color.lightGray, 0, 0, 1, 0, 0, 8, 0, 8));
        setBackground(new Color(0xdddddd));
        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
    }

    public JLabel addTheTitleLabel(String text) {
        title = new JLabel(text);
        add(title);
        return title;
    }
 
    /**
     * Adds a new label with bold font
     * @param text the text for the label
     * @return the created component
     */
    public JLabel addTitleLabel(String text) {
        JLabel _title = new JLabel(text);
        add(_title);
        return _title;
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

    public AppButton addAppButton(String path, String tip) {
        return addAppButton(path, tip, null);
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

    public AppButton addAppButton(String path, String tip, JPopupMenu popup) {
        AppButton button = new AppButton(path, tip, popup);
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
            this(TitlePanel.class, path);
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

    public static class AppButton extends PressButton implements java.awt.event.MouseMotionListener {

        private Icon icon, disabledIcon;
        private GeneralPath gp;
        private int radialX = 0;
        private int radialY = 0;

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

        public AppButton(String path, String tip, JPopupMenu popup) {
            icon = ResourceUtils.getIcon(path);
            setFocusable(false);
            setToolTipText(tip);
            TipManager.register(this);
            this.popup = popup;
            this.addMouseMotionListener(this);
        }

        public AppButton(Class<?> clazz, String path, String tip, JPopupMenu popup) {
            this(path, tip, popup);
            if (clazz != null) {
                icon = ResourceUtils.getIcon(clazz, path);
            }
        }

        public AppButton(Class<?> clazz, String path) {
            if (clazz != null) {
                icon = ResourceUtils.getIcon(clazz, path);
            }
            this.addMouseMotionListener(this);
        }

        public AppButton(String path) {
            this(TitlePanel.class, path);
        }
        private static final int qr = 7;// Quad Radius
        private static final Color pressedLineColor = new Color(29, 70, 159);
        private static final Color innerLineColor = new Color(67, 135, 228);
        private static final Color insideLineColor = new Color(31, 72, 161);
        private static final Color upperGradColorInside = new Color(69, 130, 226);
        private static final Color lowerGradColorInside = new Color(50, 103, 188);
        private static final Color upperGradColorPressed = new Color(40, 97, 178).darker();
        private static final Color lowerGradColorPressed = new Color(56, 139, 228);
        private static final Color upperGradColorLS = new Color(48, 102, 187);
        private static final Color lowerGradColorLS = new Color(104, 170, 239);

        @Override
		protected void paint(Graphics2D g,
                boolean inside, boolean pressed, boolean focused) {
            gp = new GeneralPath();
            int h = this.getHeight() - 1;
            int w = this.getWidth() - 1;
            int x = 0;
            int y = 0;

            if (h == 0 || w == 0) {
                gp.moveTo(x, y);
                gp.lineTo(x + w, y + h);
            } else {
                gp.moveTo(x, y + (h + 1));
                gp.lineTo(x, y + qr);
                gp.quadTo(x, y, x + qr, y);
                gp.lineTo((x + w) - qr, y);
                gp.quadTo((x + w), y, x + w, y + qr);
                gp.lineTo(x + w, y + (h + 1));
                gp.moveTo(x, y + (h + 1));
            }
            if (isEnabled()) {
                if (pressed && inside) {
                    g.setPaint(new GradientPaint(h * .25F, w * .1F, upperGradColorPressed, h * .25F, w*.9F, lowerGradColorPressed));
                    g.fill(gp);
                    g.setColor(innerLineColor);
                    g.drawRoundRect(x + 1, y + 1, w - 2, h - 1, qr + 1, qr + 1);
                    g.setColor(new Color(39, 96, 181));
                    g.draw(gp);
                } else if (pressed || inside || !inside) {
                    g.setPaint(
                            new RadialGradientPaint(new Point2D.Double(radialX, radialY),
                            getHeight() * 0.80F,
                            new float[]{0.0f, 1.0f},
                            new Color[]{lowerGradColorLS, upperGradColorLS}));
                    g.fill(gp);
                    g.setColor(innerLineColor);
                    g.drawRoundRect(x + 1, y + 1, w - 2, h - 1, qr + 1, qr + 1);
                    g.setColor(insideLineColor);
                    g.draw(gp);
                }
                icon.paintIcon(this, g, (int) (getPreferredSize().height / 8.0), (int) (getPreferredSize().width / 16.0));
            } else {
                if (disabledIcon == null) {
                    disabledIcon = UIManager.getLookAndFeel().getDisabledIcon(this, icon);
                }
                disabledIcon.paintIcon(this, g, 2, 2);
            }
        }

        @Override
        public void mouseExited(MouseEvent e) {
            super.mouseExited(e);
            radialX = (int) (this.getWidth() / 2.0);
            radialY = this.getHeight();
            repaint();
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(icon.getIconWidth() + 10, icon.getIconHeight() + 6);
        }

        @Override
        protected void actionPerformed() {
            if (popup != null) {
                popup.show(this, 0, getHeight());
            }
        }

        public void mouseDragged(MouseEvent e) {
        }

        public void mouseMoved(MouseEvent e) {
            radialX = e.getX();
            radialY = e.getY();
            repaint();
        }
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

    private static class StringToolButton extends PressButton {

        private JPopupMenu popup = null;
        private String label;

        StringToolButton(String label, JPopupMenu popup) {
            this.label = label;
            setFocusable(false);
            this.popup = popup;
        }

        private StringToolButton(String title) {
            this(title, null);
        }
        private static final Color focusedCol = new Color(133, 133, 133);
        private static final Color pressedCol = new Color(103, 103, 103);

        @Override
		protected void paint(Graphics2D g, boolean inside, boolean pressed, boolean focused) {
            if (isEnabled()) {
                if (pressed || inside) {
                    g.setColor((pressed && inside) ? pressedCol : focusedCol);
                    g.fillRoundRect(0, 0, getWidth(), getHeight(), 0, 0);
                    g.setColor(Color.WHITE);
                    g.drawString(label, 2, (getPreferredSize().height / 2) + 4);
                }
                if (popup != null) {
                    if (popup.isVisible()) {
                        g.setColor(pressedCol);
                        g.fillRoundRect(0, 0, getWidth(), getHeight(), 0, 0);
                        g.setColor(Color.WHITE);
                        g.drawString(label, 2, (getPreferredSize().height / 2) + 4);
                    }
                }
                g.drawString(label, 2, (getPreferredSize().height / 2) + 4);
            }
        }

        @Override
        public Dimension getPreferredSize() {
            FontMetrics fm = this.getFontMetrics(this.getFont());
            return new Dimension(fm.stringWidth(label) + 6, this.getParent().getHeight() - 8);
        }

        @Override
        protected void actionPerformed() {
            if (popup != null) {
                popup.show(this, 0, getHeight());
            }
        }
    }
}
