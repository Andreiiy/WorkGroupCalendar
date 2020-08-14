package com.appoftatar.workgroupcalendar.di.components;

import com.appoftatar.workgroupcalendar.di.modules.FireBaseModule;
import com.appoftatar.workgroupcalendar.di.modules.PresentersModule;
import com.appoftatar.workgroupcalendar.di.modules.viewsModules.CalendarViewModule;
import com.appoftatar.workgroupcalendar.ui.calendar.CalendarFragment;

import dagger.Component;

@Component(modules = {PresentersModule.class, FireBaseModule.class, CalendarViewModule.class})
public interface CalendarComponent {
    void inject(CalendarFragment fragment);
}
