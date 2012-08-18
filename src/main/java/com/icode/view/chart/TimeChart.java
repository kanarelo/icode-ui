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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.GeneralPath;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
public abstract class TimeChart extends JPanel implements MouseMotionListener {

    private long minTime, maxTime;
    private float minValue, maxValue;
    private List<TimeLine> timeLines;
    private List<ValueLine> valueLines;
    private Rectangle area;
    private double scaleX, scaleY;

    /**
     *
     */
    protected TimeChart() {
        setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        ToolTipManager.sharedInstance().registerComponent(this);
        this.addMouseMotionListener(this);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        maxTime = calendar.getTimeInMillis();
        calendar.add(Calendar.DATE, -29);
        minTime = calendar.getTimeInMillis();
    }

    /**
     *
     * @return
     */
    protected long getTimeFrom() {
        return minTime;
    }

    /**
     *
     * @return
     */
    protected long getTimeTo() {
        return maxTime;
    }

    /**
     *
     * @param minValue
     * @param maxValue
     */
    protected void setRange(float minValue, float maxValue) {
        this.minValue = Math.max(0, minValue - (maxValue - minValue) / 3);
        this.maxValue = maxValue + (maxValue - minValue) * 0.05f;
        doLayout();
        Commons.repaintAncestry(this);
    }

    /**
     *
     */
    protected abstract void timeRangeUpdated();

    @Override
    public void doLayout() {
        timeLines = null;
        valueLines = null;
    }
    private final int[] fields = {Calendar.YEAR, Calendar.MONTH, Calendar.DATE,
        Calendar.DATE, Calendar.HOUR, Calendar.MINUTE, Calendar.SECOND};
    private final int[] types = {Calendar.YEAR, Calendar.MONTH, Calendar.DATE,
        Calendar.HOUR_OF_DAY, Calendar.MINUTE, Calendar.SECOND, Calendar.MILLISECOND};
    private final int[] steps = {1, 1, 7, 1, 1, 1, 1};
    private final long[] lengths = {31536000000l, 2592000000l, 604800000, 86400000, 3600000, 60000, 1000};

    @Override
    protected void paintComponent(Graphics g) {
        if ((timeLines == null) || (valueLines == null)) {
            Insets is = getInsets();
            area = new Rectangle(is.left, is.top,
                    getWidth() - is.left - is.right, getHeight() - is.top - is.bottom);
            area.height = Math.min(area.height, area.width * 2 / 3);

            //
            timeLines = new ArrayList<TimeLine>(5);
            scaleX = (float) area.width / (maxTime - minTime);

            Calendar calendar = Calendar.getInstance(Locale.FRANCE);
            int level = 0;
            for (int scale = 0; scale < 7; scale++) { // year, month, week, day, hour, minute, second
                if ((maxTime - minTime) / lengths[scale] >= getWidth() / 24) {
                    break;
                }

                // set the start value
                calendar.setTimeInMillis(minTime);
                boolean reset = false;
                for (int i = 0; i < types.length; i++) {
                    if (reset) {
                        calendar.set(types[i], 0);
                    }
                    reset = reset || (types[i] == fields[scale]);
                }
                if (scale == 2) // week
                {
                    calendar.add(Calendar.DATE, calendar.getFirstDayOfWeek()
                            - calendar.get(Calendar.DAY_OF_WEEK));
                }
                if (calendar.getTimeInMillis() < minTime) {
                    calendar.add(fields[scale], steps[scale]);
                }

                boolean added = false;
                for (; calendar.getTimeInMillis() <= maxTime; calendar.add(fields[scale], steps[scale])) {
                    long time = calendar.getTimeInMillis();
                    boolean found = false;
                    for (TimeLine timeLine : timeLines) {
                        if (timeLine.time == time) {
                            found = true;
                            break;
                        }
                    }
                    if (found) {
                        continue;
                    }

                    int x = (int) ((time - minTime) * scaleX);
                    String text = String.valueOf(calendar.get(fields[scale]));
                    timeLines.add(new TimeLine(time, x, text, level));
                    added = true;
                }
                if (added) {
                    level++;
                }
            }

            //
            valueLines = new ArrayList<ValueLine>(5);
            scaleY = area.height / (maxValue - minValue);

            double v = (maxValue - minValue) * 50 / area.height;
            int s = Math.max(1, (int) Math.pow(10, (int) Math.log10(v)));
            double d = v / s;
            int step = ((d < 1.5) ? 1 : (d < 3.5) ? 2 : (d < 7.5) ? 5 : 10) * s;

            int first = ((int) minValue / step + 1) * step;
            int n = ((int) maxValue - first) / step + 1;
            for (int i = 0; i < n; i++) {
                int value = first + i * step;
                valueLines.add(new ValueLine(
                        area.height - (int) ((value - minValue) * scaleY), String.valueOf(value)));
            }

            revalidateChart();
        }


        Graphics2D g2 = ResourceUtils.init(g);

        g.setColor(Color.white);
        g2.fill(area);

        paintBackground(g2);


        g.setColor(new Color(0xe0e0e0e0));
        for (TimeLine timeLine : timeLines) {
            g.drawLine(area.x + timeLine.x, area.y, area.x + timeLine.x, area.y + area.height);
        }
        for (ValueLine valueLine : valueLines) {
            g.drawLine(area.x, area.y + valueLine.y, area.x + area.width, area.y + valueLine.y);
        }


        paintForeground(g2);


        g.setColor(Color.darkGray);
        FontMetrics fm = g.getFontMetrics();
        int fa = fm.getAscent(), fd = fm.getDescent(), fc = (fa - fd) / 2;
        for (TimeLine timeLine : timeLines) {
            g.drawString(timeLine.text,
                    area.x + timeLine.x - fm.stringWidth(timeLine.text) / 2,
                    area.y + area.height - fd);
        }
        for (ValueLine valueLine : valueLines) {
            g.drawString(valueLine.text, area.x, area.y + valueLine.y + fc);
        }

        g.setColor(Color.lightGray);
        g.drawRect(area.x, area.y, area.width - 1, area.height - 1);

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
        return area.x + (int) (scaleX * (time - minTime));
    }

    /**
     *
     * @param value
     * @return
     */
    public int getValue(float value) {
        return area.y + area.height - (int) (scaleY * (value - minValue));
    }

    /**
     *
     * @param g
     * @param path
     * @param i
     * @param n
     */
    protected void paintPath(Graphics2D g, GeneralPath path, int i, int n) {
        g.setStroke(new BasicStroke(2, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_ROUND));
        g.setColor(getLineColor(i, n));
        g.draw(path);
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
        fillpath.lineTo(area.x + area.width, area.y + area.height);
        fillpath.lineTo(area.x, area.y + area.height);
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

    /**
     * Invoked when the mouse cursor has been moved onto a component
     * but no buttons have been pushed.
     * @param e 
     */
    public void mouseMoved(MouseEvent e) {
        this.getToolTipText(e);
    }

    @Override
    public String getToolTipText(MouseEvent e) {
        if (area == null || !area.contains(e.getX(), e.getY())) {
            return "";
        }
        long time = minTime + (long) ((e.getX() - area.x) / scaleX);
        float value = maxValue - (float) ((e.getY() - area.y) / scaleY);
        return getToolTipText(time, value);
    }

    /**
     *
     * @param time
     * @param value
     * @return
     */
    protected String getToolTipText(long time, float value) {
        return "<html>(" + time + "," + value + ")";
    }

    private class TimeLine {

        private long time;
        private int x;
        private String text;
        private int level;

        private TimeLine(long time, int x, String text, int level) {
            this.time = time;
            this.x = x;
            this.text = text;
            this.level = level;
        }
    }

    private class ValueLine {

        private int y;
        private String text;

        private ValueLine(int y, String text) {
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
            text = format.format(new Date(minTime)) + " - "
                    + format.format(new Date(maxTime));
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
                maxTime = minTime;
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(maxTime);
                calendar.add(Calendar.DATE, -29);
                minTime = calendar.getTimeInMillis();
                timeRangeUpdated();
                updateText();
                Commons.repaintAncestry(this);//If this is not present, shit happens
            } else if (e.getX() >= getWidth() - 40) { // right
                minTime = maxTime;
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(minTime);
                calendar.add(Calendar.DATE, 29);
                maxTime = calendar.getTimeInMillis();
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
