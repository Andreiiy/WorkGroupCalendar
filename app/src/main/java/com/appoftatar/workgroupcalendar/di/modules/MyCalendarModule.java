package com.appoftatar.workgroupcalendar.di.modules;

import com.appoftatar.workgroupcalendar.calendar.MyCalendar;

import dagger.Module;
import dagger.Provides;

@Module
public class MyCalendarModule {

    @Provides
    public MyCalendar provideMyCalendar(){
        return new MyCalendar();
    }
}
