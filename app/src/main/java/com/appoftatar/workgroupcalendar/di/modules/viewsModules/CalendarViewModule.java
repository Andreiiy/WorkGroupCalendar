package com.appoftatar.workgroupcalendar.di.modules.viewsModules;

import com.appoftatar.workgroupcalendar.views.CalendarView;

import dagger.Module;
import dagger.Provides;

@Module
public class CalendarViewModule {

    private CalendarView view;


    public CalendarViewModule(CalendarView view) {
        this.view = view;
    }

    @Provides
    public CalendarView provideCalendarView(){
        return this.view;
    }
}
