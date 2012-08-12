package com.icode.view.component;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import com.icode.view.component.fields.ComboBox;
import com.icode.view.i18n.Localization;

public class DurationChooser extends ComboBox {

    private final Localization bundle = new Localization(new Class[]{super.getClass()});

    public DurationChooser(boolean paramBoolean1, boolean paramBoolean2) {
        if (paramBoolean1) {
            add(null, this.bundle.get("none"), this.bundle.get("nonedetails"));
        }
        Calendar calendarInst = Calendar.getInstance();
        calendarInst.set(14, 0);
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT);
        Date timeNow = calendarInst.getTime();
        calendarInst.add(12, -10);
        add("intenminutes", calendarInst.getTime(), null, dateFormat);
        calendarInst.setTime(timeNow);
        calendarInst.add(10, -1);
        add("inanhour", calendarInst.getTime(), null, dateFormat);
        calendarInst.setTime(timeNow);
        calendarInst.set(11, 0);
        calendarInst.set(12, 0);
        calendarInst.set(13, 0);
        Date localDate2 = calendarInst.getTime();
        add("today", localDate2, null, dateFormat);
        Date localDate3;
        if (paramBoolean2) {
            calendarInst.add(14, -1);
            localDate3 = calendarInst.getTime();
            calendarInst.add(14, 1);
            calendarInst.add(5, -1);
            add("yesterday", calendarInst.getTime(), localDate3, dateFormat);
        }
        calendarInst.setTime(localDate2);
        calendarInst.set(7, calendarInst.getFirstDayOfWeek());
        add("thisweek", calendarInst.getTime(), null, dateFormat);
        if (paramBoolean2) {
            calendarInst.add(14, -1);
            localDate3 = calendarInst.getTime();
            calendarInst.add(14, 1);
            calendarInst.add(5, -7);
            add("lastweek", calendarInst.getTime(), localDate3, dateFormat);
        }
        calendarInst.setTime(localDate2);
        calendarInst.add(5, -7);
        add("inaweek", calendarInst.getTime(), null, dateFormat);
        calendarInst.setTime(localDate2);
        calendarInst.set(5, 1);
        add("thismonth", calendarInst.getTime(), null, dateFormat);
        if (paramBoolean2) {
            calendarInst.add(14, -1);
            localDate3 = calendarInst.getTime();
            calendarInst.add(14, 1);
            calendarInst.add(2, -1);
            add("lastmonth", calendarInst.getTime(), localDate3, dateFormat);
        }
        calendarInst.setTime(localDate2);
        calendarInst.add(2, -1);
        add("inamonth", calendarInst.getTime(), null, dateFormat);
        calendarInst.setTime(localDate2);
        calendarInst.set(2, 0);
        calendarInst.set(5, 1);
        add("thisyear", calendarInst.getTime(), null, dateFormat);
        if (paramBoolean2) {
            calendarInst.add(14, -1);
            localDate3 = calendarInst.getTime();
            calendarInst.add(14, 1);
            calendarInst.add(1, -1);
            add("lastyear", calendarInst.getTime(), localDate3, dateFormat);
        }
        calendarInst.setTime(localDate2);
        calendarInst.add(1, -1);
        add("inayear", calendarInst.getTime(), null, dateFormat);
        setMaximumRowCount(getItemCount());
        setSelectedIndex(0);
    }

    private void add(String strBundle, Date from, Date to, DateFormat dateFormat) {
        add(new Duration(from, to), this.bundle.get(strBundle), dateFormat.format(from) + ((to == null) ? " -" : new StringBuilder(2).append(" - ").append(dateFormat.format(to)).toString()));
    }

    public Date getFrom() {
        Duration duration = (Duration) getContent();
        return (duration != null) ? duration.from : null;
    }

    public Date getTo() {
        Duration duration = (Duration) getContent();
        return (duration != null) ? duration.to : null;
    }

    private static class Duration {

        private Date from;
        private Date to;

        private Duration(Date from, Date to) {
            this.from = from;
            this.to = to;
        }
    }
}
