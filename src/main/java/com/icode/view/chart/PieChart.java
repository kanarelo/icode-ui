/* Copyright 2008-2009 Robert Bajzat. All rights reserved. Use is subject to license terms. */
package com.icode.view.chart;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;

import com.icode.resources.ResourceUtils;
import com.icode.util.Commons;

/**
 * 
 * @author Nes
 */
public class PieChart extends JComponent {

    private List<Segment> segments;
    private transient int[] arcs;

    /**
     *
     */
    public PieChart() {
        setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        segments = new ArrayList<Segment>(2);
        ToolTipManager.sharedInstance().registerComponent(this);
    }

    /**
     *
     * @param value
     * @param title
     * @param description
     * @param tooltip
     */
    public void addSegment(float value, String title, String description, String tooltip) {
        segments.add(new Segment(value, title, description, tooltip));
        arcs = null;
        Commons.repaintAncestry(this);
    }

    private static class Segment {

        private float value;
        private String title;
        private String description;
        private String tooltip;

        private Segment(float value, String title, String description, String tooltip) {
            this.value = value;
            this.title = title;
            this.description = description;
            this.tooltip = tooltip;
        }
    }
    private final Font plainFont = UIManager.getFont("Label.font").deriveFont(UIManager.getFont("Label.font").getSize()*.95F);
    private final Font boldFont  = new Font(plainFont.getName(), Font.BOLD, plainFont.getSize());

    @Override
    public Dimension getPreferredSize() {
        FontMetrics fmp = getFontMetrics(plainFont),
                fmb = getFontMetrics(boldFont);
        int w = 0;
        for (Segment segment : segments) {
            w = segment.description != null ? 
            		Math.max(w, Math.max(fmb.stringWidth(segment.title), fmp.stringWidth(segment.description))):
            			Math.max(w, fmb.stringWidth(segment.title));
        }
        Insets is = getInsets();
        return new Dimension(getWidth() + 16 + 16 + w + is.left + is.right, getHeight() + is.top + is.bottom);
    }

    @Override
    public String getToolTipText(MouseEvent e) {
        if (segments.isEmpty()) {
            return null;
        }
        Insets is = getInsets();
        int x = e.getX() - 150 - is.left, y = e.getY() - 150 - is.top;
        if ((x < -150) || (y < -150) || (x >= 150) || (y >= 150)
                || (arcs == null) || (x * x + y * y >= 150 * 150)) {
            return null;
        }
        int a = (int) (Math.atan((double) x / y) * 180 / Math.PI);
        a = (y >= 0) ? (180 - a) : (x >= 0) ? -a : (360 - a);
        int index = 0;
        for (int i = 0, s = 0; i < arcs.length; i++) {
            if ((a >= s) && (a <= s + arcs[i])) {
                index = i;
                break;
            }
            s += arcs[i];
        }
        return segments.get(index).tooltip;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Insets is = getInsets();
        int w = getWidth() - is.left - is.right, h = getHeight() - is.top - is.bottom,
                d = Math.min(w, h), x = is.left + (w - d) / 2, y = is.top + (h - d) / 2;
        int fx = x + d + 16;
        int fy = y;
        Graphics2D g2 = ResourceUtils.init(g);
        if (arcs == null) {
            int n = segments.size();
            arcs = new int[n];
            float s = 0;
            for (Segment segment : segments) {
                s += segment.value;
            }
            for (int i = 0, remain = 360; i < n; i++) {
                Segment segment = segments.get(i);
                arcs[i] = (int) (segment.value * remain / s);
                remain -= arcs[i];
                s -= segment.value;
            }
        }

        if (arcs.length == 0) {
            Stroke stroke = g2.getStroke();
            g2.setStroke(new BasicStroke(2));
            g2.setColor(Color.lightGray);
            g2.drawOval(x, y, d, d);
            g2.setStroke(stroke);
            return;
        }

        FontMetrics fmp = g2.getFontMetrics(plainFont),
                fmb = g2.getFontMetrics(boldFont);
        int fap = fmp.getAscent(), fdp = fmp.getDescent(),
                fab = fmb.getAscent(), fdb = fmb.getDescent();

        Stroke stroke = g2.getStroke();
        g2.setStroke(new BasicStroke(2,
                BasicStroke.CAP_SQUARE, BasicStroke.JOIN_ROUND));
        for (int i = 0, n = arcs.length, start = 0; i < n; i++) {
            g2.setColor(getFillColor(i, n));
            g2.fillArc(x, y, d, d, 90 - start, -arcs[i]);
            g2.setColor(getLineColor(i, n));
            g2.drawArc(x, y, d, d, 90 - start, -arcs[i]);
            start += arcs[i];

            Segment segment = segments.get(i);
            g2.fillRect(fx, fy + (fab + fdb - 12) / 2, 12, 12);

            g2.setColor(Color.darkGray);
            g2.setFont(boldFont);
            g2.drawString(segment.title, fx + 16, fy + fab);
            fy += fab + fdb;

            if (segment.description != null){
	            g2.setColor(Color.black);
	            g2.setFont(plainFont);
	            g2.drawString(segment.description, fx + 16, fy + fap);
	            fy += fap + fdp + 4;
            }
        }
        g2.setStroke(stroke);
    }

    private static Color getLineColor(int i, int n) {
        return Color.getHSBColor((0.6f + ((float) i / n)) % 1, 0.7f, 0.8f);
    }

    private static Color getFillColor(int i, int n) {
        return Color.getHSBColor((0.6f + ((float) i / n)) % 1, 0.4f, 0.9f);
    }
    private static final Logger LOG = Logger.getLogger(PieChart.class.getName());
}
