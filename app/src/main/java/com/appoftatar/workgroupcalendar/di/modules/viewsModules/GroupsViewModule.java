package com.appoftatar.workgroupcalendar.di.modules.viewsModules;

import com.appoftatar.workgroupcalendar.views.GroupsView;

import dagger.Module;
import dagger.Provides;

@Module
public class GroupsViewModule {

    private GroupsView groupsView;

    public GroupsViewModule(GroupsView groupsView) {
        this.groupsView = groupsView;
    }

    @Provides
    public GroupsView provideGroupsView(){
        return this.groupsView;
    }
}
