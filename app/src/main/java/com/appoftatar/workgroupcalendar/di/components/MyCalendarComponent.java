package com.appoftatar.workgroupcalendar.di.components;

import com.appoftatar.workgroupcalendar.calendar.MonthWorkCalendar;
import com.appoftatar.workgroupcalendar.di.modules.FireBaseModule;
import com.appoftatar.workgroupcalendar.di.modules.MyCalendarModule;

import dagger.Component;

@Component(modules = {MyCalendarModule.class})
public interface MyCalendarComponent {
    void inject(MonthWorkCalendar monthWorkCalendar);
}
