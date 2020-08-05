package com.appoftatar.workgroupcalendar.calendar;

import java.util.Date;

public class WorkDayWithRemainder {

private Integer year;
private Integer month;
private Integer date;
private Integer hours;
private Integer minutes;
private String Event;


    public WorkDayWithRemainder() {

    }

    public Integer getYear() {
        return year;
    }

    public Integer getMonth() {
        return month;
    }

    public Integer getDate() {
        return date;
    }

    public Integer getHours() {
        return hours;
    }

    public Integer getMinutes() {
        return minutes;
    }

    public String getEvent() {
        return Event;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public void setDate(Integer date) {
        this.date = date;
    }

    public void setHours(Integer hours) {
        this.hours = hours;
    }

    public void setMinutes(Integer minutes) {
        this.minutes = minutes;
    }

    public void setEvent(String event) {
        Event = event;
    }
}
