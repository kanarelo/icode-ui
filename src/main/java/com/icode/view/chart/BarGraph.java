/* Copyright 2008-2009 Robert Bajzat. All rights reserved. Use is subject to license terms. */
package com.icode.view.chart;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.GeneralPath;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.ToolTipManager;

import com.icode.resources.ResourceUtils;
import com.icode.util.Commons;

/**
 *
 * @author Nes
 */
public abstract class BarGraph extends JPanel implements MouseMotionListener {

    private long xMin, xMax;
    private float yMin, yMax;
    private List<XLine> xLines;
    private List<YLine> yLines;
    private Rectangle graphArea;
    private Rectangle chartArea;
    private double scaleX, scaleY;

    /**
     *
     * @param xMax
     * @param xMin
     * @param scaleX
     * @param scaleY 
     */
    protected BarGraph(long xMax, long xMin, double scaleX, double scaleY) {
        setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        ToolTipManager.sharedInstance().registerComponent(this);
        this.addMouseMotionListener(this);

        this.xMax = xMax;
        this.xMin = xMin;
        this.scaleX = scaleX;
        this.scaleY = scaleY;
    }

    /**
     *
     * @param minValue
     * @param maxValue
     */
    protected void setRange(float minValue, float maxValue) {
        this.yMin = Math.max(0, minValue - (maxValue - minValue) / 3);
        this.yMax = maxValue + (maxValue - minValue) * 0.05f;
        doLayout();
        Commons.repaintAncestry(this);
    }

    /**
     *
     */
    protected abstract void timeRangeUpdated();

    @Override
    public void doLayout() {
        xLines = null;
        yLines = null;
    }

    @Override
    protected void paintComponent(Graphics g) {
        if ((xLines == null) || (yLines == null)) {
            Insets is = getInsets();
            graphArea = new Rectangle(is.left, is.top,
                    getWidth() - is.left - is.right, getHeight() - is.top - is.bottom);
            graphArea.height = Math.min(graphArea.height, graphArea.width * 2 / 3);
            chartArea = graphArea.getBounds();
            int ratio = graphArea.width / graphArea.height;
            chartArea.setBounds(chartArea.x, chartArea.y, chartArea.width * ratio, chartArea.height * ratio);

            //
            xLines = new ArrayList<XLine>(5);
            scaleX = (float) graphArea.width / (xMax - xMin);

            //
            yLines = new ArrayList<YLine>(5);
            scaleY = graphArea.height / (yMax - yMin);
            revalidateChart();
        }

        Graphics2D g2 = ResourceUtils.init(g);

        g.setColor(Color.white);
        g2.fill(graphArea);

        paintBackground(g2);

        g.setColor(new Color(0xe0e0e0e0));
        for (XLine timeLine : xLines) {
            g.drawLine(graphArea.x + timeLine.x, graphArea.y, graphArea.x + timeLine.x, graphArea.y + graphArea.height);
        }
        for (YLine valueLine : yLines) {
            g.drawLine(graphArea.x, graphArea.y + valueLine.y, graphArea.x + graphArea.width, graphArea.y + valueLine.y);
        }

        paintForeground(g2);

        g.setColor(Color.darkGray);
        FontMetrics fm = g.getFontMetrics();
        int fa = fm.getAscent(), fd = fm.getDescent(), fc = (fa - fd) / 2;
        for (XLine timeLine : xLines) {
            g.drawString(timeLine.text,
                    graphArea.x + timeLine.x - fm.stringWidth(timeLine.text) / 2,
                    graphArea.y + graphArea.height - fd);
        }
        for (YLine valueLine : yLines) {
            g.drawString(valueLine.text, graphArea.x, graphArea.y + valueLine.y + fc);
        }

        g.setColor(Color.lightGray);
        g.drawRect(graphArea.x, graphArea.y, graphArea.width - 1, graphArea.height - 1);
    }

    /**
     *
     */
    protected void revalidateChart() {
    }

    /**
     *
     * @param g
     */
    protected void paintBackground(Graphics2D g) {
    }

    /**
     *
     * @param g
     */
    protected void paintForeground(Graphics2D g) {
    }

    /**
     *
     * @param time
     * @return
     */
    public int getTime(long time) {
        return graphArea.x + (int) (scaleX * (time - xMin));
    }

    /**
     *
     * @param value
     * @return
     */
    public int getValue(float value) {
        return graphArea.y + graphArea.height - (int) (scaleY * (value - yMin));
    }

    /**
     *
     * @param g
     * @param path
     * @param i
     * @param n
     */
    protected void paintPath(Graphics2D g, GeneralPath path, int i, int n) {
        Stroke stroke = g.getStroke();
        g.setStroke(new BasicStroke(2, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_ROUND));
        g.setColor(getLineColor(i, n));
        g.draw(path);
        g.setStroke(stroke);
    }

    /**
     * 
     * @param g
     * @param path
     * @param i
     * @param n
     */
    protected void fillPath(Graphics2D g, GeneralPath path, int i, int n) {
        GeneralPath fillpath = new GeneralPath(path);
        fillpath.lineTo(graphArea.x + graphArea.width, graphArea.y + graphArea.height);
        fillpath.lineTo(graphArea.x, graphArea.y + graphArea.height);
        g.setColor(getFillColor(i, n));
        g.fill(fillpath);
    }

    private static Color getLineColor(int i, int n) {
        return Color.getHSBColor((0.6f + ((float) i / n)) % 1, 0.8f, 0.8f);
    }

    private static Color getFillColor(int i, int n) {
        return Color.getHSBColor((0.6f + ((float) i / n)) % 1, 0.1f, 1f);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(480, 320);
    }

    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public String getToolTipText(MouseEvent e) {
        if (!graphArea.contains(e.getX(), e.getY())) {
            return null;
        }
        long time = xMin + (long) ((e.getX() - graphArea.x) / scaleX);
        float value = yMax - (float) ((e.getY() - graphArea.y) / scaleY);
        return getToolTipText(time, value);
    }

    public void mouseMoved(MouseEvent e) {
        this.getToolTipText(e);
    }

    /**
     *
     * @param time
     * @param value
     * @return
     */
    protected String getToolTipText(long time, float value) {
        return "<html>(<b>" + time + "</b>,<b>" + value + "</b>)";
    }

    private class XLine {

        private long time;
        private int x;
        private String text;
        //private int level;

        private XLine(long time, int x, String text, int level) {
            this.time = time;
            this.x = x;
            this.text = text;
            //this.level = level;
        }
    }

    private class YLine {

        private int y;
        private String text;

        private YLine(int y, String text) {
            this.y = y;
            this.text = text;
        }
    }

    /**
     * Returns a simple component to change the date range of this chart
     * @return the time range changer component
     */
    public JComponent getTimeControl() {
        return new TimeControl();
    }

    private class TimeControl extends JComponent
            implements MouseListener {

        private String text;

        private TimeControl() {
            addMouseListener(this);
            updateText();
        }

        private void updateText() {
            DateFormat format = DateFormat.getDateInstance(DateFormat.SHORT);
            text = format.format(new Date(xMin)) + " - "
                    + format.format(new Date(xMax));
        }

        @Override
        public Dimension getPreferredSize() {
            FontMetrics fm = getFontMetrics(getFont());
            return new Dimension(fm.stringWidth(text) + 40,
                    fm.getAscent() + fm.getDescent() + 6);
        }

        /** Overwritten to return preferred size for box layout */
        @Override
        public Dimension getMinimumSize() {
            return getPreferredSize();
        }

        /** Overwritten to return preferred size for box layout */
        @Override
        public Dimension getMaximumSize() {
            return getPreferredSize();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = ResourceUtils.init(g);
            int w = getWidth(), h = getHeight();

            g2.setPaint(new GradientPaint(0, 0, new Color(0xa5a5a5), 0, h - 1, new Color(0x989898)));
            g.fillRoundRect(0, 0, w, h, h, h);
            g2.setPaint(new GradientPaint(0, 1, Color.white, 0, h - 3, new Color(0xdfdfdf)));
            g.fillRoundRect(1, 1, w - 2, h - 2, h - 2, h - 2);

            g.setColor(Color.darkGray);
            int lx = 6, rx = w - 14, cy = h / 2;
            g.fillPolygon(new int[]{lx + 8, lx, lx + 8}, new int[]{cy - 4, cy, cy + 4}, 3);
            g.fillPolygon(new int[]{rx, rx + 8, rx}, new int[]{cy - 4, cy, cy + 4}, 3);

            FontMetrics fm = g.getFontMetrics();
            g.setColor(Color.black);
            g.drawString(text, 20, (h + fm.getAscent() - fm.getDescent()) / 2);
        }

        public void mousePressed(MouseEvent e) {
            if (e.getX() < 20) { // left
                xMax = xMin;
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(xMax);
                calendar.add(Calendar.DATE, -29);
                xMin = calendar.getTimeInMillis();
                timeRangeUpdated();
                updateText();
                Commons.repaintAncestry(this);//If this is not present, shit happens
            } else if (e.getX() >= getWidth() - 40) { // right
                xMin = xMax;
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(xMin);
                calendar.add(Calendar.DATE, 29);
                xMax = calendar.getTimeInMillis();
                timeRangeUpdated();
                updateText();
                Commons.repaintAncestry(this);//If this is not present, shit happens
            }
        }

        public void mouseClicked(MouseEvent e) {
        }

        public void mouseEntered(MouseEvent e) {
        }

        public void mouseExited(MouseEvent e) {
        }

        public void mouseReleased(MouseEvent e) {
        }
    }
}
