package com.example.cooperativeproject2;

public class DayInfo {


    /**
     * 하루의 날짜정보를 저장하는 클래스
     *
     * @author croute
     * @since 2011.03.08
     */

    private String day;
    private boolean inMonth;
    //day 표시 시도
    private boolean isDay;

    /**
     * 날짜를 반환한다.
     *
     * @return day 날짜
     */
    public String getDay() {
        return day;
    }

    /**
     * 날짜를 저장한다.
     *
     * @param day 날짜
     */
    public void setDay(String day) {
        this.day = day;
    }

    /**
     * 이번달의 날짜인지 정보를 반환한다.
     *
     * @return inMonth(true / false)
     */

    // day 표시 시도
    public boolean isDay(){
        return isDay;
    }
    public void setInDay(boolean isDay){

    }
    //

    public boolean isInMonth() {
        return inMonth;
    }

    /**
     * 이번달의 날짜인지 정보를 저장한다.
     *
     * @param inMonth(true/false)
     */
    public void setInMonth(boolean inMonth) {
        this.inMonth = inMonth;
    }

}
