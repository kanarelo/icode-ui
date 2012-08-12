/* Copyright 2008-2009 Robert Bajzat. All rights reserved. Use is subject to license terms. */
package com.icode.view.component;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.net.URI;

/**
 * A button similar to html's anchor tag
 */
public class LinkButton extends PressButton {

    private String text;
    private String url;

    /**
     * Creates a link button with text
     * @param text the text of the button
     */
    public LinkButton(String text) {
        this(text, null);
    }

    /**
     * Creates a link button with text and an url to open if pressed
     * @param text the text of the button
     * @param url the url to open
     */
    public LinkButton(String text, String url) {
        this.text = text;
        this.url = url;
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        //if (url != null) setToolTipText(url);
    }

    /**
     * Returns the button's text
     * @return the link button's text
     */
    public String getText() {
        return text;
    }

    /**
     * Updates the button's text
     * @param text the string used to set the text
     */
    public void setText(String text) {
        this.text = text;
//		revalidate(); repaint();
    }

    /**
     * Overwrite the method to return the calculated size
     */
    @Override
    public Dimension getPreferredSize() {
        FontMetrics fm = getFontMetrics(getFont());
        return new Dimension(fm.stringWidth(text), fm.getAscent() + fm.getDescent());
    }

    /**
     * Overwrite the method to paint the component
     */
    @Override
	protected void paint(Graphics2D g,
            boolean inside, boolean pressed, boolean focused) {
        if (focused) {
            g.setColor(Color.gray);
            Stroke stroke = g.getStroke();
            g.setStroke(new BasicStroke(1, BasicStroke.CAP_SQUARE,
                    BasicStroke.JOIN_MITER, 10, new float[]{1, 2}, 0));
            g.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 4, 4);
            g.setStroke(stroke);
        }

        g.setColor(isEnabled() ? (inside && pressed)
                ? Color.black : new Color(0x3366cc) : Color.lightGray);
        FontMetrics fm = g.getFontMetrics();
        int fa = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
        g.drawString(text, 0, fa);
        if (inside) {
            g.drawLine(0, fa + 1, getWidth() - 1, fa + 1);
        }
    }

    /**
     * Opens the given url in the preferred browser application
     * @param url the url to open
     */
    public static void openBrowser(String url) {
        try { // Java 1.6
            Class<?> clazz = Class.forName("java.awt.Desktop");
            clazz.getMethod("browse", URI.class).invoke(
                    clazz.getMethod("getDesktop").invoke(null), new URI(url));
        } catch (Throwable exc) {
            try { // OS X
                Class.forName("com.apple.eio.FileManager").getMethod(
                        "openURL", String.class).invoke(null, url);
            } catch (Throwable exx) {
                try { // Windows
                    Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler "
                            + url).waitFor();
                } catch (Throwable exw) {
                }
            }
        }
    }

    /**
     * Overwrite the method to make the component focusable
     */
    @Override
    public boolean isFocusable() {
        return true;
    }

    /**
     * Overwrite the method to open the given url if set
     */
    @Override
    protected void actionPerformed() {
        if (url != null) {
            openBrowser(url);
        }
    }
}
