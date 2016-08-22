package com.pjcstudio.managementpoop.Utility;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by juchanpark on 2015. 7. 27..
 */
public class CalendarTime {

    private Calendar calendar;

    public CalendarTime() {
        Date d = new Date();
        calendar = Calendar.getInstance();
        calendar.setTime(d);
    }

    public int getLastDay() {
        return calendar.getActualMaximum(Calendar.DATE);
    }

    public int getDay() {
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    public int getMonth() {
        return calendar.get(Calendar.MONTH)+1;
    }

    public int getMonthOfWeek() {
        return calendar.get(Calendar.WEEK_OF_MONTH);
    }

    public int getYear() {
        return calendar.get(Calendar.YEAR);
    }

    public int getLastDayofMonth() {
        return calendar.getMaximum(Calendar.DAY_OF_MONTH);
    }
}
