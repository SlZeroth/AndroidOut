package com.pjcstudio.managementpoop.Utility;

/**
 * Created by juchanpark on 2015. 8. 15..
 */
public class CalendarUtility {

    static public String getWeekDay(int a) {

        String str = null;
        switch (a) {
            case 1:
                str = "일요일";
                break;
            case 2:
                str = "월요일";
                break;
            case 3:
                str = "화요일";
                break;
            case 4:
                str = "수요일";
                break;
            case 5:
                str = "목요일";
                break;
            case 6:
                str = "금요일";
                break;
            case 7:
                str = "토요일";
                break;
        }
        return str;
    }
}
