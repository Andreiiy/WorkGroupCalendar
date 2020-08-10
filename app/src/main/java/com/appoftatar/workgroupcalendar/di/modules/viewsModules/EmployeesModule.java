package com.appoftatar.workgroupcalendar.di.modules.viewsModules;

import com.appoftatar.workgroupcalendar.views.EmployesView;

import dagger.Module;
import dagger.Provides;

@Module
public class EmployeesModule {

    private EmployesView view;

    public EmployeesModule(EmployesView view) {
        this.view = view;
    }

    @Provides
    public EmployesView provideEmployeesView(){
        return this.view;
    }

}
