package com.appoftatar.workgroupcalendar.di.components.managerComponents;

import com.appoftatar.workgroupcalendar.di.modules.PresentersModule;
import com.appoftatar.workgroupcalendar.di.modules.viewsModules.EmployeesModule;
import com.appoftatar.workgroupcalendar.ui.employes.EmployesFragment;

import dagger.Component;

@Component(modules = {PresentersModule.class, EmployeesModule.class})
public interface EmployeesComponent {
    void  inject(EmployesFragment fragment);
}
