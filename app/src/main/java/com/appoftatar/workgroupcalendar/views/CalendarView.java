package com.appoftatar.workgroupcalendar.views;

import com.appoftatar.workgroupcalendar.calendar.MonthWorkCalendar;

public interface CalendarView {
  void  showCalendar(MonthWorkCalendar monthWorkCalendar);
    void showProgressBar();
    void hideProgressBar();
}
