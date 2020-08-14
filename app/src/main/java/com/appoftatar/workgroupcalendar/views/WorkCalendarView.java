package com.appoftatar.workgroupcalendar.views;

import com.appoftatar.workgroupcalendar.calendar.MonthWorkCalendar;

public interface WorkCalendarView {
    void showProgressBar();
    void hideProgressBar();
    void showDataSalary(MonthWorkCalendar monthWorkCalendar);
    void showCalendar(MonthWorkCalendar monthWorkCalendar);

}
