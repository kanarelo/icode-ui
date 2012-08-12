/* Copyright 2008-2009 Robert Bajzat. All rights reserved. Use is subject to license terms. */
package com.icode.view.component.fields;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JComponent;
import javax.swing.Popup;
import javax.swing.PopupFactory;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.icode.resources.ResourceUtils;
import com.icode.view.border.LineBorder;

/**
 * Field to type or choose a date
 */
public class DateField extends AbstractField {

    private DatePopup datePopup;
    private final DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT);

    /**
     *
     * @param completeMatches
     */
    public DateField(Object[] completeMatches) {
        super(completeMatches);
    }

    /**
     *
     * @param completeMatches
     * @param useWindow
     */
    public DateField(Object[] completeMatches, boolean useWindow) {
        super(completeMatches, useWindow);
    }

    /**
     *
     * @param text
     */
    public DateField(String text) {
        super(text);
    }

    /**
     * Creates an empty date field
     */
    public DateField() {
        //setColumns(30);
        Listener listener = new Listener();
    }

    /**
     * 
     * @param col
     */
    public DateField(int col) {
        setColumns(col);
        Listener listener = new Listener();
    }

    /**
     * Updates the content
     * @param date date value in milliseconds
     */
    public void setDate(long date) {
        setText((date != -1) ? dateFormat.format(new Date(date)) : "");
    }

    /**
     * Updates the content
     * @param date date value
     */
    public void setDate(Date date) {
        setText((date != null) ? dateFormat.format(date) : "");
    }

    /**
     * Updates the content
     * @param date date value
     */
    @Override
    public void setText(String date) {
        try {
            super.setText((date == null || date.isEmpty()) ? "" : dateFormat.format(dateFormat.parse(date)));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Returns the valid date content
     * @return the date content in milliseconds
     */
    public long getDateInMillis() {
        Date date = parse(getText());
        return (date != null) ? date.getTime() : -1;
    }

    /**
     * Returns the valid date content
     * @return the date content, or Current Date if failure is observed
     *
     */
    public Date getDate() {
        return this.getContent();
    }

    /**
     * Checks content validity
     */
    @Override
    protected boolean isValid(String text) {
        return (parse(text) != null);
    }

    /**
     * Updates the field's content
     * @param value a date object, or null
     */
    @Override
    public void setContent(Object value) {
        setText((value != null) ? dateFormat.format((Date) value) : "");
    }

//    @Override
//    public void setText(String text){
//        super.setText(dateFormat.format());
//    }
    /**
     * Returns the valid date content
     * @return the date content
     */
    @Override
    public Date getContent() {
        return parse(getText());
    }

    /**
     * Overwrites the method the calculate the size of the component
     */
//    private final String formattedString = dateFormat.format(new Date());
//    @Override
//    public Dimension getPreferredSize() {
//        Dimension d = getSize(formattedString);
//        d.width += 12;
//        return d;
//    }
    private class Listener implements FocusListener, MouseListener {

        private Listener() {
            addFocusListener(this);
            addMouseListener(this);
        }

        public void focusGained(FocusEvent e) {
        }

        public void focusLost(FocusEvent e) {
            showPopup(false);
        }

        public void mousePressed(MouseEvent e) {
            if (e.getX() > getWidth() - getInsets().right - 8) {
                showPopup(datePopup == null);
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

    private Date parse(String text) {
        ParsePosition position = new ParsePosition(0);
        Date date = dateFormat.parse(text, position);
        return (position.getIndex() == text.length()) ? date : null;
    }

    private void showPopup(boolean visible) {
        if (visible == (datePopup == null)) {
            if (visible) {
                datePopup = new DatePopup(this);
                selectAll();
            } else {
                datePopup.popup.hide();
                datePopup = null;
            }
            repaint();
        }
    }

    /**
     * Overwrites the method to paint an arrow to pop up a chooser
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        ResourceUtils.init(g);

        //g.setColor(Color.darkGray);

        Insets is = getInsets();
        int x = getWidth() - 8 - is.right, y = is.top + (getHeight() - 8 - is.top - is.bottom) / 2;
        g.fillPolygon(new int[]{x, x + 8, x + 4}, (datePopup == null)
                ? new int[]{y, y, y + 8} : new int[]{y + 8, y + 8, y}, 3);
    }

    private static class DatePopup extends JComponent implements MouseListener, ComponentListener {

        private DateField dateField;
        private Popup popup;
        private final Calendar calendar = Calendar.getInstance();//_Date
        private final _Date today = new _Date(calendar.get(Calendar.DATE),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.YEAR));
        private final DateFormatSymbols symbols = new DateFormatSymbols();
        private final Font plainFont = UIManager.getFont("Label.font");
        private final Font boldFont = new Font(plainFont.getName(), Font.BOLD, plainFont.getSize());
        private final Font largeFont = new Font(plainFont.getName(), Font.PLAIN, plainFont.getSize() + 2);
        private final Font smallFont = new Font(plainFont.getName(), Font.BOLD, plainFont.getSize() - 2);
        private int dayOffset;
        private int dayCount;
        private String yearMonth;
        private int topHeight, middleHeight, gridWidth, gridHeight;

        private DatePopup(DateField dateField) {
            this.dateField = dateField;
            calendar.setFirstDayOfWeek(Calendar.SUNDAY);
            String text = dateField.getText().trim();
            Date date = (text.length() > 0) ? dateField.parse(text) : null;
            if (date != null) {
                calendar.setTime(date);
            }

            this.setOpaque(false);

            addMouseListener(this);
            SwingUtilities.windowForComponent(dateField).addComponentListener(this);

            updateCalendar();

            Point p = new Point(0, dateField.getHeight() + 1);
            SwingUtilities.convertPointToScreen(p, dateField);
            Dimension d = getPreferredSize();
            Rectangle bounds = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
            this.setBorder(new LineBorder(Color.DARK_GRAY, 1, 1, 1, 1, 8, 0, 8,
                    0));
            p.x = Math.max(bounds.x, Math.min(p.x, bounds.x + bounds.width - d.width));
            p.y = Math.max(bounds.y, Math.min(p.y, bounds.y + bounds.height - d.height));
            popup = PopupFactory.getSharedInstance().getPopup(dateField, this, p.x, p.y);
            popup.show();
        }

        private void updateCalendar() {
            yearMonth = calendar.get(Calendar.YEAR) + " " + symbols.getMonths()[calendar.get(Calendar.MONTH)];
            dayOffset = (calendar.get(Calendar.DAY_OF_WEEK) - calendar.get(Calendar.DATE) % 7 + 8 - calendar.getFirstDayOfWeek()) % 7;
            dayCount = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        }

        @Override
        public Dimension getPreferredSize() {
            FontMetrics fm = getFontMetrics(largeFont);
            gridWidth = (fm.stringWidth(yearMonth) + 32/*12+4*/) / 7;
            topHeight = fm.getAscent() + fm.getDescent() + 4;

            fm = getFontMetrics(smallFont);
            String[] weekdays = symbols.getShortWeekdays();
            for (int i = 0; i < 7; i++) {
                gridWidth = Math.max(gridWidth, fm.stringWidth(weekdays[i + 1]) + 4);
            }
            middleHeight = fm.getAscent() + fm.getDescent() + 4;

            fm = getFontMetrics(plainFont);
            gridWidth = Math.max(gridWidth, fm.stringWidth("28") + 4);
            gridHeight = fm.getAscent() + fm.getDescent() + 4;

            return new Dimension(gridWidth * 7, topHeight + middleHeight + gridHeight * 5);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = ResourceUtils.init(g);

            g2.setColor(Color.LIGHT_GRAY);
            g2.fillRect(4, 4, getWidth(), topHeight + middleHeight);
            g2.setColor(new Color(0xd6dde5));
            g2.fillRect(0, 0, getWidth(), topHeight + middleHeight);
            g2.setColor(new Color(0xd6dde5).darker());
            g2.drawRect(0, 0, getWidth(), topHeight + middleHeight);

            int lx = 4, rx = getWidth() - 12, cy = topHeight / 2;
            g2.setColor(Color.LIGHT_GRAY);
            g2.fillPolygon(new int[]{lx + 8, lx, lx + 8}, new int[]{cy - 4, cy, cy + 4}, 3);
            g2.fillPolygon(new int[]{rx, rx + 8, rx}, new int[]{cy - 4, cy, cy + 4}, 3);

            g2.setColor(Color.black);


            g2.fillPolygon(new int[]{lx + 8, lx, lx + 8}, new int[]{cy - 4, cy, cy + 4}, 3);
            g2.fillPolygon(new int[]{rx, rx + 8, rx}, new int[]{cy - 4, cy, cy + 4}, 3);

            g2.setFont(largeFont);
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(yearMonth, (7 * gridWidth - 32 - fm.stringWidth(yearMonth)) / 2 + 16, fm.getAscent() + 2);
            int fy = fm.getHeight();

            g2.setFont(smallFont);
            fm = g2.getFontMetrics();
            int fa = topHeight + fm.getAscent() + 2;
            String[] weekdays = symbols.getShortWeekdays();
            for (int i = 0; i < 7; i++) {
                String weekDay = weekdays[i + 1];
                g2.drawString(weekDay, i * gridWidth + (gridWidth - fm.stringWidth(weekDay)) / 2, fa);
            }
            fy += fm.getHeight();

            g2.setFont(plainFont);
            fm = g2.getFontMetrics();
            fa = topHeight + middleHeight + fm.getAscent() + 2;
            for (int i = 0; i < dayCount; i++) {
                int gx = (dayOffset + i) % 7;
                int gy = (dayOffset + i) / 7;
                String day = String.valueOf(i + 1);
                int x = gx * gridWidth + (gridWidth - fm.stringWidth(day)) / 2;
                int y = fa + gy * gridHeight;
                g2.setColor(Color.LIGHT_GRAY);
                g2.drawString(day, x + .5F, y + .5F);

                int day1 = 1 - dayOffset + x / gridWidth
                        + (y - topHeight - middleHeight) / gridHeight * 7;
                calendar.set(Calendar.DATE, day1);

                _Date d = new _Date(calendar.get(Calendar.DAY_OF_MONTH),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.YEAR));
                //System.out.println("d = " + d + ", today = " + today);

                if (!today.equals(d)) {
                    g2.setColor(Color.BLACK);
                    g2.drawString(day, x, y);
                } else {
                    g2.setFont(boldFont);
                    g2.setColor(Color.RED);
                    g2.drawString(day, x, y);
                    g2.setFont(this.plainFont);
                }

            }
        }

        private class _Date {

            private int day = 0;
            private int month = 0;
            private int year = 0;

            _Date(int day, int month, int year) {
                this.day = day;
                this.month = day;
                this.year = year;
            }

            @Override
            public boolean equals(Object o) {
                if ((o instanceof _Date)) {
                    _Date date = (_Date) o;
                    return (date.day == this.day)
                            & (date.month == this.month)
                            & (date.year == this.year);
                }
                return false;
            }

            @Override
            public String toString() {
                return "_Date(" + this.day + "," + this.month + "," + this.year + ")";
            }
        }

        public void mousePressed(MouseEvent e) {
            int x = e.getX();
            int y = e.getY();

            if (y < topHeight) {
                if (x < 12) { // left arrow
                    calendar.add(Calendar.MONTH, -1);
                    updateCalendar();
                    repaint(); // TODO resize
                } else if (x >= getWidth() - 12) { // right arrow
                    calendar.add(Calendar.MONTH, 1);
                    updateCalendar();
                    repaint();
                }
            } else if (y > topHeight + middleHeight) {
                int day = 1 - dayOffset + x / gridWidth
                        + (y - topHeight - middleHeight) / gridHeight * 7;
                calendar.set(Calendar.DATE, day);
                dateField.setDate(calendar.getTimeInMillis());
                dateField.selectAll();
                dateField.showPopup(false);
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

        public void componentMoved(ComponentEvent e) {
            dateField.showPopup(false);
        }

        public void componentHidden(ComponentEvent e) {
        }

        public void componentResized(ComponentEvent e) {
        }

        public void componentShown(ComponentEvent e) {
        }
    }
}
