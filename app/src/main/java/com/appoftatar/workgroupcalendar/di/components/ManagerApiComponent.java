package com.appoftatar.workgroupcalendar.di.components;

import com.appoftatar.workgroupcalendar.CreateGroupActivity;
import com.appoftatar.workgroupcalendar.di.modules.PresentersModule;
import com.appoftatar.workgroupcalendar.di.modules.viewsModules.GroupsViewModule;

import dagger.Component;

@Component(modules = {PresentersModule.class, GroupsViewModule.class})
public interface ManagerApiComponent {
    void inject(CreateGroupActivity activity);
}
