package com.appoftatar.workgroupcalendar.di.modules.viewsModules;

import com.appoftatar.workgroupcalendar.views.WorkCalendarView;

import dagger.Module;
import dagger.Provides;

@Module
public class WorkCalendarViewModule {

    WorkCalendarView view;

    public WorkCalendarViewModule(WorkCalendarView view) {
        this.view = view;
    }

    @Provides
    public WorkCalendarView provideWorkCalendarView(){
        return this.view;
    }
}
