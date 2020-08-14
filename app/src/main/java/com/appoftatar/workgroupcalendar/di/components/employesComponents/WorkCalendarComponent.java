package com.appoftatar.workgroupcalendar.di.components.employesComponents;

import com.appoftatar.workgroupcalendar.di.modules.FireBaseModule;
import com.appoftatar.workgroupcalendar.di.modules.PresentersModule;
import com.appoftatar.workgroupcalendar.di.modules.PresentersModule_ProvideBoardPresenterFactory;
import com.appoftatar.workgroupcalendar.di.modules.viewsModules.WorkCalendarViewModule;
import com.appoftatar.workgroupcalendar.ui.workcalendar.WorkCalendarFragment;

import dagger.Component;

@Component(modules = {PresentersModule.class, WorkCalendarViewModule.class})
public interface WorkCalendarComponent {
    void inject(WorkCalendarFragment fragment);
}
