package com.example.cooperativeproject2;

import android.util.Log;
import java.util.Calendar;

public class DayInfo {


    private String day;
    private boolean inMonth;


    /**
     * 날짜를 반환한다.
     * @return day 날짜
     */
    public String getDay() {
        return day;
    }

    /**
     * 날짜를 저장한다.
     * @param day 날짜
     */
    public void setDay(String day) {
        this.day = day;
    }

    /**
     * 이번달의 날짜인지 정보를 반환한다.
     * @return inMonth(true / false)
     */


    public boolean isInMonth() {
        return inMonth;
    }

    /**
     * 이번달의 날짜인지 정보를 저장한다.
     * @param inMonth(true/false)
     */
    public void setInMonth(boolean inMonth) {
        this.inMonth = inMonth;
    }


    // 5/24 - 이 두개 함수 추가
    /*
    private boolean isToday(long millis) {
        Calendar cal1 = Calendar.getInstance();
        return isSameDay(cal1.getTimeInMillis(), millis);

    }

    public boolean isSameDay(long millis1, long millis2) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTimeInMillis(millis1);
        cal2.setTimeInMillis(millis2);
        Log.d("hatti.calendar", "" + cal1.get(Calendar.YEAR) + "" + cal1.get(Calendar.MONTH) + "" + cal1.get(Calendar.DATE) + " VS " +
                cal2.get(Calendar.YEAR) + "" + cal2.get(Calendar.MONTH) + "" + cal2.get(Calendar.DATE));
        return (cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) && cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH) && cal1.get(Calendar.DATE) == cal2.get(Calendar.DATE));
    }
*/


}