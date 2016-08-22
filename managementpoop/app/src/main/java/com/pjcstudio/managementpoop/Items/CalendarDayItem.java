package com.pjcstudio.managementpoop.Items;

import java.util.ArrayList;

/**
 * Created by juchanpark on 2015. 7. 27..
 */
public class CalendarDayItem {
    public ArrayList<CalendarDaySubItem> List_poop;
    public ArrayList<CalendarDaySubItem> List_food;
    public String year, month, day;

    public CalendarDayItem() {
        List_poop = new ArrayList<CalendarDaySubItem>();
        List_food = new ArrayList<CalendarDaySubItem>();
    }
}
