/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.icode.view.container;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

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
public class DialogStrip extends JToolBar {

    private MessageType messageType = MessageType.PLAIN_MESSAGE;
    private final JLabel lblTitle = new JLabel();
    private Icon iconMessageType;

    public String getCancel() {
        return cancel;
    }

    public void setCancel(String cancel) {
        this.cancel = cancel;
    }

    public String getMessage() {
        return message;
    }

    public final void setMessage(String message) {
        this.message = message;
        this.add(new JLabel(message));
    }

    public MessageType getMessageType() {
        return messageType;
    }
    private static Map<MessageType, Icon> iconMap;

    static {
        iconMap = new Hashtable<MessageType, Icon>(6);
        iconMap.put(MessageType.ERROR, ResourceUtils.getIcon("/icons/16/error.png"));
        iconMap.put(MessageType.INFORMATION, ResourceUtils.getIcon("/icons/16/info.png"));
        iconMap.put(MessageType.PLAIN_MESSAGE, ResourceUtils.getIcon("/icons/16/user.png"));
        iconMap.put(MessageType.WARNING, ResourceUtils.getIcon("/icons/16/warning.png"));
        iconMap.put(MessageType.QUESTION, ResourceUtils.getIcon("/icons/16/question.png"));
        iconMap.put(MessageType.SUCCESS, ResourceUtils.getIcon("/icons/16/success.png"));
    }

    public final void setMessageType(MessageType messageType) {
        this.messageType = messageType;
        this.iconMessageType = iconMap.get(messageType);
        this.lblTitle.setIcon(iconMessageType);
    }

    public String getOk() {
        return ok;
    }

    public void setOk(String ok) {
        this.ok = ok;
    }

    public String getTitle() {
        return title;
    }
    private String title;
    private String message;
    private String ok;
    private String cancel;

    /**
     * Creates a non-floatable toolbar with specific border and background, and box layout
     */
    public DialogStrip() {
        setFloatable(false);
        setBorder(new LineBorder(Color.lightGray, 0, 0, 1, 0, 0, 8, 0, 8));
        setBackground(new Color(0xdddddd));
        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
    }

    public DialogStrip(MessageType messageType, String title, String message, String ok, String cancel) {
        this();
        setMessageType(messageType);
        setTitle(title);
        setMessage(message);
        this.ok = ok;
        this.cancel = cancel;
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
        if (this.messageType == MessageType.WARNING) {
            g2.setPaint(new GradientPaint(w * .25F, x, new Color(253, 217, 69), w * .25F, h, new Color(218, 187, 63)));
        } else if (this.messageType == MessageType.ERROR) {
            g2.setPaint(new GradientPaint(w * .25F, x, new Color(248, 104, 62).brighter(), w * .25F, h, new Color(255, 68, 37).brighter()));
        } else if (this.messageType == MessageType.INFORMATION || this.messageType == MessageType.QUESTION) {
            g2.setPaint(new GradientPaint(w * .25F, x, new Color(96, 195, 233).brighter(), w * .25F, h, new Color(89, 151, 232).brighter()));
        } else if (this.messageType == MessageType.PLAIN_MESSAGE) {
            g2.setPaint(new GradientPaint(w * .25F, x, new Color(255, 246, 189), w * .25F, h, new Color(255, 236, 130)));
        } else if (this.messageType == MessageType.SUCCESS) {
            g2.setPaint(new GradientPaint(w * .25F, x, new Color(114, 211, 126).brighter(), w * .25F, h, new Color(78, 214, 95).brighter()));
        }
        g2.fillRect(0, 1, w + 1, h - 1);
    }

    @Override
    public void paintBorder(Graphics g) {
        int h = this.getPreferredSize().height - 1;
        int w = this.getWidth() - 1;
        int x = 0;
        int y = 0;
        Graphics2D g2 = ResourceUtils.init(g);
        Color inner;

        if (this.messageType == MessageType.WARNING) {
            g2.setColor(new Color(180, 165, 85));
            inner = g2.getColor().darker();
        } else if (this.messageType == MessageType.ERROR) {
            g2.setColor(new Color(255, 68, 37));
            inner = g2.getColor().darker();
        } else if (this.messageType == MessageType.INFORMATION || this.messageType == MessageType.QUESTION) {
            g2.setColor(new Color(96, 195, 233));
            inner = g2.getColor().darker();
        } else if (this.messageType == MessageType.PLAIN_MESSAGE) {
            g2.setPaint(new GradientPaint(w * .25F, x, new Color(255, 246, 189), w * .25F, h, new Color(255, 236, 130)));
            inner = g2.getColor().darker();
        } else if (this.messageType == MessageType.SUCCESS) {
            g2.setColor(new Color(114, 211, 126));
            inner = g2.getColor().darker();
        }

        g2.drawLine(0, 0, w, 0);
        g2.drawLine(0, h - 1, w + 1, h - 1);
    }

    /**
     * Adds a new label with bold font
     * @param title text the text for the label
     */
    public final void setTitle(String title) {
        this.title = title;
        this.lblTitle.setText(title);
        this.lblTitle.setIcon(iconMessageType);
        this.add(lblTitle);
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

    public PressButton addPlusButton(String tootTip) {
        ToolButton button = new ToolButton(new PlusIcon(), tootTip);
        addStrut(3);
        add(button);
        return button;
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
        addStrut(3);
        add(button);
        return button;
    }

    public JLabel addIcon(String path) {
        JLabel button = new JLabel();
        button.setIcon(ResourceUtils.getIcon(path));
        addStrut(3);
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
        addStrut(3);
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
            this(DialogStrip.class, path);
        }

        public ToolButton(Icon icon, String tooltip) {
            this.icon = icon;
            setFocusable(false);
            setToolTipText(tooltip);
            TipManager.register(this);
        }

        @Override
		protected void paint(Graphics2D g,
                boolean inside, boolean pressed, boolean focused) {
            if (isEnabled()) {
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
                g.setColor(new Color(142, 129, 62));
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
        addStrut(3);
        add(button);
        return button;
    }
    private static List<StringToolButton> stringButtons = new ArrayList<StringToolButton>(5);

    private final class StringToolButton extends PressButton {

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
        private static final int arc = 6;

        @Override
		protected void paint(Graphics2D g, boolean inside, boolean pressed, boolean focused) {
            w = getWidth() - 1;
            h = getHeight() - 1;
            GradientPaint btnInsideG = null;
            GradientPaint btnPlainG = null;
            Color btnPressedC = null;
            Color btnPressedIC = null;
            Color btnPressedIIC = null;
            Color btnTextColor = Color.BLACK;
            Color btnLineC = null;

            if (messageType == MessageType.WARNING) {
                btnPlainG = new GradientPaint(w * .75F, 0, new Color(255, 246, 189), w * .75F, h, new Color(255, 236, 130));
                btnInsideG = new GradientPaint(0, 1, new Color(254, 248, 214), 0, h - 1, new Color(253, 239, 168));
                btnPressedC = new Color(255, 236, 131);
                btnPressedIC = new Color(255, 236, 131);
                btnPressedIIC = new Color(255, 236, 131);
                btnLineC = new Color(180, 165, 85);
            } else if (messageType == MessageType.ERROR) {
                btnPlainG = new GradientPaint(w * .75F, 0, new Color(248, 104, 62).brighter(), w * .75F, h, new Color(255, 68, 37).brighter());
                btnInsideG = new GradientPaint(0, 1, new Color(255, 68, 37).brighter().brighter(), 0, h - 1, new Color(255, 68, 37).brighter().brighter());
                btnPressedC = new Color(248, 104, 62);
                btnPressedIC = new Color(248, 104, 62).darker();
                btnPressedIIC = new Color(248, 104, 62);
                btnLineC = new Color(255, 68, 37).darker();
            } else if (messageType == MessageType.INFORMATION || messageType == MessageType.QUESTION) {
                btnPlainG = new GradientPaint(w * .75F, 0, new Color(255, 246, 189), w * .75F, h, new Color(255, 236, 130));
                btnInsideG = new GradientPaint(0, 1, new Color(254, 248, 214), 0, h - 1, new Color(253, 239, 168));
                btnPressedC = new Color(255, 236, 131);
                btnPressedIC = new Color(255, 236, 131);
                btnPressedIIC = new Color(255, 236, 131);
                btnLineC = new Color(180, 165, 85);
            } else if (messageType == MessageType.PLAIN_MESSAGE) {
                btnPlainG = new GradientPaint(w * .75F, 0, new Color(255, 246, 189), w * .75F, h, new Color(255, 236, 130));
                btnInsideG = new GradientPaint(0, 1, new Color(254, 248, 214), 0, h - 1, new Color(253, 239, 168));
                btnPressedC = new Color(255, 236, 131);
                btnPressedIC = new Color(0xDACA70);
                btnPressedIIC = new Color(243, 225, 125);
                btnLineC = new Color(180, 165, 85);
            } else if (messageType == MessageType.SUCCESS) {
                btnPlainG = new GradientPaint(w * .75F, 0, new Color(255, 246, 189), w * .75F, h, new Color(255, 236, 130));
                btnInsideG = new GradientPaint(0, 1, new Color(254, 248, 214), 0, h - 1, new Color(253, 239, 168));
                btnPressedC = new Color(255, 236, 131);
                btnPressedIC = new Color(255, 236, 131);
                btnPressedIIC = new Color(255, 236, 131);
                btnLineC = new Color(180, 165, 85);
            }

            if (isEnabled()) {
                if (pressed || inside) {
                    if (inside && !pressed) {
                        g.setPaint(btnInsideG);
                        g.fillRoundRect(0, 0, w, h, arc, arc);
                    } else if (pressed && inside) {
                        g.setColor(btnPressedC);
                        g.fillRoundRect(0, 0, w, h, arc, arc);
                        g.setColor(btnPressedIC);
                        g.drawRoundRect(1, 1, w - 2, h - 2, arc, arc);
                        g.setColor(btnPressedIIC);
                        g.drawRoundRect(2, 2, w - 4, h - 4, arc, arc);
                    }
                } else {
                    g.setPaint(btnPlainG);
                    g.fillRoundRect(0, 0, w, h, arc, arc);
                }

                g.setColor(btnLineC);
                g.drawRoundRect(0, 0, w, h, arc, arc);

                g.setColor(btnTextColor);
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
        public Font getFont() {
            Font f = super.getFont();
            return f.deriveFont(Font.BOLD, f.getSize() * .9F);
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

        public final int getIconWidth() {
            return 12;
        }

        public final int getIconHeight() {
            return 12;
        }

        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = ResourceUtils.init(g);
            g2.setStroke(new BasicStroke(2, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_ROUND));
            g2.drawLine(x + 2, y + 2, getIconWidth() - 2, getIconHeight() - 2);
            g2.drawLine(x + 2, getIconHeight() - 2, getIconWidth() - 2, y + 2);

        }
    }
}
